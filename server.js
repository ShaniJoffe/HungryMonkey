var express=require('express');
var bodyParser=require('body-parser');
var app=express();
var urlencodedParser = bodyParser.urlencoded({ extended: false });
app.get('/',function(req,res){
	res.sendFile(__dirname + '/index.html');
});
app.post('/',urlencodedParser,function(req,res){
	console.log(req.body);
	if (!req.body) 
		return res.sendStatus(400)
	res.send('welcome, ' + req.body.user)
});

app.listen(process.env.PORT || 3000)

