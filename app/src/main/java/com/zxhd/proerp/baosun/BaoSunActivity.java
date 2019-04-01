package com.zxhd.proerp.baosun;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;
import com.zxhd.proerp.R;
import com.zxhd.proerp.cont.Api;
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

public class BaoSunActivity extends AppCompatActivity {

    @BindView(R.id.loading_baosun)
    ProgressBar progressBar;
    @BindView(R.id.back_baosun)
    TextView backBaosun;
    @BindView(R.id.recyclerView_baosun)
    RecyclerView mRecyclerView;
    @BindView(R.id.search_baosun)
    TextView searchBaosun;
    private List<BaoSunBean> mList = new ArrayList<>();
    private BaoSunBean bean;
    private BaoSunAdapter baoSunAdapter;
    private String searchNum = "";
    private AlertDialog dialog;
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
        setContentView(R.layout.activity_bao_sun);
        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        baoSunAdapter = new BaoSunAdapter(this);
        baoSunAdapter.setOnItemClickListener(listener);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(baoSunAdapter);
        searchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
            @Override
            public void OnSearchClick(String keyword) {
                //这里处理逻辑
                searchNum = keyword;
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
                    baoSunAdapter.bind(mList);
                    if (mList.size() == 0) {
                        Toast.makeText(BaoSunActivity.this, "暂无数据！", Toast.LENGTH_LONG).show();
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

    private BaoSunAdapter.OnItemClickListener listener = new BaoSunAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            bean = mList.get(position);
            showDialog();
        }
    };

    /**
     * 报损录入
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BaoSunActivity.this);
        builder.setIcon(R.drawable.seal_logo);
        builder.setTitle("报损录入！");
        //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View view = LayoutInflater.from(BaoSunActivity.this).inflate(R.layout.didui_details_baosun, null);
        //    设置我们自己定义的布局文件作为弹出框的Content
        builder.setView(view);
        final EditText details_didui_num = view.findViewById(R.id.num_didui_baosun);
        details_didui_num.setInputType(8194);//8194只能输入数字和小数点
        final EditText details_didui_reason = view.findViewById(R.id.num_didui_reason);

        builder.setPositiveButton("确定", null);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(BaoSunActivity.this, "点击了取消！", Toast.LENGTH_SHORT).show();
            }
        });
//        builder.show();
        dialog = builder.create();
        dialog.show();//必须加这句，不然后面会报空指针错误
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = details_didui_num.getText().toString().trim();
                String b = details_didui_reason.getText().toString().trim();
                if (a.isEmpty()) {
                    Toast.makeText(BaoSunActivity.this, "请输入数量！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    double aa = Double.parseDouble(a);
                    Log.e("aa==", aa + "");
                    if (aa <= 0.0 || aa > bean.getSums()) {
                        Toast.makeText(BaoSunActivity.this, "请输入合法数量！", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        //输入数量正确，执行报损
                        doBaoSun(aa, b);
                    }
                }

            }
        });
    }

    private void doBaoSun(double a, String b) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("goodsid", bean.getGoodsid() + "");
        paramsMap.put("inList", bean.getList());
        paramsMap.put("pici", bean.getPici() + "");
        paramsMap.put("judge", bean.getJudge() + "");
        paramsMap.put("area_number", bean.getArea_number());
        paramsMap.put("sums", a + "");
        paramsMap.put("color_spec", bean.getColor_spec() + "");
        paramsMap.put("remark", b);
        paramsMap.put("gteid", bean.getGteid() + "");
        paramsMap.put("price", bean.getPrice() + "");
        Log.e("paramsMap==", paramsMap.toString());
        progressBar.setVisibility(View.VISIBLE);
        OkhttpUtil.okHttpPost(Api.GOODS_BAOSUN, paramsMap, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Log.e("onFailure", e.toString());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                Log.e("response", response);
                try {
                    JSONObject object = new JSONObject(response);
                    String msg = object.getString("msg");
                    String result = object.getString("result");
                    Toast.makeText(BaoSunActivity.this, msg, Toast.LENGTH_SHORT).show();
                    if (result.equals("ok")) {
                        dialog.dismiss();
                        getMList(searchNum);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 查询地堆产品列表
     */
    private void getMList(String area_number) {
        if (mList != null) {
            mList.clear();
        }
        area_number = WordHandle.getWord(area_number.trim(), WordHandle.regEx_num);
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("area_number", "cw" + area_number);
        progressBar.setVisibility(View.VISIBLE);
        OkhttpUtil.okHttpPost(Api.DIDUI_GOODS_DETAILS, paramsMap, new CallBackUtil.CallBackString() {
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
                        Toast.makeText(BaoSunActivity.this, msg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONArray jsonArray = new JSONArray(object.getString("items"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String goodsnumber = obj.getString("goodsnumber");
                        String goodsname = obj.getString("goodsname");
                        String twotypename = obj.getString("twotypename");
                        String goodsspec = obj.getString("goodsspec");
                        String colorNum = "";
                        if (obj.has("colorNum")) {
                            colorNum = obj.getString("colorNum");
                        }
                        String colorPicture = "";
                        if (obj.has("colorPicture")) {
                            colorPicture = obj.getString("colorPicture");
                        }
                        String metering_name = obj.getString("metering_name");
                        String metering_abbreviation = obj.getString("metering_abbreviation");
                        String list = obj.getString("list");
                        String area_number2 = obj.getString("area_number");
                        int id = obj.getInt("id");
                        int goodsid = obj.getInt("goodsid");
                        int color_spec = obj.getInt("color_spec");
                        int pici = obj.getInt("pici");
                        int area_id = obj.getInt("area_id");
                        int gteid = obj.getInt("gteid");
                        int judge = obj.getInt("judge");
                        int status = obj.getInt("status");
                        double price = obj.getDouble("price");
                        double sums = obj.getDouble("sums");
                        BaoSunBean baoSunBean = new BaoSunBean(goodsid, color_spec, pici, area_id, gteid, judge, id, status, colorNum, colorPicture, goodsname, list, area_number2, metering_name, metering_abbreviation, goodsnumber, goodsspec, twotypename, price, sums);
                        mList.add(baoSunBean);
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

    @OnClick({R.id.back_baosun, R.id.search_baosun})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_baosun:
                finish();
                break;
            case R.id.search_baosun:
                searchFragment.showFragment(getSupportFragmentManager(), SearchFragment.TAG);
                break;
        }
    }
}
