package me.joshvocal.home.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.joshvocal.home.R;

/**
 * Created by josh on 1/2/18.
 */

public class ChatRecord extends RecyclerView.ViewHolder {

    @BindView(R.id.leftText)
    TextView leftText;

    @BindView(R.id.rightText)
    TextView rightText;

    public ChatRecord(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public TextView getLeftText() {
        return leftText;
    }

    public TextView getRightText() {
        return rightText;
    }
}
