package com.hfad.e_commerce_app.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.adapters.ReviewOrderAdapter;
import com.hfad.e_commerce_app.models.CartItem;
import com.hfad.e_commerce_app.models.Payment;
import com.hfad.e_commerce_app.models.Shipment;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;

public class ReviewAndPlaceOrderActivity extends AppCompatActivity {
    private TextView tvShipTo, tvSubtotal, tvShipping, tvTax, tvOrderTotal;
    private ImageView imageViewShipment, imageViewPayment;
    private RecyclerView recyclerView;
    private ReviewOrderAdapter reviewOrderAdapter;
    private Button btnPlaceOrder;

    private List<CartItem> cartItemList;
    private String address;
    private Shipment shipment;
    private Payment payment;
    private int subtotal;
    private int orderTotal;

    // PAYPAL
    public static final String clientKey = "AYYSyJA8O_gsNpwUI6DkJcfIxHt-PCbZCwDAkBzau4qwoaM1u19lHK0mqzn8uoh-wOHkFvk7SRmRtpf3";
    public static final int PAYPAL_REQUEST_CODE = 123;

    // Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready,
            // switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            // on below line we are passing a client id.
            .clientId(clientKey);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_and_place_order);
        initView();

        // Intent toi tu choose order info activity
        Intent intent = getIntent();
        address = intent.getStringExtra("address");
        shipment = (Shipment) intent.getSerializableExtra("shipment");
        payment = (Payment) intent.getSerializableExtra("payment");
        cartItemList = (List<CartItem>) intent.getSerializableExtra("listCartItem");

        tvShipTo.setText(address);
        subtotal = 0;
        for(CartItem cartItem: cartItemList){
            subtotal+=cartItem.getProduct().getPrice();
        }
        tvSubtotal.setText("$"+subtotal+"");
        tvShipping.setText("$"+shipment.getPrice());
        orderTotal = subtotal+shipment.getPrice();
        tvOrderTotal.setText("$"+orderTotal+"");

        Glide.with(this).load(payment.getImage()).into(imageViewPayment);
        Glide.with(this).load(shipment.getImage()).into(imageViewShipment);

        reviewOrderAdapter = new ReviewOrderAdapter(cartItemList);
        recyclerView.setAdapter(reviewOrderAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));


        btnPlaceOrder.setOnClickListener(view -> {
            getPayment();
        });
    }

    private void getPayment() {

        // Creating a paypal payment on below line.
        PayPalPayment payment = new PayPalPayment(new BigDecimal(orderTotal), "USD", "Product Order",
                PayPalPayment.PAYMENT_INTENT_SALE);

        // Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        // Putting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        // Starting the intent activity for result
        // the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            // If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {

                // Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                // if confirmation is not null
                if (confirm != null) {
                    try {
                        // Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        // on below line we are extracting json response and displaying it in a text view.
                        JSONObject payObj = new JSONObject(paymentDetails);
                        String payID = payObj.getJSONObject("response").getString("id");
                        String state = payObj.getJSONObject("response").getString("state");
                        Toast.makeText(ReviewAndPlaceOrderActivity.this,"Payment " + state + "\n with payment id is " + payID,Toast.LENGTH_LONG);
                    } catch (JSONException e) {
                        // handling json exception on below line
                        Log.e("Error", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // on below line we are checking the payment status.
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                // on below line when the invalid paypal config is submitted.
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerViewCartItems);
        tvShipTo= findViewById(R.id.tvShipto);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvShipping = findViewById(R.id.tvShipping);
        tvTax = findViewById(R.id.tvTax);
        tvOrderTotal = findViewById(R.id.tvOrderTotal);
        imageViewPayment = findViewById(R.id.imageViewPayWith);
        imageViewShipment = findViewById(R.id.imageViewShipment2);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
    }
}