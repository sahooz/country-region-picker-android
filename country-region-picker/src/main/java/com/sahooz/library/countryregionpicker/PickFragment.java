package com.sahooz.library.countryregionpicker;

import android.annotation.SuppressLint;
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
 * Created by sahooz on 17/10/17.
 */

public class PickFragment extends DialogFragment {

    private final ArrayList<CountryOrRegion> allCountries = new ArrayList<>();
    private final ArrayList<CountryOrRegion> selectedCountries = new ArrayList<>();
    private PickCallback callback;

    public PickFragment() { }

    public static PickFragment newInstance(PickCallback callback) {
        PickFragment picker = new PickFragment();
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
        View root = inflater.inflate(R.layout.dialog_picker, container, false);
        EditText etSearch = root.findViewById(R.id.et_search);
        final RecyclerView rvCountryOrRegion = root.findViewById(R.id.rv_country_or_region);
        allCountries.clear();
        allCountries.addAll(CountryOrRegion.getAll());
        selectedCountries.clear();
        selectedCountries.addAll(allCountries);
        final Adapter adapter = new Adapter(getContext());
        adapter.setCallback(country -> {
            dismiss();
            if(callback != null) callback.onPick(country);
        });
        adapter.setSelectedCountries(selectedCountries);
        rvCountryOrRegion.setAdapter(adapter);
        rvCountryOrRegion.setLayoutManager(new LinearLayoutManager(getContext()));
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @SuppressLint("NotifyDataSetChanged")
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
                adapter.notifyDataSetChanged();
            }
        });
        return root;
    }
}
