const mongoose= require('mongoose')

const userSchema = new mongoose.Schema({
    name:{
        type: String,
        required: true,
        min: 6,
        max: 255
    },
    email: {
        type: String,
        required: true,
        min: 6,
        max: 255
    },
    password: {
        type: String,
        required: true,
        max: 1024,
        min: 6
    },
    date: {
        type: Date,
        default: Date.now()
    },
    verified: {
        type: Boolean,
        default: false
    },
    department: {
        type: String,
        required: true
    },
    semester: {
        type: Number,
        required: true
    },
    contact: {
        type: String,
        required: true
    }
})

const User= mongoose.model('User', userSchema);
module.exports=User;
