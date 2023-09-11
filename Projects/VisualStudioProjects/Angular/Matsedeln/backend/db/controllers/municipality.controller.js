const { Municipality } = require('../models');
const patchValidation = { runValidators: true };

exports.getMunicipalities = function(req, res) {
    // Return array of all districts in database
    Municipality.find().then((municipalities) => {
        res.send(municipalities);
    }).catch((e) => {
        res.send(e);
    });
}

exports.postMunicipality = function(req, res) {
    // Create district and return new district to user
    let municipalityName = req.body.municipalityName;
    let schools = req.body.schools;
    let newMunicipality = new Municipality({
        municipalityName,
        schools
    });
    newMunicipality.save().then((municipalityDoc) => {
        res.send(municipalityDoc);
    }).catch((e) => {
        res.send(e);
    });
}

exports.patchMunicipality = function(req, res) {
    Municipality.findOneAndUpdate({ _id: req.params.id }, {
            $set: req.body
        }, patchValidation).then(() => {
            res.sendStatus(200);
        })
        .catch((e) => {
            res.send(e);

        });
}

exports.patchSchool = function(req, res) {
    Municipality.findOneAndUpdate({
            _id: req.params.id,
            "schools._id": req.params.schoolId
        }, {
            $set: {
                'schools.$': req.body
            }
        }, patchValidation).then(() => {
            res.send({ message: 'Completed successfully' });
        })
        .catch((err) => {
            res.send(err);
        });
}

exports.deleteMunicipality = function(req, res) {
    Municipality.findOneAndRemove({
            _id: req.params.id
        }).then((removedMunicipalityDoc) => {
            res.send(removedMunicipalityDoc);
        })
        .catch((e) => {
            res.send(e);
        });
}

exports.getSchools = function(req, res) {
    Municipality.find().select({'_id': 0,'schools': 1}).then((schools) => {
        const allSchools = [];
        schools.forEach(school => {
            school.schools.forEach(element => {
                allSchools.push(element);
            });
        });
        res.send(allSchools);
    }).catch((e) => {
        res.send(e);
    });
}