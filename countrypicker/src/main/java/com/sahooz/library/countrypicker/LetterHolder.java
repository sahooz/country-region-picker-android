package com.sahooz.library.countrypicker;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class LetterHolder extends RecyclerView.ViewHolder {
    public final TextView textView;
    public LetterHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView;
    }
}
