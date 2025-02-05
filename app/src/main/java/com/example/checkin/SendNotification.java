package com.example.checkin;

// send notification to attendees using firebase messaging
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
  A Fragment responsible for sending notifications to selected events.
 */
public class SendNotification extends Fragment {

    private RequestQueue mrequest;
    private String URL2 = "https://fcm.googleapis.com/v1/projects/checkin-6a54e/messages:send";
    Button sendbtn;
    EditText titlemessage;
    EditText bodymessage;
    Button backbutton;
    Database d = new Database();
    Message m = new Message();
    String topic;
    Event myevent;
    String eventname;
    private FirebaseFirestore db;

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_notification, container, false);
        sendbtn = view.findViewById(R.id.sendmessagebtn);
        mrequest = Volley.newRequestQueue(getContext());
        titlemessage = view.findViewById(R.id.title_msg);
        bodymessage = view.findViewById(R.id.body_msg);
        backbutton = view.findViewById(R.id.backbtn);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            myevent = (Event) bundle.getSerializable("event");
            topic = myevent.getEventId();
            eventname = myevent.getEventname();
            titlemessage.setText(eventname);

        }

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                try {
                    sendNotification(topic);
                    String title = titlemessage.getText().toString();
                    String body = bodymessage.getText().toString();
                    m.setTitle(title);
                    m.setBody(body);
                    m.setEventid(myevent.getEventId());
                    m.setType("Message");
                    d.updateMessage(m);
                    titlemessage.setText("");
                    bodymessage.setText("");
                    Toast.makeText(getContext(), "Notification Sent", Toast.LENGTH_LONG).show();



                } catch (JSONException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        return view;
    }

    // https://infyom.com/blog/send-device-to-device-push-notification-using-firebase-cloud-messaging
    // https://firebase.google.com/docs/cloud-messaging/migrate-v1
    // https://www.youtube.com/watch?v=e9llz2TXBz8
    // https://www.youtube.com/watch?v=hKGLCdSEi9Y

    /**
     * Sends notification to attendees checked into an event
     * @param topic
     * @throws JSONException
     * @throws IOException
     */
    private void sendNotification(String topic) throws JSONException, IOException {
        // json object

        JSONObject notificationbody2 = new JSONObject();
        notificationbody2.put("title", titlemessage.getText().toString());
        notificationbody2.put("body", bodymessage.getText().toString());

        JSONObject message2 = new JSONObject();
        message2.put("topic", topic);
        message2.put("notification", notificationbody2);

        JSONObject mainObject2 = new JSONObject();
        mainObject2.put("message", message2);

        // https://stackoverflow.com/questions/13814503/reading-a-json-file-in-android
        String json = null;
        InputStream is = getContext().getAssets().open("checkin.json");

        int size = is.available();

        byte[] buffer = new byte[size];



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        json = new String(buffer, "UTF-8");

        // get api key
        String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
        String[] SCOPES = { MESSAGING_SCOPE };
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(is)
                .createScoped(Arrays.asList(SCOPES));
        googleCredentials.refresh();
        String token = googleCredentials.getAccessToken().getTokenValue();
        System.out.println(token);
        is.close();

        // create request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL2, mainObject2,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("NotificationSuccess", "Success ");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("NotificationError", "Error sending notification: " + error.toString());

            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json");
                header.put("Authorization", "Bearer "+ token);
                return header;
            }
        };
        // add request to send message 
        mrequest.add(request);

    }

}