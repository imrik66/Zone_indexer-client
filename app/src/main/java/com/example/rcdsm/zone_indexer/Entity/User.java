package com.example.rcdsm.zone_indexer.Entity;

import android.content.Context;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rcdsm on 25/06/2015.
 */
public class User {

    private int userId;
    private String mail;
    private String password;
    private boolean isAdmin;
    private List<Note> notes;

    public static User currentUser = new User();

    public User(){

    }

    public boolean checkAuth (String enteredPassword){
        if(this.password.equals(enteredPassword)){
            return true;
        }
        else{
            return false;
        }
    };
    
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public void create(Context context){

        Map<String,String> user = new HashMap<String,String>();
        user.put("createUser","1");
        user.put("mail", mail);
        user.put("pass", this.password);
        user.put("isAdmin", (this.isAdmin == true)?"1":"0");

        String url = "http://pierre-mar.net/Zone_indexer/";
        AQuery aq = new AQuery(context);

        aq.ajax(url, user, JSONObject.class, new AjaxCallback<JSONObject>() {
        });
    };

    public static void getUserByMail (final String mail, Context context, final UserManagement listener){

        Map<String,String> user = new HashMap<String,String>();
        user.put("getUserByMail","1");
        user.put("mail", mail);

        String url = "http://pierre-mar.net/Zone_indexer/";
        AQuery aq = new AQuery(context);

        aq.ajax(url,user , JSONObject.class, new AjaxCallback<JSONObject>()
        {
            @Override
            public void callback(String url, JSONObject response, AjaxStatus status)
            {
                if(response != null){
                    try {
                        User user = new User();
                        user.userId = response.getInt("id");
                        user.mail = mail;
                        user.password = response.getString("password");
                        user.isAdmin = response.getBoolean("isAdmin");

                        listener.getUserSuccess(user);
                    }
                    catch(Exception e){
                        listener.getUserFailed();
                    }
                }
                else{
                    listener.getUserFailed();
                }

            }
        });
    }

    public interface UserManagement {
        public void getUserFailed();
        public void getUserSuccess(User user);
    }
}
