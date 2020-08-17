package com.safepix.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.safepix.DB.PhotoAppDatabase;
import com.safepix.DB.PhotoEntity;
import com.safepix.Model.Images;
import com.safepix.Service.RetrofitClient;
import com.safepix.Service.RetrofitService;
import com.safepix.Utils.Constants;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ImageRepository {
    private RetrofitService retrofitService;
    private PhotoAppDatabase appDatabase;
    private MutableLiveData<Images> imagesMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<PhotoEntity>> photoEntityMutableLiveData = new MutableLiveData<>();
    private int page = 1;
    private int perPage = 20;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public ImageRepository(Application application) {
        retrofitService = RetrofitClient.getRetrofitService();
        appDatabase = Room.databaseBuilder(application.getApplicationContext(),
                PhotoAppDatabase.class, "Photos")
                .build();
        compositeDisposable.add(
                appDatabase.getPhotoDAO().getAllPhotos().
                        subscribeOn(Schedulers.computation()).
                        observeOn(AndroidSchedulers.mainThread()).
                        subscribe(new Consumer<List<PhotoEntity>>() {
                            @Override
                            public void accept(List<PhotoEntity> photoEntityList) throws Exception {
                                photoEntityMutableLiveData.postValue(photoEntityList);
                            }
                        })
        );
    }

    public void getAllImages(String searchText, boolean loadmore) {
        if (loadmore)
            page++;
        else page = 1;
        Observable<Images> serviceAllImages = retrofitService.getAllImages(Constants.method,
                Constants.API_KEY, searchText, Constants.format,
                Constants.nojsoncallback, String.valueOf(page), String.valueOf(perPage));
        compositeDisposable.add(serviceAllImages.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Images>() {
                    @Override
                    public void onNext(Images images) {
                        if (images != null) {
                            addImages(images, loadmore, searchText);
                            imagesMutableLiveData.postValue(images);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    private void addImages(Images images, boolean loadmore, String searchText){
        compositeDisposable.add(
                Completable.fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        for (Images.Photo photo : images.getPhotos().getPhoto())
                            if (!loadmore){
                                appDatabase.getPhotoDAO().addPhoto(new PhotoEntity(
                                        searchText,
                                        photo.getId(),
                                        photo.getSecret(),
                                        photo.getServer(),
                                        photo.getFarm()
                                ));
                            }
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        })
        );
    }

    public MutableLiveData<List<PhotoEntity>> getPhotoEntityMutableLiveData() {
        return photoEntityMutableLiveData;
    }

    public LiveData<Images> getImagesMutableLiveData() {
        return imagesMutableLiveData;
    }

    public void clear() {
        compositeDisposable.clear();
    }
}
