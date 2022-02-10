import { Component, AfterViewChecked } from '@angular/core'
import { ViewportScroller } from '@angular/common'

@Component({
  selector: 'my-about-tube',
  templateUrl: './about-tube.component.html',
  styleUrls: [ './about-tube.component.scss' ]
})

export class AboutPeertubeComponent implements AfterViewChecked {
  private lastScrollHash: string

  constructor (
    private viewportScroller: ViewportScroller
  ) {}

  ngAfterViewChecked () {
    if (window.location.hash && window.location.hash !== this.lastScrollHash) {
      this.viewportScroller.scrollToAnchor(window.location.hash.replace('#', ''))

      this.lastScrollHash = window.location.hash
    }
  }
}
