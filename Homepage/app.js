const express = require('express');
const path = require('path');
const ejs = require('ejs');

var app = express();

app.set('port', process.env.PORT || 80);
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use(require('stylus').middleware(path.join(__dirname, 'public')));
app.use(express.static(path.join(__dirname, 'public')));

// Server Start
app.listen(app.get('port'), function() {
  console.log("# Start Server..");
});

app.get('/', (req, res) => {
	res.render('index');
});