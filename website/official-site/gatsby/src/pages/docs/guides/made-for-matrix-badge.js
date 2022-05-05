import React from 'react'

import Helmet from 'react-helmet'
import { Layout } from '../../../components'

const Redirect = () => {

    return (<Layout>
        <Helmet>
            <meta http-equiv="refresh" content="0; url=/docs/guides/made-for-matrix-badge-guidelines" />
        </Helmet>
    </Layout>)
}

export default Redirect
