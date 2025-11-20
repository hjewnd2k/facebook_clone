'use client';

import { ReactKeycloakProvider } from '@react-keycloak/web';
import { ReactNode } from 'react';

import keycloak from '@/libs/keycloak';

type KeycloakProviderProps = {
  children: ReactNode;
};

export default function KeycloakProvider({ children }: KeycloakProviderProps) {
  return (
    <ReactKeycloakProvider
      authClient={keycloak}
      initOptions={{
        onLoad: 'check-sso', // hoặc 'login-required'
        // silentCheckSsoRedirectUri:
        //   window?.location?.origin + '/silent-check-sso.html',
        pkceMethod: 'S256',
      }}
    >
      {children}
    </ReactKeycloakProvider>
  );
}
