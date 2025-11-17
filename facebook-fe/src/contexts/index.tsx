"use client";

import React from "react";
import KeycloakProvider from "./KeycloakProvider";
import QueryProvider from "./QueryProvider";
import AxiosInitializer from "./AxiosInitializer";

const RootProvider = ({ children }: { children: React.ReactNode }) => {
  return (
    <QueryProvider>
      <KeycloakProvider>
        <AxiosInitializer />
        {children}
      </KeycloakProvider>
    </QueryProvider>
  );
};

export default RootProvider;
