package com.example.monopoly;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.monopoly.databinding.FragmentHomeBinding;
import com.example.monopoly.databinding.FragmentSettingsBinding;


public class SettingsFragment extends Fragment {

    private String[] shakeLevel = {"Very low", "Low", "Normal", "High", "Very high"};

    private FragmentSettingsBinding binding;

    public SettingsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        MainActivity parentActivity = (MainActivity) getActivity();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                parentActivity,
                android.R.layout.simple_list_item_1,
                shakeLevel);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(arrayAdapter);

        switch (MainActivity.SHAKE_LEVEL){
            case 6:
                binding.spinner.setSelection(0);
                break;
            case 8:
                binding.spinner.setSelection(1);
                break;
            case 10:
                binding.spinner.setSelection(2);
                break;
            case 12:
                binding.spinner.setSelection(3);
                break;
            case 14:
                binding.spinner.setSelection(4);
                break;
        }


        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        MainActivity.SHAKE_LEVEL = 6;
                        break;
                    case 1:
                        MainActivity.SHAKE_LEVEL = 8;
                        break;
                    case 2:
                        MainActivity.SHAKE_LEVEL = 10;
                        break;
                    case 3:
                        MainActivity.SHAKE_LEVEL = 12;
                        break;
                    case 4:
                        MainActivity.SHAKE_LEVEL = 14;
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return binding.getRoot();
    }
}