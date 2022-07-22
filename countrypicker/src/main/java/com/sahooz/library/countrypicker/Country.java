package com.sahooz.library.countrypicker;

import android.content.Context;
import androidx.annotation.NonNull;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by android on 17/10/17.
 */

public class Country implements PyEntity {
    private static final String TAG = Country.class.getSimpleName();
    public int code;
    public String name, translate, locale, pinyin;
    public int flag;
    private static ArrayList<Country> countries = new ArrayList<>();

    public Country(int code, String name, String translate, String pinyin, String locale, int flag) {
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

    public static ArrayList<Country> getAll(){
        return new ArrayList<>(countries);
    }

    public static Country fromJson(String json){
        if(TextUtils.isEmpty(json)) return null;
        try {
            JSONObject jo = new JSONObject(json);
            return new Country(
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
        countries = new ArrayList<>();
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
            String name = jo.getString("name");
            Locale defaultLoc = Locale.getDefault();
            boolean inChina = "zh".equalsIgnoreCase(defaultLoc.getLanguage());
            countries.add(
                new Country(
                    jo.getInt("code"),
                    name,
                    translate,
                    inChina ? jo.getString("pinyin") : name,
                    locale,
                    flag
                )
            );
        }

        Collections.sort(countries, (o1, o2) -> o1.getPinyin().compareTo(o2.getPinyin()));
    }

    public static void destroy() {
        countries.clear();
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
