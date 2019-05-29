package com.example.imagefilters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThumbnailsListAdapter extends RecyclerView.Adapter<ThumbnailsListAdapter.MyviewHolder> {

    private Context mContext;
    private List<ThumbnailItem> thumbnailItems;
    private ThumbnailsAdapterListener lisntener;
    private int selectedIndex = 0;
    private ThumbnailClickListner thumbnailClickListner;

     class MyviewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgThumbnail)
        ImageView imgThumbnail;
        @BindView(R.id.txtFilterName)
        TextView txtFilterName;

         MyviewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
        }
    }

    public ThumbnailsListAdapter(Context context, List<ThumbnailItem> thumbnailItems,
                                  ThumbnailsAdapterListener lisntener) {
        this.mContext = context;
        this.thumbnailItems = thumbnailItems;
        this.lisntener = lisntener;
        thumbnailClickListner = new ThumbnailClickListner();
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.thumbnail_list_item,
                viewGroup, false);

        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder viewHolder, int i) {
        ThumbnailItem thumbnailItem = thumbnailItems.get(i);

        viewHolder.txtFilterName.setText(thumbnailItem.filterName);

        viewHolder.imgThumbnail.setImageBitmap(thumbnailItem.image);
        viewHolder.imgThumbnail.setOnClickListener(thumbnailClickListner);
        viewHolder.imgThumbnail.setTag(R.id.filter, thumbnailItem.filter);
        viewHolder.imgThumbnail.setTag(R.id.position, i);

        if (selectedIndex == i) {
            viewHolder.txtFilterName.setTextColor(ContextCompat.getColor(mContext, R.color.filter_label_selected));
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public interface ThumbnailsAdapterListener {
        void onFilterSelected(Filter filter);
    }

    private class ThumbnailClickListner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Filter filter  = (Filter) v.getTag(R.id.filter);
            lisntener.onFilterSelected(filter);
            selectedIndex = (int)v.getTag(R.id.position);
        }
    }
}
