const mongoose = require('mongoose');

const ConnectDb = async ()=>{
    const conn = await mongoose.connect(process.env.MONGO_URI);
    console.log("connected");
    
}


module.exports = ConnectDb;