package com.hfad.e_commerce_app.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.adapters.CartAdapter;
import com.hfad.e_commerce_app.models.CartItem;
import com.hfad.e_commerce_app.token_management.TokenManager;
import com.hfad.e_commerce_app.utils.APIUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private Button btnMakeOrder;


    private List<CartItem> mListCartItems = new ArrayList<>();

    private TokenManager tokenManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // init view
        recyclerView = view.findViewById(R.id.recycler_view_cart_items);
        btnMakeOrder = view.findViewById(R.id.btnMakeOrder);
        tokenManager = new TokenManager(getActivity());

        cartAdapter = new CartAdapter(mListCartItems, getActivity());
        cartAdapter.setOnItemClickListener(new CartAdapter.ItemClickListener() {
            @Override
            public void onDeleteCartItem(int cartItemId) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Notify")
                        .setMessage("Are you sure to delete this cart item.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int index) {
                                CartFragment.this.callAPIDeleteCartItem(cartItemId);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setIcon(R.drawable.ic_baseline_remove_shopping_cart_red)
                        .create();
                alertDialog.show();

            }

            @Override
            public void onCartItemQuantityChanged(int productId, int quantity, int cartId) {
                callAPIUpdateCartItem(cartId, quantity);
            }
        });
        recyclerView.setAdapter(cartAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));

        callAPIGetAllCartItemOfUser();

        btnMakeOrder.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), ChooseOrderInfoActivity.class);
            intent.putExtra("listCartItem", (Serializable) mListCartItems);
            startActivity(intent);
        });



    }

    private void callAPIGetAllCartItemOfUser(){
        APIUtils.getApiServiceInterface().getAllCartItems("Bearer "+tokenManager.getAccessToken())
                .enqueue(new Callback<List<CartItem>>() {
                    @Override
                    public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            mListCartItems = response.body();
                            cartAdapter.setmListCartItems(mListCartItems);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CartItem>> call, Throwable t) {

                    }
                });
    }

    private void callAPIDeleteCartItem(int cartItemId){
        APIUtils.getApiServiceInterface().deleteCartItem("Bearer "+tokenManager.getAccessToken(),cartItemId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            try {
                                cartAdapter.deleteCartItem(cartItemId);
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                String message = jsonObject.getString("message");
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }

    private void callAPIUpdateCartItem( int cartId, int quantity){
        APIUtils.getApiServiceInterface().updateCartItem("Bearer "+tokenManager.getAccessToken(),quantity,cartId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            try {
                                String jsonStringResponse = response.body().string();
                                JSONObject jsonObject = new JSONObject(jsonStringResponse);
                                String message = jsonObject.getString("message");
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                cartAdapter.updateCartItem(cartId,quantity);

                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }
}
