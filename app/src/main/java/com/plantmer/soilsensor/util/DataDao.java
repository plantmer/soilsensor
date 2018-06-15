package com.plantmer.soilsensor.util;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by edgars.martinovs on 2018-06-15.
 */

@Dao
public interface DataDao {

    @Query("SELECT * FROM DataObj")
    List<DataObj> getAll();

    @Query("SELECT * FROM DataObj WHERE dateTime < (:end) and dateTime > (:start)")
    List<DataObj> getRange(long start , long end);

    @Insert
    void insertAll(DataObj... data);

    @Delete
    void delete(DataObj data);

    @Query("DELETE FROM DataObj WHERE dateTime < (:end)")
    abstract void deleteOlder(long end);
}
