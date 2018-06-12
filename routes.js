'use strict';

const auth = require('basic-auth');
const jwt = require('jsonwebtoken');
var NodeGeocoder = require('node-geocoder');
const register = require('./functions/register');
const login = require('./functions/login');
const setFavorites = require('./functions/setFavs');
const getRecs = require('./functions/recommendationsForUser');
const getFavorites = require('./functions/getFavs');
const config = require('./config/config.json');
const setRestDetails= require('./functions/setRestDetails');
const setRestMenu= require('./functions/setRestMenu');
const setAllMenus= require('./functions/setAllMenus');
const getRestDetails= require('./functions/getRestDetails');
const getRestMenu = require('./functions/getRestMenu');
const advancedSearch= require('./functions/advancedSearch');
const basicSearch= require('./functions/basicSearch');
const mergeJSON = require("merge-json") ;
const multer 	 = require('multer');
const multerS3	 = require('multer-s3');
const aws = require('aws-sdk');
const passport = require('passport');
var tableify = require('tableify');
var secrets = require('./config/secrets');
module.exports = router => {
	var s3;
	var options = {
		provider: 'google',
		httpAdapter: 'https', // Default
	    apiKey: secrets.apiKey, // for Mapquest, OpenCage, Google Premier
		formatter: '%S'         // 'gpx', 'string', ...
	};
	var geocoder = NodeGeocoder(options);


	//aws config
	aws.config.update({
    secretAccessKey: secrets.secretAccessKey,
    accessKeyId: secrets.accessKeyId,
    region: 'eu-central-1'
	});
	s3 = new aws.S3();
	var upload = multer({
		storage: multerS3({
			s3: s3,
			bucket: 'hmfpbucket',
			key: function (req, file, cb) {
				console.log(file);
				cb(null, file.originalname); //use Date.now() for unique file keys
			}

		})
	});
	router.get('/set_restDetails',(req,res)=> {
		console.log('user accessing set_restDetails page');
		res.render('set_restDetails')});

	router.get('/viewMenu',(req,res)=> {
		console.log('user accessing viewMenu page');
		res.render('viewMenu')});


	router.get('/login',(req,res)=> {
		console.log('user accessing login page');
		res.render('login')});


	router.get('/set_menu',(req,res)=> {
		console.log('user accessing set_menu page');
		res.render('set_menu')});

	router.get('/set_menu/:restid',(req,res)=> {
		console.log('user accessing set_menu page with id');
		console.log("restid: "+req.params.restid);
		res.render('set_menu',{restid : req.params.restid})});

	router.post('/set_restDetails', (req, res) => {
		var tempbol=false;
		var kosher='כשר';
		var tempKosher=req.body.rest_Kosher;
		var tempGeoLoc;
		console.log(req.body.rest_address);
		if(kosher.localeCompare(req.body.rest_Kosher)==0)
		{
			tempbol=true;
		}
		geocoder.geocode(req.body.rest_address)
		.then(result=>{
			tempGeoLoc={'lat':result[0].latitude, 'lon':result[0].longitude};
			var RestDetails={	'rest_name'	: req.body.rest_name,
					'rest_zip'	:req.body.rest_zip,
					'Kosher'	:	tempbol,
					'rest_location':tempGeoLoc,
					'rest_address'	:req.body.rest_address,
					'rest_desc'	:req.body.rest_desc
				};
			setRestDetails.setRestD(RestDetails)
			.then(result=>{
				console.log(result);
				res.render('set_menu',{restid : result.message});// result.message=rest id
				}).catch(err => res.status(err.status).json({ message: err.message }));
		}).catch(function(err) {
			console.log(err);
		});
	});
	router.post('/set_menu/:restid',upload.array('imgUploader',1),(req, res,next) => {

		if(req.body.flag==1)// case its an image upload
		{
			var imgLocation=req.files[0].location;
			console.log("in image upload\n");
			console.log("location: "+imgLocation);
			res.json({location: imgLocation });
		}
		else
		{
			console.log("in form upload\n");
			if(req.body.menu!==undefined)
			{
			//	console.log(req.body.menu);
				var obj = JSON.parse(req.body.menu);
				//obj
				obj.menu = obj.menu.filter(Boolean);
				var dish_id_inRest=parseFloat(req.params.restid);
				obj.menu.forEach(function(dish,index) { dish.dish_id_inRest = dish_id_inRest+index*0.1});
				console.log("ballss"+obj.menu);
				getRestDetails.getRestD(req.params.restid)
				.then(result=>{
					setRestMenu.setMenu(result.message[0]._source,obj,req.params.restid)
					.then(result=>{
						console.log(result.message);
						res.end('!התפריט הוזן בהצלחה')
					}).catch(err => res.status(err.status).json({ message: err.message }));
				}).catch(err => res.status(err.status).json({ message: err.message }));
			}
		}
	});


	router.get('/viewMenu/:rest_id',(req,res)=>{
		console.log(req.params.rest_id);
		getRestMenu.getRestM(req.params.rest_id)
		.then(result=>{
			var data = result.message[0]._source.menu;
			res.render('viewMenu', {data: data});
		})
		.catch(err => res.status(err.status).json({ message: err.message }));
	});

	router.post('/basicSearch', (req, res) => {
		var tempObj={};
		var size;
		var size2;
		basicSearch.basicS(req.body.dishName,req.body.lat,req.body.lon)
		.then(result=>{
			//console.log("balls");
			size=result.message.length;
			console.log("result");
			if(result.message=='No dishes !')
				res.json(result.message);
			else
			{
				for(var i=0;i<size;i++)
				{
					delete result.message[i]._index;
					delete result.message[i]._type;
					delete result.message[i]._id;
					delete result.message[i]._score;
					delete result.message[i].inner_hits.menu.hits.total;
					delete result.message[i].inner_hits.menu.hits.max_score;
					size2=result.message[i].inner_hits.menu.hits.hits.length;
					for(var j=0;j<size2;j++)
					{
						delete result.message[i].inner_hits.menu.hits.hits[j]._nested;
						delete result.message[i].inner_hits.menu.hits.hits[j]._score;
						//console.log(mergeJSON.isJSON(result.message[0].inner_hits.menu.hits));
					}
				}
				//console.log("balls");
				res.json(result.message);
			}
		}).catch(err => res.status(err.status).json({ message: err.message }));

	});
	router.post('/advancedSearch', (req, res) => {
		var tempObj={};
		var size;
		var size2;
		var kosher='כשר';
		var tempbool=false;//if rest kosher tempbool is true
		var distance;

		if(kosher.localeCompare(req.body.rest_Kosher)==0)
		{
			tempbool=true;
		}
			console.log(req.body);
		console.log(req.body.minPrice);
		console.log(req.body.maxPrice);
		console.log(req.body.distance);
		distance=req.body.distance.toString();
		distance+=" km";
		console.log(distance);
		advancedSearch.advancedS(req.body.dishName,tempbool,req.body.minPrice,req.body.maxPrice,distance,req.body.lat,req.body.lon)
		.then(result=>{

			size=result.message.length;

			console.log(result.message);
			if(result.message=='No dishes !')
				res.json(result.message);
			else
			{
				for(var i=0;i<size;i++)
				{
					delete result.message[i]._index;
					delete result.message[i]._type;
					delete result.message[i]._id;
					delete result.message[i]._score;
					delete result.message[i].inner_hits.menu.hits.total;
					delete result.message[i].inner_hits.menu.hits.max_score;
					size2=result.message[i].inner_hits.menu.hits.hits.length;
					for(var j=0;j<size2;j++)
					{
						delete result.message[i].inner_hits.menu.hits.hits[j]._nested;
						delete result.message[i].inner_hits.menu.hits.hits[j]._score;
						//console.log(mergeJSON.isJSON(result.message[0].inner_hits.menu.hits));
					}
				}
				//console.log("balls");
				res.json(result.message);
			}
		}).catch(err => res.status(err.status).json({ message: err.message }));

	});

	router.post('/setAllMenus',(req,res)=>{
		setAllMenus.importData(req.body.data)
		.then(result=>{
			res.json("ok");
		}).catch(err => res.status(err.status).json({ message: err.message }));
	});

	router.get('/', (req, res) =>{

		res.end('Welcome to Learn2Crack !')

		});

	router.post('/setFavs', (req, res) => {
		var temp=checkToken(req);
		console.log("token"+temp);
		if (temp) {
			var dishId=req.body.favs;
			var id=temp;
			setFavorites.setFavorites(dishId,id)
			.then(result=>{
				res.status(result.status).json({ message: result.message });
			})
			.catch(err => res.status(err.status).json({ message: err.message }));
		}else{
			res.status(401).json({ message: 'Invalid Token !' });
		}
	});

	router.get('/recsForUser', (req, res) => {
		var temp=checkToken(req);
		console.log(temp);
		if (temp) {
			var id=temp;
			var size,size2;
			getRecs.getRecs(id)
			.then(result=>{
			console.log(result.message.length);
				size=result.message.length;
				//res.json(result.message);
				if(result.message=='No dishes !')
					res.json(result.message);
				else
				{
					for(var i=0;i<size;i++)
					{
						delete result.message[i]._index;
						delete result.message[i]._type;
						delete result.message[i]._id;
						delete result.message[i]._score;
						delete result.message[i].inner_hits.menu.hits.total;
						delete result.message[i].inner_hits.menu.hits.max_score;
						size2=result.message[i].inner_hits.menu.hits.hits.length;

						for(var j=0;j<size2;j++)
						{
							delete result.message[i].inner_hits.menu.hits.hits[j]._nested;
							delete result.message[i].inner_hits.menu.hits.hits[j]._score;
							//console.log(mergeJSON.isJSON(result.message[0].inner_hits.menu.hits));
						}
					}
					console.log("balls");
					res.json(result.message);
				}
			}).catch(err => res.status(err.status).json({ message: err.message }));
		}else{
				res.status(401).json({ message: 'Invalid Token !' });
		}
	});



	router.get('/getFavs', (req, res) => {
		var temp=checkToken(req);
		console.log(temp);
		if (temp) {
			var id=temp;
			var size,size2;
			getFavorites.getFavorites(id)
			.then(result=>{
				console.log(result.message.length);
				size=result.message.length;
				//res.json(result.message);
				if(result.message=='No dishes !')
					res.json(result.message);
				else
				{
					for(var i=0;i<size;i++)
					{
						delete result.message[i]._index;
						delete result.message[i]._type;
						delete result.message[i]._id;
						delete result.message[i]._score;
						delete result.message[i].inner_hits.menu.hits.total;
						delete result.message[i].inner_hits.menu.hits.max_score;
						size2=result.message[i].inner_hits.menu.hits.hits.length;

						for(var j=0;j<size2;j++)
						{
							delete result.message[i].inner_hits.menu.hits.hits[j]._nested;
							delete result.message[i].inner_hits.menu.hits.hits[j]._score;
							//console.log(mergeJSON.isJSON(result.message[0].inner_hits.menu.hits));
						}
					}
					console.log("balls");
					res.json(result.message);
				}
			}).catch(err => res.status(err.status).json({ message: err.message }));
		}else{
				res.status(401).json({ message: 'Invalid Token !' });
		}
	});


	router.post('/auth',(req,res) => {
		var email=req.body.email;
		var password=req.body.password;
		//console.log(tempName);
		//var tempId=req;
		//console.log("tempId "+JSON.stringify(tempId));
		login.loginUser(email,password)
		.then(result => {
			var payload={id:email};
			const token = jwt.sign(payload, config.secret);
			console.log(token);
			res.status(result.status).json({ message: result.message.name,token:token});
		})
		.catch(err => res.status(err.status).json({ message: err.message }));

	});
	router.post('/users', (req, res) => {
		const name = req.body.name;
		const email = req.body.email;
		const password = req.body.password;
		if (!name || !email || !password || !name.trim() || !email.trim() || !password.trim()) {
			res.status(400).json({message: 'Invalid Request !'});
		} else {
			register.registerUser(name, password,email)
			.then(result => {
				console.log(JSON.stringify(result));
				res.json({ status: result.status, message:result.message});
				//res.status(result.status).json({ message: result.message })
			})
			.catch(err => res.status(err.status).json({ message: err.message }));
		}
	});

	function checkToken(req) {

		var token = req.headers['authorization'];

		if (token) {

			try {
				token = token.replace(/^JWT\s/, '');
				console.log("here1");
				console.log(token);
  				var decoded = jwt.verify(token, config.secret);
				//	console.log(decoded.id);
					return decoded.id;

			} catch(err) {
				console.log(err);
			console.log("here2");
				return false;
			}

		} else {
console.log("here3");
			return false;
		}
	}
}
