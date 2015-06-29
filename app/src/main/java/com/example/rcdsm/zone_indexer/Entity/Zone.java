package com.example.rcdsm.zone_indexer.Entity;

import android.content.Context;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rcdsm on 29/06/2015.
 */
public class Zone {

    private int zoneId;
    private String title;
    private String color;
    private String perimeter;
    private List<Coordinates> pointsArray;
    private int used;

    public static Zone currentZone = new Zone();

    public Zone(){
        this.pointsArray = new ArrayList<Coordinates>();
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPerimeter() {
        return perimeter;
    }

    public void setPerimeter(String perimeter) {
        this.perimeter = perimeter;
    }

    public List<Coordinates> getPointsArray() {
        return pointsArray;
    }

    public void setPointsArray(List<Coordinates> pointsArray) {
        this.pointsArray = pointsArray;
    }

    private void parsePerimeter(){
        try {
            JSONArray jsonArray = new JSONArray(this.perimeter);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject JSONCoordinate = jsonArray.getJSONObject(i);
                double latitude = JSONCoordinate.getDouble("lat");
                double longitude = JSONCoordinate.getDouble("long");
                this.pointsArray.add(new Coordinates((float) latitude, (float) longitude));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void getAllZones(Context context, final ZoneManagement listener){
        Map<String,String> note = new HashMap<String,String>();
        note.put("getAllZones", "1");

        String url = "http://pierre-mar.net/Zone_indexer/";
        AQuery aq = new AQuery(context);

        aq.ajax(url,note , JSONArray.class, new AjaxCallback<JSONArray>(){
            @Override
            public void callback(String url, JSONArray response, AjaxStatus status)
            {
                if(response != null){
                    ArrayList<Zone> zonesList = new ArrayList<Zone>();
                    try{
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject JSONNote = response.getJSONObject(i);
                            Zone zone = new Zone();
                            zone.zoneId = JSONNote.getInt("id");
                            zone.title = JSONNote.getString("title");
                            zone.color = JSONNote.getString("color");
                            zone.perimeter = JSONNote.getString("perimeter");
                            zone.parsePerimeter();
                            zone.used = JSONNote.getInt("used");
                            zonesList.add(zone);
                        }

                    }
                    catch(Exception e){
                        listener.zoneActionFailed("Failed to get zones");
                    }
                    listener.didDownloadAllZones(zonesList);
                }
                else{
                    listener.zoneActionFailed("Failed to get zones");
                }

            }
        });
    }

    public interface ZoneManagement {
        public void didDownloadAllZones(List<Zone> zones);
        public void zoneActionFailed(String message);
    }

}
