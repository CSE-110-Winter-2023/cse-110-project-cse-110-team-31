package edu.ucsd.cse110.socialcompass.model;

import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class LocationAPI {

    private volatile static LocationAPI instance = null;
    private OkHttpClient client;

    public LocationAPI() {
        this.client = new OkHttpClient();
    }

    public static LocationAPI provide() {
        if (instance == null) {
            instance = new LocationAPI();
        }
        return instance;
    }

    public Location getLocation(String title) throws ExecutionException, InterruptedException, TimeoutException {

        Log.i("GET", title);

        var request = new Request.Builder()
                .url("https://socialcompass.goto.ucsd.edu/location/" + title)
                .method("GET", null)
                .build();

        var executor = Executors.newSingleThreadExecutor();
        Callable<Location> callable = () -> {
            var response = client.newCall(request).execute();
            assert response.body() != null;
            var body = response.body().string();
            Log.i("GET LOCATION", body);
            return Location.fromJSON(body);
        };
        Future<Location> future_get = executor.submit(callable);
        return future_get.get(1, TimeUnit.SECONDS);
    }

    public void putLocation(Location loc) {
        final MediaType JSON = MediaType.get("application/json; charset=utf-8");

        String noteJson = loc.toPutJSON();
        System.out.println(noteJson+" "+loc.UID);
        Log.i("PUTLOC JSON", noteJson + " " +loc.UID);
        Thread putThread = new Thread(() -> {
            var body = RequestBody.create(noteJson, JSON);
            Request request = new Request.Builder()
                    .url("https://socialcompass.goto.ucsd.edu/location/" + loc.UID)
                    .put(body)
                    .build();
            try (var response = client.newCall(request).execute()) {
                assert response.body() != null;
                System.out.println(response.body().string());
                Log.i("POSTLOC", response.body().string());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        putThread.start();
    }
}
