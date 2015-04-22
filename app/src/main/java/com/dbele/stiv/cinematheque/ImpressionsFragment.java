package com.dbele.stiv.cinematheque;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

public class ImpressionsFragment extends DialogFragment {

    private EditText etImpressions;
    private MovieFragment movieFragment;

    public static ImpressionsFragment newInstance(MovieFragment movieFragment) {
        ImpressionsFragment frag = new ImpressionsFragment();
        frag.movieFragment = movieFragment;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_impressions, container);
        etImpressions = (EditText) view.findViewById(R.id.tvImpressions);
        if (movieFragment.getMovieImpressions()!=null) {
            etImpressions.setText(movieFragment.getMovieImpressions());
        }
        getDialog().setTitle(R.string.insert_impressions);
        etImpressions.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        ImageView ivImpressionsDone = (ImageView) view.findViewById(R.id.ivImpressionsDone);
        ivImpressionsDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieFragment.setMovieImpressions(etImpressions.getText().toString().length() > 0 ? etImpressions.getText().toString() : null);
                getDialog().dismiss();
            }
        });
       return view;
    }

}