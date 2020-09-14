const express = require('express');
const bodyParser = require('body-parser');
const path = require('path');


const db = require('./myDB');
db.init();


var app = express();
app.set('port', process.env.PORT || 3000);
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
  extended: false
}));
app.use(express.static(path.join(__dirname, 'public')));
app.use(require('stylus').middleware(path.join(__dirname, 'public')));


app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');
app.engine('html', require('ejs').renderFile);


app.listen(app.get('port'), function() {
  console.log("# Start Server..");
});


var deviceRouter = require('./routes/user');
app.use('/user', deviceRouter);

var imgRouter = require('./routes/pattern');
app.use('/pattern', imgRouter);


app.get('/manager', function(req, res, next) {
  res.render('manager.html');
});