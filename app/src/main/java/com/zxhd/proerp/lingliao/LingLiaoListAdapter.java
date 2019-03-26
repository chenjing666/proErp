package com.zxhd.proerp.lingliao;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxhd.proerp.R;
import com.zxhd.proerp.outware.OutWareList;

import java.util.List;

public class LingLiaoListAdapter extends RecyclerView.Adapter<LingLiaoListAdapter.ListViewHolder> {
    private List<OutWareList> mList;
    private Context context;

    public LingLiaoListAdapter(Context context) {
        this.context = context;
    }

    public void bind(List<OutWareList> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_lingliao, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder listViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        public ListViewHolder(View itemView) {
            super(itemView);
        }
    }
    // ###################################   item的点击事件（接口回调） ##############
    public interface OnItemClickListener {

        void onItemClick(View view, int position);

    }

    private OnItemClickListener onItemClickListener;

    //对外提供一个监听的方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
