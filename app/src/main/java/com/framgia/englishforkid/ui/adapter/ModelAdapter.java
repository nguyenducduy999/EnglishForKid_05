package com.framgia.englishforkid.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.framgia.englishforkid.R;
import com.framgia.englishforkid.data.model.VideoModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by duynguyenduc on 18/01/2017.
 */
public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.ViewHolder> {
    public static final int STATE_LIST = 0;
    public static final int STATE_GRID = 1;
    private List<VideoModel> mListVideo;
    private LayoutInflater mLayoutInflater;
    private int mViewType = STATE_LIST;
    private Context mContext;

    public ModelAdapter(List<VideoModel> modelList, Context context, int viewType) {
        mListVideo = modelList;
        mLayoutInflater = LayoutInflater.from(context);
        mViewType = viewType;
        mContext = context;
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
        VideoModel model = mListVideo.get(position);
        holder.bindData(model);
    }

    @Override
    public int getItemCount() {
        return mListVideo == null ? 0 : mListVideo.size();
    }

    public int getViewType() {
        return mViewType;
    }

    public void setViewType(int viewType) {
        mViewType = viewType;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_song)
        ImageView mImageSong;
        @BindView(R.id.text_title)
        TextView mTextTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(VideoModel videoModel) {
            if (videoModel == null) return;
            mTextTitle.setText(videoModel.getSongTitle());
            Glide.with(mContext).load(videoModel.getIconUrl()).into(mImageSong);
        }
    }
}


