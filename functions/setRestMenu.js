const elasticsearch = require('elasticsearch');
const esClient = new elasticsearch.Client({
		host: 'https://search-hungrymonkey-3eiz5dewb4yoyjt6ykeufmsjn4.eu-central-1.es.amazonaws.com',
		log: 'error'
	});
	
	
exports.setMenu = (menu,id) => 
	new Promise((resolve,reject) => {	
		let bulkBody = [];
		bulkBody.push({
			update: {
				_index: 'hungrymonkeyrests',
				_type: 'restaurants',
				_id: id
			}
			});
		bulkBody.push({
			doc: { title: menu } 
			});
			//bulkBody.push(menu);
			console.log(bulkBody);
			 esClient.bulk({body: bulkBody})
			.then(response=>{
					resolve({ status: 201, message: "fuck yeah"})})
						.catch(console.error);
	});	