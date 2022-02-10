from twisted.trial import util

from chat.util.patch_inline_callbacks import do_patch

# attempt to do the patch before we load any Chat server code
do_patch()

util.DEFAULT_TIMEOUT_DURATION = 20
