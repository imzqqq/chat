function buildApiUrl (path: string) {
  const normalizedPath = path.startsWith('/') ? path : '/' + path

  const base = process.env.VUE_APP_API_URL || ''
  return base + normalizedPath
}

function durationToString (duration: number) {
  const hours = Math.floor(duration / 3600)
  const minutes = Math.floor((duration % 3600) / 60)
  const seconds = duration % 60

  const minutesPadding = minutes >= 10 ? '' : '0'
  const secondsPadding = seconds >= 10 ? '' : '0'
  const displayedHours = hours > 0 ? hours.toString() + ':' : ''

  return (
    displayedHours + minutesPadding + minutes.toString() + ':' + secondsPadding + seconds.toString()
  ).replace(/^0/, '')
}

function pageToAPIParams (page: number, itemsPerPage: number) {
  const start = (page - 1) * itemsPerPage
  const count = itemsPerPage

  return { start, count }
}

function durationRangeToAPIParams (durationRange: string) {
  if (!durationRange) {
    return { durationMin: undefined, durationMax: undefined }
  }

  const fourMinutes = 60 * 4
  const tenMinutes = 60 * 10

  switch (durationRange) {
    case 'short':
      return { durationMin: undefined, durationMax: fourMinutes}

    case 'medium':
      return { durationMin: fourMinutes, durationMax: tenMinutes }

    case 'long':
      return { durationMin: tenMinutes, durationMax: undefined }

    default:
      console.error('Unknown duration range %s', durationRange)
      return { durationMin: undefined, durationMax: undefined }
  }
}

function publishedDateRangeToAPIParams (publishedDateRange: string) {
  if (!publishedDateRange) {
    return { startDate: undefined, endDate: undefined }
  }

  // today
  const date = new Date()
  date.setHours(0, 0, 0, 0)

  switch (publishedDateRange) {
    case 'today':
      break

    case 'last_7days':
      date.setDate(date.getDate() - 7)
      break

    case 'last_30days':
      date.setDate(date.getDate() - 30)
      break

    case 'last_365days':
      date.setDate(date.getDate() - 365)
      break

    default:
      console.error('Unknown published date range %s', publishedDateRange)
      return { startDate: undefined, endDate: undefined }
  }

  return { startDate: date.toISOString(), endDate: undefined }
}

function extractTagsFromQuery (value: any | any[]) {
  if (!value) return []

  if (Array.isArray(value) === true) {
    return (value as any[]).map(v => ({ text: v }))
  }

  return [ { text: value } ]
}

export {
  buildApiUrl,
  durationToString,
  publishedDateRangeToAPIParams,
  pageToAPIParams,
  durationRangeToAPIParams,
  extractTagsFromQuery
}

