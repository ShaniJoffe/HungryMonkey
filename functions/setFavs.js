'use strict';
const config = require('../config/secrets');
const redis = require('redis');
//data base configurtions and connection before we query
const client = redis.createClient(6379, '18.184.83.111');
const bcrypt = require('bcryptjs');
client.auth(config.passwordForRedis);
client.on('connect', function() {
    console.log('connected');
});
client.on('error', function(err){
  console.log('Something went wrong ', err)
});

exports.setFavorites = (dishId,id) =>// adding a dish id to users set of favorites by hes id

	new Promise((resolve,reject) => {
		var key='user:'+id;
		console.log(key);
		client.hmget(key,["favorites"],function (err, obj) {
			if(err){
				console.log("0error\n"+err);
				reject({ status: 500, message: 'Internal Server Error !' });
			}
			else
			{
				//console.log(obj);
				client.sadd(obj[0],dishId,function(err, reply) {//setting the set of favorites
					if(err){

						console.log("1error\n"+err);
						reject({ status: 500, message: 'Internal Server Error !' });
					}
					else
					{
						client.sadd(dishId,key,function(err, reply) {//adding for each dish the user who likes it
							if(!err)
								resolve({ status: 200, message:"successfully done"});
							else
								reject({ status: 500, message: 'Internal Server Error !' });
						});
					}


				});

			}
		});

	});
