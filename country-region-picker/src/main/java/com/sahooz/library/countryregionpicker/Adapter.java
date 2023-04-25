package com.sahooz.library.countryregionpicker;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<VH> {

    private ArrayList<CountryOrRegion> selectedCountries = new ArrayList<>();
    private final LayoutInflater inflater;
    private PickCallback callback = null;
    private final Context context;
    public Adapter(Context ctx) {
        inflater = LayoutInflater.from(ctx);
        context = ctx;
    }

    public void setSelectedCountries(ArrayList<CountryOrRegion> selectedCountries) {
        this.selectedCountries = selectedCountries;
        notifyDataSetChanged();
    }

    public void setCallback(PickCallback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflater.inflate(R.layout.item_country_region, parent, false));
    }

    private int itemHeight = -1;
    public void setItemHeight(float dp) {
        itemHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp, context.getResources().getDisplayMetrics());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(VH holder, int position) {
        final CountryOrRegion countryOrRegion = selectedCountries.get(position);
        holder.ivFlag.setImageResource(countryOrRegion.flag);
        holder.tvName.setText(countryOrRegion.translate);
        holder.tvCode.setText("+" + countryOrRegion.code);
        if(itemHeight != -1) {
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            params.height = itemHeight;
            holder.itemView.setLayoutParams(params);
        }
        holder.itemView.setOnClickListener(v -> {
            if(callback != null) callback.onPick(countryOrRegion);
        });
    }

    @Override
    public int getItemCount() {
        return selectedCountries.size();
    }

}
