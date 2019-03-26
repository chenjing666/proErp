package com.zxhd.proerp.outware.diduigoods;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxhd.proerp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiDuiGoodsAdapter extends RecyclerView.Adapter<DiDuiGoodsAdapter.ListViewHolder> {
    private List<DiDuiGoodsList> mList;
    private Context context;
    private DiDuiGoodsList mData;

    public DiDuiGoodsAdapter(Context context) {
        this.context = context;
    }

    public void bind(List<DiDuiGoodsList> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.didui_goods_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        mData = mList.get(position);
        holder.repoName.setText(mData.getRepo_name());
        holder.areaNumber.setText(mData.getArea_number());
        holder.districtNumber.setText(mData.getDistrict_number());
        holder.districtNumber.setText(mData.getDistrict_number());
        holder.list.setText(mData.getList());
        holder.pici.setText(mData.getPici() + "");
        holder.sums.setText(mData.getSums() + "");
        int type = mData.getJudge();
        switch (type) {
            case 1:
                holder.judge.setText("原材料");
                break;
            case 2:
                holder.judge.setText("半成品");
                break;
            case 3:
                holder.judge.setText("产品");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.repo_name)
        TextView repoName;
        @BindView(R.id.district_number)
        TextView districtNumber;
        @BindView(R.id.area_number)
        TextView areaNumber;
        @BindView(R.id.judge)
        TextView judge;
        @BindView(R.id.list)
        TextView list;
        @BindView(R.id.pici)
        TextView pici;
        @BindView(R.id.sums)
        TextView sums;

        public ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
