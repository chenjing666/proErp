package com.zxhd.proerp.baosun;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zxhd.proerp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BaoSunActivity extends AppCompatActivity {

    @BindView(R.id.loading_baosun)
    ProgressBar progressBar;
    @BindView(R.id.back_baosun)
    TextView backBaosun;
    @BindView(R.id.recyclerView_baosun)
    RecyclerView mRecyclerView;
    private List<BaoSunBean> mList=new ArrayList<>();
    private BaoSunBean bean;
    private BaoSunAdapter baoSunAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao_sun);
        ButterKnife.bind(this);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                default:
                    break;
            }
        }
    };

    @OnClick(R.id.back_baosun)
    public void onViewClicked() {
        finish();
    }
}
