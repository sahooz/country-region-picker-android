package com.sahooz.library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by android on 17/10/17.
 */

public class CountryPicker extends DialogFragment {

    private ArrayList<Country> allCountries = new ArrayList<>();
    private ArrayList<Country> selectedCountries = new ArrayList<>();
    private OnPick onPick;

    public CountryPicker() { }

    public static CountryPicker newInstance(Bundle args, OnPick onPick) {
        CountryPicker picker = new CountryPicker();
        picker.setArguments(args);
        picker.onPick = onPick;
        return picker;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_country_picker, container, false);
        EditText etSearch = (EditText) root.findViewById(R.id.et_search);
        final RecyclerView rvCountry = (RecyclerView) root.findViewById(R.id.rv_country);
        allCountries.clear();
        allCountries.addAll(Country.getAll(getContext(), null));
        selectedCountries.clear();
        selectedCountries.addAll(allCountries);
        final Adapter adapter = new Adapter(getContext());
        adapter.setOnPick(country -> {
            dismiss();
            if(onPick != null) onPick.onPick(country);
        });
        adapter.setSelectedCountries(selectedCountries);
        rvCountry.setAdapter(adapter);
        rvCountry.setLayoutManager(new LinearLayoutManager(getContext()));
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override public void afterTextChanged(Editable s) {
                String string = s.toString();
                selectedCountries.clear();
                for (Country country : allCountries) {
                    if(country.name.toLowerCase().contains(string.toLowerCase()))
                        selectedCountries.add(country);
                }
                adapter.notifyDataSetChanged();
            }
        });
        return root;
    }
}
