
'use strict';
//data base configurtions and connection before we query
const config = require('../config/secrets');
const redis = require('redis');
const client = redis.createClient(6379, '18.184.83.111');
client.auth(config.passwordForRedis);
const bcrypt = require('bcryptjs');
client.on('connect', function() {
    console.log('connected');
});
client.on('error', function(err){
  console.log('Something went wrong ', err)
});
exports.loginUser = (email, password) =>
	new Promise((resolve,reject) => {
		var hashed_password=0;
		var key='user:'+email;// key to get user's profile object
		client.exists(key, function(err, reply) {//check if user exist
			console.log(reply);
			if(!err){
				if (reply == 1) {
					client.hmget(key,["hashed_password","username"],function (err, obj) {// quering user's hashed password his id and username
						if(err){
							reject({ status: 500, message: 'Internal Server Error !' });
						}
						else{
							hashed_password =obj[0];
							var responseObject = { "name":obj[1]};
							if (bcrypt.compareSync(password, hashed_password))// comapre passwords case valid returns user id
								resolve({ status: 200, message: responseObject});
							else
								reject({ status: 401, message: 'Invalid Credentials !' });
						}
					});
				} else {
					reject({ status: 404, message: 'User Not Found !' });
				}
			}
			else
				reject({ status: 500, message: 'Internal Server Error !' });
		});
	});
