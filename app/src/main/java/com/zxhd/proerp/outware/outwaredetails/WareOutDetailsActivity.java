package com.zxhd.proerp.outware.outwaredetails;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zxhd.proerp.R;
import com.zxhd.proerp.cont.Api;
import com.zxhd.proerp.outware.diduigoods.DiDuiDetailsActivity;
import com.zxhd.proerp.utils.http.CallBackUtil;
import com.zxhd.proerp.utils.http.OkhttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class WareOutDetailsActivity extends AppCompatActivity implements WareOutDetailsAdapter.SaveEditListener {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.ware_out_do)
    FloatingActionButton wareOutDo;
    private RecyclerView mRecyclerView;
    private List<WareOutDetailsList> mList = new ArrayList<>();
    private WareOutDetailsAdapter wareOutDetailsAdapter;
    private ProgressBar progressBar;
    private int pageSize = 0;
    private int nowPage = 0;
    private int outwarehouse_id;
    private int respository_id;
    private int cha;
    private String msg = "";//地堆信息
    private int goodsid;
    private int judge;
    private int color_spec;
    private int state;
    private WareOutDetailsList mData;
    private String person_id;
    private int list_type;
    private String aa;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_out_details);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        outwarehouse_id = intent.getIntExtra("id", 0);
        respository_id = intent.getIntExtra("respository_id", 0);
        state = intent.getIntExtra("state", 0);
        list_type = intent.getIntExtra("list_type", 0);

        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        person_id = preferences.getString("id", null);

        progressBar = findViewById(R.id.loading);
        mRecyclerView = findViewById(R.id.recyclerView_ware_out);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        wareOutDetailsAdapter = new WareOutDetailsAdapter(this);
        wareOutDetailsAdapter.setOnItemClickListener(listener);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(wareOutDetailsAdapter);
    }

    @OnClick({R.id.back, R.id.ware_out_do})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ware_out_do:
                //执行出库
