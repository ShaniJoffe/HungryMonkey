const elasticsearch = require('elasticsearch');
const esClient = new elasticsearch.Client({
		host: 'https://search-hungrymonkey-3eiz5dewb4yoyjt6ykeufmsjn4.eu-central-1.es.amazonaws.com',
		log: 'error'
	});
const mergeJSON = require("merge-json") ;	
	
exports.setMenu = (restDetails,menu,id) => 
	new Promise((resolve,reject) => {	
		let bulkBody = [];
		var temp=mergeJSON.merge(restDetails,menu);
		bulkBody.push({
			index: {
				_index: 'hungrymonkeyrests',
				_type: 'restaurants',
				_id: id
			}
			});
			console.log(temp);
			bulkBody.push(temp);	
			 esClient.bulk({body: bulkBody})
			.then(response=>{
					resolve({ status: 201, message: "fuck yeah"})})
						.catch(console.error);
	});	