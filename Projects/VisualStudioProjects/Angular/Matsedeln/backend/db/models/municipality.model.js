const mongoose = require('mongoose');

const MunicipalitySchema = new mongoose.Schema({
    municipalityName: {
        type: String,
        required: [true, "Municipality name required"],
        minlength: [2, "Municipality name length should be atleast 2"],
        trim: true
    },
    schools: [{
        schoolName: {
            type: String,
            required: [true, "School name required"],
            minlength: [1, "School name length should be atleast 1"],
            trim: true
        },
        _menuId: {
            type: String
        }
    }]
})

const Municipality = mongoose.model('Municipality', MunicipalitySchema);

module.exports = { Municipality }