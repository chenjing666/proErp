package com.zxhd.proerp.transfer;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import com.zxhd.proerp.baosun.BaoSunAdapter;
import com.zxhd.proerp.baosun.BaoSunBean;
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

public class TransferActivity extends AppCompatActivity {

    @BindView(R.id.loading_transfer)
    ProgressBar progressBar;
    @BindView(R.id.back_transfer)
    TextView backTransfer;
    @BindView(R.id.recyclerView_transfer)
    RecyclerView mRecyclerView;
    private List<BaoSunBean> mList = new ArrayList<>();
    private BaoSunBean bean;
    private BaoSunAdapter baoSunAdapter;
    private String searchNum = "";
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        baoSunAdapter = new BaoSunAdapter(this);
        baoSunAdapter.setOnItemClickListener(listener);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(baoSunAdapter);
        searchNum = "2019030240001";
        getMList(searchNum);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    baoSunAdapter.bind(mList);
                    break;
                default:
                    break;
            }
        }
    };

    private BaoSunAdapter.OnItemClickListener listener = new BaoSunAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            bean = mList.get(position);
            showDialog();
        }
    };

    /**
     * 移位录入
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TransferActivity.this);
        builder.setIcon(R.drawable.seal_logo);
        builder.setTitle("移位！");
        //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View view = LayoutInflater.from(TransferActivity.this).inflate(R.layout.didui_details_transfer, null);
        //    设置我们自己定义的布局文件作为弹出框的Content
        builder.setView(view);
        final EditText details_didui_num = view.findViewById(R.id.num_didui_transfer);
        details_didui_num.setInputType(8194);//8194只能输入数字和小数点
        final EditText details_didui_reason = view.findViewById(R.id.num_didui_didui_transfer);

        builder.setPositiveButton("确定", null);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(TransferActivity.this, "点击了取消！", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(TransferActivity.this, "请输入数量！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    double aa = Double.parseDouble(a);
                    Log.e("aa==", aa + "");
                    if (aa <= 0.0 || aa > bean.getSums()) {
                        Toast.makeText(TransferActivity.this, "请输入合法数量！", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        //输入数量正确，执行移位
                        doTransfer(aa, b);
                    }
                }

            }
        });
    }

    private void doTransfer(double a, String b) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("goodsid", bean.getGoodsid() + "");
        paramsMap.put("area_id", bean.getArea_id() + "");
//        paramsMap.put("inList", bean.getList());
        paramsMap.put("pici", bean.getPici() + "");
        paramsMap.put("judge", bean.getJudge() + "");
        paramsMap.put("sums", a + "");
        paramsMap.put("color_spec", bean.getColor_spec() + "");
        paramsMap.put("area_number", "cw" + b);
        paramsMap.put("gteid", bean.getGteid() + "");
        paramsMap.put("price", bean.getPrice() + "");
        Log.e("paramsMap==", paramsMap.toString());
        progressBar.setVisibility(View.VISIBLE);
        OkhttpUtil.okHttpPost(Api.GOODS_YIWEI, paramsMap, new CallBackUtil.CallBackString() {
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
                    Toast.makeText(TransferActivity.this, msg, Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.back_transfer)
    public void onViewClicked() {
        finish();
    }

    /**
     * 查询地堆产品列表
     */
    private void getMList(String area_number) {
        if (mList != null) {
            mList.clear();
        }
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
                        Toast.makeText(TransferActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                        String colorPicture = obj.getString("colorPicture");
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
}
