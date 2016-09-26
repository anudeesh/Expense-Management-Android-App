package com.example.anudeesh.hw2;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

public class ShowExpensesActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView expNameShow, expAmountShow, expDateShow, expCategoryShow;
    private ImageView billImageShow;
    private ArrayList expensesShow;
    private Expense expShow;
    private int expSelected=0;
    private ListIterator li;
    private ImageView first, last, prev, next;
    private Button buttonFinish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expenses);

        expNameShow = (TextView) findViewById(R.id.textViewShowNameVal);
        expAmountShow =(TextView) findViewById(R.id.textViewShowAmountVal);
        expCategoryShow = (TextView) findViewById(R.id.textViewShowCategoryVal);
        expDateShow = (TextView) findViewById(R.id.textViewShowDateVal);
        billImageShow = (ImageView) findViewById(R.id.imageViewReceipt);
        first = (ImageView) findViewById(R.id.imageViewFirstExp);
        last = (ImageView) findViewById(R.id.imageViewLastExp);
        prev = (ImageView) findViewById(R.id.imageViewPrev);
        next = (ImageView) findViewById(R.id.imageViewNext);
        buttonFinish = (Button) findViewById(R.id.buttonFinish);

        expensesShow = (ArrayList<Expense>) getIntent().getExtras().getSerializable(MainActivity.EXPENSES_KEY);
        showDetails(0);

        first.setOnClickListener(this);
        last.setOnClickListener(this);
        prev.setOnClickListener(this);
        next.setOnClickListener(this);
        buttonFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.imageViewFirstExp) {
            expSelected = 0;
            showDetails(expSelected);
        }
        else if(v.getId() == R.id.imageViewLastExp) {
            expSelected = expensesShow.size()-1;
            showDetails(expSelected);
        }
        else if(v.getId() == R.id.imageViewPrev) {
            if (expSelected>0) {
                expSelected--;
                showDetails(expSelected);
            } else {
                Toast.makeText(getApplicationContext(),"No more expenses to show", Toast.LENGTH_SHORT).show();
            }
        }
        else if(v.getId() == R.id.imageViewNext) {
            if(expSelected<expensesShow.size()-1) {
                expSelected++;
                showDetails(expSelected);
            } else {
                Toast.makeText(getApplicationContext(),"No more expenses to show", Toast.LENGTH_SHORT).show();
            }
        }
        else if(v.getId() == R.id.buttonFinish) {
            finish();
        }
    }

    public void showDetails(int index) {
        expShow = (Expense) expensesShow.get(index);

        expNameShow.setText(expShow.getExpName());
        expAmountShow.setText("$ "+expShow.getExpAmount());
        expCategoryShow.setText(expShow.getExpCategory());
        expDateShow.setText(expShow.getExpDate());
        billImageShow.setImageDrawable(null);
        Uri img = expShow.getBillImage();
        Bitmap imgBitmap = null;
        if(expShow.getBillImage() != Uri.EMPTY)
        {
            try {
                imgBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),expShow.getBillImage());
                billImageShow.setImageBitmap(imgBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Image not available",Toast.LENGTH_SHORT).show();
        }
    }
}
