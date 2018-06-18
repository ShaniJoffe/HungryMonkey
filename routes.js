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
	//configurtions for google api for locaions
	var options = {
		provider: 'google',
		httpAdapter: 'https', // Default
		apiKey: secrets.apiKey, // for Mapquest, OpenCage, Google Premier
		formatter: '%S'         // 'gpx', 'string', ...
	};
	var geocoder = NodeGeocoder(options);


	// configurtions for the aws s3 bucket for files upload
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
	//  get route for the set rest details
	router.get('/set_restDetails',(req,res)=> {
		console.log('user accessing set_restDetails page');
		res.render('set_restDetails')});
		// route for viewing the resturant menu
		router.get('/viewMenu',(req,res)=> {
			console.log('user accessing viewMenu page');
			res.render('viewMenu')});
			//  get route for the set menu details
			router.get('/set_menu',(req,res)=> {
				console.log('user accessing set_menu page');
				res.render('set_menu')});

				router.get('/set_menu/:restid',(req,res)=> {
					console.log('user accessing set_menu page with id');
					console.log("restid: "+req.params.restid);
					res.render('set_menu',{restid : req.params.restid})});
					// post route for setting rest details
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
						geocoder.geocode(req.body.rest_address)// geting the locations from google api
						.then(result=>{
							tempGeoLoc={'lat':result[0].latitude, 'lon':result[0].longitude};
							var RestDetails={	'rest_name'	: req.body.rest_name,//preparing the restaurant object with all the details
							'rest_zip'	:req.body.rest_zip,
							'Kosher'	:	tempbol,
							'rest_location':tempGeoLoc,
							'rest_address'	:req.body.rest_address,
							'rest_desc'	:req.body.rest_desc
						};
						setRestDetails.setRestD(RestDetails)
						.then(result=>{
							//after rest details were stored we render to set menu page for menu data insertion
							res.render('set_menu',{restid : result.message});// result.message=rest id
						}).catch(err => res.status(err.status).json({ message: err.message }));
					}).catch(function(err) {
						console.log(err);
					});
				});
				//post route for storing the menu data for specific restuarant
				router.post('/set_menu/:restid',upload.array('imgUploader',1),(req, res,next) => {
					// there will be ajax calls from the client side for each image upload and a final one for the whole menu object
					if(req.body.flag==1)// case its an image upload
					{
						var imgLocation=req.files[0].location;
						console.log("in image upload\n");
						console.log("location: "+imgLocation);
						res.json({location: imgLocation });// after image uploading we return its url
					}
					else
					{
						console.log("in form upload\n");
						if(req.body.menu!==undefined)
						{
							var obj = JSON.parse(req.body.menu);
							obj.menu = obj.menu.filter(Boolean);
							var dish_id_inRest=parseFloat(req.params.restid);
							obj.menu.forEach(function(dish,index) { dish.dish_id_inRest = dish_id_inRest+index*0.1});// each dish id will be adouble which the inteager will be the resturant id and the dec will be the id of the dish in the the resturant
							getRestDetails.getRestD(req.params.restid)
							.then(result=>{
								setRestMenu.setMenu(result.message[0]._source,obj,req.params.restid)// provideing the resturants details and the menu object for restuarnt object update
								.then(result=>{
									console.log(result.message);
									res.end('!התפריט הוזן בהצלחה')
								}).catch(err => res.status(err.status).json({ message: err.message }));
							}).catch(err => res.status(err.status).json({ message: err.message }));
						}
					}
				});

				// returns the menu page by id
				router.get('/viewMenu/:rest_id',(req,res)=>{
					console.log(req.params.rest_id);
					getRestMenu.getRestM(req.params.rest_id)
					.then(result=>{
						var data = result.message[0]._source.menu;
						res.render('viewMenu', {data: data});
					})
					.catch(err => res.status(err.status).json({ message: err.message }));
				});
				//basic search route for post method expecting geo locations and a dish name
				router.post('/basicSearch', (req, res) => {
					var tempObj={};
					var size;
					var size2;
					basicSearch.basicS(req.body.dishName,req.body.lat,req.body.lon)
					.then(result=>{
						size=result.message.length;// how manu resturants objects there are
						if(result.message=='No dishes !')
						res.json(result.message);
						else
						{
							for(var i=0;i<size;i++)// deleting all the not needed data
							{
								delete result.message[i]._index;
								delete result.message[i]._type;
								delete result.message[i]._id;
								delete result.message[i]._score;
								delete result.message[i].inner_hits.menu.hits.total;
								delete result.message[i].inner_hits.menu.hits.max_score;
								size2=result.message[i].inner_hits.menu.hits.hits.length;// how many dish objects there are
								for(var j=0;j<size2;j++)
								{
									delete result.message[i].inner_hits.menu.hits.hits[j]._nested;// deleting all the not needed data
									delete result.message[i].inner_hits.menu.hits.hits[j]._score;
								}
							}
							res.json(result.message);// returning all the dishes
						}
					}).catch(err => res.status(err.status).json({ message: err.message }));

				});
				//advanced search route for post method expecting geo locations,dish name,kosher,price and radius limits
				router.post('/advancedSearch', (req, res) => {
					var tempObj={};
					var size;
					var size2;
					var kosher='כשר';
					var tempbool=false;//if rest kosher tempbool is true
					var distance;

					if(kosher.localeCompare(req.body.restKosher)==0)
					{
						tempbool=true;
					}
					distance=req.body.distance.toString();
					distance+=" km";
					advancedSearch.advancedS(req.body.dishName,tempbool,req.body.minPrice,req.body.maxPrice,distance,req.body.lat,req.body.lon)
					.then(result=>{
						size=result.message.length;// how manu resturant objects there are
						if(result.message=='No dishes !')
						res.json(result.message);
						else
						{
							for(var i=0;i<size;i++)// deleting all the not needed data
							{
								delete result.message[i]._index;
								delete result.message[i]._type;
								delete result.message[i]._id;
								delete result.message[i]._score;
								delete result.message[i].inner_hits.menu.hits.total;
								delete result.message[i].inner_hits.menu.hits.max_score;
								size2=result.message[i].inner_hits.menu.hits.hits.length;// how manu dish objects there are
								for(var j=0;j<size2;j++)// deleting all the not needed data
								{
									delete result.message[i].inner_hits.menu.hits.hits[j]._nested;
							//		delete result.message[i].inner_hits.menu.hits.hits[j]._score;
									//console.log(mergeJSON.isJSON(result.message[0].inner_hits.menu.hits));
								}
							}
							res.json(result.message);// returning all the dishes
						}
					}).catch(err => res.status(err.status).json({ message: err.message }));

				});
				// method which expects an array of json objects which each object is aresturant with menu objects nested
				router.post('/setAllMenus',(req,res)=>{
					setAllMenus.importData(req.body.data)
					.then(result=>{
						res.json("ok");
					}).catch(err => res.status(err.status).json({ message: err.message }));
				});
				// route for post req, expects id  of favorite dish, only exist user can access
				router.post('/setFavs', (req, res) => {
					var temp=checkToken(req);
					if (temp) {// check token validation before access
						var dishId=req.body.favs;
						var id=temp;
						console.log(dishId);
						if(dishId.localeCompare("undefined")==0){// check that the dish isnt undefined
							res.status(400).json({ message: 'Invalid Dish id!' });
						}else{
							setFavorites.setFavorites(dishId,id)
							.then(result=>{
								res.status(result.status).json({ message: result.message });
							})
							.catch(err => res.status(err.status).json({ message: err.message }));
						}
					}else{
						res.status(401).json({ message: 'Invalid Token !' });
					}
				});
				// route for get recommendations for exist user, only exist user can access
				router.get('/recsForUser', (req, res) => {
					var temp=checkToken(req);
					console.log(temp);
					if (temp) {// check token validation before access
						var id=temp;
						var size,size2;
						getRecs.getRecs(id)
						.then(result=>{
							size=result.message.length;// the size of the returnd object(all the rest and dishes data)
							if(result.message=='No dishes !')
							res.json(result.message);
							else
							{
								for(var i=0;i<size;i++)// cleaning the object from all the unneeded data
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
								res.json(result.message);
							}
						}).catch(err => res.status(err.status).json({ message: err.message }));
					}else{
						res.status(401).json({ message: 'Invalid Token !' });
					}
				});

				//route for get request, will return exist user's favorites,access for exists users
				router.get('/getFavs', (req, res) => {
					var temp=checkToken(req);
					if (temp) {// check token validation before access
						var id=temp;
						var size,size2;
						getFavorites.getFavorites(id)
						.then(result=>{
							size=result.message.length;// the size of the returnd object(all the rest and dishes data)
							if(result.message=='No dishes !')
							res.json(result.message);
							else
							{
								for(var i=0;i<size;i++)// cleaning the object from all the unneeded data
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
									}
								}
								res.json(result.message);
							}
						}).catch(err => res.status(err.status).json({ message: err.message }));
					}else{
						res.status(401).json({ message: 'Invalid Token !' });
					}
				});

				//post route for login, expects user username and password
				router.post('/auth',(req,res) => {
					var email=req.body.email;
					var password=req.body.password;
					login.loginUser(email,password)
					.then(result => {
						var payload={id:email};// generate token for logged in user which will store his key
						const token = jwt.sign(payload, config.secret);
						res.status(result.status).json({ message: result.message.name,token:token});// returns user's name and his token
					})
					.catch(err => res.status(err.status).json({ message: err.message }));
				});
				// post route for registertion, expects name email and password
				router.post('/users', (req, res) => {
					const name = req.body.name;
					const email = req.body.email;
					const password = req.body.password;
					if (!name || !email || !password || !name.trim() || !email.trim() || !password.trim()) {
						res.status(400).json({message: 'Invalid Request !'});
					} else {
						register.registerUser(name, password,email)
						.then(result => {
							res.json({ status: result.status, message:result.message});
						})
						.catch(err => res.status(err.status).json({ message: err.message }));
					}
				});
				//private function which decodes the accapted token and returns the stored key
				function checkToken(req) {
					var token = req.headers['authorization'];

					if (token) {

						try {
							token = token.replace(/^JWT\s/, '');
							var decoded = jwt.verify(token, config.secret);
							return decoded.id;

						} catch(err) {
							console.log(err);
							return false;
						}

					} else {
						return false;
					}
				}
			}
