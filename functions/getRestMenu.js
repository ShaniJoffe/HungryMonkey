const elasticsearch = require('elasticsearch');
//data base configurtions and connection before we query
const esClient = new elasticsearch.Client({
		host: 'https://search-hungrymonkey-3eiz5dewb4yoyjt6ykeufmsjn4.eu-central-1.es.amazonaws.com',
		log: 'error'
	});
const search = function search(index, body){
	return esClient.search({index: index, body: body});
	};
	exports.getRestM = (rest_id) =>
	new Promise((resolve,reject) => {
		esClient.search({//get the rest's menu
			index: 'hungrymonkeyrests',
			type: 'restaurants',
			_source: ['rest_name','menu'],// those are the fields which we return
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
				if (results.hits.total > 0)
				{
						resolve({ status: 200, message: results.hits.hits});// returns the requsted menu
				}
				else
				{
					console.log('no dishes found');
					reject({ status: 400, message: 'No dishes !' });
				}
		}).catch(console.error);
	});
