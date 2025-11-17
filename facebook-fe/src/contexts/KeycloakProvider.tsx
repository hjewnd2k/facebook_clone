"use client";

import keycloak from "@/libs/keycloak";
import { ReactKeycloakProvider } from "@react-keycloak/web";
import { ReactNode } from "react";

type KeycloakProviderProps = {
  children: ReactNode;
};

export default function KeycloakProvider({ children }: KeycloakProviderProps) {
  return (
    <ReactKeycloakProvider
      authClient={keycloak}
      initOptions={{
        onLoad: "check-sso", // hoặc 'login-required'
        silentCheckSsoRedirectUri:
          window.location.origin + "/silent-check-sso.html",
        pkceMethod: "S256",
      }}
    >
      {children}
    </ReactKeycloakProvider>
  );
}
