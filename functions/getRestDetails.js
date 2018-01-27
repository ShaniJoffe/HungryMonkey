const elasticsearch = require('elasticsearch');
const esClient = new elasticsearch.Client({
		host: 'https://search-hungrymonkey-3eiz5dewb4yoyjt6ykeufmsjn4.eu-central-1.es.amazonaws.com',
		log: 'error'
	});
const search = function search(index, body){
	return esClient.search({index: index, body: body});
	};
	
exports.getRestD = (id) => 	
	new Promise((resolve,reject) => {
		esClient.search({//check if exists 
			index: 'hungrymonkeyrests',
			type: 'restaurants',
			_source: ['rest_name','Kosher','rest_location','rest_address','rest_desc'],
			body :{
				"query": {
					"match": {
						"_id": {
							"query":id,
							//"type": "phrase"
						}
					}
				}	
			}		
	}).then(results => {
				//console.log(results);
			console.log(`found ${results.hits.total} items in ${results.took}ms`);
			if (results.hits.total > 0)
			{				
					//results.hits.hits.forEach((hit, index) => 
						//console.log(`\t${body.from + ++index} - ${hit._source} (score: ${hit._score})`)
					//);
					resolve({ status: 200, message: results.hits.hits});	

			}
			else
				reject({ status: 404, message: 'User Not Found !' });
		}).catch(console.error);
		});
