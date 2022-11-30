package com.dev334.trace.ui.login;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dev334.trace.R;
import com.dev334.trace.database.TinyDB;
import com.dev334.trace.model.User;
import com.dev334.trace.ui.home.HomeActivity;
import com.dev334.trace.util.app.AppConfig;
import com.dev334.trace.util.retrofit.ApiClient;
import com.dev334.trace.util.retrofit.ApiInterface;
import com.dev334.trace.util.retrofit.NoConnectivityException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private LoginHomeFragment loginHome;
    private FragmentManager fragmentManager;
    private SignUpFragment SignupFrag;
    private LoginFragment loginFrag;
    private EmailVerifyFragment verificationFragment;
    private com.dev334.trace.ui.login.CreateProfileFragment CreateProfileFragment;
    private String PhoneNo,Username,Organisation,Facebook, Instagram;
    private ArrayList<String> Organisations,userInterest;
    private TinyDB tinyDB;
    private TextView loginTxt;
    private int FRAGMENT=0; //0>default 1->emailVerification
    private String UserId;
    private final String TAG="LoginActivityLog";
    private AppConfig appConfig;

    private String email, password,  phoneNo, verificationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginHome=new LoginHomeFragment();
        SignupFrag=new SignUpFragment();
        loginFrag=new LoginFragment();
        verificationFragment=new EmailVerifyFragment();
        CreateProfileFragment=new CreateProfileFragment();

        tinyDB=new TinyDB(getApplicationContext());

        fragmentManager=getSupportFragmentManager();

        FRAGMENT=getIntent().getIntExtra("FRAGMENT",0);


        appConfig = new AppConfig(this);

        if(FRAGMENT==0){
            replaceFragment(loginHome);
        }else if(FRAGMENT==1){
            replaceFragment(CreateProfileFragment);
        }else{
            replaceFragment(loginFrag);
        }


    }

    public void openLogin(){
        replaceFragment(loginFrag);
    }

    public void openSignup(){
        replaceFragment(SignupFrag);
    }

    public void openVerifyEmail(){
        replaceFragment(verificationFragment);
    }

    public void openCreateProfile(){
        replaceFragment(CreateProfileFragment);
    }

    public void setUserID(String UserId){
        Log.i(TAG, "setUserID: "+UserId);
        appConfig.setUserID(UserId);
        this.UserId=UserId;
    }

    public void setSignUpCredentials(String email, String password){
        this.email=email;
        this.password=password;
        appConfig.setUserEmail(email);
    }

    public String getUserEmail(){
        return appConfig.getUserEmail();
    }

    public String getSignUpEmail(){
        return email;
    }

    public String getSignUpPassword(){
        return password;
    }

    private void replaceFragment(Fragment fragmentToShow) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        // Hide all of the fragments
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            transaction.hide(fragment);
        }

        if (fragmentToShow.isAdded()) {
            // When fragment was previously added - show it
            transaction.show(fragmentToShow);
        } else {
            // When fragment is adding first time - add it
            transaction.add(R.id.LoginContainer, fragmentToShow);
        }

        transaction.commit();
    }

    public void openHomeActivity() {
        User user = new User();
        Call<User> call = ApiClient.getApiClient(getApplicationContext()).create(ApiInterface.class).getUser(appConfig.getUserID());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){

                    if(response.code()==401){
                        Toast.makeText(getApplicationContext(), "Email not verified", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onResponse: "+response.code());
                    Log.i(TAG, "onResponse: "+response.message());
                    return;
                }

                Log.i(TAG, "onResponse: "+response.message());

                user.setId(appConfig.getUserID());
                user.setUserData(response.body().getName(), response.body().getEmail(),
                        response.body().getDepartment(), response.body().getSemester(),
                        response.body().getReg());

                appConfig.setUserInfo(user);

                appConfig.setProfileCreated(true);
                appConfig.setLoginStatus(true);

                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(i);
                finish();

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.getMessage());
                if(t instanceof NoConnectivityException){
                    showNoInternetDialog();

                    return;
                }

            }
        });
    }

    private void showNoInternetDialog() {
        final Dialog dialog=new Dialog(getApplicationContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_no_internet);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        Button goToHome=dialog.findViewById(R.id.go_to_home4);
        goToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}