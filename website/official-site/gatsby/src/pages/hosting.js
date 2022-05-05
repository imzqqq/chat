import React from 'react'
import { graphql } from 'gatsby'

import Helmet from 'react-helmet'
import { Layout, MXContentMain } from '../components'
import config from '../../config'


const title= `Hosting | ${config.siteTitle}`;
const Hosting = ({ data }) => {

    return (<Layout titleOverride={title} navmode="discover"
    excerptOverride="Browse Chat hosting options">
      <MXContentMain>
        <Helmet title={title} />
            <h1 id="matrix-hosting">Chat Hosting</h1>
            <div className="mxgrid">
                <div className="mxgrid__item50">
                    <h2 id="ems">Element Chat Services</h2>
                    <p><img style={{"height": "130px"}} src="/images/logo-ems-primary.svg" alt="" /></p>
                    <p><a href="https://ems.element.io/">ems.element.io</a></p>
                    <ul>
                        <li>Hosted Homeservers</li>
                        <li><a href="https://element.io/element-matrix-store">Hosted Integrations including Slack, IRC, Github bridging</a></li>
                    </ul>
                    <p>Brought to you by the creators of Chat, who have been running the biggest homeserver
                        in the network since 2014. Every homeserver comes with a custom instance of Element.</p>
                </div>

                <div className="mxgrid__item50">
                    <h2 id="ungleich">Ungleich.ch</h2>
                    <p><img style={{"border-radius": "9px", "height": "130px"}} src="/images/ungleich_zerocarbonmatrix.jpg" alt="" /></p>
                    <p><a href="https://matrix.zerocarbon.shop/">matrix.zerocarbon.shop</a></p>
                    <ul>
                        <li>Hosted Homeservers</li>
                    </ul>
                    <p>Service provided by <a href="https://ungleich.ch">ungleich.ch</a> in their own datacenter
                    in Linthal, Switzerland. They use an old building, second-hand servers, passive cooling and
                    are directly plugged into an on-site hydroelectric power plant!</p>
                </div>

                <div className="mxgrid__item50">
                    <h2 id="ungleich">etke.cc</h2>
                    <p><img style={{"border-radius": "9px", "height": "130px"}} src="/images/etke.cc.png" alt="" /></p>
                    <p><a href="https://etke.cc">etke.cc</a></p>
                    <ul>
                    <li>Setup of hosted Homeserver on any hosting by your choice</li>
                    <li>Setup of hosted integrations (full list available on website)</li>
                    <li>Setup of additional services (wireguard, languagetool, miniflux, etc.)</li>
                    <li>Ongoing maintenance of host & Chat components</li>
                    <li>Email services for your domain</li>
                    </ul>
                    <p>
                        That service will create your Chat Homeserver on your domain and server (doesn't matter if it's cloud 
                        provider or on an old laptop in the corner of your room), (optional) maintains it (server's system updates,
                        cleanup, security adjustments, tuning, etc.; Chat Homeserver updates & maintenance) and (optional)
                        provide full-featured email service for your domain.
                        <br /><br />
                        <a href="https://to.chat.imzqqq.top/#/#service:etke.cc">Chat Space</a>&nbsp;
                        | <a href="https://to.chat.imzqqq.top/#/#announcements:etke.cc">Announcements on Chat</a>
                    </p>
                </div>

                <div className="mxgrid__item50">
                    <h2 id="ungleich">GoMatrixHosting</h2>
                    <p><img style={{"border-radius": "9px", "height": "130px"}} src="/images/GoMatrixHosting.png" alt="" /></p>
                    <p><a href="https://gomatrixhosting.com">GoMatrixHosting.com</a></p>
                    <p>
                        Ansible AWX UI to:
                        <ul>
                            <li>Setup of hosted Homeserver on DigitalOcean or own on-premises server</li>
                            <li>Setup of hosted integrations (bridges and bots)</li>
                        </ul>

                        Members can be assigned a server from DigitalOcean, or they can connect their on-premises
                        server. This <a href="https://github.com/ansible/awx">AWX</a> system can manage the updates,
                        configuration, import and export, backups, and monitoring on its own. For more information see 
                        our <a href="https://gitlab.com/GoMatrixHosting">GitLab group</a> or
                        come <a href="https://to.chat.imzqqq.top/#/#general:gomatrixhosting.com">visit us on Chat</a>.
               
                    </p> </div>
            </div>
            <em>The providers listed all have a history of providing Chat services, though The Chat.org Foundation doesn't explicitly endorse these services.</em>
        </MXContentMain>
    </Layout>)
}


export const query = graphql`
{
    allFile(filter: { sourceInstanceName: { eq: "projects" } }) {
        
        edges {
            node {
                childMdx {
                    frontmatter {
                        title
                        maturity
                        description
                        thumbnail
                        featured
                        categories
                        language
                        author
                        repo
                        room
                        e2e
                    }
                    body
                }
                absolutePath
            }
        }
    }
}
`
export default Hosting
