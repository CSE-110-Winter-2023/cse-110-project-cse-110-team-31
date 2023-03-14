package edu.ucsd.cse110.socialcompass.model;

import android.util.Log;

import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONObject;

public class LocationAPI {

    private LocalDateTime dateTimeConnected;
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

    public void diffTime(){
        LocalDateTime dateTime1 = LocalDateTime.now();

        Duration duration = Duration.between(dateTime1, dateTimeConnected);
        long diff = Math.abs(duration.toMinutes());
        Log.i("Something Offensive",String.valueOf(diff));
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
            JSONObject responseObject = new JSONObject(body);
            String updatedAt = responseObject.getString("updated_at");
            Log.i("GET LOCATION2",updatedAt);
            return Location.fromJSON(body);
        };
        Future<Location> future_get = executor.submit(callable);
        return future_get.get(1, TimeUnit.SECONDS);
    }

    public void putLocation(Location loc) {
        dateTimeConnected = LocalDateTime.now();
        final MediaType JSON = MediaType.get("application/json; charset=utf-8");
        AtomicReference<LocalDateTime> dateTime = new AtomicReference<>(LocalDateTime.now());
        LocalDateTime dateTime3 = LocalDateTime.now();
        String locJson = loc.toLimitedJSON(new String[]{"private_code", "label", "latitude", "longitude", "time"});
        Log.i("PUTLOC JSON", locJson + " " + loc.UID);

        Thread putThread = new Thread(() -> {
            var body = RequestBody.create(locJson, JSON);
            //dateTime.set(LocalDateTime.now());
            Request request = new Request.Builder()
                    .url("https://socialcompass.goto.ucsd.edu/location/" + loc.UID)
                    .put(body)
                    .build();
            //dateTime3 = LocalDateTime.now();
            dateTime.set(LocalDateTime.now());
            Log.i("Sex", locJson + " " + loc.UID + " " + dateTime);
            try (var response = client.newCall(request).execute()) {
                assert response.body() != null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        putThread.start();
        LocalDateTime dateTime2 = LocalDateTime.now();
        Duration duration = Duration.between(dateTime2, dateTime3);
        long diff = Math.abs(duration.toMinutes());
        //var diff = dateTime2-dateTime;
        Log.i("FUCK",String.valueOf(diff));
    }

    public void patchLocation(Location loc) {
        final MediaType JSON = MediaType.get("application/json; charset=utf-8");

        String locJson = loc.toLimitedJSON(new String[]{"private_code", "isPublic"});
        System.out.println(locJson);
        Log.i("PATCHLOC JSON", locJson + " " +loc.UID);
        Thread putThread = new Thread(() -> {
            var body = RequestBody.create(locJson, JSON);
            Request request = new Request.Builder()
                    .url("https://socialcompass.goto.ucsd.edu/location/" + loc.UID)
                    .patch(body)
                    .build();
            try (var response = client.newCall(request).execute()) {
                assert response.body() != null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        putThread.start();
    }

    public void deleteLoc(Location loc) {
        final MediaType JSON = MediaType.get("application/json; charset=utf-8");

        String locJson = loc.toLimitedJSON(new String[]{"private_code"});
        System.out.println(locJson);
        Log.i("DELETELOC JSON", locJson + " " +loc.UID);
        Thread putThread = new Thread(() -> {
            var body = RequestBody.create(locJson, JSON);
            Request request = new Request.Builder()
                    .url("https://socialcompass.goto.ucsd.edu/location/" + loc.UID)
                    .delete(body)
                    .build();
            try (var response = client.newCall(request).execute()) {
                assert response.body() != null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        putThread.start();
    }
}
