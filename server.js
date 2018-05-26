'use strict';

const express    = require('express');        
const app        = express();                
const bodyParser = require('body-parser');
const logger 	 = require('morgan');
const router 	 = express.Router();
const port 	   	 = process.env.PORT || 3000;
const passport   = require('passport');
const flash      = require('connect-flash');
const morgan     = require('morgan');
const session    = require('express-session');
const cookieParser = require('cookie-parser');

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


// set up ejs for templating
app.set('views', __dirname + '/views');
app.set('view engine','ejs');

app.use('/client',express.static('client'));
app.use('/assets',express.static('assets'));


// set up our express application
app.use(morgan('dev')); // log every request to the console
app.use(cookieParser()); // read cookies (needed for auth)
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(logger('dev'));

/*
// required for passport
app.use(session({ secret: 'ballsBallsasdsadsadsadsa' })); // session secret
app.use(passport.initialize());
app.use(passport.session()); // persistent login sessions
app.use(flash()); // use connect-flash for flash messages stored in session
*/

require('./routes')(router);
app.use('/api/v1', router);
app.listen(port);

console.log(`App Runs on ${port}`);