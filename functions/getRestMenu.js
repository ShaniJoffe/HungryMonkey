const elasticsearch = require('elasticsearch');
const esClient = new elasticsearch.Client({
		host: 'https://search-hungrymonkey-3eiz5dewb4yoyjt6ykeufmsjn4.eu-central-1.es.amazonaws.com',
		log: 'error'
	});
const search = function search(index, body){
	return esClient.search({index: index, body: body});
	};		
	exports.getRestM = (rest_id) => 
	new Promise((resolve,reject) => {
			//console.log(dish_name);
		esClient.search({//get the rest's menu 
			index: 'hungrymonkeyrests',
			type: 'restaurants',
			_source: ['rest_name','menu'],
			body :{
				query: {
					bool: {
						must: [
							{ match: {
								"_id":{query:rest_id}  
								}
							}
						]
						 
					}
				}
			}				
		}).then(results => {
				//console.log(JSON.stringify(results));
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
		
		