package com.zxhd.proerp.inware;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 到货列表
 */
public class WareInActivity extends AppCompatActivity {

    @BindView(R.id.loading_in)
    ProgressBar loadingIn;
    @BindView(R.id.back_in)
    TextView backIn;
    @BindView(R.id.search_view_in)
    SearchView searchViewIn;
    @BindView(R.id.recyclerView_ware_in)
    RecyclerView mRecyclerView;
    private List<WareInBean> mList = new ArrayList<>();
    private WareInAdapter wareInAdapter;
    private int pageSize = 20;
    private int nowPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_in);
        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        wareInAdapter = new WareInAdapter(this);
        wareInAdapter.setOnItemClickListener(listener);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(wareInAdapter);

    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    wareInAdapter.bind(mList);
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

    @OnClick(R.id.back_in)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_in:
                finish();
                break;
        }

    }

    private WareInAdapter.OnItemClickListener listener = new WareInAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

        }
    };

    /**
     * 获取出库需求单列表
     */
    private void getMList() {
        if (mList != null) {
            mList.clear();
        }
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("offSet", nowPage + "");
        paramsMap.put("pageSize", pageSize + "");
        loadingIn.setVisibility(View.VISIBLE);
        OkhttpUtil.okHttpPost(Api.GOODS_WARE_IN_ARRIVAL, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Log.e("onFailure", e.toString());
            }

            @Override
            public void onResponse(String response) {
                loadingIn.setVisibility(View.GONE);
                try {
                    JSONObject object = new JSONObject(response);
                    String result = object.getString("result");
                    String msg = object.getString("msg");
                    if (result.equals("error")) {
                        Toast.makeText(WareInActivity.this, msg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONArray jsonArray = new JSONArray(object.getString("items"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String employeeName = obj.getString("employeeName");
                        String judges = obj.getString("judges");
                        String list = obj.getString("list");
                        String list_type = obj.getString("list_type");
                        String lists = obj.getString("lists");
                        String remark = obj.getString("remark");
                        String repo_time = obj.getString("repo_time");
                        String respository_name = obj.getString("respository_name");
                        int gteid = obj.getInt("gteid");
                        int id = obj.getInt("id");
                        int pici = obj.getInt("pici");
                        int repo_name = obj.getInt("repo_name");
                        int respository_id = obj.getInt("respository_id");
                        int returnState = obj.getInt("returnState");
                        int shenghe = obj.getInt("shenghe");
                        int state = obj.getInt("state");
                        double totalPrice = obj.getInt("totalPrice");
                        WareInBean wareInBean = new WareInBean(employeeName, judges, list, list_type, lists, remark, repo_time, respository_name, gteid, id, pici, repo_name, respository_id, returnState, shenghe, state, totalPrice);
                        mList.add(wareInBean);
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
