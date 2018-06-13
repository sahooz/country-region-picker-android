package com.sahooz.library;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class LetterHolder extends RecyclerView.ViewHolder {
    public final TextView textView;
    public LetterHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView;
    }
}
