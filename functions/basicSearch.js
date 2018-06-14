const elasticsearch = require('elasticsearch');
//data base configurtions and connection before we query
const esClient = new elasticsearch.Client({
		host: 'https://search-hungrymonkey-3eiz5dewb4yoyjt6ykeufmsjn4.eu-central-1.es.amazonaws.com',
		log: 'error'
	});
const search = function search(index, body){
	return esClient.search({index: index, body: body});
	};

const mergeJSON = require("merge-json") ;
	exports.basicS = (dish_name,lat,lon) =>
		new Promise((resolve,reject) => {
		esClient.search({//check if dish exists
			index: 'hungrymonkeyrests',
			type: 'restaurants',
			_source: ['rest_name','rest_description','rest_location','Kosher','rest_address'],//keys of rest object which will be presented
			body :{
				"query": {
					"bool":{
						"must": [
							{"nested": {
								path: 'menu',
								"inner_hits": {
									_source: ['menu.dish_name','menu.dish_description','menu.dish_price','menu.dish_id_inRest','menu.imgUrl']//keys of menu object which will be presented
								},
								"query": {
									 "bool" : {
										"should" : [// 1 of 2 should happend or the part of the input should be in the dish name or all of it the dish category
											{"term": {"menu.dish_cat": dish_name}},
											{"bool": {
												"should" : [
													{"match": {
															"menu.dish_name":{
																"query": dish_name,
																"operator": "and",
																//"minimum_should_match": "75%"
															}
														}
													}
												]
											}
											}
										]
									 }
								}
							}}],
							"filter" : {// query to filter results by distance,default 150 km
								"geo_distance" : {
									"distance" : "150km",
									"validation_method":"IGNORE_MALFORMED",
									"rest_location" : {
										"lat" : lat,
										"lon" : lon
									}
								}
							}

					}
				}
			}
		}).then(results => {
			if (results.hits.total > 0)// case there are results it will resolve the dishes objects
			{
				resolve({ status: 200, message: results.hits.hits});
			}
			else//case not will return string 'no dishes found'
			{
				console.log('no dishes found');
				reject({ status: 400, message: 'No dishes !' });
			}
		}).catch(console.error);
	});
