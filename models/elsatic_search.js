(function () {
'use strict';

const elasticsearch = require('elasticsearch');
const search_users=require('./functions/search_user');
const esClient = new elasticsearch.Client({
    host: '127.0.0.1:9200',
	log: 'error'
});

const search = function search(index, body) {
    return esClient.search({index: index, body: body});
  };

  
  
  module.exports = {
    search
  };
} ());