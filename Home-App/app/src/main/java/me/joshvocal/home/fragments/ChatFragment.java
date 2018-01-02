package me.joshvocal.home.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import me.joshvocal.home.ChatMessage;
import me.joshvocal.home.ChatRecord;
import me.joshvocal.home.Config;
import me.joshvocal.home.R;

public class ChatFragment extends Fragment implements View.OnClickListener,
        AIListener,
        TextWatcher {

    public static final String TAG = ChatFragment.class.getName();

    private RecyclerView recyclerView;
    private EditText editText;
    private RelativeLayout addBtn;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<ChatMessage, ChatRecord> adapter;

    private AIService aiService;
    private AIRequest aiRequest;
    private AIDataService aiDataService;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        addBtn = rootView.findViewById(R.id.addBtn);
        addBtn.setOnClickListener(this);

        editText = rootView.findViewById(R.id.editText);
        editText.addTextChangedListener(this);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);

        final AIConfiguration config = new AIConfiguration(Config.ACCESS_TOKEN,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);


        aiService = AIService.getService(getContext(), config);
        aiService.setListener(this);

        aiDataService = new AIDataService(config);

        aiRequest = new AIRequest();

        adapter = new FirebaseRecyclerAdapter<ChatMessage, ChatRecord>(ChatMessage.class,
                R.layout.msglist,
                ChatRecord.class,
                databaseReference.child("chat")) {

            @Override
            protected void populateViewHolder(ChatRecord viewHolder, ChatMessage model, int position) {
                if (model.getMsgUser().equals("user")) {

                    viewHolder.getRightText().setText(model.getMsgText());
                    viewHolder.getRightText().setVisibility(View.VISIBLE);
                    viewHolder.getLeftText().setVisibility(View.GONE);
                } else {

                    viewHolder.getLeftText().setText(model.getMsgText());
                    viewHolder.getRightText().setVisibility(View.GONE);
                    viewHolder.getLeftText().setVisibility(View.VISIBLE);
                }
            }
        };

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                int msgCount = adapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if (lastVisiblePosition == -1 ||
                        (positionStart >= (msgCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);

                }

            }
        });

        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onResult(AIResponse response) {

        Result result = response.getResult();

        String message = result.getResolvedQuery();
        ChatMessage chatMessageUser = new ChatMessage(message, "user");
        databaseReference.child("chat").push().setValue(chatMessageUser);

        String reply = result.getFulfillment().getSpeech();
        ChatMessage chatMessageBot = new ChatMessage(reply, "bot");
        databaseReference.child("chat").push().setValue(chatMessageBot);
    }

    @Override
    public void onError(AIError error) {
        // Required Empty Method

    }

    @Override
    public void onAudioLevel(float level) {
        // Required Empty Method
    }

    @Override
    public void onListeningStarted() {
        // Required Empty Method
    }

    @Override
    public void onListeningCanceled() {
        // Required Empty Method
    }

    @Override
    public void onListeningFinished() {
        // Required Empty Method
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Required Empty Method
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Required Empty Method
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Required Empty Method
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onClick(View v) {

        String message = editText.getText().toString().trim();

        if (!message.isEmpty()) {

            ChatMessage chatMessage = new ChatMessage(message, "user");
            databaseReference.child("chat").push().setValue(chatMessage);

            aiRequest.setQuery(message);

            new AsyncTask<AIRequest, Void, AIResponse>() {

                @Override
                protected AIResponse doInBackground(AIRequest... aiRequests) {
                    final AIRequest request = aiRequests[0];

                    try {
                        final AIResponse response = aiDataService.request(aiRequest);

                        return response;
                    } catch (AIServiceException e) {

                    }

                    return null;
                }

                @Override
                protected void onPostExecute(AIResponse response) {
                    super.onPostExecute(response);

                    if (response != null) {

                        Result result = response.getResult();
                        String reply = result.getFulfillment().getSpeech();
                        ChatMessage chatMessage = new ChatMessage(reply, "bot");
                        databaseReference.child("chat").push().setValue(chatMessage);
                    }
                }
            }.execute(aiRequest);

        } else {
            aiService.startListening();
        }

        editText.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
