const mongoose = require('mongoose');
const dbHandler = require('../db-handler');
const userController = require('../../db/controllers/user.controller');
const { fakeUserData } = require('./index');
// const chai = require('chai');
// const expect = chai.expect;
const {
    validateNotEmpty,
    validateStringEquality,
    validateMongoDuplicationError,
} = require('./validators.utils.');
const { TestWatcher } = require('@jest/core');
const { User } = require('../../db/models');

beforeAll(async() => await dbHandler.connect());

afterEach(async() => await dbHandler.clearDatabase());

afterAll(async() => await dbHandler.closeDatabase());

describe('User model', () => {
    test('should post a new user successfully', async() => {
        const validUser = new User({
            local: fakeUserData,
            lastName: fakeUserData.lastName,
            email: fakeUserData.email,
            password: fakeUserData.password,
            permissions: fakeUserData.permissions,
        });
        const postedUser = await validUser.save();

        validateNotEmpty(postedUser);

        validateStringEquality(postedUser.permissions, fakeUserData.permissions);
        validateStringEquality(postedUser.email, fakeUserData.email);
        validateStringEquality(postedUser.firstName, fakeUserData.firstName);
        validateStringEquality(postedUser.lastName, fakeUserData.lastName);
        validateStringEquality(postedUser.password, fakeUserData.password);
    });

    test('should validate MongoError duplicate error with code 11000', async() => {
        expect.assertions(2);
        const validUser = new User({
            local: fakeUserData,
            permissions: fakeUserData.permissions
        });

        try {
            await validUser.save();
        } catch (error) {
            const { name, code } = error;
            console.log(name, code)
            validateMongoDuplicationError(name, code);
        }

    });

});