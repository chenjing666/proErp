package com.zxhd.proerp.more;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zxhd.proerp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoreActivity extends AppCompatActivity {
    @BindView(R.id.back_more)
    TextView backMore;
    @BindView(R.id.clear_more)
    TextView clearMore;
    @BindView(R.id.et_result_scan)
    EditText tvResultScan;
    private String tvMsg="";
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
        setContentView(R.layout.activity_more);
        ButterKnife.bind(this);

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
                    Bundle bundle = msg.getData();
                    String theCode = bundle.getString("scannerdata");
                    if (!theCode.isEmpty()) {
                        //接收到条码，执行
                        tvMsg=theCode+"\n"+tvMsg;
                    }
                    tvResultScan.setText(tvMsg);
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
                Message obtain = Message.obtain();
                obtain.what = 0;
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

    @OnClick({R.id.back_more, R.id.clear_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_more:
                finish();
                break;
            case R.id.clear_more:
                tvMsg="";
                tvResultScan.setText("");
                break;
        }
    }
}
