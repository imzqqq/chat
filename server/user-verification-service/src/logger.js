const winston = require('winston');
const { combine, timestamp, prettyPrint } = winston.format;

require('dotenv').config();

winston.configure({
    format: combine(
        timestamp(),
        prettyPrint()
    ),
    transports: [
        new winston.transports.Console({
            level: process.env.UVS_LOG_LEVEL || 'info',
        }),
    ],
});

module.exports = winston;
