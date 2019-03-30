package com.zxhd.proerp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zxhd.proerp.baosun.BaoSunActivity;
import com.zxhd.proerp.inware.WareInActivity;
import com.zxhd.proerp.lingliao.LingLiaoActivity;
import com.zxhd.proerp.more.MoreActivity;
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
    private LinearLayout ll_main_one, ll_main_two;
    private int width;
    private int height;
    private int height_my;//屏幕3分之后的宽度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        WindowManager manager = this.getWindowManager();
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        manager.getDefaultDisplay().getMetrics(outMetrics);
//        width = outMetrics.widthPixels;
//        height = outMetrics.heightPixels;
//        Log.e("width==", width + "");
//        Log.e("height==", height + "");
        setContentView(R.layout.activity_main);
//        boolean isHeightInteger = isInteger(width / 3 + "");
//        if (isHeightInteger) {
//            height_my = width / 3;
//        } else {
//            height_my = Math.round(width / 3);
//        }
        /**
         * 动态设置主布局的高度
         */
//        ll_main_one = findViewById(R.id.ll_main_one);
//        ll_main_two = findViewById(R.id.ll_main_two);
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll_main_one.getLayoutParams();
//        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) ll_main_two.getLayoutParams();
//        params.width = dip2px(MainActivity.this, width);
//        params.height = dip2px(MainActivity.this, height_my);
//        params2.height = dip2px(MainActivity.this, height_my);
//        params.height = height_my;
//        params2.height = height_my;
        // params.setMargins(dip2px(MainActivity.this, 1), 0, 0, 0); // 可以实现设置位置信息，如居左距离，其它类推
        // params.leftMargin = dip2px(MainActivity.this, 1);
//        ll_main_one.setLayoutParams(params);
//        ll_main_two.setLayoutParams(params2);
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

    /**
     * 是否整数
     *
     * @param value
     * @return
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * dp转为px
     *
     * @param context  上下文
     * @param dipValue dp值
     * @return
     */
    private int dip2px(Context context, float dipValue) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
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
                    startActivity(new Intent(MainActivity.this, MoreActivity.class));
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
