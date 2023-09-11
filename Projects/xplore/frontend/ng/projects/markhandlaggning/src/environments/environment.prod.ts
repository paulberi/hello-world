export const environment = {
  production: true,
  projektUrl: "/api/projekt",
  hamtaAvtalUrl: "/api/avtal/",
  authIssuer: window.location.origin + "/auth/realms/Markkoll",
  keyCloakClientId: "markhandlaggning",
  keyCloakScope: "openid profile email offline_access",
  allowedKeyCloakRole: "markhandlaggning_user",
};
