'use strict';
const config = require('../config/secrets');
const redis = require('redis');
//data base configurtions and connection before we query
const client = redis.createClient(6379, '18.184.83.111');
client.auth(config.passwordForRedis);
const bcrypt = require('bcryptjs');
client.on('connect', function() {
    console.log('connected');
});
client.on('error', function(err){
  console.log('Something went wrong ', err)
});

exports.registerUser = (name,password,email) =>
	new Promise((resolve,reject) => {
	    const salt = bcrypt.genSaltSync(10);
		const hash = bcrypt.hashSync(password, salt);
		console.log(email);
		var key='user:'+email;//users key for his hash
		var favorites= "u"+email+"fs";
		console.log(key);
		console.log(favorites);
		client.exists(key, function(err, reply) {//check if user exist
			console.log(reply);
			if (reply == 1) {
				console.log('exists');
				reject({ status: 409, message: 'User Already Registered !' });
			} else {
				client.hmset(key, {// this the new user object which stored in hash
					'username': name,
					'email':email,
					'hashed_password': hash,
					'favorites': favorites
				},function (err, res) {
					if(!err){
						console.log("no errors");
						resolve({ status: 201, message:'User Registered Sucessfully !'});
					}
					else
						reject({ status: 500, message: 'Internal Server Error !' });
				});
			}
		});




	});
