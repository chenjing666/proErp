package com.zxhd.proerp.outware.outwaredetails;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zxhd.proerp.R;
import com.zxhd.proerp.cont.Api;
import com.zxhd.proerp.outware.WareOutActivity;
import com.zxhd.proerp.outware.diduigoods.DiDuiDetailsActivity;
import com.zxhd.proerp.utils.http.CallBackUtil;
import com.zxhd.proerp.utils.http.OkhttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

public class WareOutDetailsActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_out_details);
        Intent intent = getIntent();
        outwarehouse_id = intent.getIntExtra("id", 0);
        respository_id = intent.getIntExtra("respository_id", 0);
        state = intent.getIntExtra("state", 0);
        progressBar = findViewById(R.id.loading);
        mRecyclerView = findViewById(R.id.recyclerView_ware_out);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        wareOutDetailsAdapter = new WareOutDetailsAdapter(this);
        wareOutDetailsAdapter.setOnItemClickListener(listener);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(wareOutDetailsAdapter);

        //返回
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WareOutDetailsActivity.this);
        builder.setIcon(R.drawable.seal_logo);
        builder.setTitle("请输入用户名和密码");
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
                intent.putExtra("color_spec", color_spec + "");
                intent.putExtra("cha", cha);
                intent.putExtra("area_number", a);
                startActivity(intent);
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
            goodsid = mList.get(position).getGoodsid();
            judge = mList.get(position).getJudge();
            color_spec = mList.get(position).getColor_spec();
            cha = mList.get(position).getCha();
            getDiDuiDetails(goodsid, judge, color_spec);
        }
    };

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
                try {
                    JSONObject object = new JSONObject(response);
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

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
//                Log.e("onResponse", response);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject object = new JSONObject(response);
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
                        int downnumber = obj.getInt("downnumber");
                        int haveout = obj.getInt("haveout");
                        int goodsid = obj.getInt("goodsid");
                        int color_spec = obj.getInt("color_spec");
                        int cha = obj.getInt("cha");
                        WareOutDetailsList wareOutDetailsList = new WareOutDetailsList(id, goodsnumber, goodsname, judge, twotypename, goodsspec, colorNum, metering_name, metering_abbreviation, outnumber, downnumber, haveout, goodsid, color_spec, cha);
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
}
