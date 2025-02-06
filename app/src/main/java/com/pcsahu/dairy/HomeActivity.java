package com.pcsahu.dairy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.pcsahu.dairy.Adapters.CustomerAdapter;
import com.pcsahu.dairy.Interfaces.RecycleViewClickListener;
import com.pcsahu.dairy.models.CustomerModel;
import com.pcsahu.dairy.models.Extras;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements RecycleViewClickListener {
    FloatingActionButton add_cust_button;

    RecyclerView rv;
    CustomerAdapter ca;
    TextView tv;

    ArrayList<CustomerModel> customers;

    ProgressBar pb;

    String seller_pn = "7828582123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );



        rv = findViewById( R.id.recycler_view );
        pb = findViewById( R.id.pb );
        tv = findViewById( R.id.EmptyTv );
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize( true );

        add_cust_button = findViewById( R.id.add_customer );




        customers = new ArrayList<>();

        rv.setVisibility( View.GONE );
        pb.setVisibility( View.VISIBLE );

        fetchCustomer();


        add_cust_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddcustomerDialog();
            }
        });
    }


    private void AddCustomer(String Name, String phNO, String qnt, int rate, String startDate, AlertDialog dialog) {
        String url = "https://dairy-hejo.onrender.com/customer_routes/add_customer";

        JSONObject body2 = new JSONObject();
        try {
            body2.put("sellerPn", seller_pn);
            body2.put("Name", Name);
            body2.put("phNo", phNO);
            body2.put("qnt", qnt);
            body2.put("rate", rate);
            body2.put("startDate", startDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body2, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getBoolean( "success" )){
                        Toast.makeText( HomeActivity.this,response.getString("msg" ),Toast.LENGTH_SHORT ).show();
                        dialog.cancel();
                        fetchCustomer();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException( e );
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText( HomeActivity.this,error.toString(),Toast.LENGTH_SHORT ).show();

                NetworkResponse response = error.networkResponse;
                if(error==null || error.networkResponse == null){
                    return;
                }
                String body;

                try {
                    body  = new String(error.networkResponse.data,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException( e );
                }
                try {
                    JSONObject errorObject = new JSONObject(body);
                    Toast.makeText(HomeActivity.this,"Error : "+errorObject.getString( "msg" ),Toast.LENGTH_SHORT );
                } catch (JSONException e) {
                    throw new RuntimeException( e );
                }





            }
        } );

        // Add the request to the RequestQueue
        int socketTime = 3000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTime,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy( retryPolicy );
        RequestQueue rq = Volley.newRequestQueue( this );
        rq.add( jsonObjectRequest );


    }

    private void deleteCustomer(String phNo) {
            String url = "https://dairy-hejo.onrender.com/customer_routes/delete_customer";
            JSONObject body2 = new JSONObject();
           try {
               body2.put( "sellerPn",seller_pn );
               body2.put("phNo",phNo);
           }catch (JSONException e) {
               throw new RuntimeException( e );
           }

           JsonObjectRequest jor  = new JsonObjectRequest( Request.Method.POST, url, body2, new Response.Listener<JSONObject>() {
               @Override
               public void onResponse(JSONObject response) {

                   try {
                       if(response.getBoolean( "success" )){
                           Toast.makeText( HomeActivity.this,"customer deleted successfully",Toast.LENGTH_SHORT).show();
                           customers.clear();
                           fetchCustomer();
                       }
                   } catch (JSONException e) {
                       throw new RuntimeException( e );
                   }

               }
           }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {

                   Toast.makeText( HomeActivity.this,error.toString(),Toast.LENGTH_SHORT ).show();

                   NetworkResponse response = error.networkResponse;
                   if(error==null || error.networkResponse == null){
                       return;
                   }
                   String body;

                   try {
                       body  = new String(error.networkResponse.data,"UTF-8");
                   } catch (UnsupportedEncodingException e) {
                       throw new RuntimeException( e );
                   }
                   try {
                       JSONObject errorObject = new JSONObject(body);
                       Toast.makeText(HomeActivity.this,"Error : "+errorObject.getString( "msg" ),Toast.LENGTH_SHORT );
                   } catch (JSONException e) {
                       throw new RuntimeException( e );
                   }


               }
           } ){
               @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("Content-Type","application/json");

                return params;
            }
        };

           int sockettime = 3000;
            RetryPolicy retryPolicy = new DefaultRetryPolicy(sockettime,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jor.setRetryPolicy( retryPolicy );
            RequestQueue rq = Volley.newRequestQueue( this );
            rq.add( jor );

        }


    private void fetchCustomer() {
        customers.clear();



        String url = "https://dairy-hejo.onrender.com/customer_routes/fetch_customer";
        HashMap<String,String> body2 = new HashMap<>();
        body2.put( "sellerPn",seller_pn);


        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());

        JsonObjectRequest jor = new JsonObjectRequest( Request.Method.POST, url, new JSONObject(body2), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {



                    if(response.getBoolean( "success" )){
                        JSONArray customerArray = response.getJSONArray( "customers" );
                        Log.d("API Response", response.toString());
                        if(customerArray.length()==0){


//                             testview to show no customer
                            pb.setVisibility( View.GONE );
                            rv.setVisibility( View.GONE );
                            tv.setVisibility( View.VISIBLE );


                        }else{
                            tv.setVisibility( View.GONE );
                            for(int i = 0;i<customerArray.length();i++){

                                JSONObject jo = customerArray.getJSONObject( i );

                                String tempdate = jo.getString( "startDate" );
                                Date startDate = isoFormat.parse(tempdate);

                                ArrayList<Extras> extralist = new ArrayList<>();
                                for(int j = 0;j<jo.getJSONArray( "extras" ).length();j++){

                                    JSONObject extratemp = jo.getJSONArray( "extras" ).getJSONObject( i );
                                    Date extradate = isoFormat.parse(jo.getString( "extraDate" ));
                                    Extras extobj = new Extras( extratemp.getString( "extraQnt" ),extradate );
                                    extralist.add( extobj );



                                }

                                ArrayList<Date> noSupply = new ArrayList<>();
                                for(int j = 0;j<jo.getJSONArray( "noSupply" ).length();j++){

                                    JSONObject extratemp = jo.getJSONArray( "noSupply" ).getJSONObject( i );
                                    Date nosup = isoFormat.parse( extratemp.getString( "noSupplyDate" ) );


                                    noSupply.add( nosup );



                                }
                                CustomerModel customerModel = new CustomerModel( jo.getString( "sellerPn" ),jo.getString( "Name" ),jo.getString("phNo"),jo.getString( "qnt" ),jo.getInt( "rate" ),startDate,extralist,noSupply,jo.getString( "_id" ));
                                customers.add( customerModel );


                            }

                            pb.setVisibility( View.GONE );
                            rv.setVisibility( View.VISIBLE );
                            ca = new CustomerAdapter( HomeActivity.this,HomeActivity.this,customers);
                            rv.setAdapter(ca);







                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    throw new RuntimeException( e );
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText( HomeActivity.this,error.toString(),Toast.LENGTH_SHORT ).show();

                NetworkResponse response = error.networkResponse;
                if(error==null || error.networkResponse == null){
                    return;
                }
                String body;

                try {
                    body  = new String(error.networkResponse.data,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException( e );
                }
                try {
                    JSONObject errorObject = new JSONObject(body);
                    Toast.makeText(HomeActivity.this,"Error : "+errorObject.getString( "msg" ),Toast.LENGTH_SHORT );
                } catch (JSONException e) {
                    throw new RuntimeException( e );
                }





            }
        } ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("Content-Type","application/json");

                return params;
            }
        };

        int socketTime = 7000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTime,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jor.setRetryPolicy( retryPolicy );
        RequestQueue rq = Volley.newRequestQueue( this );
        rq.add( jor );

    }

    private void editCustomer(String Name, String phNO_new, String qnt, int rate, String startDate, AlertDialog dialog, int position) {
        String phNo_old = customers.get( position ).getPhNo();
        String url = "https://dairy-hejo.onrender.com/customer_routes/edit_customer";

        JSONObject body2 = new JSONObject();
        try {
            body2.put("sellerPn", seller_pn);
            body2.put("Name", Name);
            body2.put("phNo_old", phNo_old);
            body2.put( "phNo_new",phNO_new );
            body2.put("qnt", qnt);
            body2.put("rate", rate);
            body2.put("startDate", startDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                body2, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getBoolean( "success" )){
                        Toast.makeText( HomeActivity.this,response.getString("msg" ),Toast.LENGTH_SHORT ).show();
                        dialog.cancel();
                        customers.clear();
                        fetchCustomer();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException( e );
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText( HomeActivity.this,error.toString(),Toast.LENGTH_SHORT ).show();

                NetworkResponse response = error.networkResponse;
                if(error==null || error.networkResponse == null){
                    return;
                }
                String body;

                try {
                    body  = new String(error.networkResponse.data,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException( e );
                }
                try {
                    JSONObject errorObject = new JSONObject(body);
                    Toast.makeText(HomeActivity.this,"Error : "+errorObject.getString( "msg" ),Toast.LENGTH_SHORT );
                } catch (JSONException e) {
                    throw new RuntimeException( e );
                }





            }
        } );

        // Add the request to the RequestQueue
        int socketTime = 3000;
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTime,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy( retryPolicy );
        RequestQueue rq = Volley.newRequestQueue( this );
        rq.add( jsonObjectRequest );



    }

    private void editCustomer(int position) {
        showEditcustomerDialog(position);
    }


    private void showAddcustomerDialog() {

        LayoutInflater inflater = getLayoutInflater();

        View alertView  = inflater.inflate( R.layout.custom_dialog_layout,null );
        final EditText name = alertView.findViewById( R.id.customer_name );
        final EditText phNo = alertView.findViewById( R.id.customer_phNo );
        final EditText qnt = alertView.findViewById( R.id.qnt );
        final EditText rate = alertView.findViewById( R.id.rate);
        final EditText startdate =alertView.findViewById( R.id.startDate );
        final Button add = alertView.findViewById( R.id.addBtn );
        final Button cancel = alertView.findViewById(R.id.cancelBtn );
        final ImageButton calendarBtn = alertView.findViewById( R.id.calendarBtn );
        final AlertDialog dialog = new AlertDialog.Builder(HomeActivity.this)
                .setView( alertView )
                .setCancelable( false )
                .create();

        add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               AddCustomer(name.getText().toString(),phNo.getText().toString(),qnt.getText().toString(),Integer.parseInt( rate.getText().toString() ),startdate.getText().toString(), dialog );

            }
        } );

        cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

            }
        } );

        int year,month,day;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get( Calendar.YEAR );
        month = calendar.get(Calendar.MONTH);
        day  = calendar.get( Calendar.DATE );

        calendarBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog( HomeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int yr, int mn, int dt) {
                        String date = dt + "/" + (mn + 1) + "/" + yr;
                        startdate.setText( date );

                    }
                } ,year,month,day);

                datePickerDialog.show();


            }
        } );








        dialog.show();


        return;

    }

    private void showEditcustomerDialog(int position) {

        LayoutInflater inflater = getLayoutInflater();

        View alertView  = inflater.inflate( R.layout.custom_dialog_layout,null );
        final EditText name = alertView.findViewById( R.id.customer_name );
        final EditText phNo = alertView.findViewById( R.id.customer_phNo );
        final EditText qnt = alertView.findViewById( R.id.qnt );
        final EditText rate = alertView.findViewById( R.id.rate);
        final EditText startdate =alertView.findViewById( R.id.startDate );
        final Button add = alertView.findViewById( R.id.addBtn );
        final Button cancel = alertView.findViewById(R.id.cancelBtn );
        final ImageButton calendarBtn = alertView.findViewById( R.id.calendarBtn );

        CustomerModel cm = customers.get( position );
        name.setText( cm.getName() );
        phNo.setText( cm.getPhNo() );
        qnt.setText( cm.getQnt());
        rate.setText( cm.getRate().toString());
        startdate.setText( cm.getStartDate().toString() );


        final AlertDialog dialog = new AlertDialog.Builder(HomeActivity.this)
                .setView( alertView )
                .setCancelable( false )
                .create();

        add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editCustomer(name.getText().toString(),phNo.getText().toString(),qnt.getText().toString(),Integer.parseInt( rate.getText().toString() ),startdate.getText().toString(), dialog,position);

            }
        } );

        cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

            }
        } );

        int year,month,day;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get( Calendar.YEAR );
        month = calendar.get(Calendar.MONTH);
        day  = calendar.get( Calendar.DATE );

        calendarBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog( HomeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int yr, int mn, int dt) {
                        String date = dt + "/" + (mn + 1) + "/" + yr;
                        startdate.setText( date );

                    }
                } ,year,month,day);

                datePickerDialog.show();


            }
        } );








        dialog.show();


        return;

    }








    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(HomeActivity.this, CustomerDetailActivity.class);
        Gson gson = new Gson();
        String jsonstring = gson.toJson( customers.get( position ) );
        intent.putExtra( "cust",jsonstring );
        startActivity( intent );

    }

    @Override
    public void onLongItemClick(int position) {

    }

    @Override
    public void onEditButtonClick(int position) {
        editCustomer(position);
    }

    @Override
    public void onDeleteButtonClick(int position) {
        String phNo = customers.get( position ).getPhNo();

        deleteCustomer(phNo);

    }


}