package com.sahooz.library.countrypicker;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by android on 17/10/17.
 */

public class CountryPickerFragment extends DialogFragment {

    private ArrayList<Country> allCountries = new ArrayList<>();
    private ArrayList<Country> selectedCountries = new ArrayList<>();
    private PickCountryCallback callback;

    public CountryPickerFragment() { }

    public static CountryPickerFragment newInstance(PickCountryCallback callback) {
        CountryPickerFragment picker = new CountryPickerFragment();
        picker.callback = callback;
        return picker;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        if(window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_country_picker, container, false);
        EditText etSearch = root.findViewById(R.id.et_search);
        final RecyclerView rvCountry = root.findViewById(R.id.rv_country);
        allCountries.clear();
        allCountries.addAll(Country.getAll());
        selectedCountries.clear();
        selectedCountries.addAll(allCountries);
        final Adapter adapter = new Adapter(getContext());
        adapter.setCallback(country -> {
            dismiss();
            if(callback != null) callback.onPick(country);
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
