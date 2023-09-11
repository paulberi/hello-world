const express = require('express');
const dotenv = require('dotenv');
const morgan = require('morgan');
const bodyparser = require("body-parser")
const path = require('path');
const { dirname } = require('path');


const app = express();

dotenv.config({ path: 'config.env' })
const PORT = process.env.PORT || 8080 //serves variables? check what it means

//log requests using the morgan model
app.use(morgan('tiny'));

//parse request to body-parser
app.use(bodyparser.urlencoded({ extended: true }))

//set view engine
app.set("view engine", "ejs") //where you have ejs can be html or anything you desire
    //app.set("views",path.resolve(--dirname,"views/ejs"))

//load assests folders
app.use('/css', express.static(path.resolve(--dirname, "assets/css")))
app.use('/img', express.static(path.resolve(--dirname, "assets/css")))
app.use('/js', express.static(path.resolve(--dirname, "assets/css")))
    //to access files in assests use ex css/style.css to access

app.get('/', (req, res) => { //request and response
    res.send("Crud Application");
})

app.listen(PORT, () => { console.log(`Server is running on http://localhost:${PORT}`) });