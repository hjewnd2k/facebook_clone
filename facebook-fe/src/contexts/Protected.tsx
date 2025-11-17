"use client";

import { useKeycloak } from "@react-keycloak/web";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

export default function Protected({ children }: { children: React.ReactNode }) {
  const { keycloak, initialized } = useKeycloak();
  const router = useRouter();

  useEffect(() => {
    if (initialized && !keycloak.authenticated) {
      keycloak.login();
    }
  }, [initialized, keycloak, router]);

  if (!initialized) return <div>Loading...</div>;
  if (!keycloak.authenticated) return null;

  return <>{children}</>;
}
