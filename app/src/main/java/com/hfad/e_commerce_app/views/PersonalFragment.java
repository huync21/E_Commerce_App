package com.hfad.e_commerce_app.views;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.hfad.e_commerce_app.R;
import com.hfad.e_commerce_app.models.User;
import com.hfad.e_commerce_app.token_management.TokenManager;
import com.hfad.e_commerce_app.utils.APIUtils;
import com.hfad.e_commerce_app.utils.RealPathUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalFragment extends Fragment {
    private static final int MY_REQUEST_CODE = 10;
    private CircleImageView circleImageView;
    private TextView tvUpdateAvatar;
    private EditText edEmail, edFirstName, edLastName, edPhone, edUsername;
    private Button btnUpdateInfo;
    private Button btnLogOut;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TokenManager tokenManager;

    private Uri selectedImageUri;
    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data==null){
                            return;
                        }
                        selectedImageUri = data.getData();

                        try {
                            Bitmap bitmap = MediaStore.Images.Media
                                    .getBitmap(getActivity().getContentResolver(), selectedImageUri);
                            circleImageView.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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

        callAPIGetUserInfo();

        tvUpdateAvatar.setOnClickListener(view1 -> {
            onClickRequestOpenGalleryPermission();
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Processing ...");

        btnUpdateInfo.setOnClickListener(view1 -> {
            progressDialog.show();
            callAPIChangeUserInfo(edUsername.getText().toString(),
                    edFirstName.getText().toString(),
                    edLastName.getText().toString(),
                    edPhone.getText().toString(),
                    selectedImageUri);
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callAPIGetUserInfo();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initView(View view) {
        btnLogOut = view.findViewById(R.id.btn_logout);
        circleImageView = view.findViewById(R.id.circle_image_view_avatar_personal);
        tvUpdateAvatar = view.findViewById(R.id.tv_change_avatar);
        edEmail = view.findViewById(R.id.edit_text_email);
        edFirstName = view.findViewById(R.id.edit_text_first_name);
        edLastName = view.findViewById(R.id.edit_text_last_name);
        edPhone = view.findViewById(R.id.edit_text_phone);
        edUsername = view.findViewById(R.id.edUserName);
        btnUpdateInfo = view.findViewById(R.id.btn_update_user_info);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);
    }

    private void onClickRequestOpenGalleryPermission() {
        // Neu sdk version nho hon 23 thi mo gallery luon ko can xin quyen nguoi dung
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            openGallery();
            return;
        }

        // Neu da co quyen san roi thi mo gallery
        if(getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            openGallery();
        }else{ // Neu khong thi xin quyen
            String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            getActivity().requestPermissions(permission, MY_REQUEST_CODE);
        }
    }

    // Lang nghe nguoi dung xin quyen xong,
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_REQUEST_CODE)
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openGallery();
            }
    }

    private void openGallery() {
        Intent openGalleryIntent = new Intent();
        openGalleryIntent.setType("image/*");
        openGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(Intent
                .createChooser(openGalleryIntent, "Select Avatar Picture"));
    }




    private void callAPIGetUserInfo(){
        APIUtils.getApiServiceInterface().getUserInfo("Bearer "+tokenManager.getAccessToken())
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            User user = response.body();
                            if(user.getImage() !=null){
                                Glide.with(getActivity()).load(user.getImage()).into(circleImageView);
                            }
                            edEmail.setText(user.getEmail());
                            edFirstName.setText(user.getFirstName());
                            edLastName.setText(user.getLastName());
                            if(user.getPhoneNumber() !=null){
                                edPhone.setText(user.getPhoneNumber());
                            }else{
                                edPhone.setText("");
                            }
                            edUsername.setText(user.getUserName());

                            tokenManager.saveUserInfo(user.getEmail(),user.getFirstName()
                            ,user.getLastName(),user.getPhoneNumber(),user.getImage());
                        }

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
    }

    private void callAPIChangeUserInfo(String username, String firstName, String lastName,
                                       String phone, Uri image){
        RequestBody requestBodyUserName = RequestBody.create(MediaType.parse("multipart/form-data"), username.trim());
        RequestBody requestBodyFirstName = RequestBody.create(MediaType.parse("multipart/form-data"), firstName.trim());
        RequestBody requestBodyLastName = RequestBody.create(MediaType.parse("multipart/form-data"), lastName.trim());
        RequestBody requestBodyPhone = RequestBody.create(MediaType.parse("multipart/form-data"), phone.trim());

        MultipartBody.Part multipartBodyImageAvatar = null;
        if(image !=null){
            String strRealPath = RealPathUtil.getRealPath(getActivity(), image);
            Log.e("Real path of image", strRealPath);
            File imageFile = new File(strRealPath);
            RequestBody requestBodyImageAvatar = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            multipartBodyImageAvatar = MultipartBody.Part.createFormData("image",imageFile.getName(), requestBodyImageAvatar);
        }

        APIUtils.getApiServiceInterface()
                .changeUserInfo("Bearer "+tokenManager.getAccessToken(), requestBodyUserName
                        ,requestBodyFirstName, requestBodyLastName,requestBodyPhone, multipartBodyImageAvatar)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        ResponseBody responseBody= response.body();
                        progressDialog.cancel();
                        if(response.isSuccessful() && responseBody!=null){
                            try {
                                JSONObject jsonObject = new JSONObject(responseBody.string());
                                String message = jsonObject.getString("message");
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Success Notification")
                                        .setIcon(R.drawable.ic_baseline_check_24)
                                        .setMessage(message)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        })
                                        .create().show();
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if(!response.isSuccessful() && response.errorBody()!=null)
                        {
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                String message = jsonObject.getString("message");
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Error")
                                        .setIcon(R.drawable.ic_baseline_error_24)
                                        .setMessage(message)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        })
                                        .create().show();
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
