package com.example.anudeesh.hw2;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class EditExpenseActivity extends AppCompatActivity {

    private EditText expNameEdit, expAmountEdit, expDateEdit;
    private Spinner categorySpinnerEdit;
    private String expCategoryEdit;
    private ImageView billImageEdit;
    private Calendar calendarEdit;
    private Uri galleryImageEdit = Uri.EMPTY;
    private Expense expEdit;
    private DatePickerDialog.OnDateSetListener dateSetListenerEdit;
    private int expSelected = 0;
    private ArrayList expensesEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        expNameEdit = (EditText) findViewById(R.id.editTextExpNameval_Edit);
        expAmountEdit = (EditText) findViewById(R.id.editTextAmountVal_Edit);
        expDateEdit = (EditText) findViewById(R.id.editTextDate_Edit);
        categorySpinnerEdit = (Spinner) findViewById(R.id.spinnerCategory_Edit);
        expensesEdit = (ArrayList<Expense>) getIntent().getExtras().getSerializable(MainActivity.EXPENSES_KEY);

        expDateEdit.setEnabled(false);
        calendarEdit = Calendar.getInstance();

        dateSetListenerEdit = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                expDateEdit.setText((monthOfYear+1)+"/"+dayOfMonth+"/"+year);
            }
        };

        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(this, R.array.categories,
                android.R.layout.simple_spinner_item);
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinnerEdit.setAdapter(staticAdapter);

        categorySpinnerEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                expCategoryEdit = (String)parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        billImageEdit = (ImageView) findViewById(R.id.imageFind_Edit);
        billImageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imgIntent = new Intent(Intent.ACTION_GET_CONTENT);
                imgIntent.setType("image/*");
                startActivityForResult(imgIntent,AddExpenseActivity.IMAGE_KEY);
            }
        });

        findViewById(R.id.imageCalendar_Edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditExpenseActivity.this,dateSetListenerEdit,calendarEdit.get(Calendar.YEAR),calendarEdit.get(Calendar.MONTH),calendarEdit.get(Calendar.DATE)).show();
            }
        });

        findViewById(R.id.buttonSelectExp_Edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditExpenseActivity.this);
                builder.setTitle("Pick an Expense");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EditExpenseActivity.this, android.R.layout.select_dialog_item);
                expEdit = null;
                int len = expensesEdit.size();

                for(int i=0;i<len;i++) {
                    expEdit = (Expense)expensesEdit.get(i);
                    arrayAdapter.add(expEdit.getExpName());
                }

                /*builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });*/

                builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        expEdit = (Expense)expensesEdit.get(which);
                        expSelected = which;

                        expNameEdit.setText(expEdit.getExpName());
                        expAmountEdit.setText(expEdit.getExpAmount());
                        expDateEdit.setText(expEdit.getExpDate());
                        galleryImageEdit = expEdit.getBillImage();

                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(EditExpenseActivity.this, R.array.categories, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        categorySpinnerEdit.setAdapter(adapter);

                        if (!expEdit.getExpCategory().equals(null)) {
                            int index = adapter.getPosition(expEdit.getExpCategory());
                            categorySpinnerEdit.setSelection(index);
                        }

                        billImageEdit.setImageResource(0);
                        Bitmap imgBitmap = null;
                        if(expEdit.getBillImage() != Uri.EMPTY)
                        {
                            try {
                                imgBitmap = MediaStore.Images.Media.getBitmap(EditExpenseActivity.this.getContentResolver(),expEdit.getBillImage());
                                billImageEdit.setImageBitmap(imgBitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            // Assume thisActivity is the current activity
                            /*int permissionCheck = ContextCompat.checkSelfPermission(EditExpenseActivity.this,
                                    Manifest.permission.READ_EXTERNAL_STORAGE);
                            if (permissionCheck == 0){
                                billImageEdit.setImageURI(expEdit.getBillImage());
                            }
                            else{
                                Log.d("debug","no permissions");
                            }*/

                            /*if (ContextCompat.checkSelfPermission(EditExpenseActivity.this,
                                    Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {

                                // Should we show an explanation?
                                if (ActivityCompat.shouldShowRequestPermissionRationale(EditExpenseActivity.this,
                                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                                    // Show an expanation to the user *asynchronously* -- don't block
                                    // this thread waiting for the user's response! After the user
                                    // sees the explanation, try again to request the permission.

                                } else {

                                    // No explanation needed, we can request the permission.

                                    ActivityCompat.requestPermissions(EditExpenseActivity.this,
                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                            0);

                                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                    // app-defined int constant. The callback method gets the
                                    // result of the request.
                                }
                            }*/
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        findViewById(R.id.buttonSave_Edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = expNameEdit.getText().toString();
                String amount = expAmountEdit.getText().toString();
                String date = expDateEdit.getText().toString();

                if(name.isEmpty() || amount.isEmpty() || date.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Enter all the details",Toast.LENGTH_SHORT).show();
                }
                else if (Double.parseDouble(amount) == 0) {
                    Toast.makeText(getApplicationContext(),"Amount cannot be zero",Toast.LENGTH_SHORT).show();
                }
                else {
                    Expense updatedExpense = (Expense) expensesEdit.get(expSelected);
                    updatedExpense.setExpName(name);
                    updatedExpense.setExpCategory(expCategoryEdit);
                    updatedExpense.setExpAmount(amount);
                    updatedExpense.setExpDate(date);
                    updatedExpense.setBillImage(galleryImageEdit);

                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.EXPENSES_KEY,expensesEdit);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });

        findViewById(R.id.buttonCancel_Edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == AddExpenseActivity.IMAGE_KEY)
        {
            if(resultCode == RESULT_OK)
            {
                galleryImageEdit = data.getData();
                Bitmap imgBitmap;

                try {
                    imgBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),galleryImageEdit);
                    billImageEdit.setImageBitmap(imgBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    billImageEdit.setImageResource(0);
                    //Bitmap imgBitmap = null;
                    if(expEdit.getBillImage() != Uri.EMPTY) {
                        //galleryImageEdit = expEdit.getBillImage();
                        billImageEdit.setImageURI(expEdit.getBillImage());
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }*/
}
