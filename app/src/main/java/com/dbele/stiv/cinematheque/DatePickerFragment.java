package com.dbele.stiv.cinematheque;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by dbele on 4/15/2015.
 */
public class DatePickerFragment extends DialogFragment {
    DatePickerDialog.OnDateSetListener ondateSet;

    public static final String DATE_YEAR = "datepickerfragment.year";
    public static final String DATE_MONTH = "datepickerfragment.month";
    public static final String DATE_DAY = "datepickerfragment.day";

    private int year, month, day;

    public DatePickerFragment() {
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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), ondateSet, year, month, day);
    }
}
