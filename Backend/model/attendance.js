const mongoose= require('mongoose')

const attendanceSchema = new mongoose.Schema({
    date: {
        type: String,
        required: true
    },
    department: {
        type: String,
        required: true
    },
    semester: {
        type: String,
        required: true
    },
    subject: {
        type: String,
        required: true
    },
    pictures: {
        type: [String],
        required: true
    }
})

const Attendance= mongoose.model('Attendance', attendanceSchema);
module.exports=Attendance;