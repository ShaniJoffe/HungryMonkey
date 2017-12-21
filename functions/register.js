'use strict';

const elasticsearch = require('elasticsearch');
const esClient = new elasticsearch.Client({
		host: 'https://search-hungrymonkey-3eiz5dewb4yoyjt6ykeufmsjn4.eu-central-1.es.amazonaws.com',
		log: 'error'
	});
const bcrypt = require('bcryptjs');

exports.registerUser = (name,password) => 
		
	new Promise((resolve,reject) => {
	
	    const salt = bcrypt.genSaltSync(10);
		const hash = bcrypt.hashSync(password, salt);
		console.log(name);
		console.log(hash);
		esClient.index({
			  index: 'hungrymonkey',
			  type: 'users',
			  id: '1',
			  body: {
				'name': name,
				'hashed_password':hash,
				'password': password
			  }
		}).then(response => {
				console.log("balls");
			let errorCount = 0;
			response.items.forEach(item => {
				if (item.index && item.index.error) {
					console.log(++errorCount, item.index.error);
					reject({ status: 409, message: 'User Already Registered !' });
				}
			});
		console.log(`Successfully indexed ${data.length - errorCount} out of ${data.length} items`);
		resolve({ status: 201, message: 'User Registered Sucessfully !' });
    })
    .catch(console.err);
	
	});