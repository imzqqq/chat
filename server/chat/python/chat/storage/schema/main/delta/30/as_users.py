import logging

from chat.config.appservice import load_appservices

logger = logging.getLogger(__name__)


def run_create(cur, database_engine, *args, **kwargs):
    # NULL indicates user was not registered by an appservice.
    try:
        cur.execute("ALTER TABLE users ADD COLUMN appservice_id TEXT")
    except Exception:
        # Maybe we already added the column? Hope so...
        pass


def run_upgrade(cur, database_engine, config, *args, **kwargs):
    cur.execute("SELECT name FROM users")
    rows = cur.fetchall()

    config_files = []
    try:
        config_files = config.app_service_config_files
    except AttributeError:
        logger.warning("Could not get app_service_config_files from config")
        pass

    appservices = load_appservices(config.server_name, config_files)

    owned = {}

    for row in rows:
        user_id = row[0]
        for appservice in appservices:
            if appservice.is_exclusive_user(user_id):
                if user_id in owned.keys():
                    logger.error(
                        "user_id %s was owned by more than one application"
                        " service (IDs %s and %s); assigning arbitrarily to %s"
                        % (user_id, owned[user_id], appservice.id, owned[user_id])
                    )
                owned.setdefault(appservice.id, []).append(user_id)

    for as_id, user_ids in owned.items():
        n = 100
        user_chunks = (user_ids[i : i + 100] for i in range(0, len(user_ids), n))
        for chunk in user_chunks:
            cur.execute(
                "UPDATE users SET appservice_id = ? WHERE name IN (%s)"
                % (",".join("?" for _ in chunk),),
                [as_id] + chunk,
            )
