package com.dbele.stiv.cinematheque;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dbele.stiv.model.Movie;
import com.dbele.stiv.persistence.MovieDatabaseHelper;
import com.dbele.stiv.persistence.MoviesContentProvider;
import com.dbele.stiv.utitlities.Utility;

import java.util.Calendar;
import java.util.Date;


public class MovieFragment extends Fragment {

    public static final String EXTRA_MOVIE_ID = "com.dbele.stiv.cinematheque.extra.movie.id";

    private Movie movie;
    private CheckBox cbWatched;
    private TextView tvMovieName;
    private TextView tvMovieGenre;
    private TextView tvMovieDesc;
    private TextView tvMovieDirector;
    private TextView tvMovieActors;
    private ImageView ivMoviePic;
    private ImageView ivWatchedDate;
    private TextView tvWatchedDate;
    private ImageView ivImpressions;
    private TextView tvImpressions;
    private LinearLayout llPersonalDetails;


    public static MovieFragment createMovieFragment(Movie movie) {
        MovieFragment movieFragment = new MovieFragment();
        movieFragment.movie = movie;
        return movieFragment;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        ViewGroup viewGroup = (ViewGroup) getView();
        viewGroup.removeAllViewsInLayout();
        View view = onCreateView(inflater, viewGroup, null);
        viewGroup.addView(view);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        llPersonalDetails = (LinearLayout) view.findViewById(R.id.llPersonalDetails);
        fillData(view);
        setupListeners(view);
        return view;
    }

    private void setupListeners(View view) {
        ivWatchedDate = (ImageView) view.findViewById(R.id.ivWatchedDate);
        ivWatchedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        tvWatchedDate = (TextView) view.findViewById(R.id.tvDateWatched);
        tvWatchedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        ivImpressions = (ImageView) view.findViewById(R.id.ivImpressions);
        ivImpressions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImpressionsDialog();
            }
        });
        tvImpressions = (TextView) view.findViewById(R.id.tvImpressions);
        tvImpressions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImpressionsDialog();
            }
        });

    }

    private void showImpressionsDialog() {
        ImpressionsFragment impressionsFragment = ImpressionsFragment.newInstance(this);
        impressionsFragment.show(getFragmentManager(), null);
    }

    private void showDatePicker() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        Calendar calendar = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt(DatePickerFragment.DATE_YEAR, calendar.get(Calendar.YEAR));
        args.putInt(DatePickerFragment.DATE_MONTH, calendar.get(Calendar.MONTH));
        args.putInt(DatePickerFragment.DATE_DAY, calendar.get(Calendar.DAY_OF_MONTH));
        datePickerFragment.setArguments(args);
        datePickerFragment.setCallBack(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                setWatchedDate(year, monthOfYear, dayOfMonth);
            }
        });
        datePickerFragment.show(getFragmentManager(), "Date picker");
    }

    private void setWatchedDate(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieDatabaseHelper.COLUMN_WATCHED_DATE, calendar.getTimeInMillis());
        updateMovie(contentValues);

        tvWatchedDate.setText(Utility.getFormattedDate(Utility.DATE_WATCHED_FORMAT, calendar.getTime()));
    }


    private void fillData(View view) {
        cbWatched = (CheckBox) view.findViewById(R.id.cbWatched);
        cbWatched.setChecked(movie.getWatched() == 1);
        cbWatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movie.setWatched(cbWatched.isChecked() ? 1 : 0);
                ContentValues contentValues = new ContentValues();
                contentValues.put(MovieDatabaseHelper.COLUMN_WATCHED, movie.getWatched());
                updateMovie(contentValues);
                showHidePersonalDetails();
            }
        });

        tvMovieName = (TextView) view.findViewById(R.id.tvMovieName);
        tvMovieName.setText(movie.getName() != null ? movie.getName() : "");

        tvMovieDesc = (TextView) view.findViewById(R.id.tvMovieDesc);
        tvMovieDesc.setText(movie.getDescription() != null ? movie.getDescription() : "");

        tvMovieGenre = (TextView) view.findViewById(R.id.tvMovieGenre);
        tvMovieGenre.setText(movie.getGenre() != null ? movie.getGenre() : "");

        tvMovieDirector = (TextView) view.findViewById(R.id.tvMovieDirector);
        tvMovieDirector.setText(movie.getDirector() != null ? movie.getDirector() : "");

        tvMovieActors = (TextView) view.findViewById(R.id.tvMovieActors);
        tvMovieActors.setText(movie.getActors() != null ? movie.getActors() : "");

        ivMoviePic = (ImageView) view.findViewById(R.id.ivMoviePic);
        if (movie.getPicturePath() != null) {
            Uri pictureUri = Uri.parse(movie.getPicturePath());
            ivMoviePic.setImageURI(pictureUri);
        } else {
            ivMoviePic.setImageResource(R.drawable.logo);
        }

        tvWatchedDate = (TextView) view.findViewById(R.id.tvDateWatched);
        if (movie.getWatchedDate() != 0) {
            tvWatchedDate.setText(Utility.getFormattedDate(Utility.DATE_WATCHED_FORMAT, new Date(movie.getWatchedDate())));
        }

        tvImpressions = (TextView) view.findViewById(R.id.tvImpressions);
        if (movie.getImpressions() != null) {
            tvImpressions.setText(movie.getImpressions());
        }

        showHidePersonalDetails();
    }

    private void showHidePersonalDetails() {
        llPersonalDetails.setVisibility(movie.getWatched() == 1 ? View.VISIBLE : View.INVISIBLE);
    }

    private int updateMovie(ContentValues contentValues) {
        int rowsUpdated = getActivity().getContentResolver().
                update(Uri.parse(MoviesContentProvider.CONTENT_URI + "/" + movie.getIdMovie()),
                        contentValues, null, null);
        return rowsUpdated;

    }

    public void setMovieImpressions(String impressions) {
        movie.setImpressions(impressions);

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieDatabaseHelper.COLUMN_IMPRESSIONS, movie.getImpressions());
        updateMovie(contentValues);

        tvImpressions.setText(movie.getImpressions()!=null ? movie.getImpressions() : getResources().getString(R.string.insert_impressions, ""));
    }

    public String getMovieImpressions() {
        return movie.getImpressions();
    }

}
