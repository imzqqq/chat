import React from 'react';
import { Link } from 'react-router-dom';
import { FormattedMessage } from 'react-intl';

import LanguageSelectContainer from './LanguageSelectContainer';
import Dropdown from './Dropdown';

import mastodonLogo from './assets/logo_full.svg';

const options = [
  { value: 'https://blog.imzqqq.top/', label: <FormattedMessage id='nav.blog' defaultMessage='Blog' />},
  { value: 'https://discourse.imzqqq.top', label: <FormattedMessage id='nav.support' defaultMessage='Support' /> },
  { value: 'https://flow.docs.imzqqq.top', label: <FormattedMessage id='nav.docs' defaultMessage='Documentation' /> },
  { value: 'https://github.com/imzqqq', label: <FormattedMessage id='nav.merch' defaultMessage='Merch' /> },
];

const Navigation = () => (
  <div className='navbar container'>
    <ul className='left'>
      <li>
        <Link className='brand' to='/'>
          <img className='link-logo' src={mastodonLogo} alt='Flow'/>
        </Link>
      </li>
    </ul>

    <ul className='right'>
      <li><Link to='/apps'><FormattedMessage id='nav.apps' defaultMessage='Apps' /></Link></li>
      <li><Link to='/sponsors'><FormattedMessage id='nav.sponsors' defaultMessage='Sponsors' /></Link></li>
      <li><a href='https://github.com/imzqqq'><FormattedMessage id='nav.code' defaultMessage='Code' /></a></li>
      <li><Dropdown asLinks label={<FormattedMessage id='nav.resources' defaultMessage='Resources' />} options={options} /></li>
      <li><LanguageSelectContainer /></li>
    </ul>
  </div>
);

export default Navigation;
