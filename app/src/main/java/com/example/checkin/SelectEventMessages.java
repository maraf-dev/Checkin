package com.example.checkin;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

/**
 Displays events to select and send notifications to
 */
public class SelectEventMessages extends Fragment {

    private ArrayList<Event> datalist;
    private ListView eventslist;
    private ArrayAdapter<Event> EventAdapter;
    private EventList allevents;
    Button backbutton;

    Organizer organizer;

    private FirebaseFirestore db;

    ProgressBar p;

    RelativeLayout maincontent;

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
        View view = inflater.inflate(R.layout.fragment_select_event_messages, container, false);
        ListView eventslist = (ListView) view.findViewById(R.id.events);
        backbutton = view.findViewById(R.id.backbtn);
        p = view.findViewById(R.id.progress);
        maincontent = view.findViewById(R.id.maincontent);


        allevents = new EventList();

        db = FirebaseFirestore.getInstance();
        Database database = new Database();
        String android_id = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        // retrieve organizer's events from firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference organizerRef = db.collection("Organizers").document(android_id);
        organizerRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Convert the document snapshot to an Organizer object using Database class method
                        organizer = database.getOrganizer(document);
                        // Proceed with setting up the UI using the retrieved organizer object
                    } else {
                        Log.d("document", "No such document");
                    }
                } else {
                    Log.d("error", "get failed with ", task.getException());
                }
            }
        });


        // Query events collection based on organizer ID
        db.collection("Events")
                .whereEqualTo("Creator", android_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        p.setVisibility(View.GONE);
                        maincontent.setVisibility(View.VISIBLE);
                        if (task.isSuccessful()) {

                            for (DocumentSnapshot document : task.getResult()) {
                                Event event = database.getEvent(document);
                                allevents.addEvent(event);
                            }
                            EventAdapter = new ArrayAdapter<Event>(getActivity(), R.layout.content, allevents.getEvents()) {
                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    View view = convertView;
                                    if (view == null) {
                                        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        view = inflater.inflate(R.layout.content, null);
                                    }

                                    TextView textView = view.findViewById(R.id.event_text);
                                    textView.setText(allevents.getEvents().get(position).getEventname());
                                    return view;
                                }
                            };
                            eventslist.setAdapter(EventAdapter);


                        }}
                });



        // move back to previous fragment when clicked
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // if event list is not null, then set eventlist
        if (allevents.getEvents() != null) {
            EventAdapter = new ArrayAdapter<Event>(getActivity(), R.layout.content, allevents.getEvents()) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = convertView;
                    if (view == null) {
                        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        view = inflater.inflate(R.layout.content, null);
                    }

                    TextView textView = view.findViewById(R.id.event_text);
                    textView.setText(allevents.getEvents().get(position).getEventname());
                    return view;
                }
            };
            eventslist.setAdapter(EventAdapter);
        }

        // move to send notfications fragment when an event is selected
        eventslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SendNotification send_frag = new SendNotification();
                Bundle args = new Bundle();
                args.putSerializable("event", allevents.getEvents().get(i));
                send_frag.setArguments(args);
                getParentFragmentManager().setFragmentResult("event",args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.org_view, send_frag).addToBackStack(null).commit();


            }
        });
        return view;
    }

}
