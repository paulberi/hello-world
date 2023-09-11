const express = require('express');
const router = express.Router();

const{ User } = require('../models');


router.get('/', (req, res) => {
    User.find().then((users) => {
        res.send(users);
    }).catch((e) => {
        res.send(e);
    });
})

router.post('/', (req, res) => {
    let firstName = req.body.firstName;
    let lastName = req.body.lastName;
    let email = req.body.email;
    let password = req.body.password;
    let districts = req.body.districts;
    let newUser = new User({
        firstName,
        lastName,
        email,
        password,
        districts
    });
    newUser.save().then((userDoc) => {
        res.send(userDoc);
    })
})

router.patch('/:id', (req, res) => {
    User.findOneAndUpdate({ _id: req.params.id }, {
        $set: req.body
    }).then(() => {
        res.sendStatus(200);
    });
})

router.delete('/:id', (req, res) => {
    User.findOneAndRemove({
        _id: req.params.id
    }).then((removedUserDoc) => {
        res.send(removedUserDoc);
    })
})

module.exports = router