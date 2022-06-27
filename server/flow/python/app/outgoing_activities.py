import email
import time
import traceback
from datetime import datetime
from datetime import timedelta

import httpx
from loguru import logger
from sqlalchemy.orm import Session

from app import activitypub as ap
from app import models
from app.database import SessionLocal
from app.database import now

_MAX_RETRIES = 16


def new_outgoing_activity(
    db: Session,
    recipient: str,
    outbox_object_id: int,
) -> models.OutgoingActivity:
    outgoing_activity = models.OutgoingActivity(
        recipient=recipient,
        outbox_object_id=outbox_object_id,
    )

    db.add(outgoing_activity)
    db.commit()
    db.refresh(outgoing_activity)
    return outgoing_activity


def _parse_retry_after(retry_after: str) -> datetime | None:
    try:
        # Retry-After: 120
        seconds = int(retry_after)
    except ValueError:
        # Retry-After: Wed, 21 Oct 2015 07:28:00 GMT
        dt_tuple = email.utils.parsedate_tz(retry_after)
        if dt_tuple is None:
            return None

        seconds = int(email.utils.mktime_tz(dt_tuple) - time.time())

    return now() + timedelta(seconds=seconds)


def _exp_backoff(tries: int) -> datetime:
    seconds = 2 * (2 ** (tries - 1))
    return now() + timedelta(seconds=seconds)


def _set_next_try(
    outgoing_activity: models.OutgoingActivity,
    next_try: datetime | None = None,
) -> None:
    if not outgoing_activity.tries:
        raise ValueError("Should never happen")

    if outgoing_activity.tries == _MAX_RETRIES:
        outgoing_activity.is_errored = True
        outgoing_activity.next_try = None
    else:
        outgoing_activity.next_try = next_try or _exp_backoff(outgoing_activity.tries)


def process_next_outgoing_activity(db: Session) -> bool:
    q = (
        db.query(models.OutgoingActivity)
        .filter(
            models.OutgoingActivity.next_try <= now(),
            models.OutgoingActivity.is_errored.is_(False),
            models.OutgoingActivity.is_sent.is_(False),
        )
        .order_by(models.OutgoingActivity.next_try)
    )
    q_count = q.count()
    logger.info(f"{q_count} outgoing activities ready to process")
    if not q_count:
        logger.info("No activities to process")
        return False

    next_activity = q.limit(1).one()

    next_activity.tries = next_activity.tries + 1
    next_activity.last_try = now()

    payload = ap.wrap_object_if_needed(next_activity.outbox_object.ap_object)
    logger.info(f"{payload=}")
    try:
        resp = ap.post(next_activity.recipient, payload)
    except httpx.HTTPStatusError as http_error:
        logger.exception("Failed")
        next_activity.last_status_code = http_error.response.status_code
        next_activity.last_response = http_error.response.text
        next_activity.error = traceback.format_exc()

        if http_error.response.status_code in [429, 503]:
            retry_after: datetime | None = None
            if retry_after_value := http_error.response.headers.get("Retry-After"):
                retry_after = _parse_retry_after(retry_after_value)
            _set_next_try(next_activity, retry_after)
        elif 400 <= http_error.response.status_code < 500:
            logger.info(f"status_code={http_error.response.status_code} not retrying")
            next_activity.is_errored = True
            next_activity.next_try = None
        else:
            _set_next_try(next_activity)
    except Exception:
        logger.exception("Failed")
        next_activity.error = traceback.format_exc()
        _set_next_try(next_activity)
    else:
        logger.info("Success")
        next_activity.is_sent = True
        next_activity.last_status_code = resp.status_code
        next_activity.last_response = resp.text

    db.commit()
    return True


def loop() -> None:
    db = SessionLocal()
    while 1:
        try:
            process_next_outgoing_activity(db)
        except Exception:
            logger.exception("Failed to process next outgoing activity")
            raise

        time.sleep(1)


if __name__ == "__main__":
    loop()
