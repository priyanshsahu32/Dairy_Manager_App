package com.pcsahu.dairy;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.pcsahu.dairy.Adapters.CustomerDetailsPagerAdapter;
import com.pcsahu.dairy.models.CustomerModel;
import com.pcsahu.dairy.models.Extras;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomerDetailActivity extends AppCompatActivity {

    private TextView tvCustomerName, tvCustomerPhone, tvCustomerQnt, tvCustomerRate, tvCustomerStartDate;
    private CustomerModel cm;
    private String customerJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_details);

        // Initialize TextViews
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerPhone = findViewById(R.id.tvCustomerPhone);
        tvCustomerQnt = findViewById(R.id.tvCustomerQnt);
        tvCustomerRate = findViewById(R.id.tvCustomerRate);
        tvCustomerStartDate = findViewById(R.id.tvCustomerStartDate);

        // Deserialize the CustomerModel object from the passed JSON string
        Gson gson = new Gson();
        customerJson = getIntent().getStringExtra("cust");
        cm = gson.fromJson(customerJson, CustomerModel.class);

        // Set the customer details to the TextViews
        setCustomerDetails();

        // Set up TabLayout and ViewPager2
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPagerCustomerDetails);

        ArrayList<Date> noSupplyDates = cm.getNoSupply();
        ArrayList<Extras> extras = cm.getExtra();

        // Convert the dates to strings for display
        ArrayList<String> noSupplyDatesStr = new ArrayList<>();
        for (Date date : noSupplyDates) {
            noSupplyDatesStr.add(new SimpleDateFormat("dd MMMM yyyy").format(date));
        }

        ArrayList<String> extrasStr = new ArrayList<>();
        for (Extras extra : extras) {
            String formattedDate = new SimpleDateFormat("dd MMMM yyyy").format(extra.getExtraDate());
            extrasStr.add("Qnt: " + extra.getExtraQnt() + ", Date: " + formattedDate);
        }

        // Set up the ViewPager adapter with no supply and extras data
        CustomerDetailsPagerAdapter adapter = new CustomerDetailsPagerAdapter(this, noSupplyDatesStr, extrasStr);
        viewPager.setAdapter(adapter);

        // Attach TabLayout to ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("No Supply Days");
            } else {
                tab.setText("Extras");
            }
        }).attach();
    }

    private void setCustomerDetails() {
        // Set Customer Name
        tvCustomerName.setText("Name: " + cm.getName());

        // Set Customer Phone
        tvCustomerPhone.setText("Phone: " + cm.getPhNo());

        // Set Customer Quantity
        tvCustomerQnt.setText("Quantity: " + cm.getQnt());

        // Set Customer Rate
        tvCustomerRate.setText("Rate: ₹" + cm.getRate() + "/L");

        // Format and set Start Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        String formattedDate = dateFormat.format(cm.getStartDate());
        tvCustomerStartDate.setText("Start Date: " + formattedDate);
    }
}
