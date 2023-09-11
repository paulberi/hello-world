const mongoose = require('mongoose');
const bcrypt = require('bcrypt');

const UserSchema = new mongoose.Schema({
    firstName: {
        type: String,
        trim: true
    },
    lastName: {
        type: String,
        required: [true, "User must have a last name"],
        minlength: [1, "Users last name must be at least 1 character long!"],
        trim: true
    },
    email: {
        type: String,
        required: [true, "User must have an email"],
        minlength: [5, "Users email must be at least 5 character long!"],
        trim: true,
        unique: true
    },
    password: {
        type: String,
        required: [true, "User must have a password"],
        minlength: [5, "Users password must be at least 5 characters long!"],
        trim: true
    },
    schoolIds: [{
        type: mongoose.Types.ObjectId
    }],
    permissions: [{
        type: String,
        enum: {
            values: ['admin'],
            message: 'value is not supported'

        }
    }],
    menuIds: [{
        type: mongoose.Types.ObjectId
    }],
})

UserSchema.pre('save', function(next) {
    let user = this;
    let costFactor = 10;

    if (user.isModified('password')) {
        // if the password field has been edited/changed then run this code.

        // Generate salt and hash password
        bcrypt.genSalt(costFactor, (err, salt) => {
            bcrypt.hash(user.password, salt, (err, hash) => {
                user.password = hash;
                next();
            })
        })
    } else {
        next();
    }
});

const User = mongoose.model('User', UserSchema);

module.exports = { User }