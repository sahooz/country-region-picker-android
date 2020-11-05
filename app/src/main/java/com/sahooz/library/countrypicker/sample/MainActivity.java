package com.sahooz.library.countrypicker.sample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sahooz.library.countrypicker.Country;
import com.sahooz.library.countrypicker.CountryPickerFragment;
import com.sahooz.library.countrypicker.Language;
import com.sahooz.library.countrypicker.PickActivity;

import org.json.JSONException;

import java.io.IOException;

@SuppressLint("SetTextI18n")
public class MainActivity extends AppCompatActivity {

    private ImageView ivFlag;
    private TextView tvName;
    private TextView tvCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivFlag = findViewById(R.id.iv_flag);
        tvName = findViewById(R.id.tv_name);
        tvCode = findViewById(R.id.tv_code);
        try {
            Country.load(this, Language.SIMPLIFIED_CHINESE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void click(View view) {
        if(view.getId() == R.id.btnDialog)
            CountryPickerFragment.newInstance(country -> {
                if(country.flag != 0) ivFlag.setImageResource(country.flag);
                tvName.setText(country.name);
                tvCode.setText("+" + country.code);
            }).show(getSupportFragmentManager(), "country");
        else {
            startActivityForResult(new Intent(getApplicationContext(), PickActivity.class), 111);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 111 && resultCode == Activity.RESULT_OK) {
            Country country = Country.fromJson(data.getStringExtra("country"));
            assert country != null;
            if(country.flag != 0) ivFlag.setImageResource(country.flag);
            tvName.setText(country.name);
            tvCode.setText("+" + country.code);
        }
    }

    @Override
    protected void onDestroy() {
        Country.destroy();
        super.onDestroy();
    }
}
