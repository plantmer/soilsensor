package com.plantmer.soilsensor.util;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by edgars.martinovs on 2018-06-15.
 */

@Database(entities = {DataObj.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DataDao dataDao();
}