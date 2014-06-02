package com.noveogroup.highlightify.sample;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import com.noveogroup.highlightify.Highlightify;
import com.noveogroup.highlightify.Target;


public class MainActivity extends Activity {

    private static final String STATE_HIGHLIGHTIFY = "state_highlightify";


    private Highlightify highlightify;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_HIGHLIGHTIFY, highlightify);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            // Use default set of targets and filter
            highlightify = new Highlightify();
            showFragment();
        } else {
            highlightify = (Highlightify) savedInstanceState.getSerializable(STATE_HIGHLIGHTIFY);
        }
    }

    private void showFragment() {
        final Fragment fragment = ContentFragment.newInstance(highlightify);
        final FragmentManager manager = getFragmentManager();
        final FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        manager.executePendingTransactions();
        transaction.commit();
    }

    private class Adapter extends ArrayAdapter<Target> {
        private Adapter() {
            super(MainActivity.this, -1, Target.values());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final CheckedTextView checkedTextView;
            if (convertView == null) {
                checkedTextView = new CheckedTextView(getContext());
            } else {
                checkedTextView = (CheckedTextView) convertView;
            }
            final Target target = getItem(position);
            checkedTextView.setText(target.toString());
            checkedTextView.setChecked(highlightify.getTargets().contains(target));
            return checkedTextView;
        }
    }
}
