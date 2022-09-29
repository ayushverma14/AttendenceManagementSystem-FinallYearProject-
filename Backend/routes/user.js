const router = require('express').Router();
const UserController = require('../controllers/user');
const verify = require('../middleware/verify-token')


//register new user
router.post('/register', UserController.user_register);

//login user
router.post('/login', UserController.user_login);

//verify user email
router.post('/verify/:us', UserController.user_verify);

//finding one user
router.get('/users/:id', verify, UserController.user_find_one)


module.exports=router