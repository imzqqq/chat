Before problems, here's a few tips to see whether there *is* a problem:

* You configure **The Construct** by using the console (or the `!control:SERVERNAME` room). Settings get stored in *RocksDB*. No config file is *not* a problem.

* Database/configuration is stored in `SERVERNAME` folder, that's the only parameter you have to use starting the server. You can try various server configurations in parallel by using a different server name.


There are some things which is not yet fixed and knowing it may help to new admins:

* Updating `git pull`s advised to `make clean`ed before (re)build.

* Using LetsEncrypt certificates works well, but you have to give `fullchain.pem` *twice*:
  * `net listen matrix * 8448 privkey.pem fullchain.pem fullchain.pem`

* If you screw up the config and the server doesn't start you can force configuraiton by using environment variables like:
  * `ircd_client_sync_linear_buffer_size=524288 bin/construct SERVERNAME`

* Your helpful crowd sits in the room `#test:zemos.net` (alias `#admins:matrix.org, #construct:maunium.net, #construct:colab.de`)
