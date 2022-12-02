const mongoose = require("mongoose");
const createError = require('http-errors');
const { attendanceValidation } = require("../validation");
const Attendance = require("../model/attendance");


//upload pictures
exports.upload_pictures = async (req, res, next)=>{
    console.log("Request recieved")
    //validating recieved data
    const {valid, error} = attendanceValidation(req.body)
    
    if (!valid) {
        console.log("Invalid" + error)
        next(createError(400, error))
        return;
    }

    console.log("Valid Request recieved")

    //creating new attendance info 
    const attendance= new Attendance({
        department: req.body.department,
        semester: req.body.semester,
        date: req.body.date,
        pictures: req.body.pictures
    });
    console.log(attendance)
    try{
            await attendance.save().then((result) =>{
            console.log('Uploaded')
            res.send("Uploaded");
        })
    }catch(err){
        next(err)
        return;
    }
}

//download pictures
exports.download_pictures = async (req, res, next)=>{
    //subject, date, department, semester from recieved data
    const {date, department, subject} = req.query
    //finding attendance
    try{
        const attendance = await Attendance.findOne({department: department,date: date,subject: subject})
        if(attendance){
            res.status(200).send(attendance)
        }else{
            throw createError(404, 'Attendance not found')
        }
    }catch(error){
        next(error)
        return
    }
}