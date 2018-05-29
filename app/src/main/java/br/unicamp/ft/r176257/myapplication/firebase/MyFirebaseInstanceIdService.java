package br.unicamp.ft.r176257.myapplication.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private static final String QUIZ_ENGAGE_TOPIC = "quiz_engage";

    public void onTokenRefresh() {
        // Colocando o token recebido no logcat
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "FCM Token: " + token);
        // Logo que um novo token for gerado, vamos subscrever para um t´opico.
        FirebaseMessaging.getInstance()
                .subscribeToTopic(QUIZ_ENGAGE_TOPIC);
    }
}

