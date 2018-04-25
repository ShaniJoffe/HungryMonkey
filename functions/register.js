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



exports.registerUser = (name,password,id) => 
		
	new Promise((resolve,reject) => {
	    const salt = bcrypt.genSaltSync(10);
		const hash = bcrypt.hashSync(password, salt);
		var key='user:'+id;
		console.log(name);
		console.log(id);
		console.log(hash);	
		client.exists(key, function(err, reply) {
			console.log(reply);
			if (reply == 1) {
				console.log('exists');
				reject({ status: 409, message: 'User Already Registered !' });
			} else {
				console.log('doesn\'t exist');
				client.hmset(key, {
					'username': name,
					'password': password,
					'hashed_password': hash
				},function (err, res) {
					if(res.localeCompare('OK')==0)
						resolve({ status: 201, message: 'User Registered Sucessfully !' });
					else
						reject({ status: 500, message: 'Internal Server Error !' });
				});
			}
		});		
	});			
			
		
 
    
	
	