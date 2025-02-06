const express = require('express');


const customer = require('../models/customer_model');

const router = express.Router();

// TO ADD NEW CUSTOMER
router.post('/add_customer',async(req,resp)=>{
    try {
        const seller_phoneno = req.body.sellerPn;
        const cust_phoneno = req.body.phNo;
        const cust_exist = await customer.findOne({sellerPn:seller_phoneno,phNo:cust_phoneno});

        if(cust_exist){
            return resp.status(200).json({
                success:true,
                msg:'CUSTOMER ALREADY EXIST'
            });
        }
        const cust = await customer.create({
            sellerPn:seller_phoneno,
            Name:req.body.Name,
            phNo:req.body.phNo,
            qnt:req.body.qnt,
            rate:req.body.rate,
            startDate:req.body.startDate
        });

        if(!cust){
            return resp.status(400).json({
                success:false,
                msg:'Something went wrong'
            });
        }

        return resp.status(200).json({
            success:true,
            Customer : cust,
            msg:'Successfully Created'
        });


    } catch (error) {
        return resp.status(500).json({
            success:false,
            msg:'Server Error'
        });
    }




    

});



//  TO DELETE CUSTOMER

router.delete('/delete_customer',async(req,resp)=>{

    try {

        const seller_phoneno = req.body.sellerPn;
        const cust_phoneno = req.body.phNo;

        const user_deleted = customer.deleteOne({sellerPn:seller_phoneno,phNo:cust_phoneno});

        if((await user_deleted).deletedCount>0){
            return resp.status(200).json({
                success:true,
                msg:'customer removed successfully'
            });

        }else{

            return resp.status(400).json({
                success:true,
                msg:'customer not found'
            });
        }
        
    } catch (error) {
        return resp.status(500).json({
            success:false,
            msg:'server error'
        });
    }

});


// to edit customer detail

router.put('/edit_customer',async(req,resp)=>{

    try {

        const seller_phoneno = req.body.sellerPn;
        const cust_phoneno_old = req.body.phNo_old;
        const cust_phoneno_new = req.body.phNo_new;


        
        const update_cust = await customer.updateOne({
            sellerPn:seller_phoneno,
            phNo:cust_phoneno_old

        },{$set:{

            Name:req.body.Name,
            phNo:cust_phoneno_new,
            qnt:req.body.qnt,
            rate:req.body.rate,
            startDate:req.body.startDate

        }});

        if(update_cust.acknowledged){
            return resp.status(200).json({
                success:true,
                msg:'customer updated successfully'
            });

        }else{
            return resp.status(400).json({
                success:true,
                msg:'not updated'
            });
        }

        
    } catch (error) {

        return resp.status(500).json({
            success:false,
            msg:'server error'
        });
        
    }

});

// to add extras
router.put('/add_extras',async (req,resp)=>{
    try {
        const seller_phoneno =  req.body.sellerPn;
        const cust_phoneno =  req.body.phNo;
        const extra_qnt =  req.body.qnt;

        
        const add_extra = await customer.updateOne({

            sellerPn:seller_phoneno,
            phNo:cust_phoneno

        },{
            $push:{
                extras:{
                    extraQnt:extra_qnt
                }
            }
        });


        if(add_extra.modifiedCount>0){
            return resp.status(200).json({
                success:true,
                msg:'extra qnt added'
            });
        }
        else{
            return resp.status(200).json({
                success:true,
                msg:'not updated'
            });
        }
    } catch (error) {

        return resp.status(500).json({
            success:false,
            msg:'server error'
        });
        
    }


    
});


//  to add date of no supply
router.put('/add_noSupply',async (req,resp)=>{

    try {
        const seller_phoneno =  req.body.sellerPn;
        const cust_phoneno =  req.body.phNo;
        

        
        const add_extra = await customer.updateOne({

            sellerPn:seller_phoneno,
            phNo:cust_phoneno

        },{
            $push:{
                noSupply:{
                    noSupplyDate:new Date()
                }
            }
        });


        if(add_extra.modifiedCount>0){
            return resp.status(200).json({
                success:true,
                msg:'npsupply qnt added'
            });
        }
        else{
            return resp.status(200).json({
                success:true,
                msg:'not updated'
            });
        }
    } catch (error) {

        return resp.status(500).json({
            success:false,
            msg:'server error'
        });
        
    }

});


// to fetch all the customer
router.post('/fetch_customer',async (req,resp)=>{

    const seller_phoneno =  req.body.sellerPn; 

    try{
        const customers = await customer.find({
            "sellerPn":seller_phoneno
        });

        if(customers){
            return resp.status(200).json({
                success:true,
                msg:'all customer fetched successfully',
                customers:customers
            });
        }else{
            return resp.status(200).json({
                success:true,
                msg:'customer not found'
            });
        }
    }
    catch(error){
        return resp.status(500).json({
            success:false,
            msg:'server error'
        });
    }

});


module.exports = router;