package com.minisoftwareandgames.ryan.etherticker;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RemoteViews;
import android.widget.Spinner;

import com.minisoftwareandgames.ryan.etherticker.objects.Widget;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ryan on 8/11/15.
 */
public class SettingsFragment extends Fragment {

    private SQLiteHelper helper;
    private Widget widget;

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
        View view = inflater.inflate(R.layout.fragment_configuration, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        helper = ((ConfigActivity) getActivity()).helper;
        if (helper != null && helper.hasWidgets()) {
            widget = ((ConfigActivity) getActivity()).widget;
            if (widget != null) {
                Log.d("etherticker", "settingFragment: " + widget.id);
                String exchange = widget.exchange;
                String currency = widget.currency;
//                Log.d("etherticker", "WIDGET -> exchange: " + exchange + ", currency: " + currency);

                Spinner exchanges = (Spinner) view.findViewById(R.id.spinner_exchange);
                ArrayList<String> exchangeArray = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.exchange_array)));
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.exchange_array, R.layout.spinner_item);
                adapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
                exchanges.setAdapter(adapter);
                exchanges.setOnItemSelectedListener(updateListener);
                exchanges.setSelection(exchangeArray.indexOf(exchange));

                Spinner currencies = (Spinner) view.findViewById(R.id.spinner_currency);
                ArrayList<String> currencyArray = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.currency_array)));
                adapter = ArrayAdapter.createFromResource(getActivity(), R.array.currency_array, R.layout.spinner_item);
                adapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
                currencies.setAdapter(adapter);
                currencies.setOnItemSelectedListener(updateListener);
                currencies.setSelection(currencyArray.indexOf(currency));
            }
        } else {
            /* no active widgets */
//            Log.d("etherticker", "no widget or null helper");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            Intent intent = new Intent(getActivity(), EtherTickerWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            int[] ids = AppWidgetManager.getInstance(getActivity().getApplication()).getAppWidgetIds(new ComponentName(getActivity().getApplication(), EtherTickerWidgetProvider.class));
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            getActivity().sendBroadcast(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    AdapterView.OnItemSelectedListener updateListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getId()) {
                case R.id.spinner_exchange:
                    widget.exchange = parent.getSelectedItem().toString();
//                    Log.d("exchange changed", parent.getSelectedItem().toString());
                    break;
                case R.id.spinner_currency:
                    widget.currency = parent.getSelectedItem().toString();
//                    Log.d("currency changed", parent.getSelectedItem().toString());
                    break;
            }
            helper.updateWidget(widget);
            Log.d("etherticker", "onItemSelected -> id: " + widget.id);
            Intent intent = new Intent(getActivity(), EtherTickerWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
            // since it seems the onUpdate() is only fired on that:
            int[] ids = AppWidgetManager.getInstance(getActivity().getApplication()).getAppWidgetIds(new ComponentName(getActivity().getApplication(), EtherTickerWidgetProvider.class));
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            getActivity().sendBroadcast(intent);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }
    };

}