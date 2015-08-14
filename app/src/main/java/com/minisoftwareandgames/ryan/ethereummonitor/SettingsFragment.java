package com.minisoftwareandgames.ryan.ethereummonitor;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

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

        SharedPreferences prefs = getActivity().getSharedPreferences("EtherTicker", Context.MODE_PRIVATE);
        String exchange = prefs.getString("exchange", "GateCoin");
        String currency = prefs.getString("currency", "BTC");

        Spinner exchanges = (Spinner) view.findViewById(R.id.spinner_exchange);
        ArrayList<String> exchangeArray = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.exchange_array)));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.exchange_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        exchanges.setAdapter(adapter);
        exchanges.setOnItemSelectedListener(updateListener);
        exchanges.setSelection(exchangeArray.indexOf(currency));

        Spinner currencies = (Spinner) view.findViewById(R.id.spinner_currency);
        ArrayList<String> currencyArray = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.currency_array)));
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.currency_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        currencies.setAdapter(adapter);
        currencies.setOnItemSelectedListener(updateListener);
        currencies.setSelection(currencyArray.indexOf(currency));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new SettingsFragment(), "settings").addToBackStack(null).commit();
//            return true;
//        }
        if (id == R.id.action_refresh) {
            Intent intent = new Intent(getActivity(), EthereumAppWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
            // since it seems the onUpdate() is only fired on that:
            int[] ids = AppWidgetManager.getInstance(getActivity().getApplication()).getAppWidgetIds(new ComponentName(getActivity().getApplication(), EthereumAppWidgetProvider.class));
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            getActivity().sendBroadcast(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    AdapterView.OnItemSelectedListener updateListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            SharedPreferences.Editor editor = getActivity().getSharedPreferences("EtherTicker", Context.MODE_PRIVATE).edit();
            switch (parent.getId()) {
                case R.id.spinner_exchange:
                    editor.putString("exchange", parent.getSelectedItem().toString());
                    Log.d("exchange changed", parent.getSelectedItem().toString());
                    break;
                case R.id.spinner_currency:
                    editor.putString("currency", parent.getSelectedItem().toString());
                    Log.d("currency changed", parent.getSelectedItem().toString());
                    break;
            }
            editor.commit();
            Intent intent = new Intent(getActivity(), EthereumAppWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
            // since it seems the onUpdate() is only fired on that:
            int[] ids = AppWidgetManager.getInstance(getActivity().getApplication()).getAppWidgetIds(new ComponentName(getActivity().getApplication(), EthereumAppWidgetProvider.class));
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            getActivity().sendBroadcast(intent);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }
    };

}
