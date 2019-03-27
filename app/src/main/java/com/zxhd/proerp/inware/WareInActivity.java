package com.zxhd.proerp.inware;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zxhd.proerp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WareInActivity extends AppCompatActivity {

    @BindView(R.id.loading_in)
    ProgressBar loadingIn;
    @BindView(R.id.back_in)
    TextView backIn;
    @BindView(R.id.search_view_in)
    SearchView searchViewIn;
    @BindView(R.id.recyclerView_ware_in)
    RecyclerView recyclerViewWareIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_in);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.back_in)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_in:
                finish();
                break;
        }

    }
}
