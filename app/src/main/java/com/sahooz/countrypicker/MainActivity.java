package com.sahooz.countrypicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sahooz.library.Country;
import com.sahooz.library.CountryPicker;
import com.sahooz.library.OnPick;

public class MainActivity extends AppCompatActivity {

    private ImageView ivFlag;
    private TextView tvName;
    private TextView tvCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivFlag = (ImageView) findViewById(R.id.iv_flag);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvCode = (TextView) findViewById(R.id.tv_code);
    }

    public void click(View view) {
        CountryPicker.newInstance(null, new OnPick() {
            @Override
            public void onPick(Country country) {
                if(country.flag != 0) ivFlag.setImageResource(country.flag);
                tvName.setText(country.name);
                tvCode.setText("+" + country.code);
            }
        }).show(getSupportFragmentManager(), "country");
    }

    @Override
    protected void onDestroy() {
        Country.destroy();
        super.onDestroy();
    }
}
