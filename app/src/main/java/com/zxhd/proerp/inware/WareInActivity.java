package com.zxhd.proerp.inware;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;
import com.zxhd.proerp.R;
import com.zxhd.proerp.cont.Api;
import com.zxhd.proerp.inware.inwaredetails.WareInDetailsActivity;
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
    @BindView(R.id.recyclerView_ware_in)
    RecyclerView mRecyclerView;
    @BindView(R.id.search_ware_in)
    TextView searchWareIn;
    @BindView(R.id.swipeRefreshLayout_in)
    SwipeRefreshLayout swipeRefreshLayoutIn;
    private List<WareInBean> mList = new ArrayList<>();
    private WareInAdapter wareInAdapter;
    private int pageSize = 20000;
    private int nowPage = 1;
    private String searchNum = "";
    SearchFragment searchFragment = SearchFragment.newInstance();

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
    private boolean isScan = false;//默认没有按扫描键

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
        setContentView(R.layout.activity_ware_in);
        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        wareInAdapter = new WareInAdapter(this);
        wareInAdapter.setOnItemClickListener(listener);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(wareInAdapter);
        swipeRefreshLayoutIn.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //重新获取数据
                getMList("");
                //获取完成
                swipeRefreshLayoutIn.setRefreshing(false);
            }
        });
        searchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
            @Override
            public void OnSearchClick(String keyword) {
                //这里处理逻辑
                searchNum = keyword;
                //输入或扫描出库条码，请求出库单详情，跳转到详情页
                getMList(keyword);
            }
        });
        //扫描
        filter = new IntentFilter(SCAN_ACTION);
        filter.addAction(START_ACTION);
        filter.addAction(END_ACTION);
        //扫描
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (mList.size() == 0) {
                        wareInAdapter.bind(mList);
                        Toast.makeText(WareInActivity.this, "暂无数据！", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (mList.size() == 1 && (!searchNum.isEmpty() || isScan)) {
                        WareInBean bean = mList.get(0);
                        Intent intent = new Intent(WareInActivity.this, WareInDetailsActivity.class);
                        intent.putExtra("id", bean.getId());
                        intent.putExtra("respository_id", bean.getRespository_id());
                        intent.putExtra("name", bean.getRespository_name());
                        isScan = false;
                        searchNum = "";
                        startActivity(intent);
                    } else {
                        wareInAdapter.bind(mList);
                        if(mList.size()==0){
                            Toast.makeText(WareInActivity.this, "暂无数据！", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case 1:
                    Bundle bundle = msg.getData();
                    String theCode = bundle.getString("scannerdata");
                    if (!theCode.isEmpty()) {
                        //接收到条码，执行搜索
                        getMList(theCode);
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
        getMList("");
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
//                String str = bundle.getString("scannerdata");
//                Log.e("aaa", str);
                isScan = true;
                Message obtain = Message.obtain();
                obtain.what = 1;
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

    private WareInAdapter.OnItemClickListener listener = new WareInAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            WareInBean bean = mList.get(position);
            //id=505&respository_id=1&name=拉菲草a
            Intent intent = new Intent(WareInActivity.this, WareInDetailsActivity.class);
            intent.putExtra("id", bean.getId());
            intent.putExtra("respository_id", bean.getRespository_id());
            intent.putExtra("name", bean.getRespository_name());
            startActivity(intent);
        }
    };

    /**
     * 获取出库需求单列表
     */
    private void getMList(String searchText) {
        if (mList != null) {
            mList.clear();
        }
        HashMap<String, String> paramsMap = new HashMap<>();
        if (searchText.isEmpty()) {
            paramsMap.put("offSet", nowPage + "");
            paramsMap.put("pageSize", pageSize + "");
        } else {
            paramsMap.put("inquires", searchText.trim());
            paramsMap.put("shenghe", "");
            paramsMap.put("offSet", nowPage + "");
            paramsMap.put("pageSize", pageSize + "");
        }
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
                        double totalPrice = obj.getDouble("totalPrice");
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

    @OnClick({R.id.back_in, R.id.search_ware_in})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_in:
                finish();
                break;
            case R.id.search_ware_in:
                searchFragment.showFragment(getSupportFragmentManager(), SearchFragment.TAG);
                break;
        }
    }
}
