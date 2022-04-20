package com.hfad.e_commerce_app.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.models.Shipment;

import java.util.List;

public class ShipmentSpinnerAdapter extends BaseAdapter {
    private List<Shipment> shipmentList;
    private Context context;

    public ShipmentSpinnerAdapter(List<Shipment> shipmentList, Context context) {
        this.shipmentList = shipmentList;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(shipmentList!=null)
            return shipmentList.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(shipmentList!=null)
            return shipmentList.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        if(shipmentList!=null)
            return shipmentList.get(i).getId();
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.item_shipment_spinner,viewGroup,false);
        Shipment shipment = shipmentList.get(i);
        ImageView imageView = view.findViewById(R.id.imageViewShipment);
        TextView tvShipmentName = view.findViewById(R.id.tvShipmentName);
        Glide.with(context).load(shipment.getImage()).into(imageView);
        tvShipmentName.setText(shipment.getServiceProvider());
        return view;
    }
}
