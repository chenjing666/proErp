package com.zxhd.proerp.inware.inwaredetails;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.zxhd.proerp.outware.outwaredetails.WareOutDetailsActivity;
import com.zxhd.proerp.outware.outwaredetails.WareOutDetailsList;
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

public class WareInDetailsActivity extends AppCompatActivity {
    @BindView(R.id.loading_in)
    ProgressBar progressBar;
    @BindView(R.id.back_in)
    TextView backIn;
    @BindView(R.id.recyclerView_ware_in)
    RecyclerView mRecyclerView;
    private int id;
    private int respository_id;
    private String name;

    private List<WareInDetailsBean> mList = new ArrayList<>();
    private WareInDetailsAdapter wareInDetailsAdapter;
    private WareInDetailsBean bean;
    private int pageSize = 0;
    private int nowPage = 0;
    private String didui;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_in_details);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        respository_id = intent.getIntExtra("respository_id", 0);
        name = intent.getStringExtra("name");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        wareInDetailsAdapter = new WareInDetailsAdapter(this);
        wareInDetailsAdapter.setOnItemClickListener(listener);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(wareInDetailsAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMList();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    wareInDetailsAdapter.bind(mList);
                    break;
                case 1:
                    showDialog();
                    break;
                default:
                    break;
            }
        }
    };

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WareInDetailsActivity.this);
        builder.setIcon(R.drawable.seal_logo);
        builder.setTitle("上货！");
        //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View view = LayoutInflater.from(WareInDetailsActivity.this).inflate(R.layout.didui_details_in, null);
        //    设置我们自己定义的布局文件作为弹出框的Content
        builder.setView(view);
        TextView textView = view.findViewById(R.id.details_didui_in);
        textView.setText(didui);
        final EditText details_didui = view.findViewById(R.id.num_didui_in);//地堆编码
        final EditText details_didui_num = view.findViewById(R.id.num_didui_in_num);//数量

        builder.setPositiveButton("确定", null);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(WareInDetailsActivity.this, "点击了取消！", Toast.LENGTH_SHORT).show();
            }
        });
        dialog = builder.create();
        dialog.show();//必须加这句，不然后面会报空指针错误
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = details_didui.getText().toString().trim();
                String b = details_didui_num.getText().toString().trim();
                if (a.isEmpty()) {
                    Toast.makeText(WareInDetailsActivity.this, "请输入或扫描地堆编码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (b.isEmpty()) {
                    Toast.makeText(WareInDetailsActivity.this, "请输入上架数量！", Toast.LENGTH_SHORT).show();
                    return;
                }
                doUpGoods(a, b);
            }
        });
    }

    private void doUpGoods(String a, String b) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("area_id", a);//地堆
        paramsMap.put("sums", b);//数量
        paramsMap.put("gteid", bean.getGteid() + "");
        paramsMap.put("pici", bean.getPici() + "");
        paramsMap.put("inWarehouse_id", bean.getInWarehouse_id() + "");
        paramsMap.put("judge", bean.getJudge() + "");
        paramsMap.put("repo_name", bean.getRepo_name() + "");
        paramsMap.put("endproduct", bean.getEndproduct() + "");
        paramsMap.put("color_spec", bean.getColor_spec() + "");
        paramsMap.put("price", bean.getPrice() + "");
        progressBar.setVisibility(View.VISIBLE);
        OkhttpUtil.okHttpPost(Api.GOODS_WARE_IN_UPGOODS, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Log.e("onFailure", e.toString());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(String response) {
                Log.e("response", response);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject object = new JSONObject(response);
                    String msg = object.getString("msg");
                    String result = object.getString("result");
                    Toast.makeText(WareInDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    if (result.equals("ok")) {
                        dialog.dismiss();
                        getMList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private WareInDetailsAdapter.OnItemWareInDetailsClickListener listener = new WareInDetailsAdapter.OnItemWareInDetailsClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            bean = mList.get(position);
            if (bean.getQuantity() == bean.getIn()) {
                Toast.makeText(WareInDetailsActivity.this, "已经上架完成！", Toast.LENGTH_LONG).show();
                return;
            }
            getArea(bean.getGoodsid() + "", bean.getJudge() + "");
        }
    };

    private void getArea(String goodsid, String judge) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("goodsid", goodsid);
        paramsMap.put("judge", judge);
        OkhttpUtil.okHttpPost(Api.GOODS_WARE_IN_ARRIVAL_DETAILS_DIDUI_RECOMEMEND, paramsMap, new CallBackUtil.CallBackString() {
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
                        Toast.makeText(WareInDetailsActivity.this, msgTwo, Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        String area_number = object.getString("items");
                        didui = "推荐地堆：" + area_number;
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

    @OnClick(R.id.back_in)
    public void onViewClicked() {
        finish();
    }

    /**
     * 获取出库单详情列表
     */
    private void getMList() {
        if (mList != null) {
            mList.clear();
        }
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("inWarehouse_id", id + "");
        paramsMap.put("offSet", nowPage + "");
        paramsMap.put("pageSize", pageSize + "");
        progressBar.setVisibility(View.VISIBLE);
        OkhttpUtil.okHttpPost(Api.GOODS_WARE_IN_ARRIVAL_DETAILS, paramsMap, new CallBackUtil.CallBackString() {
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
                        Toast.makeText(WareInDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONArray jsonArray = new JSONArray(object.getString("items"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        double cha = obj.getDouble("cha");
                        double cha2 = obj.getDouble("cha2");
                        double haveReturn = obj.getDouble("haveReturn");
                        double in = obj.getDouble("in");
                        double price = obj.getDouble("price");
                        double quantity = obj.getDouble("quantity");
                        double sums = obj.getDouble("sums");
                        String colorNum = obj.getString("colorNum");
                        String colorPicture = obj.getString("colorPicture");
                        String ep_name = obj.getString("ep_name");
                        String ep_number = obj.getString("ep_number");
                        String ep_spec = obj.getString("ep_spec");
                        String metering_name = obj.getString("metering_name");
                        String metering_abbreviation = obj.getString("metering_abbreviation");
                        String repo_name = obj.getString("repo_name");
                        String supplier_Name = obj.getString("supplier_Name");
                        String twotypename = obj.getString("twotypename");
                        int color_spec = obj.getInt("color_spec");
                        int endproduct = obj.getInt("endproduct");
                        int ep_unit = obj.getInt("ep_unit");
                        int goodsid = obj.getInt("goodsid");
                        int gteid = obj.getInt("gteid");
                        int id = obj.getInt("id");
                        int inWarehouse_id = obj.getInt("inWarehouse_id");
                        int judge = obj.getInt("judge");
                        int pici = obj.getInt("pici");
                        int respository_id = obj.getInt("respository_id");
                        int status = obj.getInt("status");
                        WareInDetailsBean wareInDetailsBean = new WareInDetailsBean(cha, cha2, haveReturn, in, price, quantity, sums, colorNum, colorPicture, ep_name, ep_number, ep_spec, metering_abbreviation, metering_name, repo_name, supplier_Name, twotypename, color_spec, endproduct, ep_unit, goodsid, gteid, id, inWarehouse_id, judge, pici, respository_id, status);
                        mList.add(wareInDetailsBean);
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