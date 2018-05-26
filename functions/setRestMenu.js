const elasticsearch = require('elasticsearch');
const esClient = new elasticsearch.Client({
		host: 'https://search-hungrymonkey-3eiz5dewb4yoyjt6ykeufmsjn4.eu-central-1.es.amazonaws.com',
		log: 'error'
	});
const mergeJSON = require("merge-json") ;	
	
exports.setMenu = (restDetails,menu,id) => 
	new Promise((resolve,reject) => {	
		let bulkBody = [];
		//console.log(restDetails);
		//console.log(menu);
		var temp=mergeJSON.merge(restDetails,menu);//merge the rest details the rest menu to one object 
		//console.log(temp);
		bulkBody.push({//insration of the object into array 
			index: {
				_index: 'hungrymonkeyrests',
				_type: 'restaurants',
				_id: id
			}
		});
		//console.log(temp);
		bulkBody.push(temp);	
		 esClient.bulk({body: bulkBody})// insartion to database by id
		.then(response=>{//case success resolve string "menu have been submited"
				resolve({ status: 201, message: "menu have been submited"})})
		.catch(console.error);
	});	