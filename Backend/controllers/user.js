const mongoose = require("mongoose");
const jwt=require('jsonwebtoken')
const bcrypt=require('bcryptjs')
const nodemailer=require('nodemailer')
const {v4: uuidv4} = require("uuid")


const User=require('../model/user');
const {registerValidation, loginValidation} = require('../validation')
const UserVerifcation = require('../model/user-verification');
const createError = require('http-errors');

//nodemailer transpoter
let transporter = nodemailer.createTransport({
    host: "smtp.gmail.com",
    service: "gmail",
    auth: {
        user: process.env.AUTH_EMAIL,
        pass: process.env.AUTH_PASS,
    }
})


// testing nodemailer
transporter.verify((error, success) =>{
    if(error){
        console.log(error)
    }else{
        console.log("Ready for messages")
        console.log(success)
    }
})

//find one user
exports.user_find_one = async (req, res, next)=>{
    const {id} = req.params
    //finding user
    try{
        const user= User.findOne({_id: id})
        if(user){
            res.status(200).send(user) 
        }else{
            throw createError(404, 'Not found')
        }
    }catch(error){
        next(error)
        return
    }

}

// registering new user
exports.user_register= async (req, res, next)=>{
    //validating user data
    const {valid, error} = registerValidation(req.body)
    
    if (!valid) {
         next(createError(400, error))
         return;
    }

    //checking if the user already exsist

    try{
        const emailExist= await User.findOne({email: req.body.email})

        if(emailExist){
            throw createError(400, "Email already exist")
            return;
        }
    }catch(err){
        next(err)
        return;
    }

    //hash password
    const salt= await bcrypt.genSalt(10);
    const hashedPassword= await bcrypt.hash(req.body.password, salt)

    //creating new user
    const user= new User({
        name: req.body.name,
        email: req.body.email,
        password: hashedPassword
    });
    console.log(user)
    try{
            await user.save().then((result) =>{
            console.log('Sending email')
            sendVerificationEmail(result, res);
        })
    }catch(err){
        next(err)
        return;
    }
}

//login new user
exports.user_login = async (req, res, next)=>{
    //validating user data
    const {valid, error} = loginValidation(req.body)
    
    if (!valid) {
        next(createError(400, error))
        return;
    }

    //checking if the email exsist
    try{
        const user= await User.findOne({email: req.body.email})

        if(!user){
            throw createError(404, "Incorrect Email")
        }

        if(!user.verified){
            next(createError(401, "Email not verified"))
            return;
        }

        const validPass = await bcrypt.compare(req.body.password, user.password)
        if(!validPass){
            next(createError(400, "Incorrect password"))
            return;
        }

        //create web token
        const token = jwt.sign({_id: user._id}, process.env.TOKEN_SECRET)
        res.header('auth_token', token).send({
            status: 200,
            message: 'Successful'
        })

    }catch(error){
        next(error)
        return;
    }

}

// verifying user email
exports.user_verify = async (req, res, next)=>{
    const {us}=req.params
    try{
        const user= await UserVerifcation.findOne({uniqueString: us})
        if(user){
            const tuser= await User.updateOne({_id: user.userID}, {verified: true})
            if(tuser){
                await UserVerifcation.deleteOne({uniqueString: us})
                res.send("Verified")
            }else{
                res.send("Already verified")
            }
        }else{
            throw createError(404, 'User not found')
        }
    }catch(error){
        next(error)
        return
    }
}




//send verification email 
const sendVerificationEmail = ({name, _id, email}, res, next)=>{
    //url to be used in the email

    const uniqueString = uuidv4() + _id;
    const curUrl= "http://localhost:3000/"+"api/user/verify/"+uniqueString;

    var mailOptions = {
        from: process.env.AUTH_EMAIL,
        to : email,
        subject : 'Email verification',
        html: `Hi ${name} <br>\
        <p>Thanks for registering, click on the below link to verify your email id <br>\
        ${curUrl} </p>`
     };
    transporter.sendMail(mailOptions, async function (error, response) {
        if (error) {
            next(error)
            return;
        }else{
            const userVerify= new UserVerifcation({
                userID: _id, 
                uniqueString: uniqueString
            })
            const savedVerify=await userVerify.save()
            console.log('message sent')
            console.log(savedVerify)
            res.status=200
            res.send({
                    status: 200,
                    message: 'Email sent'
            })
        }
    });
}