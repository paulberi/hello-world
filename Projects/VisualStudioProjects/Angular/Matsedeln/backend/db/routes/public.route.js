const express = require('express');
const router = express.Router();

const { getMenus, getMenuNames, getMenuWithId, getMenuNameWithId } = require('../controllers/menu.controller');
const { getMunicipalities, getSchools } = require('../controllers/municipality.controller');

const { doLogin } = require('../controllers/login');

router.post('/login', doLogin)

router.get('/menu', getMenus)

router.get('/menu/Name', getMenuNames)

router.get('/menu/:id', getMenuWithId)

router.get('/menu/Name/:id', getMenuNameWithId)


router.get('/municipality', getMunicipalities)

router.get('/schools', getSchools)

module.exports = router