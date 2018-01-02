package me.joshvocal.home.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

import ai.api.android.AIConfiguration;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.model.Status;
import ai.api.ui.AIButton;
import me.joshvocal.home.Config;
import me.joshvocal.home.R;
import me.joshvocal.home.TTS;

public class VoiceFragment extends Fragment implements AIButton.AIButtonListener {

    public static final String TAG = VoiceFragment.class.getName();

    private AIButton aiButton;
    private TextView resultTextView;

    public VoiceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_voice, container, false);

        resultTextView = rootView.findViewById(R.id.resultTextView);
        aiButton = rootView.findViewById(R.id.micButton);

        final AIConfiguration config = new AIConfiguration(Config.ACCESS_TOKEN,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        config.setRecognizerStartSound(getResources().openRawResourceFd(R.raw.test_start));
        config.setRecognizerStopSound(getResources().openRawResourceFd(R.raw.test_stop));
        config.setRecognizerCancelSound(getResources().openRawResourceFd(R.raw.test_cancel));

        aiButton.initialize(config);
        aiButton.setResultsListener(this);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

        // Use this method ot disconnect form speech recognition service
        // Not destroying the SpeechRecognition object in onPause would block other apps
        // from using SpeechRecognition service.
        aiButton.pause();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Use this method to reinit connection to recognition service
        aiButton.resume();
    }

    @Override
    public void onResult(final AIResponse response) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                resultTextView.setText(response.getResult()
                        .getFulfillment()
                        .getSpeech());

                final String speech = response.getResult()
                        .getFulfillment()
                        .getSpeech();
                TTS.speak(speech);
            }
        });
    }

    @Override
    public void onError(final AIError error) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onError");
                resultTextView.setText(error.toString());
            }
        });
    }

    @Override
    public void onCancelled() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onCancelled");
                resultTextView.setText("");
            }
        });
    }
}
