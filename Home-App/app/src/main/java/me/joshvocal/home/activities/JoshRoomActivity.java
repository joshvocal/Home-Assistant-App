package me.joshvocal.home.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.joshvocal.home.adapters.RoomSwitchItemAdapter;
import me.joshvocal.home.R;
import me.joshvocal.home.model.Room;
import me.joshvocal.home.model.Switch;
import me.joshvocal.home.utils.Utils;

public class JoshRoomActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view_josh_room)
    RecyclerView mSwitchRecyclerView;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private RoomSwitchItemAdapter mAdapter;
    private Room joshRoom;
    private List<String> switchKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_josh_room);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Utils.setBackButton(this.getSupportActionBar());

        mSwitchRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mSwitchRecyclerView.setLayoutManager(layoutManager);
        mSwitchRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSwitchRecyclerView.addItemDecoration(
                new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        joshRoom = new Room();
        switchKeys = new ArrayList<>();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("JoshsRoom");

        mAdapter = new RoomSwitchItemAdapter(this, joshRoom.getSwitchList(), mDatabaseReference);

        getFirebaseData();
    }

    private void getFirebaseData() {
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Switch switchValue = dataSnapshot.getValue(Switch.class);
                joshRoom.getSwitchList().add(switchValue);

                String switchKey = dataSnapshot.getKey();
                switchKeys.add(switchKey);

                mSwitchRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                int index = switchKeys.indexOf(key);
                Switch switchData = dataSnapshot.getValue(Switch.class);

                for (Switch switchIndex : joshRoom.getSwitchList()) {
                    if (switchIndex.getName().equals(key)) {
                        switchIndex.setValues(switchData);
                    }
                }

                joshRoom.getSwitchList().set(index, switchData);

                mSwitchRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Required Empty Method
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // Required Empty Method
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Required Empty Method
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
