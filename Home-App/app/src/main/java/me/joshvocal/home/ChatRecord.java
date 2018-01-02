package me.joshvocal.home;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by josh on 1/2/18.
 */

public class ChatRecord extends RecyclerView.ViewHolder {

    private TextView leftText;
    private TextView rightText;

    public ChatRecord(View itemView) {
        super(itemView);

        leftText = itemView.findViewById(R.id.leftText);
        rightText = itemView.findViewById(R.id.rightText);
    }

    public TextView getLeftText() {
        return leftText;
    }

    public TextView getRightText() {
        return rightText;
    }
}
