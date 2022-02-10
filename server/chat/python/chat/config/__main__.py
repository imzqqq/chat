from chat.config._base import ConfigError

if __name__ == "__main__":
    import sys

    from chat.config.homeserver import HomeServerConfig

    action = sys.argv[1]

    if action == "read":
        key = sys.argv[2]
        try:
            config = HomeServerConfig.load_config("", sys.argv[3:])
        except ConfigError as e:
            sys.stderr.write("\n" + str(e) + "\n")
            sys.exit(1)

        print(getattr(config, key))
        sys.exit(0)
    else:
        sys.stderr.write("Unknown command %r\n" % (action,))
        sys.exit(1)
