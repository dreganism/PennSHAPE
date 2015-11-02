package com.pennshape.app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pennshape.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PSChatMessageFragmentTab extends Fragment {


    public PSChatMessageFragmentTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.ps_fragment_chat_message, container, false);
    }


}
