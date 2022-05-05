import React from 'react'
import PropTypes from 'prop-types'
import styled from 'styled-components'

const Wrapper = styled.div`
  display: flex;
  margin: 6rem auto 0 auto;
  a {
    color: ${props => props.theme.colors.primary};
    display: flex;
    align-items: center;
  }
  justify-items: center;
`

const Prev = styled.div`
  span {
    text-transform: uppercase;
    font-size: 0.8rem;
    color: ${props => props.theme.colors.grey.light};
  }
`

const Next = styled.div`
  margin-left: auto;
  text-align: right;
  span {
    text-transform: uppercase;
    font-size: 0.8rem;
    color: ${props => props.theme.colors.grey.light};
  }
`

const PrevNext = ({ next, prev }) => (
  <Wrapper>
    {prev && (
      <Prev>
        <span>Previous</span>
        <a href={prev.fields.slug}>{prev.frontmatter.title}</a>
      </Prev>
    )}

    {next && (
      <Next>
        <span>Next</span>
        <a href={next.fields.slug}>{next.frontmatter.title}</a>
      </Next>
    )}
  </Wrapper>
)

export default PrevNext

PrevNext.propTypes = {
  next: PropTypes.object,
  prev: PropTypes.object,
}

PrevNext.defaultProps = {
  next: null,
  prev: null,
}
