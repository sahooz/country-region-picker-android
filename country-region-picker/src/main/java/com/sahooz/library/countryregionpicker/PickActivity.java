package com.sahooz.library.countryregionpicker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PickActivity extends AppCompatActivity {

    private final ArrayList<CountryOrRegion> selectedCountries = new ArrayList<>();
    private final ArrayList<CountryOrRegion> allCountries = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick);
        RecyclerView rvPick = findViewById(R.id.rv_pick);
        SideBar side = findViewById(R.id.side);
        EditText etSearch = findViewById(R.id.et_search);
        TextView tvLetter = findViewById(R.id.tv_letter);
        allCountries.clear();
        allCountries.addAll(CountryOrRegion.getAll());
        selectedCountries.clear();
        selectedCountries.addAll(allCountries);
        final CAdapter adapter = new CAdapter(selectedCountries);
        rvPick.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvPick.setLayoutManager(manager);
        rvPick.setAdapter(adapter);
        rvPick.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override public void afterTextChanged(Editable s) {
                String string = s.toString();
                selectedCountries.clear();
                for (CountryOrRegion countryOrRegion : allCountries) {
                    if(countryOrRegion.name.toLowerCase().contains(string.toLowerCase())
                            || countryOrRegion.translate.toLowerCase().contains(string.toLowerCase())
                            || countryOrRegion.getPinyin().toLowerCase().contains(string.toLowerCase())
                    )
                        selectedCountries.add(countryOrRegion);
                }
                adapter.update(selectedCountries);
            }
        });

        side.addIndex("#", side.indexes.size());
        side.setOnLetterChangeListener(new SideBar.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                tvLetter.setVisibility(View.VISIBLE);
                tvLetter.setText(letter);
                int  position = adapter.getLetterPosition(letter);
                if (position != -1) {
                    manager.scrollToPositionWithOffset(position, 0);
                }
            }

            @Override
            public void onReset() {
                tvLetter.setVisibility(View.GONE);
            }
        });
    }

    class CAdapter extends PyAdapter<RecyclerView.ViewHolder> {

        public CAdapter(List<? extends PyEntity> entities) {
            super(entities, '#');
        }

        @Override
        public RecyclerView.ViewHolder onCreateLetterHolder(ViewGroup parent, int viewType) {
            return new LetterHolder(getLayoutInflater().inflate(R.layout.item_letter, parent, false));
        }

        @Override
        public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
            return new VH(getLayoutInflater().inflate(R.layout.item_country_region_large_padding, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindHolder(RecyclerView.ViewHolder holder, PyEntity entity, int position) {
            VH vh = (VH)holder;
            final CountryOrRegion countryOrRegion = (CountryOrRegion)entity;
            vh.ivFlag.setImageResource(countryOrRegion.flag);
            vh.tvName.setText(countryOrRegion.translate);
            vh.tvCode.setText("+" + countryOrRegion.code);
            holder.itemView.setOnClickListener(v -> {
                Intent data = new Intent();
                data.putExtra("country", countryOrRegion.toJson());
                setResult(Activity.RESULT_OK , data);
                finish();
            });
        }

        @Override
        public void onBindLetterHolder(RecyclerView.ViewHolder holder, LetterEntity entity, int position) {
            ((LetterHolder)holder).textView.setText(entity.letter.toUpperCase());
        }
    }
}
