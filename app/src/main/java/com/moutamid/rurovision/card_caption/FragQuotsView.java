package com.moutamid.rurovision.card_caption;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.moutamid.rurovision.R;


public class FragQuotsView extends Fragment {

    TextView tvCaption;
    TextView tvAuthor;
    int _position;
    String strQuots;
    String strAuthor;

    public FragQuotsView() {
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
        View mMainView = inflater.inflate(R.layout.fragment_frag_quots_view, container, false);
        _position = getArguments().getInt("CurrentPosition");
        tvCaption = mMainView.findViewById(R.id.tvCaption);
        tvAuthor = mMainView.findViewById(R.id.tvAuthor);

        strQuots = getArguments().getString("CurrentQuots");
        strAuthor = getArguments().getString("CurrentAuthor");

        tvCaption.setText(strQuots);
        tvAuthor.setText(strAuthor);
        return mMainView;
    }
}