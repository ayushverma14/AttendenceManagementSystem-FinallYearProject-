package com.dev334.trace.ui.login;

import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.dev334.trace.R;
import com.dev334.trace.model.ApiResponse;
import com.dev334.trace.model.User;
import com.dev334.trace.util.retrofit.ApiClient;
import com.dev334.trace.util.retrofit.ApiInterface;
import com.dev334.trace.util.retrofit.NoConnectivityException;

import java.util.Calendar;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateProfileFragment extends Fragment {

    private View view;
    private String selectedDepartment,selectedSemester;
    private Spinner departmentSpinner,semesterSpinner;
    private ArrayAdapter<CharSequence> departmentAdapter,semesterAdapter;
    private EditText phone;
    private String phoneString;
    private Button nextButton;
    private String TAG="CreateProfile";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_profile, container, false);

        departmentSpinner=view.findViewById(R.id.spinner_indian_states);

        departmentAdapter=ArrayAdapter.createFromResource(getContext(),R.array.array_indian_states,
                R.layout.spinner_layout);
         departmentAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
         departmentSpinner.setAdapter(departmentAdapter);

        semesterSpinner=view.findViewById(R.id.spinner_indian_district);

        semesterAdapter = ArrayAdapter.createFromResource(getContext(), R.array.array_blood_group,
                R.layout.spinner_layout);
        semesterSpinner.setAdapter(semesterAdapter);

         nextButton = view.findViewById(R.id.btnCreate);
         phone=view.findViewById(R.id.EditContactRecord);

         departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View v, int i, long l) {

                 semesterSpinner=view.findViewById(R.id.spinner_indian_district);

                 selectedDepartment=departmentSpinner.getSelectedItem().toString();

                 int parentID=parent.getId();
                 if(parentID==R.id.spinner_indian_states)
                 {
                     switch (selectedDepartment){
                         case "Select Your Department": semesterAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_default_districts, R.layout.spinner_layout);
                             break;
                         case "CSE": semesterAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_andhra_pradesh_districts, R.layout.spinner_layout);
                             break;
                         case "IT": semesterAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_arunachal_pradesh_districts, R.layout.spinner_layout);
                             break;
                         case "ECE": semesterAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_assam_districts, R.layout.spinner_layout);
                             break;
                         case "EE": semesterAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                 R.array.array_bihar_districts, R.layout.spinner_layout);
                             break;
                         default: break;
                     }
                     semesterAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                     semesterSpinner.setAdapter(semesterAdapter);
                     semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                         @Override
                         public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                             selectedSemester=semesterSpinner.getSelectedItem().toString();
                         }

                         @Override
                         public void onNothingSelected(AdapterView<?> adapterView) {

                         }
                     });

                 }

             }

             @Override
             public void onNothingSelected(AdapterView<?> adapterView) {

             }
         });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneString=phone.getText().toString();
                if(check()){
                    createUser();
                }
            }


        });


        return view;
    }

    private void createUser() {
        User user = new User(); //create user((LoginActivity)getActivity()).getUserEmail(),
        Call<ApiResponse> call = ApiClient.getApiClient(getContext()).create(ApiInterface.class).createUser(user);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(!response.isSuccessful()){
                    Log.i(TAG, "onResponse: "+response.code());
                    Log.i(TAG, "onResponse: "+response.toString());
                    Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, "onResponse: "+response.body());
                if(response.body().getStatus()==200){
                    Log.i(TAG, "onResponse: Successful");
                    ((LoginActivity)getActivity()).openHomeActivity();
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.getMessage());
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



    private boolean check() {
        if(selectedSemester.equals("Select Your Department")){
            Toast.makeText(getContext(),"Enter your department",Toast.LENGTH_LONG).show();
            return false;
        }
        if(selectedSemester.equals("Select Your Semester")){
            Toast.makeText(getContext(),"Enter your semester",Toast.LENGTH_LONG).show();
            return false;
        }

        if(phoneString.isEmpty()){
            phone.setError("Enter valid contact no.");
            return false;
        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    
}