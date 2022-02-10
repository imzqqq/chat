import { CONFIG } from '../../initializers/constants'
import { doRequest } from '../../helpers/requests'

async function listIndexInstancesHost (): Promise<string[]> {
  const uri = CONFIG.INSTANCES_INDEX.URL

  const qs = {
    healthy: true,
    count: 5000
  }

  const { body } = await doRequest<any>({ uri, qs, json: true })

  return body.data.map(o => o.host as string)
}

export {
  listIndexInstancesHost
}
