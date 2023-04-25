package com.sahooz.library.countryregionpicker.sample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sahooz.library.countryregionpicker.CountryOrRegion;
import com.sahooz.library.countryregionpicker.PickFragment;
import com.sahooz.library.countryregionpicker.PickActivity;

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
            CountryOrRegion.load(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void click(View view) {
        if(view.getId() == R.id.btnDialog)
            PickFragment.newInstance(country -> {
                if(country.flag != 0) ivFlag.setImageResource(country.flag);
                tvName.setText(country.name + "(" + country.translate + ")");
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
            CountryOrRegion countryOrRegion = CountryOrRegion.fromJson(data.getStringExtra("country"));
            assert countryOrRegion != null;
            if(countryOrRegion.flag != 0) ivFlag.setImageResource(countryOrRegion.flag);
            tvName.setText(countryOrRegion.name + "(" + countryOrRegion.translate + ")");
            tvCode.setText("+" + countryOrRegion.code);
        }
    }

    @Override
    protected void onDestroy() {
        CountryOrRegion.destroy();
        super.onDestroy();
    }
}
