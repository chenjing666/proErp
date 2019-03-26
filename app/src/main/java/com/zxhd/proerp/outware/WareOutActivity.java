package com.zxhd.proerp.outware;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.zxhd.proerp.R;
import com.zxhd.proerp.cont.Api;
import com.zxhd.proerp.outware.outwaredetails.WareOutDetailsActivity;
import com.zxhd.proerp.utils.http.CallBackUtil;
import com.zxhd.proerp.utils.http.OkhttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

public class WareOutActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<OutWareList> mList = new ArrayList<OutWareList>();
    private WareOutAdapter wareOutAdapter;
    private ProgressBar progressBar;
    private int pageSize = 20;
    private int nowPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_out);
        progressBar = findViewById(R.id.loading);
        mRecyclerView = findViewById(R.id.recyclerView_ware_out);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        wareOutAdapter = new WareOutAdapter(this);
        wareOutAdapter.setOnItemClickListener(listener);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(wareOutAdapter);

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
                    wareOutAdapter.bind(mList);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        getMList();
    }

    private WareOutAdapter.OnItemClickListener listener = new WareOutAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(WareOutActivity.this, WareOutDetailsActivity.class);
            intent.putExtra("id", mList.get(position).getLl_id());
            intent.putExtra("respository_id", mList.get(position).getRespository_id());
            intent.putExtra("state", mList.get(position).getState());
            startActivity(intent);
        }
    };

    private void getMList() {
        if (mList != null) {
            mList.clear();
        }
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("offSet", nowPage + "");
        paramsMap.put("pageSize", pageSize + "");
        progressBar.setVisibility(View.VISIBLE);
        OkhttpUtil.okHttpPost(Api.OUT_WARE_LIST, paramsMap, new CallBackUtil.CallBackString() {
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
                        String ll_num = obj.getString("lists");
                        String ll_num_lingliao = obj.getString("fromList");
                        String ll_time = obj.getString("repo_time");
                        String ll_user_create = obj.getString("employeeName");
                        String ll_ware = obj.getString("repo_name");
                        String ll_remark = obj.getString("remark");
                        String ll_other = "";
                        int ll_id = obj.getInt("id");
                        int ll_type = obj.getInt("list_type");
                        int ll_status = obj.getInt("state");
                        int respository_id = obj.getInt("respository_id");
                        int state = obj.getInt("state");
                        OutWareList outWareList = new OutWareList(ll_num, ll_num_lingliao, ll_time, ll_user_create, ll_ware, ll_id, ll_type, ll_status, ll_remark, ll_other, respository_id, state);
                        mList.add(outWareList);
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
