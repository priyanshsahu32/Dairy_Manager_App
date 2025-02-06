const express = require('express');
const morgan = require('morgan');
const app = express();
const dotenv = require('dotenv');
const connectdb = require('./config/db');

app.use(morgan('dev'));
app.use(express.json({}));
app.use(express.json({
    extended:true
}));
dotenv.config({
    path:'./config/config.env'
});

connectdb();
const Port = process.env.PORT || 3000;

app.use('/customer_routes',require('./Routes/customer_routes'));

app.listen(Port,console.log('running'));
