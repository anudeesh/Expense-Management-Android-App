package com.example.anudeesh.hw2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText expNameVal, expAmountVal, expDateVal;
    private Spinner categorySpinner;
    private String expCategoryVal;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private ImageView billImage, calendarImage;
    private Uri galleryImage = Uri.EMPTY;
    private Button buttonAddNew;
    final static int IMAGE_KEY = 0x03;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        expNameVal = (EditText) findViewById(R.id.editTextExpNameval);
        expAmountVal = (EditText) findViewById(R.id.editTextAmountVal);
        expDateVal = (EditText) findViewById(R.id.editTextDate);
        categorySpinner = (Spinner) findViewById(R.id.spinnerCategory);

        expDateVal.setEnabled(false);
        calendar = Calendar.getInstance();

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                expDateVal.setText((monthOfYear+1)+"/"+dayOfMonth+"/"+year);
            }
        };

        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(this, R.array.categories,
                        android.R.layout.simple_spinner_item);
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(staticAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                expCategoryVal = (String)parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        billImage = (ImageView) findViewById(R.id.imageFind);
        billImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imgIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imgIntent.setType("image/*");
                startActivityForResult(imgIntent,IMAGE_KEY);
            }
        });

        calendarImage = (ImageView)findViewById(R.id.imageCalendar);
        calendarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddExpenseActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE)).show();
            }
        });

        buttonAddNew = (Button) findViewById(R.id.buttonAddExp);
        assert buttonAddNew != null;
        buttonAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = expNameVal.getText().toString();
                String amount = expAmountVal.getText().toString();
                String date = expDateVal.getText().toString();

                if(name.isEmpty() || amount.isEmpty() || date.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter all the details",Toast.LENGTH_SHORT).show();
                }
                else if (Double.parseDouble(amount) == 0) {
                    Toast.makeText(getApplicationContext(),"Amount cannot be zero",Toast.LENGTH_SHORT).show();
                }
                else {
                    Expense expense = new Expense(name,expCategoryVal,amount,date,galleryImage);
                    Log.d("debug",expense.toString());
                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.NEWEXP_KEY,expense);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == IMAGE_KEY)
        {
            if(resultCode == RESULT_OK)
            {
                galleryImage = data.getData();
                Bitmap imgBitmap;
                try {
                    imgBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),galleryImage);
                    billImage.setImageBitmap(imgBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
