package com.noveogroup.highlightify.sample;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noveogroup.highlightify.Highlightify;

public class ContentFragment extends Fragment {

    private static final String ARG_HIGHLIGHTIFY = "arg_highlightify";

    public static ContentFragment newInstance(final Highlightify highlightify) {
        final ContentFragment fragment = new ContentFragment();
        final Bundle args = new Bundle();
        args.putSerializable(ARG_HIGHLIGHTIFY, highlightify);
        fragment.setArguments(args);
        return fragment;
    }


    public ContentFragment() {
        super();
    }

    private Highlightify highlightify;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args == null || !args.containsKey(ARG_HIGHLIGHTIFY)) {
            throw new IllegalArgumentException("You must supply Highlightify object via arguments.");
        }
        highlightify = (Highlightify) args.getSerializable(ARG_HIGHLIGHTIFY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        highlightify.highlightClickable(view);
        highlightify.highlightWithChildren(view.findViewById(R.id.group));
        final View image = view.findViewById(R.id.image);
        highlightify.highlight(image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.setActivated(!image.isActivated());
            }
        });
    }
}
