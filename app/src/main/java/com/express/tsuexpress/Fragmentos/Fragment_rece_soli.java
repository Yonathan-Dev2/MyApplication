package com.express.tsuexpress.Fragmentos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.express.tsuexpress.R;

/**
 * A simple {@link Fragment} subclass.
 */



public class Fragment_rece_soli extends Fragment {

    View vista;
    Fragment_rece_soli contexto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        contexto = this;

        vista = inflater.inflate(R.layout.fragment_rece_soli, container, false);


        return  vista;
    }

}
