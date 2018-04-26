package com.delaroystudios.alarmreminder;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddingTripActivity extends AppCompatActivity {

   static EditText select_date;

    private  int mYear;
    private  int mMonth;
    private  int mDay;
    private int mHour;
    private int mMinute;

    DatabaseReference rootRef;

    DatabaseReference demoRef;
    DatabaseReference demoRef1;
    DatabaseReference demoRef2;
    Button save;
    EditText tripName;
    EditText tripNote;
    EditText tripDate;
    Intent comingIntent;

    EditText tripTo;
    EditText tripFrom;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;


    PlaceAutocompleteFragment autocompleteFragment;
    PlaceAutocompleteFragment autocompleteFragment1;
    String email;
    public static final String shP ="login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_trip);

//        select_date= (EditText) findViewById(R.id.tripDateAndTime);
//        select_date.setOnClickListener(new View.OnClickListener() {
//
//         @Override
//             public void onClick(View v) {
//                 showTruitonTimePickerDialog(v);
//                 showTruitonDatePickerDialog(v);
//                         }
//                            });



        comingIntent=getIntent();

        save= (Button) findViewById(R.id.save);
        tripNote= (EditText) findViewById(R.id.addNote);
       // tripDate= (EditText) findViewById(R.id.tripDateAndTime);
        tripName= (EditText) findViewById(R.id.tripName);



         email=comingIntent.getStringExtra("userEmail");
    if(email == null){

    SharedPreferences setting = getSharedPreferences(shP,0);
    email = setting.getString("email","0");



}
        email = email.split("@")[0];






        // autocomplete start point

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setHint("Start Point ");


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                //  Log.i(TAG, "Place: " + place.getName());

                String place_name= place.getName().toString();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                // Log.i(TAG, "An error occurred: " + status);
            }
        });

//autocomplete end point


        autocompleteFragment1 = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment1);

        autocompleteFragment1.setHint("End Point ");




        autocompleteFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                //  Log.i(TAG, "Place: " + place.getName());

                String place_name1= place.getName().toString();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                // Log.i(TAG, "An error occurred: " + status);
            }
        });



        //save data in firebase

        // rootRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidproject1-58342.firebaseio.com/trips");
        rootRef = FirebaseDatabase.getInstance().getReference();
        //database reference pointing to demo node

        demoRef = rootRef.child(email);
//        demoRef = rootRef.child("name");
//       demoRef1 = rootRef.child("date");

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                String name = tripName.getText().toString();
                String date = "";
                String note = tripNote.getText().toString();


                tripFrom = (EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input);


                String startpoint=tripFrom.getText().toString();


                tripTo = (EditText)autocompleteFragment1.getView().findViewById(R.id.place_autocomplete_search_input);


                String endpoint=tripTo.getText().toString();

                // String userId="";



                User user = new User(email,name, date,note,startpoint,endpoint);

                demoRef.push().setValue(user);


                //push creates a unique id in database
//                demoRef.push().setValue(name);
//                demoRef1.push().setValue(date);
//                demoRef2.push().setValue(note);

                Toast.makeText(AddingTripActivity.this, "Done!",
                        Toast.LENGTH_LONG).show();

                tripName.setText("");
               // tripDate.setText("");
                tripNote.setText("");
                tripFrom.setText("");
                tripTo.setText("");





            }








        });




    }



    public void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            select_date.setText(day + "/" + (month + 1) + "/" + year);



        }
    }



    public void showTruitonTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
          select_date.setText(select_date.getText() + " / " + hourOfDay + ":" + minute);
        }
    }













    /*

    //date and time
            select_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showTruitonDatePickerDialog(v);
                Calendar mcurrentDate = Calendar.getInstance();
                mYear = mcurrentDate.get(Calendar.YEAR);
                mMonth = mcurrentDate.get(Calendar.MONTH);
                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                mHour=mcurrentDate.get(Calendar.HOUR);
                mMinute=mcurrentDate.get(Calendar.MINUTE);


                final DatePickerDialog mDatePicker = new DatePickerDialog(AddingTripActivity.this, new DatePickerDialog.OnDateSetListener() {


                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);


                        String myFormat = "dd/MM/yy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);


                        select_date.setText(sdf.format(myCalendar.getTime()));

                        mDay = selectedday;
                        mMonth = selectedmonth;
                        mYear = selectedyear;
                    }
                }, mYear, mMonth, mDay);

                mDatePicker.show();
            }

                private void showTruitonDatePickerDialog(View v) {


                    DialogFragment newFragment = new TimePickerFragment();
                    newFragment.show(getSupportFragmentManager(), "timePicker");



                }











            });








     */




    }


