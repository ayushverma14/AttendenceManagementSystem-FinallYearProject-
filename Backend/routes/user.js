

const router = require('express').Router();
const UserController = require('../controllers/user');
const verify = require('../middleware/verify-token')
const path=require('path')

//register new user
router.get('/',(req,res)=>
{
    console.log(path.join(__dirname,'../../Frontend/index1'))
    res.render(path.join(__dirname,'../../Frontend/index1'))
});
router.post('/register', UserController.user_register);

//login user
router.post('/login', UserController.user_login);

//verify user email
router.post('/verify/:us', UserController.user_verify);

//finding one user
router.get('/users/:id', verify, UserController.user_find_one)


module.exports=router

/////////////////////////////////////////////////


const exp = require("express");
let alert = require('alert');

const { spawn } = require("child_process");
const bodyParser = require('body-parser')
var fs = require('fs');
app = exp();
app.use(bodyParser.urlencoded({ extended: true }));

router.use(exp.static(path.join(__dirname,'../../Frontend/public/')));

// app.get("/", function (req, res) {
//   res.sendFile(__dirname + "/index1.html");
// });
router.use(bodyParser.urlencoded({ extended: true }));
router.get('/dashboard',function(req,res)
{
  res.render(path.join(__dirname,'../../Frontend/document'));
})

router.get('/login_signup',function(req,res)
{
    console.log(path.join(__dirname,'../../Frontend/Login_Signup.html'))
  res.render(path.join(__dirname,'../../Frontend/Login_Signup'));
})
// it executes the python script from node using child

router.get('/python',function(req,res)
{
  res.render(path.join(__dirname,'../../Frontend/attendence'));
});
router.post("/python", function (req, res) {
  console.log(req.body.course)
  console.log(req.files.img.name);
  const child = spawn("python", ["../Python_source_files/main1.py",req.body.course,req.files.img.name]);

  child.stdout.on("data", (data) => {
    console.log(`stdout:${data}`);
  });
  child.stderr.on("data", (data) => {
    console.log(`stdout:${data}`);
  });
  child.on("close", (data) => {
    console.log(`stdout:${data}`);
  });
});

// uploaded the image

router.post("/submit", function (req, res) {
  console.log(req.body);
  if (req.files) {
    var file = req.files.file;
    var dir='./'+req.body.course;
    if (!fs.existsSync(dir)){
      fs.mkdirSync(dir);
  }
    file.mv("../Python_source_files/"+req.body.course+"/"+req.body.Registration+".jpeg", function (err) {
      if (err) {
        res.send(err);
      } else alert("Added to sheet");
    });
    var name=req.body.Name;
    var regs=req.body.Registration
    var number=req.body.nums
    var email=req.body.email
    var dept=req.body.department
    var course=req.body.course
    const child = spawn("python", ["../Python_source_files/exc.py",name,regs,number,email,dept,course]);

    child.stdout.on("data", (data) => {
      console.log(typeof(data));
      
      console.log(`stdout:${data}`);
      //alert("Submitted Succesfully");
    });
    child.stderr.on("data", (data) => {
      console.log(`stdout:${data}`);
      //alert(`Error:${data}`);
    });
    child.on("close", (data) => {
      console.log(`stdout:${data}`);
    });
  }

});
router.post('/short_attendence/email',(req,res)=>
{
  console.log(req.body)

  const child = spawn("python", ["../Python_source_files/sms.py",req.body.email,req.body.dept]);

    child.stdout.on("data", (data) => {
      console.log(typeof(data));
      
      console.log(`stdout:${data}`);
      //alert("Submitted Succesfully");
    });
    child.stderr.on("data", (data) => {
      console.log(`stdout:${data}`);
      //alert(`Error:${data}`);
    });
    child.on("close", (data) => {
      console.log(`stdout:${data}`);
    });
})

router.post('/short_attendence',(req,res)=>
{
  var course=req.body.course
  var department=req.body.department
  console.log(req.body)
  const child = spawn("python", ["../Python_source_files/short_attendence.py",department,course]);

  child.stdout.on("data", (data) => {
    
    
  console.log(`stdout:${data}`);
 
   console.log(data)
   console.log(typeof(data));
   
   
   console.log(data.toString('utf8'))
   data=data.toString('ascii');
   res1=[];
   var i=0;
   while( i<data.length)
   {
    r1="";
    
    while((data[i]>='A' && data[i]<='Z')||(data[i]>='a' && data[i]<='z')||(data[i]>='0' && data[i]<='9')||(data[i]=='@' || data[i]=='.'))
    {r1=r1+data[i];
      i++;}
//r1=data[i][0]+'_'+data[i][1];
console.log(r1);
if(r1.length!=0)
res1.push(r1);
i++;
   }
   
   console.log(res1);
   res.render(path.join(__dirname,'../../Frontend/short_attendence'),{data:res1});
    //alert("Submitted Succesfully");
  });
  child.stderr.on("data", (data) => {
    console.log(`stdout:${data}`);
    //alert(`Error:${data}`);
  });
  child.on("close", (data) => {
    console.log(`stdout:${data}`);
  });

})
router.get('/short_attendence',(req,res)=>
{
  data=[]
  res.render(path.join(__dirname,'../../Frontend/short_attendence'),{data:data});
});
//saving the snap_shot on registration portal

router.post('/save_snap',(req,res)=>
{
  const child = spawn("python", ["../Python_source_files/snap.py",req.body.uri]);

    child.stdout.on("data", (data) => {
      console.log(typeof(data));
      
      console.log(`stdout:${data}`);
      //alert("Submitted Succesfully");
    });
    child.stderr.on("data", (data) => {
      console.log(`stdout:${data}`);
      //alert(`Error:${data}`);
    });
    child.on("close", (data) => {
      console.log(`stdout:${data}`);
    });
});
// calling the mark_attendence.py module
router.post('/attend',(req,res)=>
{
  var child = spawn("python", ["../Python_source_files/mark_attendence.py",req.body.dept,req.body.course]);

  child.stdout.on("data", (data) => {
    console.log(`stdout:${data}`);
  });
  child.stderr.on("data", (data) => {
    console.log(`stdout:${data}`);
  });
  child.on("close", (data) => {
    console.log(`stdout:${data}`);
  });
})
// server call

// app.listen("3000", function (err) {
//   console.log("Started on 3000");
// });
