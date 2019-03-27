package com.zxhd.proerp.outware.outwaredetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

    public interface SaveEditListener {

        void SaveNum(int position, String string);

        void SaveRemark(int position, String string);
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
        holder.numWareOut.setText(mData.getWareoutnum()+"");
        holder.piciWareOut.setText(mData.getWareoutremark()+"");
        //添加editText的监听事件
        holder.numWareOut.addTextChangedListener(new TextSwitcher(holder));
//        holder.numWareOut.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        holder.piciWareOut.addTextChangedListener(new TextSwitcherTwo(holder));
        //通过设置tag，防止position紊乱
        holder.numWareOut.setTag(i);
        holder.piciWareOut.setTag(i);
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

    //自定义EditText的监听类
    class TextSwitcher implements TextWatcher {

        private ListViewHolder mHolder;

        public TextSwitcher(ListViewHolder mHolder) {
            this.mHolder = mHolder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //用户输入完毕后，处理输入数据，回调给主界面处理
            SaveEditListener listener = (SaveEditListener) context;
            if (s != null) {
                listener.SaveNum(Integer.parseInt(mHolder.numWareOut.getTag().toString()), s.toString());
//                listener.SaveRemark(Integer.parseInt(mHolder.piciWareOut.getTag().toString()), s.toString());
            }

        }
    }

    //自定义EditText的监听类
    class TextSwitcherTwo implements TextWatcher {

        private ListViewHolder mHolder;

        public TextSwitcherTwo(ListViewHolder mHolder) {
            this.mHolder = mHolder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //用户输入完毕后，处理输入数据，回调给主界面处理
            SaveEditListener listener = (SaveEditListener) context;
            if (s != null) {
//                listener.SaveNum(Integer.parseInt(mHolder.numWareOut.getTag().toString()), s.toString());
                listener.SaveRemark(Integer.parseInt(mHolder.piciWareOut.getTag().toString()), s.toString());
            }

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
        @BindView(R.id.num_ware_out)
        EditText numWareOut;
        @BindView(R.id.pici_ware_out)
        EditText piciWareOut;

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
