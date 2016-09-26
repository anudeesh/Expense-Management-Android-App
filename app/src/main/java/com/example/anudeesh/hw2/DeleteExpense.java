package com.example.anudeesh.hw2;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class DeleteExpense extends AppCompatActivity {

    private EditText expNameDel, expAmountDel, expDateDel;
    private Spinner categorySpinnerDel;
    private String expCategoryDel;
    private ImageView billImageDel;
    private Calendar calendarDel;
    private Uri galleryImageDel = Uri.EMPTY;
    private Expense expDel;
    private DatePickerDialog.OnDateSetListener dateSetListenerDel;
    private int expSelected = 0;
    private ArrayList expensesDel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_expense);

        expNameDel = (EditText) findViewById(R.id.editTextExpNameValDel);
        expAmountDel = (EditText) findViewById(R.id.editTextAmountValDel);
        expDateDel = (EditText) findViewById(R.id.editTextDateDel);
        categorySpinnerDel = (Spinner) findViewById(R.id.spinnerCategoryDel);
        expensesDel = (ArrayList<Expense>) getIntent().getExtras().getSerializable(MainActivity.EXPENSES_KEY);
        billImageDel = (ImageView) findViewById(R.id.imageFindDel);

        expNameDel.setEnabled(false);
        expAmountDel.setEnabled(false);
        expDateDel.setEnabled(false);
        categorySpinnerDel.setEnabled(false);
        calendarDel = Calendar.getInstance();

        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinnerDel.setAdapter(staticAdapter);

        findViewById(R.id.buttonSelectExpDel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DeleteExpense.this);
                builder.setTitle("Pick an Expense");
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(DeleteExpense.this, android.R.layout.select_dialog_item);
                expDel = null;
                int len = expensesDel.size();

                for(int i=0;i<len;i++) {
                    expDel = (Expense)expensesDel.get(i);
                    arrayAdapter.add(expDel.getExpName());
                }

                builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        expDel = (Expense)expensesDel.get(which);
                        expSelected = which;

                        expNameDel.setText(expDel.getExpName());
                        expAmountDel.setText(expDel.getExpAmount());
                        expDateDel.setText(expDel.getExpDate());
                        galleryImageDel = expDel.getBillImage();

                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(DeleteExpense.this, R.array.categories, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        categorySpinnerDel.setAdapter(adapter);

                        if (!expDel.getExpCategory().equals(null)) {
                            int index = adapter.getPosition(expDel.getExpCategory());
                            categorySpinnerDel.setSelection(index);
                        }

                        billImageDel.setImageResource(0);
                        Bitmap imgBitmap = null;
                        if(expDel.getBillImage() != Uri.EMPTY)
                        {
                            try {
                                imgBitmap = MediaStore.Images.Media.getBitmap(DeleteExpense.this.getContentResolver(),expDel.getBillImage());
                                billImageDel.setImageBitmap(imgBitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        findViewById(R.id.buttonDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expensesDel.remove(expSelected);
                Intent intent = new Intent();
                intent.putExtra(MainActivity.EXPENSES_KEY,expensesDel);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        findViewById(R.id.buttonCancelDel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
