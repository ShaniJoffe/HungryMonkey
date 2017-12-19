'use strict';

//const user = require('../models/user');
//const searchUsers= require('../functions/search_users');
const bcrypt = require('bcryptjs');
const elasticsearch = require('elasticsearch');
const esClient = new elasticsearch.Client({
		host: '127.0.0.1:9200',
		log: 'error'
	});
const search = function search(index, body){
	return esClient.search({index: index, body: body});
	};

exports.loginUser = (name, pass) =>
	new Promise((resolve,reject) => {	
		var counter=0;
		var tempString= '(name:'+name+')  AND (password:'+pass+')';
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
		search('hungrymonkey', body)
		.then(results => {
			console.log(`found ${results.hits.total} items in ${results.took}ms`);
			if (results.hits.total > 0)
			{	
				counter=results.hits.total;
				console.log(`returned user:`);
				results.hits.hits.forEach((hit, index) => console.log(`\t${body.from + ++index} - ${hit._source.name} (score: ${hit._score})`));	
				console.log(`in search counter=`+counter);
				resolve({ status: 200, message: name });	
			}
			else
				reject({ status: 404, message: 'User Not Found !' });
		}).catch(console.error);
	});
	
	/*
	.then(counter => {
		console.log(`counter=`+counter);
		if(counter==1){
			console.log(`in then,counter=`+counter);
			
		}
		else{
				//console.log(`in then,counter=`+counter);
			//reject({ status: 401, message: 'Invalid Credentials !' });
			}
	}).catch(err => reject({ status: 500, message: 'Internal Server Error !' }));
		//

	*/