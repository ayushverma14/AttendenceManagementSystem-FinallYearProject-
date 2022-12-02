const router = require('express').Router();
const AdminController = require('../controllers/admin');
const verify = require('../middleware/verify-token')

console.log("admin route reached")

//upload pictures
router.post('/upload', AdminController.upload_pictures);
router.get('/download', AdminController.download_pictures);

module.exports=router