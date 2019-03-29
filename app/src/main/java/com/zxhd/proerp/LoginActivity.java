package com.zxhd.proerp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zxhd.proerp.cont.Api;
import com.zxhd.proerp.utils.ClearWriteEditText;
import com.zxhd.proerp.utils.http.CallBackUtil;
import com.zxhd.proerp.utils.http.OkhttpUtil;
import com.zxhd.proerp.utils.toast.NiceToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;


public class LoginActivity extends AppCompatActivity {

    private ClearWriteEditText deLoginPhone;
    private ClearWriteEditText deLoginPassword;
    private Button deLoginSign;
    private ImageView deImgBackgroud;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        progressBar = findViewById(R.id.loading);
        deLoginPhone = findViewById(R.id.de_login_phone);
        deLoginPassword = findViewById(R.id.de_login_password);
        deLoginPhone.setText("1001");
        deLoginPassword.setText("111");
        deLoginSign = findViewById(R.id.de_login_sign);
        deLoginSign.setOnClickListener(listener);
        deImgBackgroud = findViewById(R.id.de_img_backgroud);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.translate_anim);
                deImgBackgroud.startAnimation(animation);
            }
        }, 200);

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.de_login_sign:
//                    getIdDetails("320323199112164031");
                    String account = deLoginPhone.getText().toString();
                    String pwd = deLoginPassword.getText().toString();
                    Map<String, String> map = new HashMap();
                    map.put("employeeNumber", account);
                    map.put("passWord", pwd);
                    progressBar.setVisibility(View.VISIBLE);
                    OkhttpUtil.okHttpPost(Api.LOGIN, map, new CallBackUtil.CallBackString() {
                        @Override
                        public void onFailure(Call call, Exception e) {

                        }

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String msg = jsonObject.getString("msg");
                                String result = jsonObject.getString("result");
                                if (result.equals("ok")) {
                                    JSONObject user = new JSONObject(jsonObject.getString("user"));
                                    JSONArray jsonArray = user.getJSONArray("roles");
                                    String roles = jsonArray.get(0).toString();
                                    JSONObject roleObject = new JSONObject(roles);
                                    SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("id", user.getString("id"));
                                    editor.putString("account", user.getString("employeeNumber"));
                                    editor.putString("uesrName", user.getString("employeeName"));
                                    editor.putString("roleId", roleObject.getString("id"));
                                    editor.putString("roleName", roleObject.getString("roleName"));
                                    editor.commit();

                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    break;
            }
        }
    };

    private void getIdDetails(String code) {
        Map<String, String> params = new HashMap();
        params.put("cardno", code);//身份证号码
        params.put("dtype", "json");//返回数据格式：json或xml,默认json
        params.put("key", "c3202af1edc058d297902a4739ff2b7a");//你申请的key
        OkhttpUtil.okHttpGet("http://apis.juhe.cn/idcard/index", params, new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(Call call, Exception e) {
                Log.e("onFailure", e.toString());
            }

            @Override
            public void onResponse(String response) {
                Log.e("onResponse", response);
//                {"resultcode":"200","reason":"成功的返回","result":{"area":"江苏省徐州市铜山县","sex":"男","birthday":"1991年12月16日","verify":""},"error_code":0}
            }
        });
    }

    public void showToast(String msg) {
        NiceToast.newNiceToast(this).setText(msg)
                .alignTop(true)
                .setDuration(1)
                .setTextColor(R.color.titleBg)
                .show();
    }
}
