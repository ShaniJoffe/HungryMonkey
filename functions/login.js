'use strict';

//const user = require('../models/user');
//const searchUsers= require('../functions/search_users');
const bcrypt = require('bcryptjs');
const elasticsearch = require('elasticsearch');
const esClient = new elasticsearch.Client({
		host: 'https://search-hungrymonkey-3eiz5dewb4yoyjt6ykeufmsjn4.eu-central-1.es.amazonaws.com',
		log: 'error'
	});
const search = function search(index, body){
	return esClient.search({index: index, body: body});
	};

exports.loginUser = (name, password) =>
	new Promise((resolve,reject) => {	
		var hashed_password=0;
		var counter=0;
		var tempString= '(username:'+name+')  AND (password:'+password+')';
		console.log(tempString);
		let body = {
		  size: 1,
		  from: 0,
		  query: {
			bool: {
			  must: [
				{
				  query_string: {
					query:tempString
				  }
				}
			  ]    
			}
		  }
		};
		search('hungrymonkeyusers', body)
		.then(results => {
			console.log(`found ${results.hits.total} items in ${results.took}ms`);
			if (results.hits.total > 0)
			{				
				hashed_password =results.hits.hits[0]._source.hashed_password;
				if (bcrypt.compareSync(password, hashed_password))
					resolve({ status: 200, message: name });	
				else
					reject({ status: 401, message: 'Invalid Credentials !' });
			}
			else
				reject({ status: 404, message: 'User Not Found !' });
		}).catch(console.error);
	});
	
