FROM quay.io/keycloak/keycloak:19.0.2

ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true
ENV KC_DB=postgres
ENV KC_HTTP_RELATIVE_PATH /auth

# Nya GUIet buggar, disabla det
ENV KC_FEATURES_DISABLED=admin2

# Cacha bara lokalt. Måste ändras om vi kör mer än en keycloak-instans
ENV KC_CACHE=local

COPY theme/metria /opt/keycloak/themes/metria
COPY  theme/miljokoll /opt/keycloak/themes/miljokoll
COPY theme/raa /opt/keycloak/themes/raa
COPY theme/mitt-metria /opt/keycloak/themes/mitt-metria

RUN /opt/keycloak/bin/kc.sh build

WORKDIR /opt/keycloak

# Lite entrypoint-specialare för att kunna sätta enviroment variabler från docker secrets
COPY entrypoint.sh /

HEALTHCHECK \
    --start-period=1000s \
    CMD curl --fail -v 'http://localhost:8080/auth/health' || exit 1
ENTRYPOINT ["/entrypoint.sh"]
