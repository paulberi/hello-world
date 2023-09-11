const express = require('express');
const app = express();
var cors = require('cors');
const { mongoose } = require('./db/mongoose');
const User = require('./db/routes/user.route');
const privateRoute = require('./db/routes/private.route');
const publicRoute = require('./db/routes/public.route');

app.use(express.urlencoded({ extended: true }));
app.use(express.json()) // To parse the incoming requests with JSON payloads

var cors = require("cors");
app.use(cors());
app.use(cors({
    'allowedHeaders': ['sessionId', 'Content-Type'],
    'exposedHeaders': ['sessionId'],
    'origin': '*',
    'methods': 'GET,HEAD,PUT,PATCH,POST,DELETE',
    'preflightContinue': false
}));
app.use(express.static('./dist/'));


app.use('/private', privateRoute)
app.use('/public', publicRoute)

app.get('/*', function(req, res) {
    res.sendFile('index.html', { root: 'dist/' });
});

app.listen(process.env.PORT || 8080);