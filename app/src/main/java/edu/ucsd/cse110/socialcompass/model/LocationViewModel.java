package edu.ucsd.cse110.socialcompass.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class LocationViewModel extends AndroidViewModel {
    private LiveData<Location> loc;
    private final LocationRepository repo;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        repo = new LocationRepository();
    }

    public LiveData<Location> getNote(String UID) {
        if (loc == null) {
            loc = repo.getRemote(UID);
        }
        return loc;
    }
}
