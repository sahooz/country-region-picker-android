package com.sahooz.library;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by android on 17/10/17.
 */

public class Country {
    private static final String TAG = Country.class.getSimpleName();
    public int code;
    public String name;
    public int flag;
    private static ArrayList<Country> countries = null;

    public Country(int code, String name, String locale, int flag) {
        this.code = code;
        this.name = name;
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "Country{" +
                "code='" + code + '\'' +
                "flag='" + flag + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public static ArrayList<Country> getAll(@NonNull Context ctx, @Nullable ExceptionCallback callback) {
        if(countries != null) return countries;
        countries = new ArrayList<>();
        boolean inChina = inChina(ctx);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(ctx.getResources().getAssets().open("code.json")));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null)
                sb.append(line);
            br.close();
            JSONArray ja = new JSONArray(sb.toString());
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                int flag = 0;
                String locale = jo.getString("locale");
                if(!TextUtils.isEmpty(locale)) {
                    flag = ctx.getResources().getIdentifier("flag_" + locale.toLowerCase(), "drawable", ctx.getPackageName());
                }
                countries.add(new Country(jo.getInt("code"), jo.getString(inChina? "zh" : "en"), locale, flag));
            }

            Log.i(TAG, countries.toString());
        } catch (IOException e) {
            if(callback != null) callback.onIOException(e);
            e.printStackTrace();
        } catch (JSONException e) {
            if(callback != null) callback.onJSONException(e);
            e.printStackTrace();
        }
        return countries;
    }

    public static void destroy(){ countries = null; }

    private static boolean inChina(Context ctx) {
        return "CN".equalsIgnoreCase(ctx.getResources().getConfiguration().locale.getCountry());
    }
}
