import validator from 'validator'
import { sep } from 'path'

function exists (value: any) {
  return value !== undefined && value !== null
}

function isSafePath (p: string) {
  return exists(p) &&
    (p + '').split(sep).every(part => {
      return [ '..' ].includes(part) === false
    })
}

function isArray (value: any) {
  return Array.isArray(value)
}

function isNotEmptyIntArray (value: any) {
  return Array.isArray(value) && value.every(v => validator.isInt('' + v)) && value.length !== 0
}

function isArrayOf (value: any, validator: (value: any) => boolean) {
  return isArray(value) && value.every(v => validator(v))
}

function isDateValid (value: string) {
  return exists(value) && validator.isISO8601(value)
}

function isIdValid (value: string) {
  return exists(value) && validator.isInt('' + value)
}

function isUUIDValid (value: string) {
  return exists(value) && validator.isUUID('' + value, 4)
}

function isIdOrUUIDValid (value: string) {
  return isIdValid(value) || isUUIDValid(value)
}

function isBooleanValid (value: any) {
  return typeof value === 'boolean' || (typeof value === 'string' && validator.isBoolean(value))
}

function toIntOrNull (value: string) {
  if (value === 'null') return null

  return validator.toInt(value)
}

function toValueOrNull (value: string) {
  if (value === 'null') return null

  return value
}

function toArray (value: any) {
  if (value && isArray(value) === false) return [ value ]

  return value
}

function toIntArray (value: any) {
  if (!value) return []
  if (isArray(value) === false) return [ validator.toInt(value) ]

  return value.map(v => validator.toInt(v))
}

// ---------------------------------------------------------------------------

export {
  exists,
  isArrayOf,
  isNotEmptyIntArray,
  isArray,
  isIdValid,
  isSafePath,
  isUUIDValid,
  isIdOrUUIDValid,
  isDateValid,
  toValueOrNull,
  isBooleanValid,
  toIntOrNull,
  toArray,
  toIntArray
}
