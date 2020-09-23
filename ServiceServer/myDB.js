
const mysql = require('mysql2');

const MYSQLIP = 'localhost';
const MYSQLID = 'root';
const MYSQLPWD = '1234';
const DBNAME = 'foot';

var dbConfig = {
  host : MYSQLIP,
  port : 3306,
  user : MYSQLID,
  password : MYSQLPWD,
  connectionLimit:100,
  waitForConnections:true
};

const SQL_CD = "CREATE DATABASE " + DBNAME;

// varbinary & varchar
const SQL_CT_USERs = "CREATE TABLE IF NOT EXISTS user ( " +
"uCode INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
"name VARCHAR(10) NOT NULL, " +
"phone VARCHAR(20));";

const SQL_CT_SCHEDULEs = "CREATE TABLE IF NOT EXISTS schedule ( " +
"no INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
"uCode INT NOT NULL, " +
"pCode INT NOT NULL, " +
"pName VARCHAR(20) NOT NULL, " +
"_order INT NOT NULL, " +
"dateTime VARCHAR(20) NOT NULL, " +
"fulfill BOOLEAN NOT NULL DEFAULT FALSE);"; 

const SQL_CT_PATTERNs = "CREATE TABLE IF NOT EXISTS pattern ( " +
"pCode INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
"pName VARCHAR(20) NOT NULL, " +
"keyword VARCHAR(250) NOT NULL);"; 

const SQL_CT_PATTERN_DETAIL = "CREATE TABLE IF NOT EXISTS pattern_detail ( " +
"pCode INT NOT NULL, " +
"_order INT NOT NULL, " +
"leftMove VARCHAR(30) NOT NULL, " +
"rightMove VARCHAR(30) NOT NULL);"; 

let dbPool = mysql.createPool(dbConfig);

module.exports.init = () => {
  initDB().then(async()=>{
    try{
      const db = dbPool.promise();
      await db.query(SQL_CT_USERs);    
      await db.query(SQL_CT_SCHEDULEs);  
      await db.query(SQL_CT_PATTERNs);    
      await db.query(SQL_CT_PATTERN_DETAIL);   
    }catch(err){
      console.log(err);
    }   
  });
}

function initDB() {
  return new Promise((res, rej) => {
    dbPool.query(SQL_CD, (err, rows, fields) => {
      dbConfig.database = DBNAME;
      dbPool = mysql.createPool(dbConfig);
      res();
    });
  });
}


module.exports.insert = (table, params) => {
  return new Promise(async(resolve, reject) => {
    var cnt = 0;    
    var data = JSON.parse(JSON.stringify(params));
    var param = Array.isArray(data) ? data[0] : data;
    var sql = "INSERT INTO " + table + "(";
    var val = "VALUES (";
    const columns = JSON.parse(JSON.stringify(Object.keys(param)));
    for await (const column of columns) {
      sql += column;
      val += "'" + param[column] + "'";
      if(columns.length -1 != cnt++){
        sql += ", ";
        val += ", ";
      }else{
        sql += ") ";
        val += ")";
      }
    }

    if (Array.isArray(data)) {
      data.shift(); // remove array 0 index.
      for await(const obj of data){
        cnt = 0;
        val += ", (";
        for await (const column of columns) {       
          val += "'" + obj[column] + "'";
          if(columns.length -1 != cnt++){
            val += ", ";
          }else{
            val += ")";
          }
        }
      }
    }
    sql += val;

    console.log(sql);
    dbPool.query(sql, (err, rows, fields) => {
      if(err)
        reject(err);    
      if(rows)
        resolve(rows);
    });
  });  
}



module.exports.select = (table, params, target, option) => {
  return new Promise(async(resolve, reject)=>{
    var cnt = 0;
    var sql = "SELECT ";
    if(params){
      for await (const column of params) {
        sql += column;
        if(params.length -1 != cnt++){
          sql += ", ";
        } 
      }
    }else{
      sql += "*";
    }
    sql += " FROM " + table;
    if(target){
      const name = Object.keys(target)[0];
      sql += " WHERE " + name + " = '"  + target[name] + "'";
    }

    console.log(sql);
    dbPool.query(sql, (err, rows, fields) => {
      if(err)
        reject(err);    
      if(rows)
        resolve(rows);
    });
  });
}


module.exports.delete = (table, target, option) =>{
  return new Promise(async(resolve, reject)=>{
    var sql = "DELETE FROM " + table;
    if(target){
      const name = Object.keys(target)[0];
      sql += " WHERE " + name + " = '"  + target[name] + "'";
    }

    console.log(sql);
    dbPool.query(sql, (err, rows, fields) => {
      if(err)
        reject(err);    
      if(rows)
        resolve(rows);
    });
  });  
}


module.exports.update = (table, params, target, option) =>{
  return new Promise(async(resolve, reject)=>{
    var cnt = 0;
    sql = "UPDATE " + table + " SET ";
    var columns = Object.keys(params);
    if(params){
      for await (const column of columns) {
        sql += column + "='" + params[column] + "'";      
        if(columns.length -1 != cnt++){
          sql += ", ";
        } 
      }
    }
    if(target){
      const name = Object.keys(target)[0];
      sql += " WHERE " + name + " = '"  + target[name] + "'";
    }

    console.log(sql);
    dbPool.query(sql, (err, rows, fields) => {
      if(err)
        reject(err);    
      if(rows)
        resolve(rows);
    });
  });
}

