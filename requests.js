const express = require("express");
const app = express();
const mysql = require('mysql');
var url = require('url');
var cors = require('cors')

app.use(cors())

const connection = mysql.createConnection({
  host     : 'localhost',
  user     : 'root',
  password : '',
  database : 'protocols'
});

connection.connect((err) => {
    if(err) throw err;
    console.log('Connected to MySQL Server!');
});

app.get("/requests",(req,res) => {

    // var q = url.parse(req.url, true);
    // var qdata = q.query; //returns an object: { year: 2017, month: 'february' }
    // var SL_No = qdata.SL_No;
    // var Roll_no = qdata.Roll_no;

    //console.log(SL_No);

    connection.query('SELECT * FROM request', (err, rows) => {
        if(err) throw err;
        console.log('The data from users table are: \n', rows);
        
        res.send(rows);

        //connection.end();
    });
});

app.listen(8085, () => {
    console.log('Server is running at port 8085');
});