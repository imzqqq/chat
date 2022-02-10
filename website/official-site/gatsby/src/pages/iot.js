import React from 'react'
import { graphql } from 'gatsby'

import Helmet from 'react-helmet'
import { Layout, MXContentMain } from '../components'
import config from '../../config'

const title = "Internet of Things (IoT)";

const Iot = ({ data }) => {
  const projects = data.allMdx.edges
    .map((edge => {
      var result = edge.node.frontmatter;
      result.slug = edge.node.fields.slug;
      return result;
    }));
  return (<Layout navmode="discover">
    <MXContentMain>
      <Helmet title={`${title} | ${config.siteTitle}`} />
      <div className="mxblock mxblock--project">
        <h1 className="mxblock--project__hx">{title}</h1>
        {
          projects
            .map(function (project, i) {
              return (
                <div key={project.slug} className="mxgrid">
                  {i % 2 !== 0 &&
                    <>
                      <div className="mxgrid__item25"><img src={project.thumbnail} alt={project.title} /></div>
                      <div className="mxgrid__item75">
                        <h2><a href={project.slug}>{project.title}</a></h2>
                        <p><a href={project.repo}>{project.repo}</a></p>
                        <p><a href={"https://to.chat.dingshunyu.top/#/" + project.room}>{project.room}</a></p>
                        <p>{project.description}</p>
                      </div>
                    </>
                  }
                  {i % 2 === 0 &&
                    <>
                      <div className="mxgrid__item75" style={{"textAlign": "right"}}>
                        <h2><a href={project.slug}>{project.title}</a></h2>
                        <p><a href={project.repo}>{project.repo}</a></p>
                        <p><a href={"https://to.chat.dingshunyu.top/#/" + project.room}>{project.room}</a></p>
                        <p>{project.description}</p>
                      </div>
                      <div className="mxgrid__item25"><img src={project.thumbnail} alt={project.title} /></div>
                    </>
                  }
                </div>
              )
            })}
      </div>
    </MXContentMain>
  </Layout>)
}


export const query = graphql`{
  allMdx(sort: {fields: frontmatter___sort_order, order: ASC},
    filter: {frontmatter: {categories: {in: ["iot"]}}}) {
    edges {
      node {
        frontmatter {
          title
          maturity
          description
          thumbnail
          language
          author
          repo
          room
          screenshot
          sort_order
        }
        fields {
          slug
        }
      }
    }
  }
}`
export default Iot
