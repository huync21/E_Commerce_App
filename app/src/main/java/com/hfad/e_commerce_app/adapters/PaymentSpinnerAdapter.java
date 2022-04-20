package com.hfad.e_commerce_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.models.Payment;

import java.util.List;

public class PaymentSpinnerAdapter extends BaseAdapter {
    private List<Payment> paymentList;
    private Context context;

    public PaymentSpinnerAdapter(List<Payment> PaymentList, Context context) {
        this.paymentList = PaymentList;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(paymentList !=null)
            return paymentList.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(paymentList !=null)
            return paymentList.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        if(paymentList !=null)
            return paymentList.get(i).getId();
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.item_payment_spinner,viewGroup,false);
        Payment Payment = paymentList.get(i);
        ImageView imageView = view.findViewById(R.id.imageViewPayment);
        TextView tvPaymentName = view.findViewById(R.id.tvPaymentName);
        Glide.with(context).load(Payment.getImage()).into(imageView);
        tvPaymentName.setText(Payment.getPaymentMethod());
        return view;
    }
}
