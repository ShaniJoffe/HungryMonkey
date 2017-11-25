var express=require('express');
var bodyParser=require('body-parser');
var app =express();
var urlencodedParser = bodyParser.urlencoded({ extended: false })

app.post(urlencodedParser, function (req, res) {
	console.log(req.body);
})
 
app.listen(3000)