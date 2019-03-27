package com.zxhd.proerp.inware.inwaredetails;

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

public class WareInDetailsAdapter extends RecyclerView.Adapter<WareInDetailsAdapter.ListViewHolder> {

    private List<WareInDetailsBean> mList;
    private Context context;
    private WareInDetailsBean bean;

    public WareInDetailsAdapter(Context context) {
        this.context = context;
    }

    public void bind(List<WareInDetailsBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_ware_in_details, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        bean = mList.get(position);
        holder.wareInDetailsCode.setText(bean.getEp_number());
        holder.wareInDetailsName.setText(bean.getEp_name());
        int judge = bean.getJudge();
        switch (judge) {
            case 1:
                holder.wareInDetailsType.setText("原材料-" + bean.getTwotypename());
                break;
            case 2:
                holder.wareInDetailsType.setText("半成品-" + bean.getTwotypename());
                break;
            case 3:
                holder.wareInDetailsType.setText("产品-" + bean.getTwotypename());
                break;
        }
        holder.wareInDetailsSpace.setText(bean.getEp_spec());
        holder.colorNum.setText(bean.getColorNum());
        holder.wareInDetailsMeteringName.setText(bean.getMetering_name() + "(" + bean.getMetering_abbreviation() + ")");
        holder.wareInDetailsOutnumber.setText(bean.getQuantity()+"");
        holder.wareInDetailsHaveout.setText(bean.getIn()+"");
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

    class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ware_in_details_code)
        TextView wareInDetailsCode;
        @BindView(R.id.ware_in_details_name)
        TextView wareInDetailsName;
        @BindView(R.id.ware_in_details_type)
        TextView wareInDetailsType;
        @BindView(R.id.ware_in_details_space)
        TextView wareInDetailsSpace;
        @BindView(R.id.colorNum)
        TextView colorNum;
        @BindView(R.id.ware_in_details_metering_name)
        TextView wareInDetailsMeteringName;
        @BindView(R.id.ware_in_details_outnumber)
        TextView wareInDetailsOutnumber;
        @BindView(R.id.ware_in_details_haveout)
        TextView wareInDetailsHaveout;

        public ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    // ###################################   item的点击事件（接口回调） ##############
    public interface OnItemWareInDetailsClickListener {

        void onItemClick(View view, int position);

    }

    private WareInDetailsAdapter.OnItemWareInDetailsClickListener onItemClickListener;

    //对外提供一个监听的方法
    public void setOnItemClickListener(WareInDetailsAdapter.OnItemWareInDetailsClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
