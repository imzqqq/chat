/* eslint-disable no-use-before-define */

export interface Managers {
  [manager: string]: Manager
}

export abstract class Manager {

  public abstract teamManager (managers: Managers): void

}
