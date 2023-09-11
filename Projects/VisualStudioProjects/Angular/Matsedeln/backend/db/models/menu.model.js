const mongoose = require('mongoose');

const MenuSchema = new mongoose.Schema({
    startDate: {
        type: Date,
        required: [true, "Menu must have an start date"]
    },
    endDate: {
        type: Date,
        required: [true, "Menu must have an end date"]
    },
    menuName: {
        type: String,
        required: [true, "Menu name required"],
        minlength: [1, "Menu name need to be min 1 characters long!"],
        maxlength: [40, "Menu name can be max 40 characters long!"],
        trim: true
    },
    meals: [{
        _id: {
            type: String,
            required: [true, "Meal id required"],
            minlength: [1, "Id too short"],
            trim: true
        },
        mealName: {
            type: String,
            required: [true, "Meal name required"],
            minlength: [1, "Meal name need to be min 1 characters long!"],
            maxlength: [85, "Meal name can be max 85 characters long!"],
            trim: true
        },
        mealDate: {
            type: Date,
            required: [true, "Meal Date required"]
        },
        foodSpecs: [{
            type: String,
            enum: {
                values: ['hot', 'pig', 'veg'],
                message: '{value} is not supported. hot, pig and veg is supported values'
            }
        }]
    }]
})

const Menu = mongoose.model('Menu', MenuSchema);

module.exports = { Menu }