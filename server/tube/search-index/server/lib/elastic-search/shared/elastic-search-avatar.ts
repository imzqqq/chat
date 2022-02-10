
import { ActorImage } from '../../../../Tube/shared/models'
import { buildUrl } from '../../../helpers/utils'

function formatAvatarForAPI (obj: { avatar?: ActorImage & { url: string } }) {
  if (!obj.avatar) return null

  return {
    url: obj.avatar.url,
    path: obj.avatar.path,
    createdAt: obj.avatar.createdAt,
    updatedAt: obj.avatar.updatedAt
  }
}

function formatAvatarForDB (obj: { avatar?: ActorImage, host: string }) {
  if (!obj.avatar) return null

  return {
    url: buildUrl(obj.host, obj.avatar.path),
    path: obj.avatar.path,
    createdAt: obj.avatar.createdAt,
    updatedAt: obj.avatar.updatedAt
  }
}

function buildAvatarMapping () {
  return {
    path: {
      type: 'keyword'
    },
    createdAt: {
      type: 'date',
      format: 'date_optional_time'
    },
    updatedAt: {
      type: 'date',
      format: 'date_optional_time'
    }
  }
}

export {
  formatAvatarForAPI,
  formatAvatarForDB,
  buildAvatarMapping
}
