package com.dbele.stiv.cinematheque;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.dbele.stiv.model.Movie;
import com.dbele.stiv.persistence.CinemaRepository;
import com.dbele.stiv.persistence.MovieDatabaseHelper;
import com.dbele.stiv.persistence.MoviesContentProvider;
import com.dbele.stiv.handlers.AnimationHandler;
import com.dbele.stiv.handlers.BackgroundMusicHandler;
import com.dbele.stiv.handlers.CameraHandler;
import com.dbele.stiv.handlers.ImagesHandler;
import com.dbele.stiv.handlers.PreferencesHandler;
import com.dbele.stiv.utilities.Utility;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MovieFragment extends Fragment {

    public static final String EXTRA_MOVIE_ID = "com.dbele.stiv.cinematheque.extra.movie.id";
    private static final int TAKE_PHOTO = 1;

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
    private ImageView ivTakePhoto;
    private ImageView ivTicket;
    private ImageView ivArrow;
    private Spinner spCinemaNames;
    private ImageView ivSetRank;
    private ImageView ivRank;
    private TextView tvRank;
    private LinearLayout llPersonalDetails;
    private ArrayList<String> cinemaSpinnerNames;
    private boolean spinnerAvoidSelection = true;

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
        if (viewGroup!=null) {
            viewGroup.removeAllViewsInLayout();
            View view = onCreateView(inflater, viewGroup, null);
            if (view!=null) {
                viewGroup.addView(view);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        fetchViews(view);
        fillData();
        setupListeners();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PHOTO && resultCode == FragmentActivity.RESULT_OK && data != null){
            Bundle extras = data.getExtras();
            Bitmap ticketBitmap = (Bitmap) extras.get("data");
            setTicket(ticketBitmap);
            getActivity().getContentResolver().delete(data.getData(), null, null);
        }
        PreferencesHandler.setContinuePlaying(getActivity(), false);
    }

    private void returnToHostActivity() {
        Intent intent = new Intent(getActivity(), HostActivity.class);
        startActivity(intent);
    }

    private void fetchViews(View view) {
        llPersonalDetails = (LinearLayout) view.findViewById(R.id.llPersonalDetails);
        cbWatched = (CheckBox) view.findViewById(R.id.cbWatched);
        ivArrow = (ImageView) view.findViewById(R.id.ivArrow);
        tvMovieName = (TextView) view.findViewById(R.id.tvMovieName);
        tvMovieDesc = (TextView) view.findViewById(R.id.tvMovieDesc);
        tvMovieGenre = (TextView) view.findViewById(R.id.tvMovieGenre);
        tvMovieDirector = (TextView) view.findViewById(R.id.tvMovieDirector);
        tvMovieActors = (TextView) view.findViewById(R.id.tvMovieActors);
        ivMoviePic = (ImageView) view.findViewById(R.id.ivMoviePic);
        ivTicket = (ImageView) view.findViewById(R.id.ivTicket);
        tvWatchedDate = (TextView) view.findViewById(R.id.tvDateWatched);
        tvImpressions = (TextView) view.findViewById(R.id.tvImpressions);
        ivWatchedDate = (ImageView) view.findViewById(R.id.ivWatchedDate);
        tvWatchedDate = (TextView) view.findViewById(R.id.tvDateWatched);
        ivImpressions = (ImageView) view.findViewById(R.id.ivImpressions);
        tvImpressions = (TextView) view.findViewById(R.id.tvImpressions);
        ivTakePhoto = (ImageView) view.findViewById(R.id.ivTakePhoto);
        ivTicket = (ImageView) view.findViewById(R.id.ivTicket);
        spCinemaNames = (Spinner) view.findViewById(R.id.spCinemaNames);
        ivSetRank = (ImageView) view.findViewById(R.id.ivSetRank);
        ivRank = (ImageView) view.findViewById(R.id.ivRank);
        tvRank = (TextView) view.findViewById(R.id.tvRank);
    }

    private void fillData() {
        //this bypass is intented for yet unknown reason when either fields from layout are null after inflating or movie instance
        //to be investigated - most probably after returning to MoviePagerActivity from stack
        if(movie == null || cbWatched == null) {
            returnToHostActivity();
        }
        cbWatched.setChecked(movie.getWatched() == 1);
        tvMovieName.setText(movie.getName() != null ? movie.getName() : "");
        tvMovieDesc.setText(movie.getDescription() != null ? movie.getDescription() : "");
        tvMovieGenre.setText(movie.getGenre() != null ? movie.getGenre() : "");
        tvMovieDirector.setText(movie.getDirector() != null ? movie.getDirector() : "");
        tvMovieActors.setText(movie.getActors() != null ? movie.getActors() : "");
        if (movie.getPicturePath() != null) {
            Uri pictureUri = Uri.parse(movie.getPicturePath());
            ivMoviePic.setImageURI(pictureUri);
        } else {
            ivMoviePic.setImageResource(R.drawable.logo);
        }
        if (movie.getTicketPath() != null) {
            Uri ticketUri = Uri.parse(movie.getTicketPath());
            ivTicket.setImageURI(ticketUri);
        }
        if (movie.getWatchedDate() != 0) {
            tvWatchedDate.setText(Utility.getFormattedDate(Utility.DATE_WATCHED_FORMAT, new Date(movie.getWatchedDate())));
        }
        if (movie.getImpressions() != null) {
            tvImpressions.setText(movie.getImpressions());
        }
        llPersonalDetails.setVisibility(movie.getWatched() == 1 ? View.VISIBLE : View.INVISIBLE);
        cinemaSpinnerNames = CinemaRepository.getCinemaNames(getActivity());
        cinemaSpinnerNames.add(0, getResources().getString(R.string.choose_cinema));
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, cinemaSpinnerNames);
        arrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spCinemaNames.setAdapter(arrayAdapter);
        if (movie.getCinemaName()!=null) {
            spCinemaNames.setSelection(cinemaSpinnerNames.indexOf(movie.getCinemaName()));
        }
        if (movie.getRank()!=null) {
            tvRank.setText(movie.getRank());
        }
        if (movie.getDegree()!=0) {
            ivRank.setImageResource(R.drawable.thumb);
            AnimationHandler.startRotatingAnimation(0, movie.getDegree(), 250, ivRank);
        }
    }

    private void setupListeners() {
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

        ivWatchedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        tvWatchedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        ivImpressions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImpressionsDialog();
            }
        });
        tvImpressions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImpressionsDialog();
            }
        });
        ivTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        ivTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        spCinemaNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerAvoidSelection) {
                    spinnerAvoidSelection = false;
                    return;
                }
                if (position == 0) {
                    setCinemaName(null);
                } else {
                    setCinemaName(cinemaSpinnerNames.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ivSetRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRankSetter();
            }
        });
        tvRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRankSetter();
            }
        });
        ivRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRankSetter();
            }
        });
    }

    private void showHidePersonalDetails() {
        if(movie.getWatched()==0) {
            AnimationHandler.startOutAnimation(getActivity(), llPersonalDetails);
        } else {
            AnimationHandler.startFadeInAnimation(getActivity(), llPersonalDetails);
            AnimationHandler.startBlinkAnimation(getActivity(), ivArrow);
        }
    }

    private void showRankSetter() {
        SetRankFragment setRankFragment = SetRankFragment.newInstance(this);
        setRankFragment.show(getFragmentManager(), null);
    }

    private void showDatePicker() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        Calendar calendar = Calendar.getInstance();
        if(movie.getWatchedDate()!=0) {
            calendar.setTimeInMillis(movie.getWatchedDate());
        }
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
        datePickerFragment.show(getFragmentManager(), getString(R.string.date_picker));
    }

    private void showImpressionsDialog() {
        ImpressionsFragment impressionsFragment = ImpressionsFragment.newInstance(this);
        impressionsFragment.show(getFragmentManager(), null);
    }

    private void takePhoto() {
        if (CameraHandler.deviceCanUseCamera(getActivity())) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, TAKE_PHOTO);

            PreferencesHandler.setContinuePlaying(getActivity(), BackgroundMusicHandler.getShouldPlay());
        } else {
            Toast.makeText(getActivity(), R.string.cannot_take_photos, Toast.LENGTH_LONG).show();
        }
    }

    public String getMovieRank() {
        return movie.getRank();
    }

    public void setMovieRank(String rank) {
        movie.setRank(rank);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieDatabaseHelper.COLUMN_RANK, movie.getRank());
        updateMovie(contentValues);
        tvRank.setText(movie.getRank()!=null ? movie.getRank() : getResources().getString(R.string.set_rank, ""));
    }

    public void setMovieDegree(float degree) {
        movie.setDegree(degree);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieDatabaseHelper.COLUMN_DEGREE, movie.getDegree());
        updateMovie(contentValues);
        ivRank.setImageResource(R.drawable.thumb);
        AnimationHandler.startRotatingAnimation(0, movie.getDegree(), 250, ivRank);
    }

    private void setWatchedDate(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        movie.setWatchedDate(calendar.getTimeInMillis());
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieDatabaseHelper.COLUMN_WATCHED_DATE, movie.getWatchedDate());
        updateMovie(contentValues);
        tvWatchedDate.setText(Utility.getFormattedDate(Utility.DATE_WATCHED_FORMAT, calendar.getTime()));
    }

    private void setCinemaName(String cinemaName) {
        movie.setCinemaName(cinemaName);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieDatabaseHelper.COLUMN_CINEMANAME, movie.getCinemaName());
        updateMovie(contentValues);
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

    private void setTicket(Bitmap ticketBitmap) {
        String ticketPath = ImagesHandler.storeBitmap(getActivity(), Movie.TICKET_JPG_PREFIX + (movie.getName().hashCode()), ticketBitmap);
        movie.setTicketPath(ticketPath);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieDatabaseHelper.COLUMN_TICKET_PATH, movie.getTicketPath());
        updateMovie(contentValues);
        ivTicket.setImageURI(null);
        Uri ticketUri = Uri.parse(movie.getTicketPath());
        ivTicket.setImageURI(ticketUri);
    }

    private int updateMovie(ContentValues contentValues) {
        return getActivity().getContentResolver().
                update(Uri.parse(MoviesContentProvider.CONTENT_URI + "/" + movie.getIdMovie()),
                        contentValues, null, null);
    }

}
