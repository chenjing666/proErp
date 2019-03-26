package com.zxhd.proerp.outware.outwaredetails;

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

public class WareOutDetailsAdapter extends RecyclerView.Adapter<WareOutDetailsAdapter.ListViewHolder> {
    private List<WareOutDetailsList> mList;
    private Context context;
    private WareOutDetailsList mData;

    public WareOutDetailsAdapter(Context context) {
        this.context = context;
    }

    public void bind(List<WareOutDetailsList> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_ware_out_details, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int i) {
        mData = mList.get(i);
        holder.goodsnumber.setText(mData.getGoodsnumber());
        holder.goodsname.setText(mData.getGoodsname());
        int judge = mData.getJudge();
        switch (judge) {
            case 1:
                holder.twotypename.setText("原材料-" + mData.getTwotypename());
                break;
            case 2:
                holder.twotypename.setText("半成品-" + mData.getTwotypename());
                break;
            case 3:
                holder.twotypename.setText("产品-" + mData.getTwotypename());
                break;
        }
        holder.goodsspec.setText(mData.getGoodsspec());
        holder.colorNum.setText(mData.getColorNum());
        holder.meteringName.setText(mData.getMetering_name() + "(" + mData.getMetering_abbreviation() + ")");
        holder.outnumber.setText(mData.getOutnumber() + "");
        holder.haveout.setText(mData.getHaveout() + "");
        holder.downnumber.setText(mData.getDownnumber() + "");
        //item点击事件
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.itemView, i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.goodsnumber)
        TextView goodsnumber;
        @BindView(R.id.goodsname)
        TextView goodsname;
        @BindView(R.id.twotypename)
        TextView twotypename;
        @BindView(R.id.goodsspec)
        TextView goodsspec;
        @BindView(R.id.colorNum)
        TextView colorNum;
        @BindView(R.id.metering_name)
        TextView meteringName;
        @BindView(R.id.outnumber)
        TextView outnumber;
        @BindView(R.id.haveout)
        TextView haveout;
        @BindView(R.id.downnumber)
        TextView downnumber;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
