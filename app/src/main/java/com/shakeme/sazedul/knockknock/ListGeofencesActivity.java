package com.shakeme.sazedul.knockknock;

/**
 * Activity Class for showing the added geofences whether they are expired or not
 * Created by Sazedul on 06-Dec-14.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class ListGeofencesActivity extends Activity
        implements ListView.OnItemLongClickListener,
        ListView.OnItemClickListener {

    Map<Integer, String> map;

    ListView mListGeofence;
    // Persistent storage for geofences
    private SimpleGeofenceStore mGeofenceStorage;
    private int checkedCount;
    private boolean[] checkedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_geofences);
        Vector<String> list = new Vector<>();
        mGeofenceStorage = new SimpleGeofenceStore(this, Context.MODE_APPEND);
        map = new HashMap<>();
        checkedCount = 0;

        mListGeofence = (ListView) findViewById(R.id.geofence_list);
        for (int i=0; i<GeofenceUtils.MAX_ID; i++) {
            SimpleGeofence geofence = mGeofenceStorage.getGeofence(Integer.toString(i));
            if (geofence != null) {
                list.add(geofence.getName());
                map.put(i, geofence.getId());
                MapsActivity.setAsActiveGeofence(Integer.parseInt(geofence.getId()));
            }
        }
        checkedItem = new boolean[list.size()];

        //for (int i=0; i<list.size(); i++) System.err.println("FUCKING CHECKED "+checkedItem[i]);

        final ArrayAdapter<String> listGeofenceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, list);
        mListGeofence.setAdapter(listGeofenceAdapter);
        mListGeofence.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        mListGeofence.setOnItemClickListener(this);
        mListGeofence.setOnItemLongClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_geofences, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            Intent intentReminder = new Intent();
            intentReminder.putExtra("GeofenceDelete", "Data successfully acquired.");

            String checkedPosition[] = new String[checkedCount];

            for (int i=0; i<checkedCount; i++) {
                if (checkedItem[i]) {
                    checkedPosition[i] = map.get(i);
                }
            }
            intentReminder.putExtra("GeofenceIds", checkedPosition);
            setResult(RESULT_OK, intentReminder);
            finish();
            return true;
        }
        else if (id == R.id.action_clear) {
            Intent intentReminder = new Intent();
            intentReminder.putExtra("GeofenceClear", "Clear All");
            setResult(RESULT_OK, intentReminder);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.err.println("POSITION IS "+position);
        System.err.println("ID IS "+id);
        if (checkedCount > 0) {
            if (checkedItem[position]) {
                mListGeofence.setItemChecked(position, false);
                checkedCount--;
                checkedItem[position] = false;
                System.err.println("ITEM IS "+mListGeofence.isItemChecked(position));
                System.err.println("CHECKED COUNT IS "+checkedCount);
            } else {
                mListGeofence.setItemChecked(position, true);
                checkedCount++;
                checkedItem[position] = true;
                System.err.println("ITEM IS "+mListGeofence.isItemChecked(position));
                System.err.println("CHECKED COUNT IS "+checkedCount);
            }
        } else {
            Intent intentReminder = new Intent();
            intentReminder.putExtra("GeofenceShow", "Data successfully acquired.");

            intentReminder.putExtra("GeofenceId", map.get(position));
            setResult(RESULT_OK, intentReminder);
            finish();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (checkedItem[position]) {
            mListGeofence.setItemChecked(position, false);
            checkedCount--;
            checkedItem[position] = false;
            System.err.println("ITEM IS "+mListGeofence.isItemChecked(position));
            System.err.println("CHECKED COUNT IS "+checkedCount);
        } else {
            mListGeofence.setItemChecked(position, true);
            checkedCount++;
            checkedItem[position] = true;
            System.err.println("ITEM IS "+mListGeofence.isItemChecked(position));
            System.err.println("CHECKED COUNT IS "+checkedCount);
        }
        return true;
    }

    public void getBackToTheParentActivity(View view) {
        finish();
    }

    public void deleteSelected(View view) {
            Intent intentReminder = new Intent();
            intentReminder.putExtra("GeofenceDelete", "Data successfully acquired.");

            String checkedPosition[] = new String[checkedCount];

            for (int i=0; i<checkedCount; i++) {
                if (checkedItem[i]) {
                    checkedPosition[i] = map.get(i);
                }
            }
            intentReminder.putExtra("GeofenceIds", checkedPosition);
            setResult(RESULT_OK, intentReminder);
            finish();
    }

    public void clearAll(View view) {
        Intent intentReminder = new Intent();
        intentReminder.putExtra("GeofenceClear", "Clear All");
        setResult(RESULT_OK, intentReminder);
        finish();
    }
}
