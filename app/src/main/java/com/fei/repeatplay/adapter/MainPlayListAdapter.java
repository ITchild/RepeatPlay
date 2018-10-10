package com.fei.repeatplay.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fei.repeatplay.R;
import com.fei.repeatplay.bean.VideoInfo;

import java.util.List;

public class MainPlayListAdapter extends RecyclerView.Adapter<MainPlayListAdapter.MyHolder> {

    private List<VideoInfo> data;
    private Context context;
    private LayoutInflater layoutInflater;

    public MainPlayListAdapter(Context context, List<VideoInfo> data) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void setData(List<VideoInfo> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_mainplaylist, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        VideoInfo info  = data.get(position);

        Glide.with(context.getApplicationContext())
                .load(info.getPath())
                .placeholder(R.mipmap.nopic)
                .into(holder.itemplay_dis_iv);
        holder.itemplay_name_tv.setText(info.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != mOnPlayFileClick){
                    mOnPlayFileClick.onPlayClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == data ? 0 : data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView itemplay_dis_iv;
        TextView itemplay_name_tv;

        public MyHolder(View itemView) {
            super(itemView);
            if(null == itemplay_name_tv){
                itemplay_name_tv = itemView.findViewById(R.id.itemplay_name_tv);
            }
            if(null == itemplay_dis_iv){
                itemplay_dis_iv = itemView.findViewById(R.id.itemplay_dis_iv);
            }
        }
    }

    public interface OnPlayFileClick{
        void onPlayClick(int position);
    }

    private OnPlayFileClick mOnPlayFileClick;

    public void setOnPlayFileClick(OnPlayFileClick onPlayFileClick){
        this.mOnPlayFileClick = onPlayFileClick;
    }

}
