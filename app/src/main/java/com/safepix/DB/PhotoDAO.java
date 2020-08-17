package com.safepix.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface PhotoDAO {

    @Insert
    public long addPhoto(PhotoEntity photoEntity);

    @Update
    public void updatePhoto(PhotoEntity photoEntity);

    @Delete
    public void deletePhoto(PhotoEntity photoEntity);

    @Query("select * from Photos")
    Flowable<List<PhotoEntity>> getAllPhotos();

    @Query("delete from Photos")
    public void deleteAllPhotos();

}
