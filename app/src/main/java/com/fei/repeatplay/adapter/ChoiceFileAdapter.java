package com.fei.repeatplay.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fei.repeatplay.R;

import java.io.File;
import java.util.List;

public class ChoiceFileAdapter extends RecyclerView.Adapter<ChoiceFileAdapter.MyHolder> {

    private List<File> data;
    private LayoutInflater mInflater;
    private Context mContext;

    public ChoiceFileAdapter(Context context, List<File> data) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void setData(List<File> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_filelist, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        if (null != data && data.size() > position) {
            final File file = data.get(position);

            holder.itemfilelist_ll.setBackgroundColor(position % 2 == 0 ?
                    mContext.getResources().getColor(R.color.white) :
                    mContext.getResources().getColor(R.color.gray));

            holder.itemfilelist_icon_iv.setImageResource(file.isFile() ?
                    R.mipmap.file : R.mipmap.folder);

            holder.itemfilelist_name_tv.setText(null == file.getName() ? "" : file.getName());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!file.isFile() && null != mOnItemFolderClick){
                        mOnItemFolderClick.onFolderClick(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return null == data ? 0 : data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        LinearLayout itemfilelist_ll;
        ImageView itemfilelist_icon_iv;
        TextView itemfilelist_name_tv;

        public MyHolder(View itemView) {
            super(itemView);
            if (null == itemfilelist_ll) {
                itemfilelist_ll = itemView.findViewById(R.id.itemfilelist_ll);
            }
            if (null == itemfilelist_icon_iv) {
                itemfilelist_icon_iv = itemView.findViewById(R.id.itemfilelist_icon_iv);
            }
            if (null == itemfilelist_name_tv) {
                itemfilelist_name_tv = itemView.findViewById(R.id.itemfilelist_name_tv);
            }
        }
    }

    public interface  OnItemFolderClick {
        void onFolderClick(int positon);
    }

    private OnItemFolderClick mOnItemFolderClick;

    public void setOnItemFolderClick (OnItemFolderClick onItemFolderClick){
        this.mOnItemFolderClick = onItemFolderClick;
    }


}
