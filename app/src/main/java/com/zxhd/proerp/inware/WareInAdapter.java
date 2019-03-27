package com.zxhd.proerp.inware;

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

public class WareInAdapter extends RecyclerView.Adapter<WareInAdapter.ListViewHolder> {
    private List<WareInBean> mList;
    private Context context;
    private WareInBean bean;

    public WareInAdapter(Context context) {
        this.context = context;
    }

    public void bind(List<WareInBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_ware_in, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        bean = mList.get(position);
        holder.inDaohuoWare.setText(bean.getRespository_name());
        holder.inNumDaohuo.setText(bean.getLists());
        holder.inNumRuku.setText(bean.getList());
        holder.inPici.setText(bean.getPici() + "");
        int type = Integer.parseInt(bean.getList_type());
        switch (type) {
            case 1:
                holder.inType.setText("采购入库");
                break;
            case 2:
                holder.inType.setText("生产入库");
                break;
            case 3:
                holder.inType.setText("退料入库");
                break;
        }

        holder.inUserCreate.setText(bean.getEmployeeName());
        holder.inTime.setText(bean.getRepo_time());
        holder.inRemark.setText(bean.getRemark());

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
        @BindView(R.id.in_num_daohuo)
        TextView inNumDaohuo;
        @BindView(R.id.in_num_ruku)
        TextView inNumRuku;
        @BindView(R.id.in_pici)
        TextView inPici;
        @BindView(R.id.in_daohuo_ware)
        TextView inDaohuoWare;
        @BindView(R.id.in_type)
        TextView inType;
        @BindView(R.id.in_user_create)
        TextView inUserCreate;
        @BindView(R.id.in_time)
        TextView inTime;
        @BindView(R.id.in_remark)
        TextView inRemark;

        public ListViewHolder(View itemView) {
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
