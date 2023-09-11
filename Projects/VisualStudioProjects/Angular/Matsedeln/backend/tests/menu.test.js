const mongoose = require('mongoose');
const dbHandler = require('./db-handler');
const menuController = require('../db/controllers/menu.controller');

beforeAll(async() => await dbHandler.connect());

afterEach(async() => await dbHandler.clearDatabase());

afterAll(async() => await dbHandler.closeDatabase());

describe('Menu', () => {

    it('Can post menu', async() => {
        expect(() => menuController.postMenu(menu, res))
            .not
            .toThrow();
    });

    it('Can delete menu', async() => {
        expect(() => menuController.deleteMenu(menu, res))
            .not
            .toThrow();
    });

    it('Can patch menu', async() => {
        expect(() => menuController.patchMenu(menu, res))
            .not
            .toThrow();
    });

    it('Can get menu names', async() => {
        expect(() => menuController.getMenuNames(menu, res))
            .not
            .toThrow();
    });

    it('Can get menu with ID', async() => {
        expect(() => menuController.getMenuWithId(menu, res))
            .not
            .toThrow();
    });

    it('Can get menu name with ID', async() => {
        expect(() => menuController.getMenuNameWithId(menu, res))
            .not
            .toThrow();
    });

    it('Can delete meal', async() => {
        expect(() => menuController.deleteMeal(menu, res))
            .not
            .toThrow();
    });

    it('Can post meal', async() => {
        expect(() => menuController.postMeal(menu, res))
            .not
            .toThrow();
    });

    it('Can patch meal', async() => {
        expect(() => menuController.patchMeal(meal, res))
            .not
            .toThrow();
    });

    it('Cant post menu with no name', async() => {
        menuController.postMenu(menuWithNoName, {
            send(err) {
                if (err != undefined) {
                    expect(err.message).toContain(" Menu name required");
                }
            }
        })
    });
    it('Cant post menu with meals with unsupported foodspecs', async() => {
        menuController.postMenu(menuWithBadFoodspec, {
            send(err) {
                if (err != undefined) {
                    expect(err.message).toContain("is not supported. hot, pig and veg is supported values");
                }
            }
        })
    });
    it('cant patch menu with no menuName', async() => {
        menuController.patchMenu(menuNoName, {
            send(err) {
                if (err != undefined) {
                    expect(err.message).toContain("Menu name required");
                }
            }
        })
    })


});

const menu = {
    body: {
        id: mongoose.Types.ObjectId(),
        startDate: new Date('2021-05-03'),
        endDate: new Date('2021-05-31'),
        menuName: 'Matsedel test',
        meals: [{ _id: 'abc123', mealName: 'Korv', mealDate: new Date('2021-05-26') }]
    },
    params: { id: mongoose.Types.ObjectId() }
};

const meal = {
    body: {
        _id: 'abc123',
        mealName: 'Korv',
        mealDate: new Date('2021-05-26')
    },
    params: { id: mongoose.Types.ObjectId() }
}

const menuWithNoName = {
    body: {
        id: mongoose.Types.ObjectId(),
        startDate: new Date('2021-05-03'),
        endDate: new Date('2021-05-31'),
        menuName: '',
        meals: [{ _id: 'abc123', mealName: 'Korv', mealDate: new Date('2021-05-26') }]
    },
    params: { id: mongoose.Types.ObjectId() }
};

const menuWithBadFoodspec = {
    body: {
        id: mongoose.Types.ObjectId(),
        startDate: new Date('2021-05-03'),
        endDate: new Date('2021-05-31'),
        menuName: 'namnet',
        meals: [{ _id: 'abc123', mealName: 'Korv', mealDate: new Date('2021-05-26'), foodSpecs: ['kyckling'] }]
    },
    params: { id: mongoose.Types.ObjectId() }
};
const menuNoName = {
    body: { menuName: '' },
    params: { id: mongoose.Types.ObjectId() }
};

const res = {
    send(doc) {
        let isErr = false;
        try {
            isErr = doc instanceof mongoose.Error;
        } catch (err) {}
        if (isErr) {
            throw (doc);
        }
    },
    sendStatus(statusNumber) {}
}