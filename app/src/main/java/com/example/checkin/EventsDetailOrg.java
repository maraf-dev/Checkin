package com.example.checkin;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

// Shows event information for an organizer
public class EventsDetailOrg extends Fragment {
    Button attendeelistbutton;
    Button qrcodebutton;
    Event myevent;
    EditText eventnametxt;
    EditText eventdetails;
    Button backbutton;

    Button posterbutton;

    Button detailscodebutton;
    EditText eventDate;
    EditText eventTime;
    EditText eventlocation;

    Button savebutton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_events_detail_org, container, false);
        // Inflate the layout for this fragment
        attendeelistbutton = (Button) view.findViewById(R.id.attendeeslistbtn);
        qrcodebutton = (Button) view.findViewById(R.id.codebtn);
        backbutton = view.findViewById(R.id.backbtn);
        eventnametxt = view.findViewById(R.id.eventname_text);
        eventdetails = view.findViewById(R.id.eventdetails_txt);
        detailscodebutton = view.findViewById(R.id.detailscode);
        posterbutton = view.findViewById(R.id.posterbtn);
        eventDate = view.findViewById(R.id.Eteventdate);
        eventTime = view.findViewById(R.id.Eteventtime);
        eventlocation = view.findViewById(R.id.Eteventlocation);
        savebutton = view.findViewById(R.id.savebtn);

        // get event object from previous fragment
        Bundle bundle = this.getArguments();
        if (bundle!=null) {
            myevent = (Event) bundle.getSerializable("event");
        }



        String time = "Time: " + myevent.getEventTime();
        String date = "Date: " + myevent.getEventDate();
        String location = "Location: " + myevent.getLocation();

        eventnametxt.setText(myevent.getEventname());
        eventdetails.setText(myevent.getEventDetails());
        eventDate.setText(date);
        eventTime.setText(time);
        eventlocation.setText(location);



        if (myevent.getPoster().equals("")){
            //no poster for this event
            //posterbutton.setError(String.format("%s has no poster.", myevent.getEventname()));
            posterbutton.setText("No Poster Available");
        }

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eventName = eventnametxt.getText().toString().trim();
                String eventDateStr = eventDate.getText().toString().trim();
                String eventTimeStr = eventTime.getText().toString().trim();
                String eventDetailsStr = eventdetails.getText().toString().trim();
                String eventlocationStr = eventlocation.getText().toString().trim();

                myevent.setEventDate(eventDateStr);
                myevent.setEventTime(eventTimeStr);
                myevent.setEventDetails(eventDetailsStr);
                myevent.setLocation(eventlocationStr);
                myevent.setEventname(eventName);
                Toast.makeText(getContext(), "Details Saved!", Toast.LENGTH_LONG).show();
            }
        });

        detailscodebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PromotionQrShare sharefrag = new PromotionQrShare();
                Bundle args = new Bundle();
                args.putSerializable("event", myevent);
                sharefrag.setArguments(args);
                getParentFragmentManager().setFragmentResult("event",args);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.org_view, sharefrag).addToBackStack(null).commit();

            }
        });


        // move back to pevious fragment when clicked
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // move to fragment where qr code is displayed
        qrcodebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareCode code_frag = new ShareCode();
                Bundle args = new Bundle();
                args.putSerializable("event", myevent);
                code_frag.setArguments(args);
                getParentFragmentManager().setFragmentResult("event",args);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.org_view, code_frag).addToBackStack(null).commit();


            }
        });

        // move to fragment that shows attendees list options
        attendeelistbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add attendees list fragment
                AttendeesOptions list_frag = new AttendeesOptions();

                Bundle args = new Bundle();
                args.putSerializable("event", myevent);
                list_frag.setArguments(args);
                getParentFragmentManager().setFragmentResult("event",args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.org_view, list_frag).addToBackStack(null).commit();

            }
        });

        //move to poster fragment
        posterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myevent.getPoster().equals("")){
                    //no poster for this event
                    //posterbutton.setError(String.format("%s has no poster.", myevent.getEventname()));
                    return;
                }

                //Create fragment
                EventPosterFrag posterShareFrag = new EventPosterFrag();

                UserImage poster = new UserImage();
                poster.setImageB64(myevent.getPoster());
                poster.setID(myevent.getEventId());

                Bundle args = new Bundle();
                args.putSerializable("Poster", poster);

                posterShareFrag.setArguments(args);
                //ShareCode code_frag = new ShareCode();
                //Bundle args = new Bundle();
                //args.putSerializable("event", myevent);
                //code_frag.setArguments(args);
                getParentFragmentManager().setFragmentResult("Poster",args);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.org_view, posterShareFrag).addToBackStack(null).commit();



                //getActivity().getSupportFragmentManager().popBackStack();
            }
        });



        return view;


    }
}