package com.zxhd.proerp.baosun;

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

public class BaoSunAdapter extends RecyclerView.Adapter<BaoSunAdapter.BaoSunViewHolder> {
    private List<BaoSunBean> mList;
    private Context context;
    private BaoSunBean bean;

    public BaoSunAdapter(Context context) {
        this.context = context;
    }

    public void bind(List<BaoSunBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaoSunViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_didui_goods, parent, false);
        return new BaoSunViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaoSunViewHolder holder, int position) {
        bean = mList.get(position);
        holder.goodsnumber.setText(bean.getGoodsnumber());
        holder.goodsname.setText(bean.getGoodsname());
        int judge = bean.getJudge();
        switch (judge) {
            case 1:
                holder.judge.setText("原材料-" + bean.getTwotypename());
                break;
            case 2:
                holder.judge.setText("半成品-" + bean.getTwotypename());
                break;
            case 3:
                holder.judge.setText("产品-" + bean.getTwotypename());
                break;
        }
        holder.goodsspec.setText(bean.getGoodsspec());
        holder.colorNum.setText(bean.getColorNum());
        holder.meteringName.setText(bean.getMetering_name() + "(" + bean.getMetering_abbreviation() + ")");
        holder.list.setText(bean.getList());
        holder.pici.setText(bean.getPici() + "");
        holder.sums.setText(bean.getSums() + "");
        //item点击事件
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class BaoSunViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.goodsnumber)
        TextView goodsnumber;
        @BindView(R.id.goodsname)
        TextView goodsname;
        @BindView(R.id.judge)
        TextView judge;
        @BindView(R.id.goodsspec)
        TextView goodsspec;
        @BindView(R.id.colorNum)
        TextView colorNum;
        @BindView(R.id.metering_name)
        TextView meteringName;
        @BindView(R.id.list)
        TextView list;
        @BindView(R.id.pici)
        TextView pici;
        @BindView(R.id.sums)
        TextView sums;

        public BaoSunViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    // ###################################   item的点击事件（接口回调） ##############
    public interface OnItemClickListener {

        void onItemClick(View view, int position);

    }

    private BaoSunAdapter.OnItemClickListener onItemClickListener;

    //对外提供一个监听的方法
    public void setOnItemClickListener(BaoSunAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
