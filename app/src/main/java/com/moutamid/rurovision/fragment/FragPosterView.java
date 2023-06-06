package com.moutamid.rurovision.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.moutamid.rurovision.R;


public class FragPosterView extends Fragment {

    ImageView ivPoster;
    int currPos;
    int[] myImageList = new int[]{R.drawable.vp_first, R.drawable.vp_second, R.drawable.vp_third, R.drawable.vp_fourth, R.drawable.vp_fifth,R.drawable.vp_sixth, R.drawable.vp_seventh};

    public FragPosterView() {
    }

    public static FragPosterView newInstance(String param1, String param2) {
        FragPosterView fragment = new FragPosterView();
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_frag_poster_view, container, false);

        assert getArguments() != null;
        currPos = getArguments().getInt("CurrentPosition");
        ivPoster = view.findViewById(R.id.ivPoster);
        ivPoster.setImageResource(myImageList[currPos]);
        return view;
    }
}