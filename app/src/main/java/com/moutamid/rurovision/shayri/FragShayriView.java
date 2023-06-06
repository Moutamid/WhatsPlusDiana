package com.moutamid.rurovision.shayri;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.moutamid.rurovision.R;


public class FragShayriView extends Fragment {

    TextView tvShayri;
    int _position;
    String strShayri;

    public FragShayriView() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mMainView = inflater.inflate(R.layout.fragment_frag_shayri_view, container, false);
        _position = getArguments().getInt("CurrentPosition");
        tvShayri = mMainView.findViewById(R.id.tvShayri);

        strShayri = getArguments().getString("CurrentShayri");

        tvShayri.setText(strShayri);
        return mMainView;
    }
}