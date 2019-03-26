package com.zxhd.proerp.outware.diduigoods;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;

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
    private int cha;
    private int pageSize = 20;
    private int nowPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_di_dui_details);
        Intent intent = getIntent();
        goodsid = intent.getStringExtra("goodsid");
        judge = intent.getStringExtra("judge");
        cha = intent.getIntExtra("cha",0);
        respository_id = intent.getStringExtra("respository_id");
        color_spec = intent.getStringExtra("color_spec");
        area_number = intent.getStringExtra("area_number");
        getDiDuiGoodsDetails(goodsid, judge, respository_id, color_spec, area_number);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    break;
                default:
                    break;
            }
        }
    };

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
                Log.e("response==",response);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = new JSONArray(object.getString("items"));
                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject obj = jsonArray.getJSONObject(i);
//                        String repo_name = obj.getString("repo_name");//仓库
//                        String district_number = obj.getString("district_number");//区域
//                        String area_number = obj.getString("area_number");//地堆
//                        String list = obj.getString("list");//入库单号
//                        int pici = obj.getInt("pici");//批次
//                        int sums = obj.getInt("sums");//在架数量
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
}
