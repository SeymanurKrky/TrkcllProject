package com.androidtutorialshub.loginregister.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidtutorialshub.loginregister.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigaMainFragment extends Fragment {


    public NavigaMainFragment() {
        // Required empty public constructor
    }

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_naviga_main, container, false);
        return v;
    }

}
