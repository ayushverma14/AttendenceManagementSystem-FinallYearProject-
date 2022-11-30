package com.dev334.trace.util.app;

import android.content.Context;
import android.util.Log;

import com.dev334.trace.database.TinyDB;
import com.dev334.trace.model.User;

public class AppConfig {
    private Context context;
    private TinyDB tinyDB;
    private final String TAG="AppConfigLog";
    private User user;

    public AppConfig(Context context){
        this.context=context;
        tinyDB=new TinyDB(context);
        user = tinyDB.getObject("User", User.class);
    }

    public boolean isUserLogin(){
        return tinyDB.getBoolean("Login");
    }

    public void setLoginStatus(Boolean status){
        tinyDB.putBoolean("Login", status);
    }

    public String getAuthToken(){
        return tinyDB.getString("AuthToken");
    }

    public void setAuthToken(String token){
        Log.i(TAG, "setAuthToken: "+token);
        tinyDB.putString("AuthToken", token);
    }

    public User getUserInfo(){
        return user;
    }

    public void setUserInfo(User user){
        this.user = user;
        tinyDB.putObject("User", user);
        Log.i(TAG, "setUserInfo: "+user.getName());
    }

    public void setProfileCreated(Boolean status){
        tinyDB.putBoolean("Profile", status);
    }

    public boolean isProfileCreated(){
        return tinyDB.getBoolean("Profile");
    }

    public void setUserEmail(String email) {
        tinyDB.putString("Email", email);
        return;
    }

    public String getUserEmail(){
        return tinyDB.getString("Email");
    }

    public String getUserID() {
        return tinyDB.getString("UserId");
    }

    public void setUserID(String UserId){
        tinyDB.putString("UserId", UserId);
    }

    public String getUsername() {
        return user.getName();
    }
}
