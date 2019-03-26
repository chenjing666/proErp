package com.zxhd.proerp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zxhd.proerp.baosun.BaoSunActivity;
import com.zxhd.proerp.inware.WareInActivity;
import com.zxhd.proerp.lingliao.LingLiaoActivity;
import com.zxhd.proerp.outware.WareOutActivity;
import com.zxhd.proerp.transfer.TransferActivity;
import com.zxhd.proerp.utils.toast.NiceToast;

public class MainActivity extends AppCompatActivity {
    private LinearLayout lingLiao;//领料单
    private LinearLayout ruku;
    private LinearLayout chuku;
    private LinearLayout yiwei;
    private LinearLayout baosun;
    private LinearLayout more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lingLiao = findViewById(R.id.lingliao);
        lingLiao.setOnClickListener(listener);
        ruku = findViewById(R.id.ruku);
        ruku.setOnClickListener(listener);
        chuku = findViewById(R.id.chuku);
        chuku.setOnClickListener(listener);
        yiwei = findViewById(R.id.yiwei);
        yiwei.setOnClickListener(listener);
        baosun = findViewById(R.id.baosun);
        baosun.setOnClickListener(listener);
        more = findViewById(R.id.more);
        more.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.lingliao:
                    startActivity(new Intent(MainActivity.this, LingLiaoActivity.class));
                    break;
                case R.id.ruku:
                    startActivity(new Intent(MainActivity.this, WareInActivity.class));
                    break;
                case R.id.chuku:
                    startActivity(new Intent(MainActivity.this, WareOutActivity.class));
                    break;
                case R.id.yiwei:
                    startActivity(new Intent(MainActivity.this, TransferActivity.class));
                    break;
                case R.id.baosun:
                    startActivity(new Intent(MainActivity.this, BaoSunActivity.class));
                    break;
                case R.id.more:
                    showToast("更多");
                    break;
            }
        }
    };

    public void showToast(String msg) {
//        Toast toast = new Toast(this);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.setText(msg);
//        toast.show();
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
