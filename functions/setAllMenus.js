
//api which store a json array full of resturants object

const elasticsearch = require('elasticsearch');
//data base configurtions and connection before we query
const esClient = new elasticsearch.Client({
		host: 'https://search-hungrymonkey-3eiz5dewb4yoyjt6ykeufmsjn4.eu-central-1.es.amazonaws.com',
		log: 'error'
	});


	exports.importData = (data) =>
		new Promise((resolve,reject) => {
		let bulkBody = [];
		data.forEach(item => {// preapre the object before insrtion for each menu set id and its type
			bulkBody.push({
				index: {
					_index: 'hungrymonkeyrests',
					_type: 'restaurants',
					_id: item.id
				}
			});

			bulkBody.push(item);
		});
		esClient.bulk({body: bulkBody})// insert the array of objects
			.then(response=>{
				let errorCount = 0;
				response.items.forEach(item => {
				if (item.index && item.index.error) {
					console.log(++errorCount, item.index.error);
				}
			});
			console.log(
					`Successfully indexed ${data.length - errorCount}
					out of ${data.length} items`
			);

		});
			});
