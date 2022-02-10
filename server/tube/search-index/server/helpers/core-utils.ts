function isTestInstance () {
  return process.env.NODE_ENV === 'test'
}

function waitMs (ms: number) {
  return new Promise<void>(res => {
    setTimeout(() => res(), ms)
  })
}

// ---------------------------------------------------------------------------

export {
  isTestInstance,
  waitMs
}
