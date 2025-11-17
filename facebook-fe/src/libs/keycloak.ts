import Keycloak from "keycloak-js";
// Setup Keycloak instance as needed
// Pass initialization options as required or leave blank to load from 'keycloak.json'
const keycloakConfig = {
  url: process.env.NEXT_PUBLIC_KEYCLOAK_URL || "http://localhost:8888",
  realm: process.env.NEXT_PUBLIC_KEYCLOAK_REALM || "",
  clientId: process.env.NEXT_PUBLIC_KEYCLOAK_CLIENT_ID || "",
};

const keycloak = new Keycloak(keycloakConfig);

export default keycloak;
