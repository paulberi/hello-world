// This file will handle connection logic to the MongoDB database

const mongoose = require('mongoose');

// To prevent deprecation warnings (from MongoDB native driver)
mongoose.set('useCreateIndex', true);
mongoose.set('useFindAndModify', false);
mongoose.set('useUnifiedTopology', true);

// mongoose.Promise = global.Promise;

//mongoose.connect('mongodb+srv://lia-user:cjqnelHL8qaNuEq5@matsedel.oudbn.mongodb.net/Menu', { 
mongoose.connect('mongodb+srv://paulberi:myfans83@cluster0.bwv65.mongodb.net/test', {
    useNewUrlParser: true
}).then(() => {
    console.log("Connected to MongoDB successfully.");
}).catch((e) => {
    console.log("Error while attempting to connect to MongoDB.");
    console.log(e);
    // Lägg in loggning av felkoder?!
});

module.exports = {
    mongoose
};