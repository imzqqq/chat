import React from 'react'

import Helmet from 'react-helmet'
import { Layout, MXContentMain } from '../components'
import config from '../../config'

const Discover = () => {

    return (<Layout navmode="discover">
        <MXContentMain>
            <Helmet title={`Discover Chat | ${config.siteTitle}`} />
            <h2 className="mxblock__hx">Discover Chat</h2>
            <div className="mxgrid">
                <div className="mxgrid__item33 mxgrid__item33--discover mxgrid__item33--bullet">
                    <div className="mxgrid__item__bg mxgrid__item__bg--develop">
                        <img src="/images/music_play_button.svg" alt="" className="mxgrid__item__bg__img mxgrid__item__bg__img--develop" />
                        <div className="mxgrid__item__bg__vert mxgrid__item__bg__vert--develop">
                            <h4 className="mxgrid__item__bg__hx mxgrid__item__bg__hx--develop"><a href="/docs/guides/introduction">Introduction</a></h4>
                            <p className="mxgrid__item__bg__p">How to get started with Chat<br /></p>
                        </div>
                    </div>
                </div>
                <div className="mxgrid__item33 mxgrid__item33--discover mxgrid__item33--bullet">
                    <div className="mxgrid__item__bg mxgrid__item__bg--develop">
                        <img src="/images/basic_spread_text.svg" alt="" className="mxgrid__item__bg__img mxgrid__item__bg__img--develop" />
                        <div className="mxgrid__item__bg__vert mxgrid__item__bg__vert--develop">
                            <h4 className="mxgrid__item__bg__hx mxgrid__item__bg__hx--develop"><a href="/docs/guides">Guides</a></h4>
                            <p className="mxgrid__item__bg__p">Guides to usage of Chat<br /></p>
                        </div>
                    </div>
                </div>
                <div className="mxgrid__item33 mxgrid__item33--discover mxgrid__item33--bullet">
                    <div className="mxgrid__item__bg mxgrid__item__bg--develop">
                        <img src="/images/basic_server_upload.svg" alt="" className="mxgrid__item__bg__img mxgrid__item__bg__img--develop" />
                        <div className="mxgrid__item__bg__vert mxgrid__item__bg__vert--develop">
                            <h4 className="mxgrid__item__bg__hx mxgrid__item__bg__hx--develop"><a href="/docs/guides/installing-synapse">Chat Installation</a></h4>
                            <p className="mxgrid__item__bg__p">Installing Chat, a homeserver implementation written in Python<br /></p>
                        </div>
                    </div>
                </div>
                
                <div className="mxgrid__item33 mxgrid__item33--discover mxgrid__item33--bullet">
                    <div className="mxgrid__item__bg mxgrid__item__bg--develop">
                        <img src="/images/basic_spread_text.svg" alt="" className="mxgrid__item__bg__img mxgrid__item__bg__img--develop" />
                        <div className="mxgrid__item__bg__vert mxgrid__item__bg__vert--develop">
                            <h4 className="mxgrid__item__bg__hx mxgrid__item__bg__hx--develop"><a href="/docs/develop">Guides for Developers</a></h4>
                            <p className="mxgrid__item__bg__p">How to develop for Chat<br /></p>
                        </div>
                    </div>
                </div>
                <div className="mxgrid__item33 mxgrid__item33--discover mxgrid__item33--bullet">
                    <div className="mxgrid__item__bg mxgrid__item__bg--develop">
                        <img src="/images/basic_signs.svg" alt="" className="mxgrid__item__bg__img mxgrid__item__bg__img--develop" />
                        <div className="mxgrid__item__bg__vert mxgrid__item__bg__vert--develop">
                            <h4 className="mxgrid__item__bg__hx mxgrid__item__bg__hx--develop"><a href="/faq/">FAQ</a></h4>
                            <p className="mxgrid__item__bg__p">Chat FAQ<br /></p>
                        </div>
                    </div>
                </div>


                <div className="mxgrid__item33 mxgrid__item33--discover mxgrid__item33--bullet">
                    <div className="mxgrid__item__bg mxgrid__item__bg--develop">
                        <img src="/images/basic_elaboration_message_happy.svg" alt="" className="mxgrid__item__bg__img mxgrid__item__bg__img--develop" />
                        <div className="mxgrid__item__bg__vert mxgrid__item__bg__vert--develop">
                            <h4 className="mxgrid__item__bg__hx mxgrid__item__bg__hx--develop"><a href="/docs/guides/moderation">Moderation</a></h4>
                            <p className="mxgrid__item__bg__p">How to manage and moderate Chat rooms<br /></p>
                        </div>
                    </div>
                </div>
                <div className="mxgrid__item33 mxgrid__item33--discover mxgrid__item33--bullet">
                    <div className="mxgrid__item__bg mxgrid__item__bg--develop">
                        <img src="/images/basic_spread_text.svg" alt="" className="mxgrid__item__bg__img mxgrid__item__bg__img--develop" />
                        <div className="mxgrid__item__bg__vert mxgrid__item__bg__vert--develop">
                            <h4 className="mxgrid__item__bg__hx mxgrid__item__bg__hx--develop"><a href="https://matrix.org/docs/spec">Spec</a></h4>
                            <p className="mxgrid__item__bg__p">The Chat Specification<br /></p>
                        </div>
                    </div>
                </div>
            </div>
            <h2 className="mxblock__hx">Find Projects</h2>
            <div className="mxgrid">
                <div className="mxgrid__item33 mxgrid__item33--discover mxgrid__item33--bullet">
                    <div className="mxgrid__item__bg mxgrid__item__bg--develop">
                        <img src="/images/software_layout_header_sideleft.svg" alt="" className="mxgrid__item__bg__img mxgrid__item__bg__img--develop" />
                        <div className="mxgrid__item__bg__vert mxgrid__item__bg__vert--develop">
                            <h4 className="mxgrid__item__bg__hx mxgrid__item__bg__hx--develop"><a href="/clients">Clients</a></h4>
                            <p className="mxgrid__item__bg__p">Find a Chat Client for you<br /></p>
                        </div>
                    </div>
                </div>
                <div className="mxgrid__item33 mxgrid__item33--discover mxgrid__item33--bullet">
                    <div className="mxgrid__item__bg mxgrid__item__bg--develop">
                        <img src="/images/basic_share.svg" alt="" className="mxgrid__item__bg__img mxgrid__item__bg__img--develop" />
                        <div className="mxgrid__item__bg__vert mxgrid__item__bg__vert--develop">
                            <h4 className="mxgrid__item__bg__hx mxgrid__item__bg__hx--develop"><a href="/bridges/">Bridges</a></h4>
                            <p className="mxgrid__item__bg__p">Bridge to platforms outside Chat<br /></p>
                        </div>
                    </div>
                </div>
                <div className="mxgrid__item33 mxgrid__item33--discover mxgrid__item33--bullet">
                    <div className="mxgrid__item__bg mxgrid__item__bg--develop">
                        <img src="/images/basic_elaboration_message_happy.svg" alt="" className="mxgrid__item__bg__img mxgrid__item__bg__img--develop" />
                        <div className="mxgrid__item__bg__vert mxgrid__item__bg__vert--develop">
                            <h4 className="mxgrid__item__bg__hx mxgrid__item__bg__hx--develop"><a href="/docs/projects/try-matrix-now">Try Chat Now</a></h4>
                            <p className="mxgrid__item__bg__p">Find a comprehensive list of Chat-related projects<br /></p>
                        </div>
                    </div>
                </div>

            </div>
        </MXContentMain>
    </Layout>)
}

export default Discover
