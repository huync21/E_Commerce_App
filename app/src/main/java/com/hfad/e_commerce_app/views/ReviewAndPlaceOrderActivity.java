package com.hfad.e_commerce_app.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.braintreepayments.api.DropInClient;
//import com.braintreepayments.api.DropInRequest;
//import com.braintreepayments.api.DropInResult;
import com.bumptech.glide.Glide;
import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.adapters.ReviewOrderAdapter;
import com.hfad.e_commerce_app.models.CartItem;
import com.hfad.e_commerce_app.models.Payment;
import com.hfad.e_commerce_app.models.Shipment;
import com.hfad.e_commerce_app.token_management.TokenManager;
import com.hfad.e_commerce_app.utils.APIUtils;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewAndPlaceOrderActivity extends AppCompatActivity {

    private TextView tvShipTo, tvSubtotal, tvShipping, tvTax, tvOrderTotal;
    private ImageView imageViewShipment, imageViewPayment;
    private RecyclerView recyclerView;
    private Button btnPlaceOrder;
    private ProgressDialog progressDialog;
    private ReviewOrderAdapter reviewOrderAdapter;


    private List<CartItem> cartItemList;
    private ArrayList<String> listCartItemId;
    private String phone;
    private String address;
    private Shipment shipment;
    private Payment payment;
    private int subtotal;
    private int orderTotal;

    private String SCRET_KEY = "sk_test_51KuTcyHHLREvwghvnhCKONbsTRSbKJdY9JNPDmuFYXLddfDO7mJWiO38EdRld12qFXevxQxeQM59AHAGHIEcdmah005iVdteaT";
    private String PUBLISH_KEY = "pk_test_51KuTcyHHLREvwghvmro8cCVaTPdOllDwXB8yjOWq7ASnNBH3OlojYye2ANDTNL3Fh8olSCHfqWyZZUkb08CMFUMz00MB0XZW19";
    PaymentSheet paymentSheet;

    private String customerId;
    private String ephericalKey;
    private String clientSecret;

    private TokenManager tokenManager;

    public static final String PAYMENT_SUCCESS = "PAYMENT SUCCESS";
    public static final int PAYMENT_SUCCESS_CODE = 99;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_and_place_order);
        initView();
        tokenManager = new TokenManager(this);

        // Intent toi tu choose order info activity
        Intent intent = getIntent();

        phone = intent.getStringExtra("phone");
        address = intent.getStringExtra("address");
        shipment = (Shipment) intent.getSerializableExtra("shipment");
        payment = (Payment) intent.getSerializableExtra("payment");
        cartItemList = (List<CartItem>) intent.getSerializableExtra("listCartItem");
        listCartItemId = new ArrayList<>();
        for(CartItem cartItem:cartItemList){
            listCartItemId.add(cartItem.getId()+"");
        }

        tvShipTo.setText(address);
        subtotal = 0;
        for (CartItem cartItem : cartItemList) {
            subtotal += cartItem.getProduct().getPrice();
        }

        tvSubtotal.setText("$" + subtotal + "");
        tvShipping.setText("$" + shipment.getPrice());
        orderTotal = subtotal + shipment.getPrice();
        tvOrderTotal.setText("$" + orderTotal + "");
        Glide.with(this).load(payment.getImage()).into(imageViewPayment);
        Glide.with(this).load(shipment.getImage()).into(imageViewShipment);
        reviewOrderAdapter = new ReviewOrderAdapter(cartItemList);
        recyclerView.setAdapter(reviewOrderAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        PaymentConfiguration.init(this, PUBLISH_KEY);
        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {
            onPaymentResult(paymentSheetResult);
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing ...");
        progressDialog.show();
        APIUtils.getOutsideAPIServiceInterface().createStripeCustomer("Bearer " + SCRET_KEY)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                customerId = jsonObject.getString("id");
                                Toast.makeText(ReviewAndPlaceOrderActivity.this, "customer id: " + customerId, Toast.LENGTH_SHORT).show();
                                getEphericalKey(customerId);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

        btnPlaceOrder.setOnClickListener(view -> {
            paymentFlow();
        });


    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(this, "Payment Success!", Toast.LENGTH_SHORT).show();
            callAPISaveTransactionInfo(phone,address,listCartItemId,payment.getId(),shipment.getId(),orderTotal);
        }
    }

    private void getEphericalKey(String customerId) {
        APIUtils.getOutsideAPIServiceInterface().getEphemeralKey("Bearer " + SCRET_KEY, "2020-08-27", customerId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                ReviewAndPlaceOrderActivity.this.ephericalKey = jsonObject.getString("id");
                                Toast.makeText(ReviewAndPlaceOrderActivity.this, "epherical key: " + ephericalKey, Toast.LENGTH_SHORT).show();
                                getClientSecretKey(customerId);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }

                });
    }

    private void getClientSecretKey(String customerId) {
        APIUtils.getOutsideAPIServiceInterface().getClientSecret("Bearer " + SCRET_KEY, customerId, orderTotal + "00"
                , "usd", "true").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        clientSecret = jsonObject.getString("client_secret");
                        Toast.makeText(ReviewAndPlaceOrderActivity.this, "client_secret: " + clientSecret, Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void paymentFlow() {
        paymentSheet.presentWithPaymentIntent(
                clientSecret, new PaymentSheet.Configuration("Huy E-commerce",
                        new PaymentSheet.CustomerConfiguration(
                                customerId,
                                ephericalKey
                        ))
        );
    }

    private void callAPISaveTransactionInfo(String phone, String address, ArrayList<String> listCartItemId,
                                            int paymentId, int shipmentId, int orderTotal){
        JSONArray jsonArray = new JSONArray(listCartItemId);
        APIUtils.getApiServiceInterface().saveTransactionInfo("Bearer "+tokenManager.getAccessToken(),
                phone,address,jsonArray,paymentId,shipmentId,orderTotal)
        .enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body()!=null){
                    Intent showAllOrderIntent = new Intent(ReviewAndPlaceOrderActivity.this, MainActivity.class);
                    showAllOrderIntent.putExtra(PAYMENT_SUCCESS, PAYMENT_SUCCESS_CODE);
                    startActivity(showAllOrderIntent);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerViewCartItems);
        tvShipTo = findViewById(R.id.tvShipto);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvShipping = findViewById(R.id.tvShipping);
        tvTax = findViewById(R.id.tvTax);
        tvOrderTotal = findViewById(R.id.tvOrderTotal);
        imageViewPayment = findViewById(R.id.imageViewPayWith);
        imageViewShipment = findViewById(R.id.imageViewShipment2);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
    }
}