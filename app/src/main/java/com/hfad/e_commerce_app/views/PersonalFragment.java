package com.hfad.e_commerce_app.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.token_management.TokenManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalFragment extends Fragment {
    private CircleImageView circleImageView;
    private TextView tvUpdateAvatar;
    private EditText edEmail, edFirstName, edLastName, edPhone;
    private Button btnUpdateInfo;
    private Button btnLogOut;
    private TokenManager tokenManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        tokenManager = new TokenManager(getActivity());
        btnLogOut.setOnClickListener(view1 -> {
            tokenManager.removeTokenAndUserInfo();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        });

        edEmail.setText(tokenManager.getUserEmail());
        edFirstName.setText(tokenManager.getUserFirstName());
        edLastName.setText(tokenManager.getUserLastName());
        if(tokenManager.getUserPhone() !=null){
            edPhone.setText(tokenManager.getUserPhone());
        }else{
            edPhone.setText("");
        }
        if(tokenManager.getImageUrl() !=null){
            Glide.with(getActivity()).load(tokenManager.getImageUrl()).into(circleImageView);
        }

    }

    private void initView(View view) {
        btnLogOut = view.findViewById(R.id.btn_logout);
        circleImageView = view.findViewById(R.id.circle_image_view_avatar_personal);
        tvUpdateAvatar = view.findViewById(R.id.tv_change_avatar);
        edEmail = view.findViewById(R.id.edit_text_email);
        edFirstName = view.findViewById(R.id.edit_text_first_name);
        edLastName = view.findViewById(R.id.edit_text_last_name);
        edPhone = view.findViewById(R.id.edit_text_phone);
        btnUpdateInfo = view.findViewById(R.id.btn_update_user_info);
    }


}
