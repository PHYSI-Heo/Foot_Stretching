const express = require('express');
const router = express.Router();
module.exports = router;

const fs = require('fs');
const db = require('../myDB');


router.post('/setting', async(req, res)=>{
	console.log(req.body);

	var totalTime = 0;
	var patternItems = [];
	var items = JSON.parse(req.body.items);
	for await(const item of items){
		let obj = item.info;
		let maxTime = obj.lefthold > obj.righthold ? obj.lefthold : obj.righthold;
		totalTime = totalTime + parseInt(maxTime) + 1;
		patternItems.push({
			"leftMove" : obj.leftDir + "," + obj.leftAngle + "," + obj.leftSpeed + "," + obj.lefthold + "," + (obj.leftReturn ? "1" : "0"),
			"rightMove" : obj.rightDir + "," + obj.rightAngle + "," + obj.rightSpeed + "," + obj.righthold + "," + (obj.rightReturn ? "1" : "0"),
		});
	}

	var _id;
	var resObj = {};
	try{
		if(req.body.patternId == -1){
			resObj.rows = await db.insert("pattern", {
				"pName" : req.body.name,
				"hCode" : req.body.hCode,
				"hName" : req.body.hName,
				"keyword" : req.body.keyword,
				"explanation" : req.body.explanation,
				"addDate" : req.body.today,
				"totalTime" : totalTime,
			});	
			_id = resObj.rows.insertId;
		}else{
			resObj.rows = await db.update("pattern", {
				"pName" : req.body.name,
				"keyword" : req.body.keyword,
				"explanation" : req.body.explanation,
				"lastDate" : req.body.today,
				"totalTime" : totalTime,
			}, 
			{"pCode" : req.body.patternId},
			null);	
			_id = req.body.patternId;
		}			
		console.log(resObj.rows);
		console.log("Pattern ID : " + _id);

		var pos = 0;
		for await(const obj of items){
			patternItems[pos].pCode = _id;
			patternItems[pos]._order = pos++;			
		}		

		await db.delete("patternItem", 
			{"pCode" : _id},  
			null);
		await db.insert("patternItem", 
			patternItems);
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


router.post('/delete', async(req, res)=>{
	var resObj = {};
	try{		
		await db.delete("hospitalPattern", 
			req.body,
			null);
		await db.delete("patternItem", 
			req.body,
			null);
		await db.delete("schedule", 
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
		resObj.rows = await db.select("patternItem", 
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


router.post('/hospital/list', async(req, res)=>{
	var resObj = {};
	try{
		resObj.rows = await db.select("hospitalPattern", 
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


router.post('/hospital/setting', async(req, res)=>{
	console.log(IsJsonString(req.body.items));
	var items;
	if(IsJsonString(req.body.items)){
		items = JSON.parse(req.body.items);
	}else{
		items = req.body.items;
	}

	var resObj = {};
	try{
		await db.delete("hospitalPattern", 
			{"hCode" : items[0].hCode},
			null);
		resObj.rows = await db.insert("hospitalPattern", 
			items);
		resObj.result = 1001;
	}catch(err){
		resObj.result = err.errno;
		resObj.err = err.code;
	}
	console.log(resObj);
	res.json(resObj);
});




function IsJsonString(obj) {
  try {
    var json = JSON.parse(obj);
    return (typeof json === 'object');
  } catch (e) {
    return false;
  }
}








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


router.post('/item/update', async(req, res)=>{
	var resObj = {};
	try{
		let target = {"pCode" : req.body[0].pCode};
		await db.delete("patternItem", 
			target,
			null);	
		resObj.rows = await db.insert("patternItem", 
			req.body);
		resObj.result = 1001;
	}catch(err){
		resObj.result = err.errno;
		resObj.err = err.code;
	}
	console.log(resObj);
	res.json(resObj);
});

