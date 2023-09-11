//https://dev.to/nyctonio/authentication-in-node-js-with-mongodb-bcrypt-and-jwt-web-tokens-with-cookies-hl3

const { User } = require('../models');
const patchValidation = { runValidators: true };
const jwt = require('jsonwebtoken');
var cookieParser = require('cookie-parser');

exports.doLogin = function(req, res) {
    let currentUser = new User(req.body);
    // we made a function to verify our user login
    User.find().then((users) => {
        users.forEach(user => {
            if (user.email == currentUser.email) {
                currentUser = user;
                console.log(currentUser);
                res.send(currentUser);
            }
        })
    })
    console.log(currentUser);
    return currentUser;
}

// user login function
const verifyUserLogin = async(email, password) => {
    try {
        const user = await User.findOne({ email }).lean()
        if (!user) {
            return { status: 'error', error: 'user not found' }
        }
        if (await bcrypt.compare(password, user.password)) {
            // creating a JWT token
            token = jwt.sign({ id: user._id, username: user.email, type: 'user' }, JWT_SECRET, { expiresIn: '2h' })
            return { status: 'ok', data: token }
        }
        return { status: 'error', error: 'invalid password' }
    } catch (error) {
        console.log(error);
        return { status: 'error', error: 'timed out' }
    }
}