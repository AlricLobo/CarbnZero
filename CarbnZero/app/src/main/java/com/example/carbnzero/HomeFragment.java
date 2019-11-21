package com.example.carbnzero;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class HomeFragment extends Fragment {
    public static CustomGauge gauge3;

    public static TextView text1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_home,container,false);
        View v = inflater.inflate(R.layout.fragment_home,container,false);
        gauge3 = v.findViewById(R.id.gauge2);
        gauge3.setStartValue(0);
        gauge3.setEndValue(1000);
        text1 = v.findViewById(R.id.textView1);
    real_MainActivity.gaugeAnimate();
        return v;
    }




}
