import { Language } from '@jshmrtn/vue3-gettext'

declare module '@vue/runtime-core' {
  export interface ComponentCustomProperties {
    $gettext: Language['$gettext']
    $pgettext: Language['$pgettext']
    $ngettext: Language['$ngettext']
    $npgettext: Language['$npgettext']
    $gettextInterpolate: Language['interpolate']
  }
}
