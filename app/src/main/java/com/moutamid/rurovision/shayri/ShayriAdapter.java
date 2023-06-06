package com.moutamid.rurovision.shayri;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class ShayriAdapter extends FragmentStatePagerAdapter {
    private ArrayList<String> data;
    FragmentManager fragmentManager;
    public ShayriAdapter(FragmentManager fragmentManager, ArrayList<String> formList) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
        this.data = formList;
    }

    @Override
    public Fragment getItem(int position) {
        FragShayriView fragment = new FragShayriView();
        Bundle bundle = new Bundle();
        bundle.putInt("CurrentPosition", position);
        bundle.putSerializable("CurrentShayri", data.get(position));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return data.size();
    }
}