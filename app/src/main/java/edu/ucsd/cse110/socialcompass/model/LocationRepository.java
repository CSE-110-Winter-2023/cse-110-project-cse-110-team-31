package edu.ucsd.cse110.socialcompass.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class LocationRepository {
    LocationAPI api = LocationAPI.provide();
    private ScheduledFuture<?> locationFuture;

    public LocationRepository() {}

    public LiveData<Location> getRemote(String UID) {
        if(locationFuture!=null&&!locationFuture.isCancelled()) {
            locationFuture.cancel(true);
        }

        var loc = new MutableLiveData<Location>();
        var executor = Executors.newSingleThreadScheduledExecutor();
        locationFuture=executor.scheduleAtFixedRate(() -> {
            Location l;
            try {
                l = api.getLocation(UID);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (TimeoutException e) {
                throw new RuntimeException(e);
            }
            loc.postValue(l);
            Log.i("LOCATION RECEIVED", l.label+" "+l.longitude);
        }, 0, 3000, TimeUnit.MILLISECONDS);
        return loc;
    }
}
