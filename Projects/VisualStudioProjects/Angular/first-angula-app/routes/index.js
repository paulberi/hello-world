var express = require('express');
var router = express.Router();
var mongo = require('mongodb');
var assert = require('assert');

var url = 'mongodb://localhost:27017/test';
// Get home page
router.get('/', function(req, res, next) {
    res.render('index', { title: 'Cool, huh!', condition: true, anyArray: [1, 2, 3] });
});
router.get('/get-data', function(req, rex, next) {

    var reslutArray = [];

    mongo.connect(url, function(err, db) {
        assert.equal(null, err);
        var cursor = db.collection('user-data').find();
        cursor.forEach(function(doc, err) {
            assert.equal(null, err);
            reslutArray.push(doc);
        }, function() {
            db.close();
            res.render('index', { items: reslutArray });
        });
    });
});

router.post('insert', function(req, rex, next) {
    var item = {
        inputTodo: req.body.inputTodo
    }
});

mongo.connect(url, function(err, db) {
    assert.equal(null, err);
    db.collection('user-data').insertOne(item, function(err, result) {
        assert.equal(null, error);
        console.log('Item inserted');
        db.close();
    })
})

res.redirect('/')
router.post('update', function(req, rex, next) {

});
router.post('delete', function(req, rex, next) {

});

module.exports = router;
