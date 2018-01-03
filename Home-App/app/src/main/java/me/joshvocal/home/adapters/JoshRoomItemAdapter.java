package me.joshvocal.home.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

import me.joshvocal.home.R;
import me.joshvocal.home.model.Switch;

/**
 * Created by josh on 12/29/17.
 */

public class JoshRoomItemAdapter extends RecyclerView.Adapter<JoshRoomItemAdapter.SwitchHolder> {

    private List<Switch> mSwitchList;
    private Context mContext;
    private DatabaseReference mDatabaseReference;

    public JoshRoomItemAdapter(Context context, List<Switch> switchList,
                               DatabaseReference databaseReference) {
        mContext = context;
        mSwitchList = switchList;
        mDatabaseReference = databaseReference;
    }

    @Override
    public SwitchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_switch, parent, false);
        return new SwitchHolder(view);
    }

    @Override
    public void onBindViewHolder(final SwitchHolder holder, int position) {
        final Switch currentSwitch = mSwitchList.get(position);

        holder.mSwitchName.setText(currentSwitch.getName());
        holder.mSwitch.setChecked(currentSwitch.getValue());
    }

    @Override
    public int getItemCount() {
        return mSwitchList.size();
    }

    public class SwitchHolder extends RecyclerView.ViewHolder
            implements CompoundButton.OnCheckedChangeListener {

        private TextView mSwitchName;
        private SwitchCompat mSwitch;

        public SwitchHolder(View itemView) {
            super(itemView);

            mSwitchName = itemView.findViewById(R.id.item_switch_name);
            mSwitch = itemView.findViewById(R.id.item_switch);

            mSwitch.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            final Switch currentSwitch = mSwitchList.get(getAdapterPosition());

            if (isChecked) {
                mSwitch.setChecked(true);
                mSwitchList.get(getAdapterPosition()).setValue(true);
                mDatabaseReference.child("BedroomLight").child("value").setValue(true);


            } else {
                mSwitch.setChecked(false);
                mSwitchList.get(getAdapterPosition()).setValue(false);
                mDatabaseReference.child("BedroomLight").child("value").setValue(false);
            }

            android.os.Handler handler = new android.os.Handler();
            final Runnable r = new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            };

            handler.post(r);

            Log.d("onCheckedChanged", Boolean.toString(mSwitchList.get(getAdapterPosition()).getValue()));
        }
    }
}
