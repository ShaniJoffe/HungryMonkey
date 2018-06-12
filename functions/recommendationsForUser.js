'use strict';
const config = require('../config/secrets');
const redis = require('redis');
const client = redis.createClient(6379, '18.184.83.111');
client.auth(config.passwordForRedis);
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

exports.getRecs = (id) =>
	new Promise((resolve,reject) => {
		var key="u"+id+"fs";// the key for user set of favorites
		var userRecs="u"+id+"recs";// the key for user set of recomends
		var arrayOfDishes=[];
		var arrayOfUsers=[];
		var tempArray=[];
		client.smembers(key,function(err,result){
			if(!err && result.length>0){
				arrayOfDishes=arrayOfDishes.concat(result);//array with all the favorites
				console.log(result.length);
				client.sunion(arrayOfDishes,function(err,result1){
					if(!err){
						for(var i=0;i<result.length;i++)//getting array of each user's favorites set
						{
							var tempIndex=result1[i].substr(5,result1[i].length);
							var temp="u"+tempIndex+"fs";
							arrayOfUsers.push(temp);
						}
						client.sunionstore(userRecs,arrayOfUsers,function(err,result2){//Get all the items that belong to the categories in which the user is interested.
							if(!err){
								client.sdiff(userRecs,key,function(err,result3){//Get the list of items that aren’t yet associated with the user, but are associated with other users with similar behavior.
									if(!err){
										console.log(result3);
										tempArray=result3.filter(Boolean);
										console.log(tempArray);
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
																					"menu.dish_id_inRest": tempArray
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
											if (results.hits.total > 0)
											{
													resolve({ status: 200, message: results.hits.hits});//return the results
											}
											else
											{
												console.log('no dishes found');
												reject({ status: 400, message: 'No dishes !' });
											}
										}).catch(console.error);
									}
									else
										reject({ status: 500, message: 'Internal Server Error3 !' });
								})

							}
							else
								reject({ status: 500, message: 'Internal Server Error2 !' });
						})
					}
					else
						reject({ status: 500, message: 'Internal Server Error1 !' });
				})
			}
			else
				reject({ status: 400, message: 'User got no favorites' });
		});
	});
