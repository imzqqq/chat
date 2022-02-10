// used from run.sh as getopts doesn't support long parameters
const idx = process.argv.indexOf("--app-url");
let hasAppUrl = false;
if (idx !== -1) {
    const value = process.argv[idx + 1];
    hasAppUrl = !!value;
}
process.stdout.write(hasAppUrl ? "1" : "0" );
