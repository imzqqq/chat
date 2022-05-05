import * as MarkdownIt from 'markdown-it'

const TEXT_RULES = [
  'linkify',
  'autolink',
  'emphasis',
  'link',
  'newline',
  'list'
]

const markdownIt = new MarkdownIt('zero', { linkify: true, breaks: true, html: false })

for (const rule of TEXT_RULES) {
  markdownIt.enable(rule)
}

export function renderMarkdown(markdown: string) {
  if (!markdown) return ''

  return markdownIt.render(markdown)
}
