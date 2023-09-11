const mongoose = require('mongoose');
const dbHandler = require('./db-handler');
const municipalityController = require('../db/controllers/municipality.controller');

beforeAll(async() => await dbHandler.connect());

afterEach(async() => await dbHandler.clearDatabase());

afterAll(async() => await dbHandler.closeDatabase());

describe('Municipality', () => {

    // Lägg till toThrow() kanske möjligtvis litegrann
    it('Can post municipality', async() => {
        expect(() => municipalityController.postMunicipality(municipality, res))
            .not
            .toThrow();
    });

    it('Can get municipalities', async() => {
        expect(() => municipalityController.getMunicipalities(municipality, res))
            .not
            .toThrow();
    });

    it('Can patch municipalities', async() => {
        expect(() => municipalityController.patchMunicipality(municipality, res))
            .not
            .toThrow();
    });

    it('Can delete municipalities', async() => {
        expect(() => municipalityController.deleteMunicipality(municipality, res))
            .not
            .toThrow();
    });

    it('Can patch schools', async() => {
        expect(() => municipalityController.patchSchool(school, res))
            .not
            .toThrow();
    });
    it('Cant post municipality with short name', async() => {
        municipalityController.postMunicipality(municipalityWithShortName, {
            send(err) {
                expect(err.message).toContain("Municipality name length should be atleast 2")
            }
        })
    });
    it('Cant post municipality with no name', async() => {
        municipalityController.postMunicipality(municipalityWithNoName, {
            send(err) {
                expect(err.message).toContain("Municipality name required")
            }
        })
    });
    it('Cant patch municipality with no name', async() => {
        municipalityController.patchMunicipality(municipalityWithNoName, {
            send(err) {
                expect(err.message).toContain("Municipality name required")
            }
        })
    });


});

const municipality = {
    body: {
        id: mongoose.Types.ObjectId(),
        municipalityName: 'Bjur',
        schools: [{ id: mongoose.Types.ObjectId(), schoolName: 'Skolan' }]
    },
    params: { id: mongoose.Types.ObjectId() }
};
const school = {
    body: {
        id: mongoose.Types.ObjectId(),
        schoolName: 'Skolan'
    },
    params: { id: mongoose.Types.ObjectId() }

}

const municipalityWithShortName = {
    body: {
        id: mongoose.Types.ObjectId(),
        municipalityName: 'a',
        schools: []
    },
    params: { id: mongoose.Types.ObjectId() }
};

const municipalityWithNoName = {
    body: {
        id: mongoose.Types.ObjectId(),
        municipalityName: '',
        schools: []
    },
    params: { id: mongoose.Types.ObjectId() }
};

const res = {
    send(doc) {
        let isErr = false;
        try {
            isErr = doc instanceof mongoose.Error;
        } catch (err) {}
        if (isErr) {
            throw new Error(doc.message);
        }
    },

    sendStatus(statusNumber) {}
}