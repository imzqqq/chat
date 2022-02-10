import { AccountSummary, VideoChannelSummary } from '../../../../Tube/shared/models'
import { AdditionalActorAttributes } from '../../../types/actor.model'
import { buildAvatarMapping, formatAvatarForAPI, formatAvatarForDB } from './elastic-search-avatar'

function buildChannelOrAccountSummaryMapping () {
  return {
    id: {
      type: 'long'
    },

    name: {
      type: 'text',
      fields: {
        raw: {
          type: 'keyword'
        }
      }
    },
    displayName: {
      type: 'text'
    },
    url: {
      type: 'keyword'
    },
    host: {
      type: 'keyword'
    },
    handle: {
      type: 'keyword'
    },

    avatar: {
      properties: buildAvatarMapping()
    }
  }
}

function buildChannelOrAccountCommonMapping () {
  return {
    ...buildChannelOrAccountSummaryMapping(),

    followingCount: {
      type: 'long'
    },
    followersCount: {
      type: 'long'
    },

    createdAt: {
      type: 'date',
      format: 'date_optional_time'
    },
    updatedAt: {
      type: 'date',
      format: 'date_optional_time'
    },

    description: {
      type: 'text'
    }
  }
}

function formatActorSummaryForAPI (actor: (AccountSummary | VideoChannelSummary) & AdditionalActorAttributes) {
  return {
    id: actor.id,
    name: actor.name,
    displayName: actor.displayName,
    url: actor.url,
    host: actor.host,

    avatar: formatAvatarForAPI(actor)
  }
}

function formatActorForDB (actor: AccountSummary | VideoChannelSummary) {
  return {
    id: actor.id,
    name: actor.name,
    displayName: actor.displayName,
    url: actor.url,
    host: actor.host,

    handle: `${actor.name}@${actor.host}`,

    avatar: formatAvatarForDB(actor)
  }
}

export {
  buildChannelOrAccountCommonMapping,
  buildChannelOrAccountSummaryMapping,
  formatActorSummaryForAPI,
  formatActorForDB
}
