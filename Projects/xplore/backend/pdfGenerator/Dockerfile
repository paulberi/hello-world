# It's not super easy to get pupeteer running in a docker environment
# This docker file is a bit of a mix between
# https://paul.kinlan.me/hosting-puppeteer-in-a-docker-container/
# and
# https://vsupalov.com/headless-chrome-puppeteer-docker/
# and running node 12 instead of 8.
FROM node:16

RUN \
    sed -i -e's/ main/ main contrib non-free/g' /etc/apt/sources.list \
    && apt-get -q update

# for https
RUN apt-get install -yyq ca-certificates
# install libraries
RUN apt-get install -yyq libappindicator1 libasound2 libatk1.0-0 libc6 libcairo2 libcups2 libdbus-1-3 libexpat1 libfontconfig1 libgcc1 libgconf-2-4 libgdk-pixbuf2.0-0 libglib2.0-0 libgtk-3-0 libnspr4 libnss3 libpango-1.0-0 libpangocairo-1.0-0 libstdc++6 libx11-6 libx11-xcb1 libxcb1 libxcomposite1 libxcursor1 libxdamage1 libxext6 libxfixes3 libxi6 libxrandr2 libxrender1 libxss1 libxtst6
# tools
RUN apt-get install -yyq gconf-service lsb-release wget xdg-utils
# and fonts
RUN apt-get install -yyq fonts-liberation
RUN echo "ttf-mscorefonts-installer msttcorefonts/accepted-mscorefonts-eula select true" | debconf-set-selections
RUN apt-get install -yyq ttf-mscorefonts-installer
RUN apt-get install -y libgbm-dev

RUN apt-get install -y dumb-init

RUN apt-get install -y curl

RUN groupadd -r pptruser && useradd -r -g pptruser -G audio,video pptruser \
    && mkdir -p /home/pptruser/Downloads \
    && chown -R pptruser:pptruser /home/pptruser

WORKDIR /home/pptruser
USER pptruser

COPY package.json .
COPY package-lock.json .
RUN npm ci
COPY . .

RUN npx tsc

HEALTHCHECK \
    --retries=1 \
    CMD curl --fail -o test.pdf -v -X POST -H "Content-Type: application/json" -d '{"url": "http://localhost:3000/"}' 'http://localhost:3000/pdf' || exit 1
ENTRYPOINT ["dumb-init", "--"]
CMD ["node", "./dist/bin/www"]
