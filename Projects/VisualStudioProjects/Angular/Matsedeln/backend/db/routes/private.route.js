const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');

const { Menu } = require('../models');
const { postMenu, patchMenu, deleteMenu, deleteMeal, postMeal, patchMeal } = require('../controllers/menu.controller');
const { postMunicipality, patchMunicipality, patchSchool, deleteMunicipality } = require('../controllers/municipality.controller');
const { postUser, patchUser, deleteUser, getUser } = require('../controllers/user.controller');

router.post('/menu', postMenu)

router.patch('/menu/:id', patchMenu)

router.delete('/menu/:id', deleteMenu)

router.delete('/menu/:id/meal/:mealId', deleteMeal)

router.post('/menu/:id/meal/', postMeal)

router.patch('/menu/:id/meal/:mealId', patchMeal)


router.post('/municipality', postMunicipality)

router.patch('/municipality/:id', patchMunicipality)

router.patch('/municipality/:id/school/:schoolId', patchSchool)

router.delete('/municipality/:id', deleteMunicipality)

router.post('/user', postUser)

router.patch('/user/:id', patchUser)

router.delete('/user/:id', deleteUser)

router.get('/user', getUser)

module.exports = router