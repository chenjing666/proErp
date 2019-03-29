package com.zxhd.proerp.lingliao;

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
import com.zxhd.proerp.outware.OutWareList;
import com.zxhd.proerp.outware.outwaredetails.WareOutDetailsActivity;
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

public class LingLiaoActivity extends AppCompatActivity {
    @BindView(R.id.back)
    TextView back;
    @BindView(R.id.search_ware_out_lingliao)
    TextView searchWareOutLingliao;
    @BindView(R.id.loading)
    ProgressBar progressBar;
    @BindView(R.id.recyclerView_ware_out)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout_ling_liao)
    SwipeRefreshLayout swipeRefreshLayoutLingLiao;
    private List<OutWareList> mList = new ArrayList<OutWareList>();
    private LingLiaoListAdapter wareOutAdapter;
    private int pageSize = 20;
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
        setContentView(R.layout.activity_ling_liao);
        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        wareOutAdapter = new LingLiaoListAdapter(this);
        wareOutAdapter.setOnItemClickListener(listener);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(wareOutAdapter);
        swipeRefreshLayoutLingLiao.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //重新获取数据
                getMList("");
                //获取完成
                swipeRefreshLayoutLingLiao.setRefreshing(false);
            }
        });
//        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == wareOutAdapter.getItemCount()) {
//                    swipeRefreshLayoutLingLiao.setRefreshing(true);
//                    //分页获取数据
//                    //获取完成
//                    swipeRefreshLayoutLingLiao.setRefreshing(false);
//                }
//            }
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
//            }
//        });
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
                    if (mList.size() == 1 && (!searchNum.isEmpty() || isScan)) {
                        isScan = false;
                        Intent intent = new Intent(LingLiaoActivity.this, WareOutDetailsActivity.class);
                        intent.putExtra("id", mList.get(0).getLl_id());
                        intent.putExtra("respository_id", mList.get(0).getRespository_id());
                        intent.putExtra("state", mList.get(0).getState());
                        intent.putExtra("list_type", mList.get(0).getLl_type());
                        startActivity(intent);
                    } else {
                        wareOutAdapter.bind(mList);
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

    private LingLiaoListAdapter.OnItemClickListener listener = new LingLiaoListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(LingLiaoActivity.this, WareOutDetailsActivity.class);
            intent.putExtra("id", mList.get(position).getLl_id());
            intent.putExtra("respository_id", mList.get(position).getRespository_id());
            intent.putExtra("state", mList.get(position).getState());
            intent.putExtra("list_type", mList.get(position).getLl_type());
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
            paramsMap.put("search", searchText.trim());
            paramsMap.put("state", "");
            paramsMap.put("offSet", nowPage + "");
            paramsMap.put("pageSize", pageSize + "");
        }
        progressBar.setVisibility(View.VISIBLE);
        OkhttpUtil.okHttpPost(Api.OUT_WARE_LIST_LINGLIAO, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                progressBar.setVisibility(View.GONE);
                Log.e("onFailure", e.toString());
            }

            @Override
            public void onResponse(String response) {
                Log.e("response", response);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject object = new JSONObject(response);
                    String result = object.getString("result");
                    String msg = object.getString("msg");
                    if (result.equals("error")) {
                        Toast.makeText(LingLiaoActivity.this, msg, Toast.LENGTH_SHORT).show();
                        return;
                    }
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

    @OnClick({R.id.back, R.id.search_ware_out_lingliao})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.search_ware_out_lingliao:
                searchFragment.showFragment(getSupportFragmentManager(), SearchFragment.TAG);
                break;
        }
    }
}
