package com.noveogroup.highlightify.sample;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.noveogroup.highlightify.Highlightify;
import com.noveogroup.highlightify.Target;

import java.util.HashSet;
import java.util.Set;


public class MainActivity extends Activity {

    private static Highlightify highlightify = new Highlightify();
    private static int highlightColor = Color.BLACK;

    private ActionBarDrawerToggle drawerToggle;
    private ImageView actionColor;

    public Highlightify getHighlightify() {
        return highlightify;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listView = (ListView) findViewById(R.id.left_drawer);
        listView.setAdapter(new Adapter());
        final Set<Target> targets = highlightify.getTargets();
        for (int position = 0; position < listView.getCount(); position++) {
            final Object item = listView.getItemAtPosition(position);
            if (targets.contains(item)) {
                listView.setItemChecked(position, true);
            }
        }

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, 0, 0) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                final SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                final Set<Target> newTargets = new HashSet<Target>();
                for (int position = 0; position < checkedItems.size(); position++) {
                    if (checkedItems.get(position)) {
                        newTargets.add((Target) listView.getItemAtPosition(position));
                    }
                }
                final Set<Target> targets = highlightify.getTargets();
                // kludge to work around HashMap null key
                targets.remove(null);
                if (!targets.containsAll(newTargets) || !newTargets.containsAll(targets)) {
                    highlightify = new Highlightify.Builder()
                            .addTargets(highlightColor, newTargets.toArray(new Target[targets.size()]))
                            .build();
                    showFragment();
                }
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState == null) {
            showFragment();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.action_color);
        actionColor = (ImageView) menuItem.getActionView();
        actionColor.setImageDrawable(new ColorDrawable(highlightColor));
        actionColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() == R.id.action_color) {
            ColorPickerDialogFragment.newInstance(highlightColor).show(getFragmentManager(), null);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setHighlightColor(final int color) {
        highlightColor = color;
        actionColor.setImageDrawable(new ColorDrawable(color));
        final Set<Target> targetSet = highlightify.getTargets();
        final Target[] targets = targetSet.toArray(new Target[targetSet.size()]);
        highlightify = new Highlightify.Builder()
                .addTargets(color, targets)
                .build();
        showFragment();
    }

    private void showFragment() {
        final FragmentManager manager = getFragmentManager();
        manager.executePendingTransactions();
        final Fragment fragment = new ContentFragment();
        final FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }

    private class Adapter extends ArrayAdapter<Target> {
        private Adapter() {
            super(MainActivity.this, -1, Target.values());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final TextView textView;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_target, parent, false);
            }
            textView = (TextView) convertView;
            final Target target = getItem(position);
            textView.setText(target.toString());
            return textView;
        }
    }
}
