'use strict';

const auth = require('basic-auth');
const jwt = require('jsonwebtoken');
var NodeGeocoder = require('node-geocoder');
const register = require('./functions/register');
const login = require('./functions/login');
const profile = require('./functions/profile');
const password = require('./functions/password');
const config = require('./config/config.json');
const setRestDetails= require('./functions/setRestDetails');
const setRestMenu= require('./functions/setRestMenu');
const basicSearch= require('./functions/basicSearch');
module.exports = router => {
	var options = {
		provider: 'google',
		httpAdapter: 'https', // Default
	    apiKey: 'AIzaSyBwMyNbVpmbIbHeQDmXwIDoDfu6i65IRVw', // for Mapquest, OpenCage, Google Premier
		formatter: '%S'         // 'gpx', 'string', ...
	};
	var geocoder = NodeGeocoder(options);
	
	router.get('/set_restDetails',(req,res)=> {
		console.log('user accessing set_restDetails page');
		
		res.render('set_restDetails')});
		
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
			console.log(result[0].latitude);
			tempGeoLoc={'lat':result[0].latitude, 'lon':result[0].longitude};
			console.log(tempGeoLoc);
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
				res.render('set_menu',{restid : result.message});		
				}).catch(err => res.status(err.status).json({ message: err.message }));		
		}).catch(function(err) {
			console.log(err);
		});	
	});
		router.post('/set_menu/:restid', (req, res) => {
			var dish_id_inRest=parseFloat(req.params.restid);
			req.body.menu.forEach(function(dish,index) { dish.dish_id_inRest = dish_id_inRest+index*0.1});
			//console.log(req.body);
			//res.render('set_menu');
			setRestMenu.setMenu(req.body,req.params.restid)
			.then(result=>{
				console.log("balls");
				});
				//res.render('set_menu');			 	
				//}).catch(err => res.status(err.status).json({ message: err.message }));	
		//console.log(req.body);
	});
		router.post('/basicSearch', (req, res) => {
		basicSearch.basicS(req.body.dishName)
		.then(result=>{
			console.log(result);
			res.json(result);
			});
		});	
	
	router.get('/', (req, res) =>{
		res.end('Welcome to Learn2Crack !')});

	router.post('/auth', (req, res) => {
		var tempName=req.body.name;
		var tempPass=req.body.password;
		login.loginUser(tempName, tempPass)
		.then(result => {
			const token = jwt.sign(result, config.secret, { expiresIn: 1440 });
			res.status(result.status).json({ message: result.message, token: token });
		})
		.catch(err => res.status(err.status).json({ message: err.message }));
	});
	router.post('/users', (req, res) => {

		const name = req.body.name;
		const password = req.body.password;
		const id=req.body.id;

		if (!name || !password || !id || !name.trim() || !password.trim()) {

			res.status(400).json({message: 'Invalid Request !'});

		} else {
			register.registerUser(name, password,id)
			.then(result => {
				console.log("after registertion");
				//res.setHeader('Location', '/users/'+email);
				res.status(result.status).json({ message: result.message })
			})

			.catch(err => res.status(err.status).json({ message: err.message }));
		}
	});

/*
	router.get('/users/:id', (req,res) => {

		if (checkToken(req)) {

			profile.getProfile(req.params.id)

			.then(result => res.json(result))

			.catch(err => res.status(err.status).json({ message: err.message }));

		} else {

			res.status(401).json({ message: 'Invalid Token !' });
		}
	});

	router.put('/users/:id', (req,res) => {

		if (checkToken(req)) {

			const oldPassword = req.body.password;
			const newPassword = req.body.newPassword;

			if (!oldPassword || !newPassword || !oldPassword.trim() || !newPassword.trim()) {

				res.status(400).json({ message: 'Invalid Request !' });

			} else {

				password.changePassword(req.params.id, oldPassword, newPassword)

				.then(result => res.status(result.status).json({ message: result.message }))

				.catch(err => res.status(err.status).json({ message: err.message }));

			}
		} else {

			res.status(401).json({ message: 'Invalid Token !' });
		}
	});

	router.post('/users/:id/password', (req,res) => {

		const email = req.params.id;
		const token = req.body.token;
		const newPassword = req.body.password;

		if (!token || !newPassword || !token.trim() || !newPassword.trim()) {

			password.resetPasswordInit(email)

			.then(result => res.status(result.status).json({ message: result.message }))

			.catch(err => res.status(err.status).json({ message: err.message }));

		} else {

			password.resetPasswordFinish(email, token, newPassword)

			.then(result => res.status(result.status).json({ message: result.message }))

			.catch(err => res.status(err.status).json({ message: err.message }));
		}
	});
*/
	function checkToken(req) {

		const token = req.headers['x-access-token'];

		if (token) {

			try {

  				var decoded = jwt.verify(token, config.secret);

  				return decoded.message === req.params.id;

			} catch(err) {

				return false;
			}

		} else {

			return false;
		}
	}
}