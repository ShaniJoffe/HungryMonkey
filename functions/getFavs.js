'use strict';
const config = require('../config/secrets');
const redis = require('redis');
const client = redis.createClient(6379, '18.184.83.111');
	client.auth(config.passwordForRedis);
const bcrypt = require('bcryptjs');
const elasticsearch = require('elasticsearch');
const esClient = new elasticsearch.Client({
		host: 'https://search-hungrymonkey-3eiz5dewb4yoyjt6ykeufmsjn4.eu-central-1.es.amazonaws.com',
		log: 'error'
	});
const search = function search(index, body){
	return esClient.search({index: index, body: body});
	};
client.on('connect', function() {
    console.log('connected');
});
client.on('error', function(err){
  console.log('Something went wrong ', err)
});
exports.getFavorites = (id)=>
	new Promise((resolve,reject) => {	
		var key="u"+id+"fs";// key for user favorites set
		var array=[];
		client.smembers(key,function(err,result){
			if(!err){
				array=array.concat(result);//array with all the favorites
				esClient.search({//check if exists 
					index: 'hungrymonkeyrests',
					type: 'restaurants',
					_source: ['rest_name','rest_description','rest_location','Kosher','rest_address'],
					body :{
						"query": { 
							"bool":{
								"must": [
									{"nested": {
										path: 'menu',
										"inner_hits": { 
										//explain:true,
											_source: ['menu.dish_name','menu.dish_description','menu.dish_price','menu.imgUrl','menu.dish_id_inRest']
										},
										"query": {
											 "bool" : {
												"should" : [
													{
														"terms": {
															"menu.dish_id_inRest": array
															}
													}		
												]
											 }
										}	 
									}}]
							}	
						}				
					}		
				}).then(results => {
					console.log(results);
					console.log(`found ${results.hits.total} items in ${results.took}ms`);
					if (results.hits.total > 0)
					{				
							resolve({ status: 200, message: results.hits.hits});	// return the results				
					}
					else
					{
						console.log('no dishes found');
						reject({ status: 400, message: 'No dishes !' });
					}
				}).catch(console.error);	
			}else{
				reject({ status: 500, message: 'Internal Server Error !' });
				console.log(err);
			}
		})
   
	});
	
