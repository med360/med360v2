package info.androidhive.loginandregistration.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import info.androidhive.loginandregistration.R;

public class Booking extends AppCompatActivity {
    private Button btnrequest;
    private Button btndate;
    private Spinner preftime;
    private EditText appdate;
    private ProgressDialog pDialog;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    private EditText dateView;

    Spinner preftimespin;

    String[] preftimes = {
            "Morning",
            "Afternoon",
            "Evening",

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        btnrequest = (Button) findViewById(R.id.btnrequest);
        btndate = (Button) findViewById(R.id.btndate);
        appdate = (EditText) findViewById(R.id.appdate);
        preftime = (Spinner) findViewById(R.id.preftime);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);



        btnrequest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String appodate = appdate.getText().toString().trim();
                String ptime = preftime.getSelectedItem().toString().trim();


                if (!appodate.isEmpty() && !ptime.isEmpty() ) {
                   // createProfile(name, nationality, gender, dob, bgp);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter the date and preferred time!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        preftimespin = (Spinner)findViewById(R.id.preftime);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, preftimes);

        preftimespin.setAdapter(adapter2);
        preftimespin.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        int position = preftimespin.getSelectedItemPosition();

                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                }
        );


        dateView = (EditText) findViewById(R.id.appdate);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

    }


    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
}
