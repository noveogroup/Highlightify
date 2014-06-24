package com.noveogroup.highlightify.sample;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noveogroup.highlightify.Highlightify;

public class ContentFragment extends Fragment {

    public ContentFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Highlightify highlightify = ((MainActivity) getActivity()).getHighlightify();

        // Highlight all clickable widgets recursively
        highlightify.highlightClickable(view);

        // Highlight each view in hierarchy
        highlightify.highlightWithChildren(view.findViewById(R.id.group));

        final View image = view.findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.setActivated(!image.isActivated());
            }
        });
        // Highlight specific widget
        highlightify.highlight(image);
    }
}
