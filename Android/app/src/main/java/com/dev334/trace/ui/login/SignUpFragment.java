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
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.dev334.trace.R;
import com.dev334.trace.model.ApiResponse;
import com.dev334.trace.model.User;
import com.dev334.trace.util.retrofit.ApiClient;
import com.dev334.trace.util.retrofit.ApiInterface;
import com.dev334.trace.util.retrofit.NoConnectivityException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends Fragment {

    private View view;
    private Button SignUp;
    private TextView EditEmail, EditPassword, Login,EditName;
    private String Email,Password,Name;
    private int RC_SIGN_IN=101;
    private ProgressBar loading;
    private ConstraintLayout parentLayout;
    private ArrayList<String> interest;
    private String TAG="signUpFragment";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_signup, container, false);


        EditEmail= view.findViewById(R.id.editEmailSignup);
        EditPassword=view.findViewById(R.id.editPasswordSignUp);
        loading=view.findViewById(R.id.SignUpLoading);
        Login=view.findViewById(R.id.LoginTextSignup);
        EditName=view.findViewById(R.id.editUserName);

        Login.setOnClickListener(v->{
            ((LoginActivity)getActivity()).openLogin();
        });

        interest=new ArrayList<>();

        loading.setVisibility(View.INVISIBLE);
        SignUp=view.findViewById(R.id.SignUpButton);

        parentLayout=view.findViewById(R.id.signUpFragmentLayout);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                Email=EditEmail.getText().toString();
                Name=EditName.getText().toString();
                Password=EditPassword.getText().toString();

                if(check(Name,Email,Password)){
                   registerUser();
                }
            }
        });

        return view;
    }

    private void registerUser() {
        User user = new User(Name,Email,Password);
        Call<ApiResponse> call = ApiClient.getApiClient(getContext()).create(ApiInterface.class).registerUser(user);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(!response.isSuccessful()){
                    Log.i(TAG, "onResponse: "+response.code());
                    Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, "onResponse: "+response.body());
                if(response.body().getStatus()==200){
                    ((LoginActivity)getActivity()).setSignUpCredentials(Email, Password);
                    ((LoginActivity)getActivity()).openVerifyEmail();
                }
                loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.getMessage());
                if(t instanceof NoConnectivityException){
                    showNoInternetDialog();

                    return;
                }
                loading.setVisibility(View.INVISIBLE);
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

    private boolean check(String name,String email, String password) {
        if(name.isEmpty()){
            EditName.setError("Name is empty");
            return false;
        }
        if(email.isEmpty()){
            EditEmail.setError("Email is empty");
            return false;
        }else if(password.isEmpty()){
            EditPassword.setError("Password is empty");
            return  false;
        }else {
            if (password.length() < 6) {
                EditPassword.setError("Password is too short");
                return false;
            } else {
                return true;
            }
        }
    }

}