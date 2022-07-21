import secrets
from dataclasses import dataclass
from datetime import timedelta
from datetime import timezone
from typing import Any

from fastapi import APIRouter
from fastapi import Depends
from fastapi import Form
from fastapi import HTTPException
from fastapi import Request
from fastapi.responses import JSONResponse
from fastapi.responses import RedirectResponse
from loguru import logger
from sqlalchemy import select

from app import config
from app import models
from app import templates
from app.admin import user_session_or_redirect
from app.config import verify_csrf_token
from app.database import AsyncSession
from app.database import get_db_session
from app.utils import indieauth
from app.utils.datetime import now

router = APIRouter()


@router.get("/.well-known/oauth-authorization-server")
async def well_known_authorization_server(
    request: Request,
) -> dict[str, Any]:
    return {
        "issuer": config.ID + "/",
        "authorization_endpoint": request.url_for("indieauth_authorization_endpoint"),
        "token_endpoint": request.url_for("indieauth_token_endpoint"),
        "code_challenge_methods_supported": ["S256"],
        "revocation_endpoint": request.url_for("indieauth_revocation_endpoint"),
        "revocation_endpoint_auth_methods_supported": ["none"],
    }


@router.get("/auth")
async def indieauth_authorization_endpoint(
    request: Request,
    db_session: AsyncSession = Depends(get_db_session),
    _: None = Depends(user_session_or_redirect),
) -> templates.TemplateResponse:
    me = request.query_params.get("me")
    client_id = request.query_params.get("client_id")
    redirect_uri = request.query_params.get("redirect_uri")
    state = request.query_params.get("state", "")
    response_type = request.query_params.get("response_type", "id")
    scope = request.query_params.get("scope", "").split()
    code_challenge = request.query_params.get("code_challenge", "")
    code_challenge_method = request.query_params.get("code_challenge_method", "")

    return await templates.render_template(
        db_session,
        request,
        "indieauth_flow.html",
        dict(
            client=await indieauth.get_client_id_data(client_id),
            scopes=scope,
            redirect_uri=redirect_uri,
            state=state,
            response_type=response_type,
            client_id=client_id,
            me=me,
            code_challenge=code_challenge,
            code_challenge_method=code_challenge_method,
        ),
    )


@router.post("/admin/indieauth")
async def indieauth_flow(
    request: Request,
    db_session: AsyncSession = Depends(get_db_session),
    csrf_check: None = Depends(verify_csrf_token),
    _: None = Depends(user_session_or_redirect),
) -> RedirectResponse:
    form_data = await request.form()
    logger.info(f"{form_data=}")

    # Params needed for the redirect
    redirect_uri = form_data["redirect_uri"]
    code = secrets.token_urlsafe(32)
    iss = config.ID + "/"
    state = form_data["state"]

    scope = " ".join(form_data.getlist("scopes"))
    client_id = form_data["client_id"]

    # TODO: Ensure that me is correct
    # me = form_data.get("me")

    # XXX: should always be code
    # response_type = form_data["response_type"]

    code_challenge = form_data["code_challenge"]
    code_challenge_method = form_data["code_challenge_method"]

    auth_request = models.IndieAuthAuthorizationRequest(
        code=code,
        scope=scope,
        redirect_uri=redirect_uri,
        client_id=client_id,
        code_challenge=code_challenge,
        code_challenge_method=code_challenge_method,
    )

    db_session.add(auth_request)
    await db_session.commit()

    return RedirectResponse(
        redirect_uri + f"?code={code}&state={state}&iss={iss}",
        status_code=302,
    )


async def _check_auth_code(
    db_session: AsyncSession,
    code: str,
    client_id: str,
    redirect_uri: str,
    code_verifier: str | None,
) -> tuple[bool, models.IndieAuthAuthorizationRequest | None]:
    auth_code_req = (
        await db_session.scalars(
            select(models.IndieAuthAuthorizationRequest).where(
                models.IndieAuthAuthorizationRequest.code == code
            )
        )
    ).one_or_none()
    if not auth_code_req:
        return False, None
    if auth_code_req.is_used:
        logger.info("code was already used")
        return False, None
    #
    if now() > auth_code_req.created_at.replace(tzinfo=timezone.utc) + timedelta(
        seconds=120
    ):
        logger.info("Auth code request expired")
        return False, None

    if (
        auth_code_req.redirect_uri != redirect_uri
        or auth_code_req.client_id != client_id
    ):
        logger.info("redirect_uri/client_id does not match request")
        return False, None

    auth_code_req.is_used = True
    await db_session.commit()

    return True, auth_code_req


