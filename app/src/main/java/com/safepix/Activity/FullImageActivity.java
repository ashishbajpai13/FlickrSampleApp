package com.safepix.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.safepix.Adapter.FullScreenImageAdapter;
import com.safepix.Model.Images;
import com.safepix.databinding.ActivityFullImageBinding;

import java.util.ArrayList;
import java.util.List;

public class FullImageActivity extends AppCompatActivity {

    private ActivityFullImageBinding binding;
    private final String IMAGELIST = "imagelist";
    private final String POSITION = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFullImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        int position = getIntent().getIntExtra(POSITION, 0);
        List<Images.Photo> photoList = (ArrayList<Images.Photo>) getIntent().getSerializableExtra(IMAGELIST);
        binding.imagesPager.setAdapter(new FullScreenImageAdapter(this, photoList));
        binding.imagesPager.setCurrentItem(position);
    }

    @Override
    protected void onDestroy() {
        supportFinishAfterTransition();
        super.onDestroy();
    }
}
