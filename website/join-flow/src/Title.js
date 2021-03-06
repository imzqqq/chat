import React from 'react';
import PropTypes from 'prop-types';
import { defineMessages, injectIntl } from 'react-intl';
import Helmet from 'react-helmet';

const messages = defineMessages({
  title: { id: 'title', defaultMessage: 'Giving social networking back to you - Flow' }
});

const Title = ({ intl }) => (
  <Helmet>
    <title>{intl.formatMessage(messages.title)}</title>

    <meta property='og:title' content='Flow' />
    <meta name='description' content='Flow is an open source decentralized social network - by the people for the people. Join the federation and take back control of your social media!' />
    <meta property='og:description' content='Flow is an open source decentralized social network - by the people for the people. Join the federation and take back control of your social media!' />
  </Helmet>
);

Title.propTypes = {
  intl: PropTypes.object.isRequired,
}

export default injectIntl(Title);
