(function () {
  'use strict';

  const elasticsearch = require('elasticsearch');
  const esClient = new elasticsearch.Client({
    host: '127.0.0.1:9200',
    log: 'error'
  });

  const search = function search(index, body) {
    return esClient.search({index: index, body: body});
  };

  // only for testing purposes
  // all calls should be initiated through the module
  const searchUsers = function searchUsers(user,pass) {
	  var tempString= '(name:+'+user+')  AND (password:'+pass+')';
    let body = {
      size: 1,
      from: 0,
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
    };

    console.log(`retrieving documents whose name add password match (displaying ${body.size} items at a time)...`);
	search('hungrymonkey', body)
    .then(results => {
      console.log(`found ${results.hits.total} items in ${results.took}ms`);
      if (results.hits.total > 0) console.log(`returned article titles:`);
      results.hits.hits.forEach((hit, index) => console.log(`\t${body.from + ++index} - ${hit._source.name} (score: ${hit._score})`));
    })
    .catch(console.error);
  };

	//searchUsers(user,pass);

  module.exports = {
    search
  };
} ());
