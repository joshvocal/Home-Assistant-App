package me.joshvocal.home.activities;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import me.joshvocal.home.model.ChatMessage;
import me.joshvocal.home.adapters.ChatRecord;
import me.joshvocal.home.R;
import me.joshvocal.home.utils.Utils;

public class ChatActivity extends AppCompatActivity
        implements View.OnClickListener,
        AIListener, TextWatcher {

    @BindView(R.id.recycler_view_chat)
    RecyclerView chatRecyclerView;

    @BindView(R.id.editText)
    EditText chatEditText;

    @BindView(R.id.sendMessageButton)
    RelativeLayout sendMessageButton;

    public static final String TAG = ChatActivity.class.getName();

    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<ChatMessage, ChatRecord> firebaseAdapter;

    private AIService aiService;
    private AIRequest aiRequest;
    private AIDataService aiDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        setTitle(R.string.title_chat);

        Utils.setBackButton(this.getSupportActionBar());

        chatEditText.addTextChangedListener(this);

        sendMessageButton.setOnClickListener(this);

        chatRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(linearLayoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);

        final AIConfiguration config = new AIConfiguration(getString(R.string.dialogflow_access_token),
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        aiDataService = new AIDataService(config);

        aiRequest = new AIRequest();

        firebaseAdapter = new FirebaseRecyclerAdapter<ChatMessage, ChatRecord>(ChatMessage.class,
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

        firebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                int msgCount = firebaseAdapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if (lastVisiblePosition == -1 ||
                        (positionStart >= (msgCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    chatRecyclerView.scrollToPosition(positionStart);

                }

            }
        });

        chatRecyclerView.setAdapter(firebaseAdapter);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onClick(View v) {

        String message = chatEditText.getText().toString().trim();

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

        chatEditText.setText("");
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
