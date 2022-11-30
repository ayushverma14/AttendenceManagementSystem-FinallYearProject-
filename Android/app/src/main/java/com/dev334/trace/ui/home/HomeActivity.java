package com.dev334.trace.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dev334.trace.R;
import com.dev334.trace.model.ApiResponse;
import com.dev334.trace.model.Attendance;
import com.dev334.trace.util.app.AppConfig;
import com.dev334.trace.util.retrofit.ApiClient;
import com.dev334.trace.util.retrofit.ApiInterface;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private Button AddImageBtn, DoneBtn;
    private ImageAdapter imageAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Uri> uris;
    private FirebaseStorage firebaseStorage;
    private Boolean success = false;
    private AlertDialog loading;
    private Integer imageSize = 0;
    private List<String> urls;
    private Attendance attendance;
    private AppConfig appConfig;
    private String TAG = "ImageUploadBackend";
    private String date,department,semester,subject;
    private CalendarView calendarView;
    private Spinner departmentSpinner, semesterSpinner, subjectSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AddImageBtn = findViewById(R.id.button_add_image);
        DoneBtn = findViewById(R.id.button_done);
        calendarView = findViewById(R.id.calendarView);
        departmentSpinner = findViewById(R.id.spinner_department);
        semesterSpinner = findViewById(R.id.spinner_semester);
        subjectSpinner = findViewById(R.id.spinner_subject);

        uris = new ArrayList<>();

        imageAdapter = new ImageAdapter(uris);

        firebaseStorage = FirebaseStorage.getInstance();

        urls = new ArrayList<>();

        appConfig = new AppConfig(getApplicationContext());

        LoadingShow();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(imageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);

        AddImageBtn.setOnClickListener(v->{
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(intent, "Pictures: "), 1);
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                date = i + "-" + i1 + "-" + i2;
                Log.i(TAG, "onSelectedDayChange: "+date);
            }
        });

        DoneBtn.setOnClickListener(v->{
            department = departmentSpinner.getSelectedItem().toString();
            semester = semesterSpinner.getSelectedItem().toString();
            subject = subjectSpinner.getSelectedItem().toString();
            if(!uris.isEmpty()){
                loading.show();
                Log.i("ImageUploadLog", "onCreate: "+ uris.size());
                imageSize = uris.size();
                for(Uri u: uris){
                    Log.i("ImageUploadLog", "onCreate: "+u.getPath());
                    StorageReference storageReference = firebaseStorage.getReference();
                    StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                    ref.putFile(u).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.i("ImageUploadLog", "onCreate: success");
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Log.i(TAG, "onSuccess: "+uri.toString());
                                        urls.add(uri.toString());
                                        imageSize--;
                                        if(imageSize==0){
                                            attendance = new Attendance(date,department,semester
                                                    ,subject,urls,appConfig.getAuthToken());
                                            UpdateBackend();
                                        }
                                    }
                                });
                            }
                            }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                success = false;
                                Log.i("ImageUploadLog", "onCreate: "+e);
                                imageSize--;
                                if(imageSize==0){
                                    loading.dismiss();
                                }
                            }
                            });
                }
                if(success){
                    Toast.makeText(getApplicationContext(), "Successfully Uploaded",
                            Toast.LENGTH_LONG).show();
                }
            }else{
                Log.i("ImageUploadLog", "onCreate: uris is empty");
            }
        });

    }

    private void UpdateBackend() {
        Call<ApiResponse> call = ApiClient.getApiClient(getApplicationContext())
                .create(ApiInterface.class).uploadPictures(attendance);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(!response.isSuccessful()){
                    Log.i(TAG, "onResponse: "+response.code());
                    Log.i(TAG, "onResponse: "+response.toString());
                    Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, "onResponse: "+response.body());
                if(response.body().getStatus()==200){
                    Log.i(TAG, "onResponse: Successful");
                }

                loading.dismiss();

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.getMessage());
                loading.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getClipData() != null) {
                    uris.clear();
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        uris.add(data.getClipData().getItemAt(i).getUri());
                    }
                    imageAdapter.notifyDataSetChanged();
                }
            } else if (data.getData() != null) {
                String imagePath = data.getData().getPath();
            }
        }
    }

    private void LoadingShow(){
        AlertDialog.Builder alert=new AlertDialog.Builder(HomeActivity.this);
        View view=getLayoutInflater().inflate(R.layout.dialog_loading,null);
        alert.setView(view);
        loading=alert.show();
        alert.setCancelable(false);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.dismiss();
    }

}