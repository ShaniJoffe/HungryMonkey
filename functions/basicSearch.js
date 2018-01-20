const elasticsearch = require('elasticsearch');
const esClient = new elasticsearch.Client({
		host: 'https://search-hungrymonkey-3eiz5dewb4yoyjt6ykeufmsjn4.eu-central-1.es.amazonaws.com',
		log: 'error'
	});
const search = function search(index, body){
	return esClient.search({index: index, body: body});
	};
	
	
	exports.basicS = (dish_name) => 
		new Promise((resolve,reject) => {
			//console.log(dish_name);
			let body = {
				size: 1000,
			    from: 0,
			    query: {
					nested : {
						path : 'title.menu',
						query: {
							bool:{
								should : [{
									query_string: {
										query: '(title.menu.*dish_name:ציפס)'	
								    }
									//'title.menu.*dish_name', 'title.menu.*dish_description'],
									//minimum_should_match: 0,
									//fuzziness: 2
								}]			
							}
						}
					}
				}					
			};
			search('hungrymonkeyrests', body)
			.then(results => {
				console.log(results);
			console.log(`found ${results.hits.total} items in ${results.took}ms`);
			if (results.hits.total > 0)
			{				
					results.hits.hits.forEach((hit, index) => 
						console.log(`\t${body.from + ++index} - ${hit._source} (score: ${hit._score})`)
					);
					resolve({ status: 200, message: results.hits.hits});	

			}
			else
				reject({ status: 404, message: 'User Not Found !' });
		}).catch(console.error);
		});
		