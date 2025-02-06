`const mongoose = require('mongoose');


const customerSchema ={
    sellerPn:{
        type:String
    },
    Name:{
        type:String
    },
    phNo:{
        type:String
    },
    qnt:{
        type:String
    },
    rate:{
        type:Number
    },
    startDate:{
        type:Date,
        default:Date.now
    },
    extras:[
        {
            extraQnt:{
                type:String
            },
            extraDate:{
                type:Date,
                default:Date.now
            }
        }
    ],
    noSupply:[
        {
            noSupplyDate:{
                type:Date
            }
        }
    ]
};


module.exports = mongoose.model('Customer',customerSchema);`