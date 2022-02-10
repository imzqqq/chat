/* eslint jsx-a11y/label-has-for:0 */

import React from 'react'
import Helmet from 'react-helmet'
import { Layout } from '../components'

import config from '../../config'



const Legal = () => {
    return (<Layout hasNavPadding="true">
            <Helmet title={`404 | ${config.siteTitle}`}>
                <script type="text/javascript" src="/js/404-catch.js"></script>
            </Helmet>
        <div>
            <h1>404, not found</h1>
            <p>Whatever it is that you want, it's not here. There are, however, things you could do to find what you're looking for.</p>
            <p><iframe title="DDGSearch" src="https://duckduckgo.com/search.html?width=400&site=matrix.org&prefill=Search Chat.org"
      style={{"overflow":"hidden","margin":0,"padding":0,"width":"458px","height":"40px"}} 
      frameBorder="0"></iframe></p>
            <p>See some popular pages:</p>
            <ul>
                <li><a href="/">Homepage</a></li>
                <li><a href="/blog/posts">Chat Blog</a></li>
                <li><a href="/faq">FAQ</a></li>
                <li><a href="https://matrix.org/docs/spec">The Chat Spec</a></li>
            </ul>
        </div>
    </Layout>)
}

export default Legal
