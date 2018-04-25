'use strict';

const express    = require('express');        
const app        = express();                
const bodyParser = require('body-parser');
const logger 	 = require('morgan');
const router 	 = express.Router();
const port 	   	 = process.env.PORT || 3000;


//CORS middleware
var allowCrossDomain = function(req, res, next) {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE');
    res.header('Access-Control-Allow-Headers', 'Content-Type, x-xsrf-token, Authorization');

    if ('OPTIONS' == req.method) {
      res.sendStatus(200);
    } else {
      next();
    }
}
app.use(allowCrossDomain);

app.set('views', __dirname + '/views');
app.set('view engine','ejs');

app.use('/client',express.static('client'));
app.use('/assets',express.static('assets'));




app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(logger('dev'));

require('./routes')(router);
app.use('/api/v1', router);
app.listen(port);

console.log(`App Runs on ${port}`);