package com.safepix.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.safepix.DB.PhotoEntity;
import com.safepix.Model.Images;
import com.safepix.Repository.ImageRepository;

import java.util.List;

public class ImageViewModel extends AndroidViewModel {

    private ImageRepository imageRepository;
    private LiveData<Images> imagesLiveData;
    private LiveData<List<PhotoEntity>> photoEntityLiveData;

    public ImageViewModel(@NonNull Application application) {
        super(application);
        imageRepository = new ImageRepository(application);
    }

    public void init(){
        imagesLiveData = imageRepository.getImagesMutableLiveData();
        photoEntityLiveData = imageRepository.getPhotoEntityMutableLiveData();
    }

    public void getAllImages(String searchText, boolean loadMore){
        imageRepository.getAllImages(searchText, loadMore);
    }

    public LiveData<List<PhotoEntity>> getPhotoEntityLiveData() {
        return photoEntityLiveData;
    }

    public LiveData<Images> getImagesLiveData() {
        return imagesLiveData;
    }

    public void clear(){
        imageRepository.clear();
    }
}
