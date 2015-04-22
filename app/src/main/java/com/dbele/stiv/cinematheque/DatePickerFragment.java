package com.dbele.stiv.cinematheque;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;


public class DatePickerFragment extends DialogFragment {

    public static final String DATE_YEAR = "com.dbele.stiv.cinematheque.datepickerfragment.year";
    public static final String DATE_MONTH = "com.dbele.stiv.cinematheque.datepickerfragment.month";
    public static final String DATE_DAY = "com.dbele.stiv.cinematheque.datepickerfragment.day";

    DatePickerDialog.OnDateSetListener ondateSet;
    private int year;
    private int month;
    private int day;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), ondateSet, year, month, day);
    }

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
        ondateSet = ondate;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt(DATE_YEAR);
        month = args.getInt(DATE_MONTH);
        day = args.getInt(DATE_DAY);
    }
}
