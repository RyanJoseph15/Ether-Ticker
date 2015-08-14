package com.minisoftwareandgames.ryan.ethereummonitor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by ryan on 8/11/15.
 */
public class SettingsFragment extends Fragment {

    public SettingsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.action_settings).setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner exchanges = (Spinner) view.findViewById(R.id.spinner_exchange);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.exchange_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        exchanges.setAdapter(adapter);

        Spinner currencies = (Spinner) view.findViewById(R.id.spinner_currency);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.currency_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        currencies.setAdapter(adapter);
    }

}
