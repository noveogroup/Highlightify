package com.noveogroup.highlightify.sample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.larswerkman.holocolorpicker.ColorPicker;

/**
 * Created on 6/25/14.
 */
public class ColorPickerDialogFragment extends DialogFragment {

    private static final String ARG_COLOR = "arg_color";

    public static ColorPickerDialogFragment newInstance(final int color) {
        final ColorPickerDialogFragment fragment = new ColorPickerDialogFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_COLOR, color);
        fragment.setArguments(args);
        return fragment;
    }


    private int color;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);

        final Bundle args = getArguments();
        if (args == null || !args.containsKey(ARG_COLOR)) {
            throw new IllegalArgumentException();
        }
        color = args.getInt(ARG_COLOR);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ColorPicker colorPicker = new ColorPicker(getActivity());
        colorPicker.setOldCenterColor(color);
        colorPicker.setNewCenterColor(color);
        return new AlertDialog.Builder(getActivity())
                .setView(colorPicker)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((MainActivity) getActivity()).setHighlightColor(colorPicker.getColor());
                            }
                        }
                )
                .create();
    }

}
