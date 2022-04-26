package com.hfad.e_commerce_app.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.adapters.PaymentSpinnerAdapter;
import com.hfad.e_commerce_app.adapters.ShipmentSpinnerAdapter;
import com.hfad.e_commerce_app.models.CartItem;
import com.hfad.e_commerce_app.models.Payment;
import com.hfad.e_commerce_app.models.Shipment;
import com.hfad.e_commerce_app.utils.APIUtils;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseOrderInfoActivity extends AppCompatActivity {
    private EditText edPhone,edAddress;
    private Spinner spinnerShipments, spinnerPayments;
    private Button btnNext;

    private List<CartItem> cartItemList;
    private List<Shipment> shipmentList;
    private List<Payment> paymentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_order_info);
        initView();
        callAPIGetAllShipments();
        callAPIGetAllPayments();

        Intent intent = getIntent();
        cartItemList = (List<CartItem>) intent.getSerializableExtra("listCartItem");

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = edPhone.getText().toString();
                String address = edAddress.getText().toString();
                Shipment shipment = (Shipment) spinnerShipments.getSelectedItem();
                Payment payment = (Payment) spinnerPayments.getSelectedItem();

                //validate
                if(phone.length()!=10){
                    Toast.makeText(ChooseOrderInfoActivity.this, "Please type in a valid phone number!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent1 = new Intent(ChooseOrderInfoActivity.this, ReviewAndPlaceOrderActivity.class);
                    intent1.putExtra("address", address);
                    intent1.putExtra("shipment",shipment);
                    intent1.putExtra("payment", payment);
                    intent1.putExtra("listCartItem", (Serializable) cartItemList);
                    startActivity(intent1);
                }
            }
        });
    }

    private void initView() {
        edPhone = findViewById(R.id.edPhone);
        edAddress = findViewById(R.id.edAddress);
        spinnerShipments = findViewById(R.id.spinnerShipment);
        spinnerPayments = findViewById(R.id.spinnerPayment);
        btnNext = findViewById(R.id.btnNext);
    }

    private void callAPIGetAllShipments(){
        APIUtils.getApiServiceInterface().getAllShipmentMethods().enqueue(new Callback<List<Shipment>>() {
            @Override
            public void onResponse(Call<List<Shipment>> call, Response<List<Shipment>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    shipmentList = response.body();
                    ShipmentSpinnerAdapter shipmentSpinnerAdapter = new ShipmentSpinnerAdapter(shipmentList,ChooseOrderInfoActivity.this);
                    spinnerShipments.setAdapter(shipmentSpinnerAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Shipment>> call, Throwable t) {

            }
        });
    }

    private void callAPIGetAllPayments(){
        APIUtils.getApiServiceInterface().getAllPaymentMethods().enqueue(new Callback<List<Payment>>() {
            @Override
            public void onResponse(Call<List<Payment>> call, Response<List<Payment>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    paymentList = response.body();
                    PaymentSpinnerAdapter PaymentSpinnerAdapter = new PaymentSpinnerAdapter(paymentList,ChooseOrderInfoActivity.this);
                    spinnerPayments.setAdapter(PaymentSpinnerAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Payment>> call, Throwable t) {

            }
        });
    }


}