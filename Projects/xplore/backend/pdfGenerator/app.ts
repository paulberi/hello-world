import cookieParser from "cookie-parser";
import express from "express";
import createError from "http-errors";
import morgan, {StreamOptions} from "morgan";
import path from "path";

import Logger from "./config/logging";

// Override the stream method by telling
// Morgan to use our custom logger instead of the console.log.
const stream: StreamOptions = {
  // Use the http severity
  write: (message) => Logger.http(message),
};


let indexRouter = require("./routes/index");
let pdfRouter = require("./routes/pdf");


let app = express();

// view engine setup
app.set("views", path.join(__dirname, "../views"));
app.set("view engine", "jade");

app.use(morgan("combined", {stream}));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, "public")));

app.use("/", indexRouter);
app.use("/pdf", pdfRouter.router);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get("env") === "development" ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render("error");
});

Logger.info("pdfGenerator started");

module.exports = app;
