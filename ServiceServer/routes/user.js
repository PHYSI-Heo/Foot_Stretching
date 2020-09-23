const express = require('express');
const router = express.Router();
module.exports = router;

const fs = require('fs');
const db = require('../myDB');


router.post('/register', async(req, res)=>{
	var resObj = {};
	try{
		resObj.rows = await db.insert("user", req.body);		
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
		resObj.rows = await db.select("user", 
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


router.post('/update/info', async(req, res)=>{
	var resObj = {};
	try{
		let target = {"uCode" : req.body.uCode};
		delete req.body.uCode; 
		resObj.rows = await db.update("user", 
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


router.post('/add/schedule', async(req, res)=>{
	var resObj = {};
	try{
		resObj.rows = await db.insert("schedule", req.body);		
		resObj.result = 1001;
	}catch(err){
		resObj.result = err.errno;
		resObj.err = err.code;
	}
	console.log(resObj);
	res.json(resObj);
});


router.post('/get/schedules', async(req, res)=>{
	var resObj = {};
	try{
		resObj.rows = await db.select("schedule", 
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


router.post('/update/schedule', async(req, res)=>{
	var resObj = {};
	try{
		let target = {"no" : req.body.no};
		delete req.body.no; 
		resObj.rows = await db.update("schedule", 
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


router.post('/delete/schedule', async(req, res)=>{
	var resObj = {};
	try{		
		resObj.rows = await db.delete("schedule", 
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
