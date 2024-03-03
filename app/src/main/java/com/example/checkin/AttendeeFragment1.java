package com.example.checkin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class AttendeeFragment1 extends Fragment {

    private ArrayList<Event> datalist;
    private ListView eventslist;
    private ArrayAdapter<Event> EventAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendee1, container, false);
        ListView eventslist = (ListView) view.findViewById(R.id.events);



        datalist = new ArrayList<>();
        ArrayList<Attendee> attendees1 = new ArrayList<>();
        attendees1.add(new Attendee("Amy"));
        datalist.add(new Event("Show", "Starts at 7", attendees1));

        // if eventlist is not null set EventAdapter to custom EventArrayAdapter
        if (datalist != null) {
            EventAdapter = new ArrayAdapter<Event>(getActivity(), R.layout.content, datalist) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = convertView;
                    if (view == null) {
                        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        view = inflater.inflate(R.layout.content, null);
                    }

                    TextView textView = view.findViewById(R.id.event_text);
                    textView.setText(datalist.get(position).getEventname());
                    return view;
                }
            };
            eventslist.setAdapter(EventAdapter);
        }

        eventslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EventDetail event_frag1= new EventDetail();
                Bundle args = new Bundle();
                args.putSerializable("event", datalist.get(i));
                event_frag1.setArguments(args);
                getParentFragmentManager().setFragmentResult("event",args);



                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.atten_view, event_frag1).commit();
                //getParentFragmentManager().beginTransaction().replace(R.id.attendeeviewfrag, event_frag1).addToBackStack(null).commit();


            }
        });
        return view;
    }
}