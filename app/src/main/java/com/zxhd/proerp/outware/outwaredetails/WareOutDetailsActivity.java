package com.zxhd.proerp.outware.outwaredetails;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.zxhd.proerp.utils.WordHandle;
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
    @BindView(R.id.ware_out_over)
    TextView wareOutOver;
    private RecyclerView mRecyclerView;
    private List<WareOutDetailsList> mList = new ArrayList<>();
    private WareOutDetailsAdapter wareOutDetailsAdapter;
    private ProgressBar progressBar;
    private int pageSize = 0;
    private int nowPage = 0;
    private int outwarehouse_id;
    private int respository_id;
    private double cha;
    private String msg = "";//地堆信息
    private int goodsid;
    private int judge;
    private int color_spec;
    private int state;
    private WareOutDetailsList mData;
    private String person_id;
    private int list_type;
    private String aa;
    private AlertDialog alertDialog = null;
    /**
     * 扫描
     */
    private String SCAN_ACTION = "com.android.server.scannerservice.seuic.scan";
    private String START_ACTION = "com.scan.onStartScan";
    private String END_ACTION = "com.scan.onEndScan";
    //scannerdata  键值名称
    public static final String BAR_CODE = "barcode";
    public static final String scan_data = "scannerdata";
    private IntentFilter filter;
    private EditText ware_out_employee;
    private AlertDialog alertDialog_end = null;
    private AlertDialog dialog = null;
    private EditText details_didui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置扫描工具广播名称
        Intent intent = new Intent(SCAN_ACTION);
        Bundle bundle = new Bundle();
        bundle.putString(BAR_CODE, scan_data);
        intent.putExtras(bundle);
        sendBroadcast(intent);
        //扫描设置
        setContentView(R.layout.activity_ware_out_details);
        ButterKnife.bind(this);
        Intent intent2 = getIntent();
        outwarehouse_id = intent2.getIntExtra("id", 0);
        respository_id = intent2.getIntExtra("respository_id", 0);
        state = intent2.getIntExtra("state", 0);
        list_type = intent2.getIntExtra("list_type", 0);

        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        person_id = preferences.getString("id", null);

        progressBar = findViewById(R.id.loading);
        mRecyclerView = findViewById(R.id.recyclerView_ware_out);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        wareOutDetailsAdapter = new WareOutDetailsAdapter(this);
        wareOutDetailsAdapter.setOnItemClickListener(listener);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(wareOutDetailsAdapter);

        //扫描
        filter = new IntentFilter(SCAN_ACTION);
        filter.addAction(START_ACTION);
        filter.addAction(END_ACTION);
        //扫描
    }

    @OnClick({R.id.back, R.id.ware_out_do, R.id.ware_out_over})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ware_out_do:
                //执行出库
                if (state == 2) {
                    Toast.makeText(WareOutDetailsActivity.this, "已经出库完成！", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mList.size() == 0) {
                    return;
                }
                aa = "";
                for (int i = 0; i < mList.size(); i++) {
                    mData = mList.get(i);
                    if (mData.getWareoutnum() == 0) {
                        Log.e("mList=", "mList000");
                    } else {
                        aa = aa + "产品编码：" + mData.getGoodsnumber() + "产品名称：" + mData.getGoodsname() + "出库数量：" + mData.getWareoutnum() + "备注：" + mData.getWareoutremark() + "\n";
                    }

                }
                if (aa.isEmpty()) {
                    Toast.makeText(WareOutDetailsActivity.this, "请输入出库信息！", Toast.LENGTH_LONG).show();
                    return;
                }
                showDialogWareOut();
                break;
            case R.id.ware_out_over:
                if (state == 2) {
                    Toast.makeText(WareOutDetailsActivity.this, "已经出库完成！", Toast.LENGTH_LONG).show();
                    return;
                }
                //结束出库
                showDialogWareOutOver();
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
                    if (mList.size() == 0) {
                        Toast.makeText(WareOutDetailsActivity.this, "暂无数据！", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 1:
                    showDialog();
                    break;
                case 2:
                    Bundle bundle = msg.getData();
                    String theCode = bundle.getString("scannerdata");
                    Log.e("theCode==", "theCode:" + theCode);
                    if (!theCode.isEmpty()) {
                        if (dialog != null && dialog.isShowing()) {
                            details_didui.setText(theCode.trim());
                        } else if (alertDialog != null && alertDialog.isShowing()) {
                            ware_out_employee.setText(theCode.trim());
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // 注册接收器
        registerReceiver(mReceiver, filter);
        getMList();
    }

    @Override
    protected void onPause() {
        // 卸载接收器
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(SCAN_ACTION)) {
                Bundle bundle = intent.getExtras();
                Message obtain = Message.obtain();
                obtain.what = 2;
                obtain.setData(bundle);
                handler.sendMessage(obtain);
            } else if (action.equals(START_ACTION)) {
                Log.e("START_ACTION", START_ACTION);
            } else if (action.equals(END_ACTION)) {
                Log.e("END_ACTION", END_ACTION);
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 结束出库
     *
     * @param a
     */
    private void doBreakWareOut(String a) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("id", outwarehouse_id + "");
        paramsMap.put("remark", a);
        OkhttpUtil.okHttpPost(Api.GOODS_WARE_OUT_OVER, paramsMap, new CallBackUtil.CallBackString() {
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
                        alertDialog_end.dismiss();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 结束出库
     */
    public void showDialogWareOutOver() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WareOutDetailsActivity.this);
        builder.setIcon(R.drawable.seal_logo);
        builder.setTitle("确认结束出库！");
        //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View view = LayoutInflater.from(WareOutDetailsActivity.this).inflate(R.layout.ware_out_over_remark, null);
        //    设置我们自己定义的布局文件作为弹出框的Content
        builder.setView(view);
        final EditText ware_out_over_remark = view.findViewById(R.id.ware_out_over_remark);

        builder.setPositiveButton("确定", null);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(WareOutDetailsActivity.this, "点击了取消！", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog_end = builder.create();
        alertDialog_end.show();//必须加这句，不然后面会报空指针错误
        alertDialog_end.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = ware_out_over_remark.getText().toString().trim();
                doBreakWareOut(a);
            }
        });
    }

    public void doWareOut(String a, String b) {
        HashMap<String, String> paramsMap = new HashMap<>();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < mList.size(); i++) {
            mData = mList.get(i);
            JSONObject object = new JSONObject();
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
                object.put("meteringId", mData.getMeteringId() + "");
                object.put("outsum", mData.getWareoutnum());
                object.put("lists","rk"+ mData.getWareoutremark());
                jsonArray.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        paramsMap.put("IDS", jsonArray.toString());
        paramsMap.put("remark", a);
        paramsMap.put("employeeNumber", b);
        paramsMap.put("outwarehouse_id", outwarehouse_id + "");
        paramsMap.put("list_type", list_type + "");
        paramsMap.put("respository_id", respository_id + "");
        paramsMap.put("personid", person_id);
        Log.e("paramsMap==", paramsMap.toString());
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
        ware_out_employee = view.findViewById(R.id.ware_out_employee);

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
                String b = ware_out_employee.getText().toString().trim();
                doWareOut(a, b);
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
        details_didui = view.findViewById(R.id.num_didui);

        builder.setPositiveButton("确定", null);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(WareOutDetailsActivity.this, "点击了取消！", Toast.LENGTH_SHORT).show();
            }
        });
//        builder.show();
        dialog = builder.create();
        dialog.show();//必须加这句，不然后面会报空指针错误
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = details_didui.getText().toString().trim();
                if (a.isEmpty()) {
                    Toast.makeText(WareOutDetailsActivity.this, "请输入或扫描地堆编码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                a = WordHandle.getWord(a, WordHandle.regEx_num);//添加截取字符串
                Intent intent = new Intent(WareOutDetailsActivity.this, DiDuiDetailsActivity.class);
                intent.putExtra("goodsid", goodsid + "");
                intent.putExtra("judge", judge + "");
                intent.putExtra("respository_id", respository_id + "");
                intent.putExtra("outwarehouse_id", outwarehouse_id + "");
                intent.putExtra("color_spec", color_spec + "");
                intent.putExtra("cha", cha);
                intent.putExtra("area_number", "cw" + a);
                intent.putExtra("outnumber", mData.getOutnumber());
                startActivity(intent);
                dialog.dismiss();
            }
        });
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
                            double sums = obj.getDouble("sums");//在架数量
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
                progressBar.setVisibility(View.GONE);
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
                        double outnumber = obj.getDouble("outnumber");
                        double waitnumber = obj.getDouble("waitnumber");
                        double downnumber = obj.getDouble("downnumber");
                        double haveout = obj.getDouble("haveout");
                        int goodsid = obj.getInt("goodsid");
                        int color_spec = obj.getInt("color_spec");
                        int meteringId = obj.getInt("meteringId");
                        double cha = obj.getDouble("cha");
                        double cha2 = obj.getDouble("cha2");
                        WareOutDetailsList wareOutDetailsList = new WareOutDetailsList(id, goodsnumber, goodsname, judge, twotypename, goodsspec, colorNum, metering_name, metering_abbreviation, outnumber, downnumber, haveout, goodsid, color_spec, cha, 0, "", waitnumber, cha2, meteringId);
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
        if (string.isEmpty()) {
            string = 0 + "";
        }
        mList.get(position).setWareoutnum(Double.parseDouble(string));
    }

    @Override
    public void SaveRemark(int position, String string) {
        //回调处理edittext内容，使用map的好处在于：position确定的情况下，string改变，只会动态改变string内容
        mList.get(position).setWareoutremark(string);
    }
}
