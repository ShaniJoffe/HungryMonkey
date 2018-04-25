
'use strict';
const redis = require('redis');
const client = redis.createClient(6379, '18.196.119.82');
const {promisify} = require('util');
const bcrypt = require('bcryptjs');
const getAsync = promisify(client.get).bind(client);

client.on('connect', function() {
    console.log('connected');
});
client.on('error', function(err){
  console.log('Something went wrong ', err)
});



exports.loginUser = (name, password,id) =>
	new Promise((resolve,reject) => {	
		var hashed_password=0;
		var counter=0;
		var key='user:'+id;
		console.log(key);
		client.hmget(key,["password","hashed_password"],function (err, obj) {
			if(err){
				console.log("error");
				reject({ status: 500, message: 'Internal Server Error !' });
			}
			else{
				console.log(obj);
				hashed_password =obj[1];
				console.log(hashed_password);
				if (bcrypt.compareSync(obj[0], hashed_password))
					resolve({ status: 200, message: id });	
				else
					reject({ status: 401, message: 'Invalid Credentials !' });
			}		
		});
	});
	
