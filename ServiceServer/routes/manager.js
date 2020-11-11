const express = require('express');
const router = express.Router();
module.exports = router;

const fs = require('fs');
const db = require('../myDB');

const masterEmail = "admin@admin.com";

function selectHospitalEmail(target) {
	return new Promise(async(resolve, reject)=>{  
		var resObj = {};
		try{
			resObj.rows = await db.select("hospital", 
				null,
				target,
				null);
			resObj.result = 1001;
		}catch(err){
			resObj.result = err.errno;
			resObj.err = err.code;
		} 
		resolve(resObj);   
	});
}


// 
// 		Login Manager
// 
router.get('/auth/login', (req, res) => {
	res.render('login');
});

router.post('/auth/login', async (req, res) => {
	let target = {"email" : req.body.email};
	var resObj = await selectHospitalEmail(target);
	
	if(resObj.rows.length == 0){
		resObj.result = 1102;
	}else if(resObj.rows[0].pwd != req.body.pwd){		
		resObj.result = 1103;
	}else{
		req.session.name = resObj.rows[0].name;
		req.session.code = resObj.rows[0].hCode;
	}
	console.log(resObj);
	res.json(resObj);
});

router.get('/auth/logout', (req, res) => {
	delete req.session.name;
	delete req.session.code;
	req.session.destroy(function () {
		res.redirect('/manager/dashboard/index');
	})
});


// 
// 		Register Manager
// 
router.get('/auth/signup', (req, res) => {	
	res.render('signup');
});

router.post('/auth/signup', async (req, res) => {
	let target = {"email" : req.body.email};
	var resObj = await selectHospitalEmail(target);

	if(resObj.rows.length != 0){
		resObj.result = 1101;
	}else{
		try{
			resObj.rows = await db.insert("hospital", 
				req.body);			
			resObj.result = 1001;
		}catch(err){
			resObj.result = err.errno;
			resObj.err = err.code;
		}		
	}
	console.log(resObj);
	res.json(resObj);
});



router.get('/dashboard/index', (req, res) => {
	res.render('index', 
	{
		session : req.session
	});
});




router.get('/dashboard/my/pattern', (req, res) => {
	res.render('myPattern', 
	{
		session : req.session
	});
});



