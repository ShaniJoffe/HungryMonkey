const elasticsearch = require('elasticsearch');
const esClient = new elasticsearch.Client({
		host: 'https://search-hungrymonkey-3eiz5dewb4yoyjt6ykeufmsjn4.eu-central-1.es.amazonaws.com',
		log: 'error'
	});
	
	
	exports.setRestD = (rest) => 
		
	new Promise((resolve,reject) => {
		var tempString= '(rest_name:'+rest.rest_name+')  AND (rest_zip:'+rest.rest_zip+')';
		let bulkBody = [];
		esClient.count({
			  index: 'hungrymonkeyrests',
			  type: 'restaurants',
			 
		}).then(temp => {
	
			if(temp.count==0)//case the db is empety
			{
				bulkBody.push({
					index: {
						_index: 'hungrymonkeyrests',
						_type: 'restaurants',
						_id: temp.count
					}
				});
				bulkBody.push(rest);
				console.log("bulkbody"+bulkBody);
				 esClient.bulk({body: bulkBody})
				.then(response=>{
					console.log("response"+response);
						resolve({ status: 201, message: temp.count})})
							.catch(console.error);
			}
			else// case not emepty
			{
				console.log("counter: "+temp.count);
				esClient.search({//check if exists 
					index: 'hungrymonkeyrests',
					type: 'restaurants',
					body: {
						query: {
							bool: {
								must: [
									{
										query_string: {
											query:tempString
										}
									}
								]    
							}
						}
					}
				}).then(temp1 => {					
					if (temp1.hits.total > 0)
					{
						console.log("Rest's details exists");
						reject({ status: 409, message: "Rest's details Already Registered !" });				
					}
					else// case not
					{
						bulkBody.push({
							index: {
								_index: 'hungrymonkeyrests',
								_type: 'restaurants',
								_id: temp.count
							}
						});
						bulkBody.push(rest);
						console.log("bulkbody"+bulkBody);
						esClient.bulk({body: bulkBody})
						.then(response=>{
							console.log("balls"+response);
								resolve({ status: 201, message: temp.count})})
									.catch(console.error);
					}
				}).catch(console.error);
			}
		}).catch(console.error);
	
	});			
		