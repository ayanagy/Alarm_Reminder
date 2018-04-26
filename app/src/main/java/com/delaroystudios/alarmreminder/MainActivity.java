package com.delaroystudios.alarmreminder;


import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;

import android.content.DialogInterface;
import android.content.Intent;

import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystudios.alarmreminder.data.AlarmReminderContract;
import com.delaroystudios.alarmreminder.data.AlarmReminderDbHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private FloatingActionButton mAddReminderButton;
    private Toolbar mToolbar;
    AlarmCursorAdapter mCursorAdapter;
    AlarmReminderDbHelper alarmReminderDbHelper = new AlarmReminderDbHelper(this);
    ListView reminderListView;
    ProgressDialog prgDialog;
    TextView reminderText;

    private String alarmTitle = "";

    private static final int VEHICLE_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);


        reminderListView = (ListView) findViewById(R.id.list);
        reminderText = (TextView) findViewById(R.id.reminderText);


        View emptyView = findViewById(R.id.empty_view);
        reminderListView.setEmptyView(emptyView);

        mCursorAdapter = new AlarmCursorAdapter(this, null);
        reminderListView.setAdapter(mCursorAdapter);

        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, AddReminderActivity.class);

                Uri currentVehicleUri = ContentUris.withAppendedId(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentVehicleUri);

                startActivity(intent);

            }
        });


        mAddReminderButton = (FloatingActionButton) findViewById(R.id.fab);

        mAddReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(v.getContext(), AddReminderActivity.class);
                //startActivity(intent);
                addReminderTitle();
            }
        });

        getSupportLoaderManager().initLoader(VEHICLE_LOADER, null, this);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                AlarmReminderContract.AlarmReminderEntry._ID,
                AlarmReminderContract.AlarmReminderEntry.KEY_TITLE,
                AlarmReminderContract.AlarmReminderEntry.KEY_DATE,
                AlarmReminderContract.AlarmReminderEntry.KEY_TIME,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE,
                AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE

        };

        return new CursorLoader(this,   // Parent activity context
                AlarmReminderContract.AlarmReminderEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
        if (cursor.getCount() > 0){
            reminderText.setVisibility(View.VISIBLE);
        }else{
            reminderText.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }

    public void addReminderTitle(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Reminder Title");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().isEmpty()){
                    return;
                }

                alarmTitle = input.getText().toString();
                ContentValues values = new ContentValues();

                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE, alarmTitle);

                Uri newUri = getContentResolver().insert(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, values);

                restartLoader();


                if (newUri == null) {
                    Toast.makeText(getApplicationContext(), "Setting Reminder Title failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Title set successfully", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void restartLoader(){
        getSupportLoaderManager().restartLoader(VEHICLE_LOADER, null, this);
    }
}































/*package com.delaroystudios.alarmreminder;


import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystudios.alarmreminder.data.AlarmReminderContract;
import com.delaroystudios.alarmreminder.data.AlarmReminderDbHelper;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private FloatingActionButton mAddReminderButton;
    private Toolbar mToolbar;
    AlarmCursorAdapter mCursorAdapter;
    AlarmReminderDbHelper alarmReminderDbHelper = new AlarmReminderDbHelper(this);
    ListView reminderListView;
    ProgressDialog prgDialog;
    TextView reminderText;



    PlaceAutocompleteFragment autocompleteFragment;
    PlaceAutocompleteFragment autocompleteFragment1;

    private String alarmTitle = "";

    private static final int VEHICLE_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);


        reminderListView = (ListView) findViewById(R.id.list);
        reminderText = (TextView) findViewById(R.id.reminderText);


        View emptyView = findViewById(R.id.empty_view);
        reminderListView.setEmptyView(emptyView);

        mCursorAdapter = new AlarmCursorAdapter(this, null);
        reminderListView.setAdapter(mCursorAdapter);

        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, AddReminderActivity.class);

                Uri currentVehicleUri = ContentUris.withAppendedId(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentVehicleUri);

                startActivity(intent);

            }
        });


        mAddReminderButton = (FloatingActionButton) findViewById(R.id.fab);

        mAddReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addReminderTitle();
      //Intent intent = new Intent(v.getContext(), AddingTripActivity.class);
        //       startActivity(intent);
               //addReminderNote();
               //addReminderTitle();

            }
        });

        getSupportLoaderManager().initLoader(VEHICLE_LOADER, null, this);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                AlarmReminderContract.AlarmReminderEntry._ID,
                AlarmReminderContract.AlarmReminderEntry.KEY_TITLE,
                AlarmReminderContract.AlarmReminderEntry.KEY_DATE,
                AlarmReminderContract.AlarmReminderEntry.KEY_TIME,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE,
                AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE

        };

        return new CursorLoader(this,   // Parent activity context
                AlarmReminderContract.AlarmReminderEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
        if (cursor.getCount() > 0){
            reminderText.setVisibility(View.VISIBLE);
        }else{
            reminderText.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }

    public void addReminderTitle(){
//        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
//
//            public void onFocusChange(final View v, boolean hasFocus) {
//                if (hasFocus) {
//                    // Must use message queue to show keyboard
//                    v.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            InputMethodManager inputMethodManager= (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                            inputMethodManager.showSoftInput(v, 0);
//                        }
//                    });
//                }
//            }
//        };
//
//        final EditText editTextName = new EditText(this);
//        editTextName.setHint("Trip Name");
//        editTextName.setFocusable(true);
//        editTextName.setClickable(true);
//        editTextName.setFocusableInTouchMode(true);
//        editTextName.setSelectAllOnFocus(true);
//        editTextName.setSingleLine(true);
//        editTextName.setImeOptions(EditorInfo.IME_ACTION_NEXT);
//        editTextName.setOnFocusChangeListener(onFocusChangeListener);
//
//
//
//
//        final EditText editTextNote = new EditText(this);
//        editTextNote.setHint("Trip Note");
//        editTextNote.setFocusable(true);
//        editTextNote.setClickable(true);
//        editTextNote.setFocusableInTouchMode(true);
//        editTextNote.setSelectAllOnFocus(true);
//        editTextNote.setSingleLine(true);
//        editTextNote.setImeOptions(EditorInfo.IME_ACTION_NEXT);
//        editTextNote.setOnFocusChangeListener(onFocusChangeListener);
//
//
//
//
//        // autocomplete start point
//
//        autocompleteFragment = (PlaceAutocompleteFragment)
//                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//
//
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                // TODO: Get info about the selected place.
//                //  Log.i(TAG, "Place: " + place.getName());
//
//                String place_name= place.getName().toString();
//            }
//
//            @Override
//            public void onError(Status status) {
//                // TODO: Handle the error.
//                // Log.i(TAG, "An error occurred: " + status);
//            }
//        });
//
//        autocompleteFragment.setHint("From");
////        autocompleteFragment.setFocusable(true);
////        autocompleteFragment.setClickable(true);
////        editTextNote.setFocusableInTouchMode(true);
////        editTextNote.setSelectAllOnFocus(true);
////        editTextNote.setSingleLine(true);
////        autocompleteFragment.setImeOptions(EditorInfo.IME_ACTION_NEXT);
////        editTextNote.setOnFocusChangeListener(onFocusChangeListener);
//
//
//
//
//
//
//
//
//
//
//
//
//
////
////        LinearLayout linearLayout = new LinearLayout(this);
////        linearLayout.setOrientation(LinearLayout.VERTICAL);
////        linearLayout.addView(editTextName);
////        linearLayout.addView(editTextNote);
////        linearLayout.addView(autocompleteFragment);
//
//
//
//
//
//
//
//
//
//        DialogInterface.OnClickListener alertDialogClickListener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which){
//                    case DialogInterface.BUTTON_POSITIVE:
//                        // Done button clicked
//
//
//                        break;
//                    case DialogInterface.BUTTON_NEGATIVE:
//                        // Cancel button clicked
//                        break;
//                }
//            }
//        };
//       final AlertDialog alertDialog = (new AlertDialog.Builder(this)).setMessage("Please enter Trip Details")
//                .setView(linearLayout)
//              .setPositiveButton("Done", alertDialogClickListener)
//              .setNegativeButton("Cancel", alertDialogClickListener)
//                .create();
//
//        editTextName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                editTextNote.requestFocus(); // Press Return to focus next one
//                return false;
//            }
//        });
//        editTextNote.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                // Press Return to invoke positive button on alertDialog.
//                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
//                return false;
//            }
//        });
//
//
//
//
//
//
//        alertDialog.show();
//
//
//
//
//

//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Trip Title");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder.setTitle("Set Trip Title");

        final EditText input1 = new EditText(this);
        input1.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input1);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().isEmpty()){
                    return;
                }


                alarmTitle = input.getText().toString();
                ContentValues values = new ContentValues();

                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE, alarmTitle);

                Uri newUri = getContentResolver().insert(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, values);

                restartLoader();


                if (newUri == null) {
                    Toast.makeText(getApplicationContext(), "Setting Reminder Title failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Title set successfully", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();




    }

//    public void addReminderNote(){
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Set Trip Note");
//
//        final EditText input = new EditText(this);
//        input.setInputType(InputType.TYPE_CLASS_TEXT);
//        builder.setView(input);
//
//        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
//        builder.setTitle("Set Trip Note");
//
//        final EditText input1 = new EditText(this);
//        input1.setInputType(InputType.TYPE_CLASS_TEXT);
//        builder.setView(input1);
//
//
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (input.getText().toString().isEmpty()){
//                    return;
//                }
//
//                alarmTitle = input.getText().toString();
//                ContentValues values = new ContentValues();
//
//                values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE, alarmTitle);
//
//                Uri newUri = getContentResolver().insert(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, values);
//
//                restartLoader();
//
//
//                if (newUri == null) {
//                    Toast.makeText(getApplicationContext(), "Setting Reminder Title failed", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Title set successfully", Toast.LENGTH_SHORT).show();
//                }

//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        builder.show();
//    }








    public void restartLoader(){
        getSupportLoaderManager().restartLoader(VEHICLE_LOADER, null, this);
    }
}
*/