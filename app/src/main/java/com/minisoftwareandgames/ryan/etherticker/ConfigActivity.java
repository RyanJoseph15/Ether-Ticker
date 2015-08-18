package com.minisoftwareandgames.ryan.etherticker;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.minisoftwareandgames.ryan.etherticker.objects.Widget;

/**
 * Created by ryan on 8/14/15.
 */
public class ConfigActivity extends AppCompatActivity {

	public Widget widget = null;
	public SQLiteHelper helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		helper = new SQLiteHelper(this);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			int widgetId = extras.getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			widget = helper.getWidget(widgetId);
		}
		getSupportFragmentManager().beginTransaction().add(R.id.container, new SettingsFragment()).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public void onDestroy() {
		helper.tickerDB.close();
		super.onDestroy();
	}

}
