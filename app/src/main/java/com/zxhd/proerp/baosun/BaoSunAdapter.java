package com.zxhd.proerp.baosun;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class BaoSunAdapter extends RecyclerView.Adapter<BaoSunAdapter.BaoSunViewHolder> {
    private List<BaoSunBean> mList;
    private Context context;

    public BaoSunAdapter(Context context) {
        this.context = context;
    }

    public void bind(List<BaoSunBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaoSunAdapter.BaoSunViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaoSunAdapter.BaoSunViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class BaoSunViewHolder extends RecyclerView.ViewHolder {
        public BaoSunViewHolder(View itemView) {
            super(itemView);
        }
    }
}
