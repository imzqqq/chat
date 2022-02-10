from chat.storage.databases.main.keys import KeyStore

# KeyStore isn't really safe to use from a worker, but for now we do so and hope that
# the races it creates aren't too bad.

SlavedKeyStore = KeyStore
