package edu.ucsd.cse110.socialcompass.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "locations")
public class Location {
    @PrimaryKey
    @SerializedName("public_code")
    @NonNull
    public String UID;

    @SerializedName("label")
    public String label;

    @SerializedName("latitude")
    public double latitude;

    @SerializedName("longitude")
    public double longitude;

    @SerializedName("created_at")
    public String created_at;

    @SerializedName("updated_at")
    public String updated_at;

    public Location(@NonNull String UID, double latitude, double longitude) {
        this.UID = UID;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Location fromJSON(String json) {
        return new Gson().fromJson(json, Location.class);
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }
}
