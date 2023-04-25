package com.sahooz.library.countryregionpicker;

import android.content.Context;
import androidx.annotation.NonNull;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * Created by sahooz on 17/10/17.
 */

public class CountryOrRegion implements PyEntity {
    private static final String TAG = CountryOrRegion.class.getSimpleName();
    public int code;
    public String name, translate, locale, pinyin;
    public int flag;
    private static ArrayList<CountryOrRegion> countryOrRegions = new ArrayList<>();

    public CountryOrRegion(int code, String name, String translate, String pinyin, String locale, int flag) {
        this.code = code;
        this.name = name;
        this.translate = translate;
        this.flag = flag;
        this.locale = locale;
        this.pinyin = pinyin;
    }

    @Override
    public String toString() {
        return "Country{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", translate='" + translate + '\'' +
                ", locale='" + locale + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", flag=" + flag +
                '}';
    }

    public static ArrayList<CountryOrRegion> getAll(){
        return new ArrayList<>(countryOrRegions);
    }

    public static CountryOrRegion fromJson(String json){
        if(TextUtils.isEmpty(json)) return null;
        try {
            JSONObject jo = new JSONObject(json);
            return new CountryOrRegion(
                    jo.optInt("code"),
                    jo.optString("name"),
                    jo.optString("translate"),
                    jo.optString("pinyin"),
                    jo.optString("locale"),
                    jo.optInt("flag")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String toJson() {
        JSONObject jo = new JSONObject();
        try {
            jo.put("name", name);
            jo.put("translate", translate);
            jo.put("code", code);
            jo.put("flag", flag);
            jo.put("pinyin", pinyin);
            jo.put("locale", locale);
            return jo.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "{}";
    }

    public static void load(@NonNull Context ctx) throws IOException, JSONException {
        countryOrRegions = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(ctx.getResources().getAssets().open("code.json")));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null)
            sb.append(line);
        br.close();
        JSONArray ja = new JSONArray(sb.toString());
        for (int i = 0; i < ja.length(); i++) {
            JSONObject jo = ja.getJSONObject(i);
            int flag = 0;
            String translate = "";
            String locale = jo.getString("locale");
            if(!TextUtils.isEmpty(locale)) {
                flag = ctx.getResources().getIdentifier("flag_" + locale.toLowerCase(), "drawable", ctx.getPackageName());
                translate = ctx.getString(ctx.getResources().getIdentifier("name_" + locale.toLowerCase(), "string", ctx.getPackageName()));
            }
            String name = translate;
            Locale defaultLoc = Locale.getDefault();
            boolean inChina = "zh".equalsIgnoreCase(defaultLoc.getLanguage());
            countryOrRegions.add(
                new CountryOrRegion(
                    jo.getInt("code"),
                    name,
                    translate,
                    inChina ? jo.getString("pinyin") : name,
                    locale,
                    flag
                )
            );
        }

        Collections.sort(countryOrRegions, (o1, o2) -> o1.getPinyin().compareTo(o2.getPinyin()));
    }

    public static void destroy() {
        countryOrRegions.clear();
    }

    @Override
    public int hashCode() {
        return code;
    }

    @NonNull @Override
    public String getPinyin() {
        return pinyin;
    }
}
