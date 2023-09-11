FROM ubuntu:18.04
RUN apt-get update
# In addition to mapproxy itself, the web server gunicorn is used to serve the application
RUN apt-get install -y mapproxy gunicorn python-eventlet
COPY config /mapproxy
WORKDIR /mapproxy
#CMD ["mapproxy-util", "serve-develop", "-b", "0.0.0.0", "mapproxy.yaml"]
CMD gunicorn -k eventlet -w 4 -b :8080 config:application --no-sendfile

