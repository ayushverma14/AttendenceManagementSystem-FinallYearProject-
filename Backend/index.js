
const express=require('express');
const createError = require('http-errors');
const app=express();
require('dotenv').config();
const mongoose=require('mongoose')
const userRoute = require('./routes/user');
const bodyParser = require('body-parser')
const path=require('path')
const upl = require("express-fileupload");
app.set('views', path.join(__dirname, '../Frontend'));
app.set('view engine','ejs');
mongoose.connect(process.env.DB_CONNECT,{useNewUrlParser: true}, (err)=>{
    if(err){
       
        console.log("Error connecting MongoDB: " + err.message)
    }else{
        console.log('Mongo DB running')
    }
})
app.use(upl());
app.use(express.json())
app.use(bodyParser.urlencoded({
    extended: true
}));
app.use(express.static(path.join(__dirname,'../Frontend/public/')));
//authentication route
app.use('/api/user', userRoute);

app.use((req, res, next)=>{
    next(createError(404, 'Not found'))
})

app.use((err, req, res, next) =>{
    res.status(err.status || 500);
    res.send({
            status: err.status || 500,
            message: err.message
    })
})
app.get('/api/user',function(req,res)
{
    res.sendFile(__dirname+'/Frontend/index1.html');
})
app.listen(8000, ()=>{
    console.log('Server up and running')
})
