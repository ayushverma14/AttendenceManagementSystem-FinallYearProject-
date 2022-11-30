package com.dev334.trace.ui.login;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.dev334.trace.R;
import com.dev334.trace.model.User;
import com.dev334.trace.util.app.AppConfig;
import com.dev334.trace.util.retrofit.ApiClient;
import com.dev334.trace.util.retrofit.ApiInterface;
import com.dev334.trace.util.retrofit.NoConnectivityException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailVerifyFragment extends Fragment {

    private View view;
    private Button Done;
    private ConstraintLayout parentLayout;
    private static String TAG="EmailVerifyLOG";
    private ProgressBar loading;
    private AppConfig appConfig;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_email_verification, container, false);

        Done=view.findViewById(R.id.verifcationDone);
        parentLayout=view.findViewById(R.id.verifyEmailLayout);
        loading=view.findViewById(R.id.VerificationLoading);

        loading.setVisibility(View.INVISIBLE);
        appConfig=new AppConfig(getContext());

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private void SignInUser(String token) {

        String Email=((LoginActivity)getActivity()).getSignUpEmail();
        String Password=((LoginActivity)getActivity()).getSignUpPassword();

        User user = new User(Email,Password, token, true);
        Call<User> call = ApiClient.getApiClient(getContext()).create(ApiInterface.class).loginUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    if(response.code()==401){
                        Toast.makeText(getContext(), "Email not verified", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.INVISIBLE);
                        return;
                    }
                    Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onResponse: "+response.code());
                    loading.setVisibility(View.INVISIBLE);
                    return;
                }

                Log.i(TAG, "onResponse: "+response.message());
                Log.i(TAG, "onResponse: "+response.headers().get("auth_token"));

                ((LoginActivity)getActivity()).setUserID(response.body().getId());
                appConfig.setLoginStatus(true);
                appConfig.setAuthToken(response.headers().get("auth_token"));
                ((LoginActivity)getActivity()).openCreateProfile();
                loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.getMessage());

                loading.setVisibility(View.INVISIBLE);
                if(t instanceof NoConnectivityException){
                    showNoInternetDialog();

                    return;
                }
            }
        });

    }

    private void showNoInternetDialog() {
        final Dialog dialog=new Dialog(getContext());
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