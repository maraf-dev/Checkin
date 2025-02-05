package com.example.checkin;// ImageListFragment class

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

/**
This fragment class is responsible of showing and displaying two types of list
(using fragment_admin_image_browsing.xml layout) to the Administrator,
one being the list of total posters uploaded in the app
and the second one being the list of all the profile pictures used and uploaded in the app.
 */
public class ImageListFragment extends Fragment {

    // Creates an instance of Administrator class.
    private Administrator administrator;

    /**
     * Called when the fragment is being created.
     * This method initializes the Administrator object for managing image browsing.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state,
     *                           this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        administrator = new Administrator();
    }

    /**
     * Creates the view for the AdminImageBrowsingFragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState This fragment's previously saved state, if any.
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_image_browsing, container, false);

        // Get references to ListView widgets
        ListView usersProfileListView = view.findViewById(R.id.usersProfileListView);
        ListView postersListView = view.findViewById(R.id.postersListView);

        // Create ArrayAdapter for users' profile pictures
        ArrayAdapter<Image> usersProfileAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, administrator.getUsersProfilePictures());
        usersProfileListView.setAdapter(usersProfileAdapter);

        // Create ArrayAdapter for posters
        ArrayAdapter<Image> postersAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, administrator.getPosters());
        postersListView.setAdapter(postersAdapter);

        // Returns a fragment which will display 2 lists, one of profiles and the other one of posters.
        return view;
    }
}
