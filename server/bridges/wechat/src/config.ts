import path from 'path'

/**
 * Export
 */

import { log } from 'wechaty'

import { packageJson }  from './package-json.js'
import { codeRoot }     from '../commonjs/code-root.cjs'
import dotenv         from 'dotenv'

dotenv.config()

const AGE_LIMIT_SECONDS = process.env['MATRIX_EVENT_AGE_LIMIT_SECONDS'] || 5 * 60   // 5 minutes
const DEFAULT_PORT      = 8788     // W:87 X:88
const VERSION = packageJson.version || '0.0.0'
const LOG_LEVEL = process.env['LOG_LEVEL']

const REGISTRATION_FILE = 'wechaty-registration.yaml'
const SCHEMA_FILE       = path.join(codeRoot, 'config/schema.yaml')

export {
  LOG_LEVEL,
  AGE_LIMIT_SECONDS,
  codeRoot,
  DEFAULT_PORT,
  log,
  REGISTRATION_FILE,
  SCHEMA_FILE,
  VERSION,
}
