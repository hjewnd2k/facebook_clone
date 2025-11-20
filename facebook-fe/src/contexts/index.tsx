'use client';

import React from 'react';

import KeycloakProvider from './KeycloakProvider';
import QueryProvider from './QueryProvider';
import UserLoader from './UserLoader';

const RootProvider = ({ children }: { children: React.ReactNode }) => {
  return (
    <QueryProvider>
      <KeycloakProvider>
        <UserLoader />
        {children}
      </KeycloakProvider>
    </QueryProvider>
  );
};

export default RootProvider;
