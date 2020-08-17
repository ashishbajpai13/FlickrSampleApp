package com.safepix.Adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.safepix.Model.Images;
import com.safepix.databinding.ItemFullImageBinding;

import java.util.List;

public class FullScreenImageAdapter extends PagerAdapter {

    private List<Images.Photo> photoList ;
    private LayoutInflater inflater;
    private Context context;
    private ItemFullImageBinding binding;

    public FullScreenImageAdapter(Context context, List<Images.Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        binding = ItemFullImageBinding.inflate(inflater, container, false);
        Images.Photo photo = photoList.get(position);
        String imageUrl =
                "https://farm" + photo.getFarm()
                        + ".staticflickr.com/" + photo.getServer()
                        + "/" + photo.getId() + "_" + photo.getSecret() + ".jpg";
        Glide.with(context).load(imageUrl).into(binding.itemImage);
        container.addView(binding.getRoot(), 0);
        return binding.getRoot();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(@Nullable Parcelable state, @Nullable ClassLoader loader) {

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Nullable
    @Override
    public Parcelable saveState() {
        return null;
    }
}

