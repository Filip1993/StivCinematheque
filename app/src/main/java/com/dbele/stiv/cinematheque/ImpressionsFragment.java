package com.dbele.stiv.cinematheque;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by dbele on 4/15/2015.
 */
public class ImpressionsFragment extends DialogFragment {

    private EditText tvImpressions;
    private ImageView ivImpressionsDone;
    private MovieFragment movieFragment;

    public static ImpressionsFragment newInstance(MovieFragment movieFragment) {
        ImpressionsFragment frag = new ImpressionsFragment();
        frag.movieFragment = movieFragment;
        //frag.setStyle(DialogFragment.STYLE_, R.style.AppTheme_PopupMenu);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_impressions, container);
        tvImpressions = (EditText) view.findViewById(R.id.tvImpressions);
        if (movieFragment.getMovieImpressions()!=null) {
            tvImpressions.setText(movieFragment.getMovieImpressions());
        }
        getDialog().setTitle(R.string.insert_impressions);
        tvImpressions.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        ivImpressionsDone = (ImageView) view.findViewById(R.id.ivImpressionsDone);
        ivImpressionsDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieFragment.setMovieImpressions(tvImpressions.getText().toString());
                getDialog().dismiss();
            }
        });

        return view;
    }

}