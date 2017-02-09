package com.framgia.englishforkid.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.framgia.englishforkid.R;
import com.framgia.englishforkid.data.model.DataObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by duynguyenduc on 18/01/2017.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    public static final int STATE_LIST = 0;
    public static final int STATE_GRID = 1;
    private List<DataObject> mListVideo = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private int mViewType = STATE_LIST;
    private Context mContext;
    private OnItemClickListenner mListenner;

    public VideoAdapter(Context context, List<DataObject> modelList, int viewType,
                        OnItemClickListenner listenner) {
        mListVideo = modelList;
        mLayoutInflater = LayoutInflater.from(context);
        mViewType = viewType;
        mContext = context;
        mListenner = listenner;
    }

    @Override
    public int getItemViewType(int position) {
        return mViewType;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = mLayoutInflater.inflate(mViewType == STATE_GRID ? R.layout.grib_row_song : R
                .layout.list_row_song,
            parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataObject model = mListVideo.get(position);
        holder.bindData(model);
    }

    @Override
    public int getItemCount() {
        return mListVideo == null ? 0 : mListVideo.size();
    }

    public void setViewType(int viewType) {
        mViewType = viewType;
    }

    public void updateData(List<DataObject> datas) {
        if (datas == null) return;
        if (mListVideo == null) mListVideo = new ArrayList<>();
        mListVideo.clear();
        mListVideo.addAll(datas);
        notifyDataSetChanged();
    }

    public interface OnItemClickListenner {
        void onClick(DataObject videoModel);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.image_song)
        ImageView mImageSong;
        @BindView(R.id.text_title)
        TextView mTextTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bindData(DataObject dataObject) {
            if (dataObject == null) return;
            mTextTitle.setText(dataObject.getTitle());
            Glide.with(mContext).load(dataObject.getUrlImage()).into(mImageSong);
        }

        @Override
        public void onClick(View v) {
            if (mListenner == null) return;
            mListenner.onClick(mListVideo.get(getAdapterPosition()));
        }
    }
}
