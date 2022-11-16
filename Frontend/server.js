const exp = require("express");
const upl = require("express-fileupload");
const { spawn } = require("child_process");
const bodyParser = require('body-parser')
var fs = require('fs');
app = exp();
app.use(bodyParser.urlencoded({ extended: true }));
app.use(upl());
app.use(exp.static(__dirname+'/public/'));

app.get("/", function (req, res) {
  res.sendFile(__dirname + "/index1.html");
});
app.post('/dashboard',function(req,res)
{
  res.sendFile(__dirname+'/document.html');
})

app.get('/login',function(req,res)
{
  res.sendFile(__dirname+'/Login_Signup.html');
})
// it executes the python script from node using child

app.get('/python',function(req,res)
{
  res.sendFile(__dirname+'/attendence.html');
});
app.post("/python", function (req, res) {
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

app.post("/", function (req, res) {
  console.log(req.body);
  if (req.files) {
    var file = req.files.file;
    var dir='./'+req.body.course;
    if (!fs.existsSync(dir)){
      fs.mkdirSync(dir);
  }
    file.mv("../Python_source_files/"+req.body.course+"/"+req.body.Name+".jpeg", function (err) {
      if (err) {
        res.send(err);
      } else res.send("File uploaded");
    });
  }
});

// server call
app.listen("3000", function (err) {
  console.log("Started on 3000");
});
