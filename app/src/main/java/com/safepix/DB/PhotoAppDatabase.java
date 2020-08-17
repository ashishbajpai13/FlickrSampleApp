package com.safepix.DB;


import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = {PhotoEntity.class}, version = 1)
public abstract class PhotoAppDatabase extends RoomDatabase {
    public abstract PhotoDAO getPhotoDAO();
}