//                Toast.makeText(WareOutDetailsActivity.this, "执行出库！", Toast.LENGTH_SHORT).show();
//                Log.e("mList==", mList.toString());
                if (mList.size() == 0) {
                    return;
                }
                aa = "";
                for (int i = 0; i < mList.size(); i++) {
                    mData = mList.get(i);
                    if (mList.get(i).getWareoutnum() == 0) {
                        break;
                    }
                    aa = aa + "产品编码：" + mData.getGoodsnumber() + "产品名称：" + mData.getGoodsname() + "出库数量：" + mData.getWareoutnum() + "备注：" + mData.getWareoutremark() + "\n";
                }
                showDialogWareOut();
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    wareOutDetailsAdapter.bind(mList);
                    break;
                case 1:
                    showDialog();
                    break;
                default:
                    break;
            }
        }
    };

    public void doWareOut(String a) {
        HashMap<String, String> paramsMap = new HashMap<>();
        JSONArray jsonArray=new JSONArray();
        for (int i=0;i<mList.size();i++){
            mData=mList.get(i);
            JSONObject object=new JSONObject();
            try {
                object.put("id", mData.getId());
                object.put("goodsnumber", mData.getGoodsnumber());
                object.put("goodsname", mData.getGoodsname());
                object.put("judge", mData.getJudge());
                object.put("twotypename", mData.getTwotypename());
                object.put("goodsspec", mData.getGoodsspec());
                object.put("colorNum", mData.getColorNum());
                object.put("outnumber", mData.getOutnumber());
                object.put("downnumber", mData.getDownnumber());
                object.put("haveout", mData.getHaveout());
                object.put("goodsid", mData.getGoodsid());
                object.put("color_spec", mData.getColor_spec());
                object.put("waitnumber", mData.getWaitnumber());
                object.put("cha2", mData.getCha2());
                object.put("cha", mData.getCha());
                object.put("outsum", mData.getWareoutnum());
                object.put("lists", mData.getWareoutremark());
                jsonArray.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        paramsMap.put("IDS", jsonArray.toString());
        paramsMap.put("remark", a);
        paramsMap.put("outwarehouse_id", outwarehouse_id + "");
        paramsMap.put("list_type", list_type + "");
        paramsMap.put("respository_id", respository_id + "");
        paramsMap.put("personid", person_id);
        Log.e("paramsMap==",paramsMap.toString());
        OkhttpUtil.okHttpPost(Api.GOODS_WARE_OUT, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Log.e("onFailure", e.toString());
            }

            @Override
            public void onResponse(String response) {
                Log.e("response", response);
                try {
                    JSONObject object = new JSONObject(response);
                    String msg = object.getString("msg");
                    String result = object.getString("result");
                    Toast.makeText(WareOutDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    if (result.equals("ok")) {
                        alertDialog.dismiss();
                        getMList();
//                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 出库弹窗
     */
    public void showDialogWareOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WareOutDetailsActivity.this);
        builder.setIcon(R.drawable.seal_logo);
        builder.setTitle("确认出库！");
        //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View view = LayoutInflater.from(WareOutDetailsActivity.this).inflate(R.layout.ware_out_remark, null);
        //    设置我们自己定义的布局文件作为弹出框的Content
        builder.setView(view);
        TextView textView = view.findViewById(R.id.ware_out_s);
        textView.setText(aa);
        final EditText ware_out_remark = view.findViewById(R.id.ware_out_remark);

        builder.setPositiveButton("确定", null);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(WareOutDetailsActivity.this, "点击了取消！", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();//必须加这句，不然后面会报空指针错误
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = ware_out_remark.getText().toString().trim();
                doWareOut(a);
            }
        });
    }

    /**
     * 推荐地堆
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WareOutDetailsActivity.this);
        builder.setIcon(R.drawable.seal_logo);
        builder.setTitle("地堆推荐！");
        //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View view = LayoutInflater.from(WareOutDetailsActivity.this).inflate(R.layout.didui_details, null);
        //    设置我们自己定义的布局文件作为弹出框的Content
        builder.setView(view);
        TextView textView = view.findViewById(R.id.details_didui);
        textView.setText(msg);
        final EditText details_didui = view.findViewById(R.id.num_didui);

        builder.setPositiveButton("确定", null);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(WareOutDetailsActivity.this, "点击了取消！", Toast.LENGTH_SHORT).show();
            }
        });
//        builder.show();
        AlertDialog dialog = builder.create();
        dialog.show();//必须加这句，不然后面会报空指针错误
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = details_didui.getText().toString().trim();
                if (a.isEmpty()) {
                    Toast.makeText(WareOutDetailsActivity.this, "请输入或扫描地堆编码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(WareOutDetailsActivity.this, DiDuiDetailsActivity.class);
                intent.putExtra("goodsid", goodsid + "");
                intent.putExtra("judge", judge + "");
                intent.putExtra("respository_id", respository_id + "");
                intent.putExtra("outwarehouse_id", outwarehouse_id + "");
                intent.putExtra("color_spec", color_spec + "");
                intent.putExtra("cha", cha);
                intent.putExtra("area_number", a);
                intent.putExtra("outnumber", mData.getOutnumber());
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMList();
    }

    private WareOutDetailsAdapter.OnItemClickListener listener = new WareOutDetailsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if (state == 2 || mList.get(position).getOutnumber() == mList.get(position).getDownnumber()) {
                Toast.makeText(WareOutDetailsActivity.this, "已出库完成！", Toast.LENGTH_SHORT).show();
                return;
            }
            msg = "";
            mData = mList.get(position);
            goodsid = mList.get(position).getGoodsid();
            judge = mList.get(position).getJudge();
            color_spec = mList.get(position).getColor_spec();
            cha = mList.get(position).getCha();
            getDiDuiDetails(goodsid, judge, color_spec);
        }
    };

    /**
     * 货位推荐
     *
     * @param goodsid
     * @param judge
     * @param color_spec
     */
    private void getDiDuiDetails(int goodsid, int judge, int color_spec) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("offSet", nowPage + "");
        paramsMap.put("pageSize", pageSize + "");
        paramsMap.put("goodsid", goodsid + "");
        paramsMap.put("judge", judge + "");
        paramsMap.put("respository_id", respository_id + "");
        paramsMap.put("color_spec", color_spec + "");
        OkhttpUtil.okHttpPost(Api.OUT_WARE_LIST_DETAILS_GOODSDIDUI, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Log.e("onFailure", e.toString());
            }

            @Override
            public void onResponse(String response) {
                Log.e("response==", response);
                try {
                    JSONObject object = new JSONObject(response);
                    String result = object.getString("result");
                    String msgTwo = object.getString("msg");
                    if (result.equals("error")) {
                        Toast.makeText(WareOutDetailsActivity.this, msgTwo, Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        JSONArray jsonArray = new JSONArray(object.getString("items"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String repo_name = obj.getString("repo_name");//仓库
                            String district_number = obj.getString("district_number");//区域
                            String area_number = obj.getString("area_number");//地堆
                            String list = obj.getString("list");//入库单号
                            int pici = obj.getInt("pici");//批次
                            int sums = obj.getInt("sums");//在架数量
                            msg = msg + "地堆编码：" + area_number + "入库单号：" + list + "批次：" + pici + "在库数量：" + sums + "\n";
                        }
                        Message obtain = Message.obtain();
                        obtain.what = 1;
                        handler.sendMessage(obtain);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 获取出库单详情列表
     */
    private void getMList() {
        if (mList != null) {
            mList.clear();
        }
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("outwarehouse_id", outwarehouse_id + "");
        paramsMap.put("respository_id", respository_id + "");
        paramsMap.put("offSet", nowPage + "");
        paramsMap.put("pageSize", pageSize + "");
        progressBar.setVisibility(View.VISIBLE);
        OkhttpUtil.okHttpPost(Api.OUT_WARE_LIST_DETAILS, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Log.e("onFailure", e.toString());
            }

            @Override
            public void onResponse(String response) {
                Log.e("onResponse", response);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject object = new JSONObject(response);
                    String result = object.getString("result");
                    String msg = object.getString("msg");
                    if (result.equals("error")) {
                        Toast.makeText(WareOutDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONArray jsonArray = new JSONArray(object.getString("items"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String goodsnumber = obj.getString("goodsnumber");
                        String goodsname = obj.getString("goodsname");
                        String twotypename = obj.getString("twotypename");
                        String goodsspec = obj.getString("goodsspec");
                        String colorNum = obj.getString("colorNum");
                        String metering_name = obj.getString("metering_name");
                        String metering_abbreviation = obj.getString("metering_abbreviation");
                        int id = obj.getInt("id");
                        int judge = obj.getInt("judge");
                        int outnumber = obj.getInt("outnumber");
                        int waitnumber = obj.getInt("waitnumber");
                        int downnumber = obj.getInt("downnumber");
                        int haveout = obj.getInt("haveout");
                        int goodsid = obj.getInt("goodsid");
                        int color_spec = obj.getInt("color_spec");
                        int cha = obj.getInt("cha");
                        int cha2 = obj.getInt("cha2");
                        WareOutDetailsList wareOutDetailsList = new WareOutDetailsList(id, goodsnumber, goodsname, judge, twotypename, goodsspec, colorNum, metering_name, metering_abbreviation, outnumber, downnumber, haveout, goodsid, color_spec, cha, 0, "", waitnumber, cha2);
                        mList.add(wareOutDetailsList);
                    }
                    Message obtain = Message.obtain();
                    obtain.what = 0;
                    handler.sendMessage(obtain);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void SaveNum(int position, String string) {
        //回调处理edittext内容，使用map的好处在于：position确定的情况下，string改变，只会动态改变string内容
        mList.get(position).setWareoutnum(Integer.parseInt(string));
    }

    @Override
    public void SaveRemark(int position, String string) {
        //回调处理edittext内容，使用map的好处在于：position确定的情况下，string改变，只会动态改变string内容
        mList.get(position).setWareoutremark(string);
    }
}
