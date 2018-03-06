const elasticsearch = require('elasticsearch');
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
			//console.log(dish_name);
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
									_source: ['menu.dish_name','menu.dish_description','menu.dish_price']
								},
								"query": {
									 "bool" : {
										"should" : [
											{"term": {"menu.dish_name": dish_name}},
											{"bool": {
												"should" : [
													{"match": {
															"menu.dish_description":{
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
							"filter" : {
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
				console.log(results);
			console.log(`found ${results.hits.total} items in ${results.took}ms`);
			if (results.hits.total > 0)
			{				
					//results.hits.hits.forEach((hit, index) => 
						//console.log(`\t${body.from + ++index} - ${hit._source} (score: ${hit._score})`)
					//);
					resolve({ status: 200, message: results.hits.hits});	
					//resolve({ status: 200, message: results.hits.hits._source, message2:results.hits.hits.inner_hits.menu.hits.hits[0]._source});	

			}
			else
			{
				console.log('no dishes found');
				reject({ status: 400, message: 'No dishes !' });
			}
		}).catch(console.error);
		});
		
		