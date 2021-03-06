import React from 'react';
import Helmet from 'react-helmet';

import Navigation from './Navigation';
import BottomNavigation from './BottomNavigation';

const IOSPrivacyPolicy = () => (
  <div className='browse-apps covenant'>
    <Navigation />

    <div className='container'>
      <h1>Flow for iOS Privacy Policy</h1>
      <p className='lead'>Last updated June 20, 2021</p>
      <hr />
      <p className='lead'>The Flow for iOS app does not collect or process any personal information from its users. The app is used to connect to third-party Flow servers that may or may not collect personal information and are not covered by this privacy policy. Each third-party Flow server comes equipped with its own privacy policy that can be viewed through the app or through that server's website.</p>
    </div>

    <BottomNavigation />

    <Helmet>
      <title>Privacy Policy - Flow for iOS</title>
      <meta property='og:title' content='Privacy Policy for Flow for iOS' />
    </Helmet>
  </div>
);

export default IOSPrivacyPolicy;
