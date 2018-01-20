var NodeGeocoder = require('node-geocoder');
 
var options = {
  provider: 'google',
 
  // Optional depending on the providers
  httpAdapter: 'https', // Default
  apiKey: 'YOUR_API_KEY', // for Mapquest, OpenCage, Google Premier
  formatter: null         // 'gpx', 'string', ...
};
var geocoder = NodeGeocoder(options);

exports.geocoder.geocode('29 champs elys?e paris')
  .then(function(res) {
    console.log(res);
  })
  .catch(function(err) {
    console.log(err);
  });