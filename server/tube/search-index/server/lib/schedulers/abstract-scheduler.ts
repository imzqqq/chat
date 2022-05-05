import { logger } from '../../helpers/logger'
import * as Bluebird from 'bluebird'
import { inspect } from 'util'

export abstract class AbstractScheduler {

  protected abstract schedulerIntervalMs: number

  private interval: NodeJS.Timer
  private isRunning = false

  enable () {
    if (!this.schedulerIntervalMs) throw new Error('Interval is not correctly set.')

    this.interval = setInterval(() => this.execute(), this.schedulerIntervalMs)
  }

  disable () {
    clearInterval(this.interval)
  }

  async execute () {
    if (this.isRunning === true) {
      logger.info('Do not run scheduler %s because the process is already running.', this.constructor.name)
      return
    }

    this.isRunning = true

    try {
      await this.internalExecute()
    } catch (err) {
      logger.error({ err: inspect(err) }, 'Cannot execute %s scheduler.', this.constructor.name)
    } finally {
      this.isRunning = false
    }
  }

  protected abstract internalExecute (): Promise<any> | Bluebird<any>
}
