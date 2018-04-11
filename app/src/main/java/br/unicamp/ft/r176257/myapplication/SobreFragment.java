package br.unicamp.ft.r176257.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SobreFragment extends Fragment {

    public SobreFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View lview = inflater.inflate(R.layout.sobre_fragment, container, false);

        // Inflate the layout for this fragment
        return lview;
    }
}
