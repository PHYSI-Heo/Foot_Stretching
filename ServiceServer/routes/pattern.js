const express = require('express');
const router = express.Router();
module.exports = router;

const fs = require('fs');
const db = require('../myDB');


router.post('/register', async(req, res)=>{
	var resObj = {};
	try{
		resObj.rows = await db.insert("pattern", req.body);		
		resObj.result = 1001;
	}catch(err){
		resObj.result = err.errno;
		resObj.err = err.code;
	}
	console.log(resObj);
	res.json(resObj);
});


router.post('/list', async(req, res)=>{
	var resObj = {};
	try{
		resObj.rows = await db.select("pattern", 
			null, 
			null, 
			null);
		resObj.result = 1001;
	}catch(err){
		resObj.result = err.errno;
		resObj.err = err.code;
	}
	console.log(resObj);
	res.json(resObj);
});


router.post('/update', async(req, res)=>{
	var resObj = {};
	try{
		let target = {"pCode" : req.body.pCode};
		delete req.body.pCode; 
		resObj.rows = await db.update("pattern", 
			req.body,
			target,
			null);
		resObj.result = 1001;
	}catch(err){
		resObj.result = err.errno;
		resObj.err = err.code;
	}
	console.log(resObj);
	res.json(resObj);
});


router.post('/delete', async(req, res)=>{
	var resObj = {};
	try{		
		await db.delete("pattern_detail", 
			req.body,
			null);
		resObj.rows = await db.delete("pattern", 
			req.body,
			null);
		resObj.result = 1001;
	}catch(err){
		resObj.result = err.errno;
		resObj.err = err.code;
	}
	console.log(resObj);
	res.json(resObj);
});


router.post('/item/list', async(req, res)=>{
	var resObj = {};
	try{
		resObj.rows = await db.select("pattern_detail", 
			null, 
			req.body, 
			null);
		resObj.result = 1001;
	}catch(err){
		resObj.result = err.errno;
		resObj.err = err.code;
	}
	console.log(resObj);
	res.json(resObj);
});


router.post('/item/update', async(req, res)=>{
	var resObj = {};
	try{
		let target = {"pCode" : req.body[0].pCode};
		await db.delete("pattern_detail", 
			target,
			null);	
		resObj.rows = await db.insert("pattern_detail", 
			req.body);
		resObj.result = 1001;
	}catch(err){
		resObj.result = err.errno;
		resObj.err = err.code;
	}
	console.log(resObj);
	res.json(resObj);
});
