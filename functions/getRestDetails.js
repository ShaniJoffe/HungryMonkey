const elasticsearch = require('elasticsearch');
//data base configurtions and connection before we query
const esClient = new elasticsearch.Client({
	host: 'https://search-hungrymonkey-3eiz5dewb4yoyjt6ykeufmsjn4.eu-central-1.es.amazonaws.com',
	log: 'error'
});
const search = function search(index, body){
	return esClient.search({index: index, body: body});
};

exports.getRestD = (id) => 	// ger rest details by id
new Promise((resolve,reject) => {
	esClient.search({//check if exists
		index: 'hungrymonkeyrests',
		type: 'restaurants',
		_source: ['rest_name','Kosher','rest_location','rest_address','rest_desc'],// those are the fields which we will return
		body :{
			"query": {
				"match": {
					"_id": {
						"query":id,
					}
				}
			}
		}
	}).then(results => {
		if (results.hits.total > 0)
		{
			resolve({ status: 200, message: results.hits.hits});	// return the rest details with the requseted id
		}
		else
		reject({ status: 404, message: 'Restaurant Not Found !' });
	}).catch(console.error);
});
