package com.sahooz.library.countrypicker;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

class VH extends RecyclerView.ViewHolder {

    TextView tvName;
    TextView tvCode;
    ImageView ivFlag;

    VH(View itemView) {
        super(itemView);
        ivFlag = itemView.findViewById(R.id.iv_flag);
        tvName = itemView.findViewById(R.id.tv_name);
        tvCode = itemView.findViewById(R.id.tv_code);
    }
}