@router.post("/auth")
async def indieauth_reedem_auth_code(
    request: Request,
    db_session: AsyncSession = Depends(get_db_session),
) -> JSONResponse:
    form_data = await request.form()
    logger.info(f"{form_data=}")
    grant_type = form_data.get("grant_type", "authorization_code")
    if grant_type != "authorization_code":
        raise ValueError(f"Invalid grant_type {grant_type}")

    code = form_data["code"]

    # These must match the params from the first request
    client_id = form_data["client_id"]
    redirect_uri = form_data["redirect_uri"]
    # code_verifier is optional for backward compat
    code_verifier = form_data.get("code_verifier")

    is_code_valid, _ = await _check_auth_code(
        db_session,
        code=code,
        client_id=client_id,
        redirect_uri=redirect_uri,
        code_verifier=code_verifier,
    )
    if is_code_valid:
        return JSONResponse(
            content={
                "me": config.ID + "/",
            },
            status_code=200,
        )
    else:
        return JSONResponse(
            content={"error": "invalid_grant"},
            status_code=400,
        )


@router.post("/token")
async def indieauth_token_endpoint(
    request: Request,
    db_session: AsyncSession = Depends(get_db_session),
) -> JSONResponse:
    form_data = await request.form()
    logger.info(f"{form_data=}")
    grant_type = form_data.get("grant_type", "authorization_code")
    if grant_type != "authorization_code":
        raise ValueError(f"Invalid grant_type {grant_type}")

    code = form_data["code"]

    # These must match the params from the first request
    client_id = form_data["client_id"]
    redirect_uri = form_data["redirect_uri"]
    # code_verifier is optional for backward compat
    code_verifier = form_data.get("code_verifier")

    is_code_valid, auth_code_request = await _check_auth_code(
        db_session,
        code=code,
        client_id=client_id,
        redirect_uri=redirect_uri,
        code_verifier=code_verifier,
    )
    if not is_code_valid or (auth_code_request and not auth_code_request.scope):
        return JSONResponse(
            content={"error": "invalid_grant"},
            status_code=400,
        )

    if not auth_code_request:
        raise ValueError("Should never happen")

    access_token = models.IndieAuthAccessToken(
        indieauth_authorization_request_id=auth_code_request.id,
        access_token=secrets.token_urlsafe(32),
        expires_in=3600,
        scope=auth_code_request.scope,
    )
    db_session.add(access_token)
    await db_session.commit()

    return JSONResponse(
        content={
            "access_token": access_token.access_token,
            "token_type": "Bearer",
            "scope": auth_code_request.scope,
            "me": config.ID + "/",
            "expires_in": 3600,
        },
        status_code=200,
    )


async def _check_access_token(
    db_session: AsyncSession,
    token: str,
) -> tuple[bool, models.IndieAuthAccessToken | None]:
    access_token_info = (
        await db_session.scalars(
            select(models.IndieAuthAccessToken).where(
                models.IndieAuthAccessToken.access_token == token
            )
        )
    ).one_or_none()
    if not access_token_info:
        return False, None

    if access_token_info.is_revoked:
        logger.info("Access token is revoked")
        return False, None

    if now() > access_token_info.created_at.replace(tzinfo=timezone.utc) + timedelta(
        seconds=access_token_info.expires_in
    ):
        logger.info("Access token is expired")
        return False, None

    return True, access_token_info


@dataclass(frozen=True)
class AccessTokenInfo:
    scopes: list[str]


async def verify_access_token(
    request: Request,
    db_session: AsyncSession = Depends(get_db_session),
) -> AccessTokenInfo:
    token = request.headers.get("Authorization", "").removeprefix("Bearer ")

    # Check if the token is within the form data
    if not token:
        form_data = await request.form()
        if "access_token" in form_data:
            token = form_data.get("access_token")

    is_token_valid, access_token = await _check_access_token(db_session, token)
    if not is_token_valid:
        raise HTTPException(
            detail="Invalid access token",
            status_code=401,
        )

    if not access_token or not access_token.scope:
        raise ValueError("Should never happen")

    return AccessTokenInfo(
        scopes=access_token.scope.split(),
    )


@router.post("/revoke_token")
async def indieauth_revocation_endpoint(
    request: Request,
    token: str = Form(),
    db_session: AsyncSession = Depends(get_db_session),
) -> JSONResponse:

    is_token_valid, token_info = await _check_access_token(db_session, token)
    if is_token_valid:
        if not token_info:
            raise ValueError("Should never happen")

        token_info.is_revoked = True
        await db_session.commit()

    return JSONResponse(
        content={},
        status_code=200,
    )
