const express = require("express");
const app = express();
const MongoClient = require('mongodb').MongoClient;

var api=require('./index.js');
const config=require("./config.json");

app.set("port", process.env.PORT || 3001);

// Express only serves static assets in production
if (process.env.NODE_ENV === "production") {
  app.use(express.static("client/build"));
}

var testApp=new api(app);
var url = config.db.client+'://'+config.db.connection.host+':'+config.db.connection.port+'/'+config.db.connection.database;
console.log(url);
MongoClient.connect(url, { 
  useNewUrlParser: true
 }, (err, database) => {
  if (err) {
    console.warn(`Failed to connect to the database. ${err.stack}`);
  }
  else{
    app.locals.db = database.db(config.db.connection.database);
    app.listen(app.get("port"), () => {
      console.log(`Find the server at: http://localhost:${app.get("port")}/`); // eslint-disable-line no-console
    });
  }
});


