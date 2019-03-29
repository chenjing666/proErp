package com.zxhd.proerp.lingliao;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxhd.proerp.R;
import com.zxhd.proerp.outware.OutWareList;

import java.util.List;

public class LingLiaoListAdapter extends RecyclerView.Adapter<LingLiaoListAdapter.ListViewHolder> {
    private List<OutWareList> mList;
    private Context context;
    private OutWareList mData;

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
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        mData = mList.get(position);
        holder.ll_num.setText(mData.getLl_num_lingliao());
        holder.ll_ware.setText(mData.getLl_ware());
        holder.ll_user_create.setText(mData.getLl_user_create());
        holder.ll_time.setText(mData.getLl_time());
        holder.ll_remark.setText(mData.getLl_remark());
        int status = mData.getLl_status();
        switch (status) {
            case 0:
                holder.ll_status.setText("未出库");
                holder.ll_status.setTextColor(context.getResources().getColor(R.color.red));
                break;
            case 1:
                holder.ll_status.setText("部分出库");
                holder.ll_status.setTextColor(context.getResources().getColor(R.color.green));
                break;
            case 2:
                holder.ll_status.setText("全部出库");
                break;
        }

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
        private LinearLayout linearLayout;
        private TextView ll_num, ll_ware, ll_status, ll_user_create, ll_time, ll_remark;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.ll_ware_out);
            ll_num = itemView.findViewById(R.id.ll_num);
            ll_ware = itemView.findViewById(R.id.ll_ware);
            ll_status = itemView.findViewById(R.id.ll_status);
//            ll_type = itemView.findViewById(R.id.ll_type);
            ll_user_create = itemView.findViewById(R.id.ll_user_create);
            ll_time = itemView.findViewById(R.id.ll_time);
            ll_remark = itemView.findViewById(R.id.ll_remark);
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
