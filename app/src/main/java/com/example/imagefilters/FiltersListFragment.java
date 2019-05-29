package com.example.imagefilters;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.imagefilters.utils.BitmapUtils;
import com.example.imagefilters.utils.SpacesItemDecoration;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class FiltersListFragment extends Fragment implements ThumbnailsListAdapter.ThumbnailsAdapterListener{

    @BindView(R.id.filters)
    RecyclerView filters;

    private ArrayList<ThumbnailItem> thumbnailItems;
    private ThumbnailsListAdapter thumbnailsListAdapter;
    private FilterListListener listener;

    public void setListener(FilterListListener listener) {
        this.listener = listener;
    }

    public FiltersListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filters_list, container, false);

        ButterKnife.bind(view);

        thumbnailItems = new ArrayList<>();
        thumbnailsListAdapter = new ThumbnailsListAdapter(getContext(), thumbnailItems, this);
        RecyclerView.LayoutManager mLayoutManger = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        filters.setLayoutManager(mLayoutManger);
        filters.setAdapter(thumbnailsListAdapter);
        filters.setItemAnimator(new DefaultItemAnimator());
        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
                getResources().getDisplayMetrics());
        filters.addItemDecoration(new SpacesItemDecoration(space));

        prepateThumbnails(null);

        return view;
    }

    public void prepateThumbnails(final Bitmap bitmap) {

        Runnable r = new Runnable() {
            @Override
            public void run() {
                Bitmap thumbImage;
                if (bitmap == null) {
                    thumbImage = BitmapUtils.getBitmapFromAssets(getActivity(), "dog.jpg", 100, 100);
                } else {
                    thumbImage = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
                }

                if (thumbImage == null) return;

                ThumbnailsManager.clearThumbs();
                thumbnailItems.clear();

                //add normal bitmap first
                ThumbnailItem thumbnailItem = new ThumbnailItem();
                thumbnailItem.image = thumbImage;
                thumbnailItem.filterName = getString(R.string.filter_normal);
                ThumbnailsManager.addThumb(thumbnailItem);

                List<Filter> filters = FilterPack.getFilterPack(getActivity());
                for (Filter filter : filters) {
                    ThumbnailItem t1 = new ThumbnailItem();
                    t1.filterName = filter.getName();
                    t1.image = thumbImage;
                    t1.filter = filter;
                    ThumbnailsManager.addThumb(t1);
                }

                thumbnailItems.addAll(ThumbnailsManager.processThumbs(getContext()));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        thumbnailsListAdapter.notifyDataSetChanged();
                    }
                });
            }
        };

        new Thread(r).start();
    }

    @Override
    public void onFilterSelected(Filter filter) {
        if (listener != null)
            listener.onFilterselected(filter);
    }

    public interface FilterListListener {
        void onFilterselected(Filter filter);
    }
}
