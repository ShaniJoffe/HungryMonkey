'use strict';

const elasticsearch = require('elasticsearch');
const esClient = new elasticsearch.Client({
		host: 'https://search-hungrymonkey-3eiz5dewb4yoyjt6ykeufmsjn4.eu-central-1.es.amazonaws.com',
		log: 'error'
	});
const bcrypt = require('bcryptjs');

exports.registerUser = (name,password,id) => 
		
	new Promise((resolve,reject) => {
	
	    const salt = bcrypt.genSaltSync(10);
		const hash = bcrypt.hashSync(password, salt);
		console.log(name);
		console.log(id);
		console.log(hash);
		
		esClient.exists({
			  index: 'hungrymonkeyusers',
			  type: 'users',
			  id: id,
			  body: {
				'username': name,
				'hashed_password':hash,
				'password': password
			  }
		}).then(temp => {

			if(temp==true)
			{
				console.log("balls exists");
				reject({ status: 409, message: 'User Already Registered !' });				
			}
			else
			{
			esClient.index({
					index: 'hungrymonkeyusers',
					type: 'users',
					id: id,
					body:{
						'username': name,
						'hashed_password':hash,
						'password': password
						}
					})
				.then(temp2=>{
						resolve({ status: 201, message: 'User Registered Sucessfully !' })})
							.catch(error=>{reject({ status: 500, message: 'Internal Server Error !' });})
			}
		});
	});			
			
		
 
    
	
	