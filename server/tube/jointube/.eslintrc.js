module.exports = {
  root: true,
  env: {
    node: true
  },
  'extends': [
    'plugin:vue/essential',
    '@vue/standard'
  ],
  rules: {
    'no-console': 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    'vue/script-indent': [ 'error', 2, {
      baseIndent: 1,
      switchCase: 1
    } ],
    'array-bracket-spacing': [ 'error', 'always' ],
    'quote-props': 'off',
    'no-case-declarations': 'off'
  },
  overrides: [
    {
      files: [ '*.vue' ],
      rules: {
        indent: 'off'
      }
    }
  ],
  parserOptions: {
    parser: 'babel-eslint'
  }
}
