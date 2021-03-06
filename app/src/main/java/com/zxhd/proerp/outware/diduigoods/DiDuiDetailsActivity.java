package com.zxhd.proerp.outware.diduigoods;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zxhd.proerp.R;
import com.zxhd.proerp.cont.Api;
import com.zxhd.proerp.utils.http.CallBackUtil;
import com.zxhd.proerp.utils.http.OkhttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

public class DiDuiDetailsActivity extends AppCompatActivity {
    private String goodsid, judge, respository_id, color_spec, area_number;
    private RecyclerView mRecyclerView;
    private List<DiDuiGoodsList> mList = new ArrayList<>();
    private DiDuiGoodsAdapter diDuiGoodsAdapter;
    private ProgressBar progressBar;
    private double cha;
    private int pageSize = 20;
    private int nowPage = 1;
    private String msg;
    private String outwarehouse_id;
    private double outnumber;
    private DiDuiGoodsList mData;
    private AlertDialog dialog;
    private double b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_di_dui_details);
        Intent intent = getIntent();
        goodsid = intent.getStringExtra("goodsid");
        judge = intent.getStringExtra("judge");
        cha = intent.getDoubleExtra("cha", 0);//剩余下架数量
        respository_id = intent.getStringExtra("respository_id");
        outwarehouse_id = intent.getStringExtra("outwarehouse_id");
        outnumber = intent.getDoubleExtra("outnumber", 0);
        color_spec = intent.getStringExtra("color_spec");
        area_number = intent.getStringExtra("area_number");
        progressBar = findViewById(R.id.loading_didui_goods);
        mRecyclerView = findViewById(R.id.recyclerView_didui_goods);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        diDuiGoodsAdapter = new DiDuiGoodsAdapter(this);
        diDuiGoodsAdapter.setOnItemClickListener(listener);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(diDuiGoodsAdapter);

        //返回
        findViewById(R.id.back_didui_goods).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getDiDuiGoodsDetails(goodsid, judge, respository_id, color_spec, area_number);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    diDuiGoodsAdapter.bind(mList);
                    if (mList.size() == 0) {
                        Toast.makeText(DiDuiDetailsActivity.this, "暂无数据！", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 1:
                    break;
                default:
                    break;
            }
        }
    };

    private DiDuiGoodsAdapter.OnItemClickListener listener = new DiDuiGoodsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            mData = mList.get(position);
            msg = "";
            msg = "所需下架数量：" + cha + "\n" + "地堆编码：" + mData.getArea_number() + "\n" + "产品总数：" + mData.getSums();
            showDialog();
        }
    };

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DiDuiDetailsActivity.this);
        builder.setIcon(R.drawable.seal_logo);
        builder.setTitle("产品下架！");
        //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View view = LayoutInflater.from(DiDuiDetailsActivity.this).inflate(R.layout.didui_goods_details, null);
        //    设置我们自己定义的布局文件作为弹出框的Content
        builder.setView(view);
        TextView textView = view.findViewById(R.id.goods_details_didui);
        textView.setText(msg);
        final EditText details_didui = view.findViewById(R.id.num_didui);

        builder.setPositiveButton("确定", null);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(DiDuiDetailsActivity.this, "点击了取消！", Toast.LENGTH_SHORT).show();
            }
        });
//        builder.show();
        dialog = builder.create();
        dialog.show();//必须加这句，不然后面会报空指针错误
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = details_didui.getText().toString().trim();
                b = Double.parseDouble(a);
                if (a.isEmpty() || b == 0) {
                    Toast.makeText(DiDuiDetailsActivity.this, "请输入合理数量！", Toast.LENGTH_SHORT).show();
                    return;
                }
                doDown();//执行下架
            }
        });
    }

    /**
     * 下架操作
     */
    private void doDown() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("area_id", mData.getArea_id() + "");
        paramsMap.put("outnumber", b + "");
        paramsMap.put("gteid", mData.getGteid() + "");
        paramsMap.put("pici", mData.getPici() + "");
        paramsMap.put("outwarehouse_id", outwarehouse_id);
        paramsMap.put("judge", mData.getJudge() + "");
        paramsMap.put("meteringId", mData.getMeteringId() + "");
        paramsMap.put("district_number", mData.getDistrict_number());
        paramsMap.put("repo_name", mData.getRepo_name());
        paramsMap.put("goodsid", mData.getGoodsid() + "");
        paramsMap.put("color_spec", color_spec);
        Log.e("paramsMap==", paramsMap.toString());
        OkhttpUtil.okHttpPost(Api.GOODS_DIDUI_DOWM, paramsMap, new CallBackUtil.CallBackString() {
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
                    Toast.makeText(DiDuiDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    if (result.equals("ok")) {
                        dialog.dismiss();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取产品所在地堆及详情
     *
     * @param goodsid
     * @param judge
     * @param respository_id
     * @param color_spec
     * @param area_number
     */
    private void getDiDuiGoodsDetails(String goodsid, String judge, String respository_id, String color_spec, String area_number) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("goodsid", goodsid);
        paramsMap.put("judge", judge);
        paramsMap.put("respository_id", respository_id);
        paramsMap.put("color_spec", color_spec);
        paramsMap.put("area_number", area_number);
        OkhttpUtil.okHttpPost(Api.OUT_WARE_LIST_DETAILS_QUERYGOODS, paramsMap, new CallBackUtil.CallBackString() {
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
                    String msg = object.getString("msg");
                    if (result.equals("error")) {
                        Toast.makeText(DiDuiDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                    JSONArray jsonArray = new JSONArray(object.getString("items"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String repo_name = obj.getString("repo_name");//仓库
                        String district_number = obj.getString("district_number");//区域
                        String area_number = obj.getString("area_number");//地堆
                        String list = obj.getString("list");//入库单号
                        int goodsid = obj.getInt("goodsid");//
                        double sums = obj.getDouble("sums");//
                        int pici = obj.getInt("pici");//
                        int area_id = obj.getInt("area_id");//
                        int id = obj.getInt("id");//
                        int judge = obj.getInt("judge");//
                        int meteringId = obj.getInt("meteringId");
                        int gteid = obj.getInt("gteid");//
                        double price = obj.getDouble("price");
                        DiDuiGoodsList diDuiGoodsList = new DiDuiGoodsList(goodsid, price, repo_name, judge, id, pici, area_id, sums, list, district_number, area_number, gteid, meteringId);
                        mList.add(diDuiGoodsList);
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
