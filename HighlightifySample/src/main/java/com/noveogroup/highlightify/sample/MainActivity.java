package com.noveogroup.highlightify.sample;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.noveogroup.highlightify.Highlightify;
import com.noveogroup.highlightify.Target;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Highlightify highlightify = new Highlightify.Builder()
                .addTargets(Color.BLACK, Target.Text, Target.Background, Target.Image, Target.Compound, Target.ImageBackground)
                .build();
        highlightify.highlightClickable(getWindow().getDecorView());
    }
}
