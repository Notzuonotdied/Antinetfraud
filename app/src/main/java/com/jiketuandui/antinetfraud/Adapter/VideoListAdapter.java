package com.jiketuandui.antinetfraud.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jiketuandui.antinetfraud.R;
import com.jiketuandui.antinetfraud.Util.VideoConstant;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


public class VideoListAdapter extends BaseAdapter {

    int[] videoIndexs = {0, 1, 2, 3, 4};
    Context context;

    public VideoListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return videoIndexs.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.item_videoview, null);
            viewHolder.jcVideoPlayerCover = (SimpleDraweeView) convertView.findViewById(R.id.videoplayer);
            viewHolder.jcVideoPlayerTitle = (TextView) convertView.findViewById(R.id.jcVideoPlayerTitle);
            viewHolder.video = (FrameLayout) convertView.findViewById(R.id.video);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final int pos = position;

        viewHolder.jcVideoPlayerCover.setImageURI(VideoConstant.videoThumbs[position]);
        viewHolder.jcVideoPlayerTitle.setText(VideoConstant.videoTitles[position]);
        viewHolder.video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JCVideoPlayerStandard.startFullscreen(context, JCVideoPlayerStandard.class,
                        VideoConstant.videoUrls[pos], VideoConstant.videoTitles[pos]);
            }
        });

        return convertView;
    }

    class ViewHolder {
        SimpleDraweeView jcVideoPlayerCover;
        TextView jcVideoPlayerTitle;
        FrameLayout video;
    }
}
