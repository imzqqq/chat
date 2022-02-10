import React from 'react'
import PropTypes from 'prop-types'
import {  graphql } from 'gatsby'
import styled from 'styled-components'
import MDXRenderer from 'gatsby-mdx/mdx-renderer'

import { Layout, SEO, MXContentMain } from '../components'

const Title = styled.h1`
`

const PostContent = styled.div`
`

const Page = ({ pageContext: { slug }, data: { mdx: postNode } }) => {
  const post = postNode.frontmatter

  return (
    <Layout hasNavPadding="true" customSEO>
        <SEO postPath={slug} postNode={postNode} article />
        <MXContentMain>
          <div class="mxcontent__main__doc">
            <Title>{post.title}</Title>
            <PostContent>
              <MDXRenderer>{postNode.body}</MDXRenderer>
            </PostContent>
          </div>
        </MXContentMain>
    </Layout>
  )
}

export default Page

Page.propTypes = {
  pageContext: PropTypes.shape({
    slug: PropTypes.string.isRequired
  }),
  data: PropTypes.shape({
    mdx: PropTypes.object.isRequired,
  }).isRequired,
}

Page.defaultProps = {
  pageContext: PropTypes.shape({  }),
}

export const pageQuery = graphql`
  query noNavPageBySlug($slug: String!) {
    mdx(fields: { slug: { eq: $slug } }) {
      body
      excerpt
      frontmatter {
        title
        date(formatString: "YYYY-MM-DD")
        author,
        image
      }
      tableOfContents
      timeToRead
      rawBody
      parent {
        ... on File {
          mtime
          birthtime
        }
      }
    }
  }
`
