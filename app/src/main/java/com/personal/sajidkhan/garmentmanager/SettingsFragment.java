package com.personal.sajidkhan.garmentmanager;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        initializeControls(v);
        return v;
    }

    private void initializeControls(View view) {
        Button calendarBtn = (Button) view.findViewById(R.id.calendarPickerButton);
        Button timeBtn = (Button) view.findViewById(R.id.timePickerButton);
        calendarBtn.setOnClickListener(this);
        timeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.calendarPickerButton:
                openCalendarDialog(getActivity());
                break;
            case R.id.timePickerButton:
                openTimeDialog(getActivity());
                break;
        }
    }

    private void openCalendarDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.weekdays_checkable_layout, null);
//        CalendarView calendarView = (CalendarView)v.findViewById(R.id.calendarView);
//        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
//            }
//        });
        builder.setView(v);

        builder.create().show();
    }

    private void openTimeDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.time_picker, null);
        TimePicker timePicker = (TimePicker) v.findViewById(R.id.timePicker);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

            }
        });
        builder.setView(v);

        builder.create().show();
    }
}
