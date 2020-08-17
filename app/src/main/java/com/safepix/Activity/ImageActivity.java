package com.safepix.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RadioGroup;
import android.widget.SearchView;

import com.google.android.material.snackbar.Snackbar;
import com.safepix.Adapter.ImageAdapter;
import com.safepix.DB.PhotoEntity;
import com.safepix.Interface.OnLoadMoreListener;
import com.safepix.Model.Images;
import com.safepix.R;
import com.safepix.ViewModel.ImageViewModel;
import com.safepix.databinding.ActivityImageBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImageActivity extends AppCompatActivity {

    private ActivityImageBinding binding;
    private ImageViewModel imageViewModel;
    private ImageAdapter imageAdapter;
    private List<Images.Photo> imagesList = new ArrayList<>();
    private List<PhotoEntity> photoEntityList = new ArrayList<>();
    private String searchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        //initialize adapter
        imageAdapter = new ImageAdapter(this, imagesList, photoEntityList, binding.nestedScrollView);
        imageAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                imageViewModel.getAllImages(searchQuery, true);
            }
        });
        setRecyclerAdapter(2);
        binding.selectedRadio2.setChecked(true);

        //initialize and add observer to view model to get live data
        imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        imageViewModel.init();
        imageViewModel.getImagesLiveData().observe(ImageActivity.this, new Observer<Images>() {
            @Override
            public void onChanged(Images images) {
                imagesList.addAll(images.getPhotos().getPhoto());
                imageAdapter.notifyDataSetChanged();
            }
        });

        imageViewModel.getPhotoEntityLiveData().observe(this, new Observer<List<PhotoEntity>>() {
            @Override
            public void onChanged(List<PhotoEntity> entityList) {
                if (!isNetworkConnected()) {
                    photoEntityList.addAll(entityList);
                    imageAdapter.notifyDataSetChanged();
                    Snackbar.make(Objects.requireNonNull
                                    ((View) findViewById(android.R.id.content)),
                            getResources().getString(R.string.no_network), Snackbar.LENGTH_LONG).show();
                }
            }
        });

        //listeners
        binding.imagesSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ImageActivity.this.searchQuery = query;
                imagesList.clear();
                imageAdapter.notifyDataSetChanged();
                if (isNetworkConnected()) {
                    imageViewModel.getAllImages(query, false);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.selected_radio_2:
                        setRecyclerAdapter(2);
                        break;

                    case R.id.selected_radio_3:
                        setRecyclerAdapter(3);
                        break;

                    case R.id.selected_radio_4:
                        setRecyclerAdapter(4);
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        //to avoid soft keypad popping again
        binding.getRoot().clearFocus();
        super.onResume();
    }


    private void setRecyclerAdapter(int spanCount) {
        //set recycler with adapter
        binding.imagesRecycler.setLayoutManager(new GridLayoutManager(this, spanCount));
        binding.imagesRecycler.setAdapter(imageAdapter);
        binding.gridCountTv.setText(String.format(getString(R.string.grid_to_show), String.valueOf(spanCount)));

        //set animation
        binding.nestedScrollView.scrollTo(0, 0);
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_left_to_right);
        binding.imagesRecycler.setLayoutAnimation(controller);
        imageAdapter.notifyDataSetChanged();
        binding.imagesRecycler.scheduleLayoutAnimation();
    }

    private boolean isNetworkConnected() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            assert cm != null;
            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        } catch (Exception e) {
            return false;
        }
    }
}
