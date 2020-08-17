package com.safepix.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.safepix.Activity.FullImageActivity;
import com.safepix.DB.PhotoEntity;
import com.safepix.Interface.OnLoadMoreListener;
import com.safepix.Model.Images;
import com.safepix.R;
import com.safepix.databinding.ItemImageBinding;

import java.io.Serializable;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private ItemImageBinding imageBinding;
    private Activity activity;
    private List<Images.Photo> imagesList;
    private OnLoadMoreListener onLoadMoreListener;
    private List<PhotoEntity> photoEntityList;
    private final String IMAGELIST = "imagelist";
    private final String POSITION = "position";


    public ImageAdapter(Activity activity, List<Images.Photo> imagesList, List<PhotoEntity> photoEntityList, NestedScrollView nestedScrollView) {
        this.activity = activity;
        this.imagesList = imagesList;
        this.photoEntityList = photoEntityList;
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                }
            }
        });
    }

    class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageBinding.imageItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.image_item) {
                Intent intent = new Intent(itemView.getContext(), FullImageActivity.class);
                intent.putExtra(IMAGELIST, (Serializable) imagesList);
                intent.putExtra(POSITION, getAdapterPosition());
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(activity, (View) imageBinding.imageItem,
                                itemView.getContext().getString(R.string.image_transition));
                activity.startActivity(intent, optionsCompat.toBundle());
            }
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        imageBinding = ItemImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ImageViewHolder(imageBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageUrl = "";
        if (imagesList.size() > 0) {
            Images.Photo photos = imagesList.get(position);
            imageUrl =
                    "https://farm" + photos.getFarm()
                            + ".staticflickr.com/" + photos.getServer()
                            + "/" + photos.getId() + "_" + photos.getSecret() + ".jpg";
        } else if (photoEntityList.size() > 0) {
            PhotoEntity photoEntity = photoEntityList.get(position);
            imageUrl =
                    "https://farm" + photoEntity.getFarm()
                            + ".staticflickr.com/" + photoEntity.getServer()
                            + "/" + photoEntity.getId() + "_" + photoEntity.getSecret() + ".jpg";
        }
        Glide.with(activity)
                .load(imageUrl)
                .into(imageBinding.imageItem);
    }

    @Override
    public int getItemCount() {
        if (imagesList.size() > 0)
            return imagesList.size();
        else
            return photoEntityList.size();
    }
}
