.PHONY: your-deploy-web

YOUR_CFGDIR := configs/
your-deploy-%: CFGDIR := $(YOUR_CFGDIR)

your-deploy-web: web
	rsync --info=progress2 -rup --del element-web/webapp/ root@47.115.52.51:/home/imzqqq/workspace/chat/shared/desktop/element-web/webapp
