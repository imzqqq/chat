import { execFile } from "child_process";

export default function getSecret(name: string): Promise<string> {
    return new Promise((resolve, reject) => {
        execFile(
            'security',
            ['find-generic-password', '-s', name, '-w'],
            {},
            (err, stdout) => {
                if (err) {
                    reject(err);
                } else {
                    resolve(stdout.trim());
                }
            },
        );
    });
}
