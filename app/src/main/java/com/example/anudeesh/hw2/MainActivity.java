package com.example.anudeesh.hw2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final int EXPENSE_ADD = 0x00;
    static final int EXPENSE_EDIT = 0x01;
    static final int EXPENSE_DELETE = 0x02;
    private Button buttonAdd, buttonEdit, buttonDelete, buttonShow, buttonFinish;
    ArrayList<Expense> expenses = new ArrayList<Expense>();
    final static String EXPENSES_KEY = "EXPENSES";
    final static String NEWEXP_KEY = "EXP";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        buttonAdd = (Button) findViewById(R.id.buttonAddExpense);
        buttonEdit = (Button) findViewById(R.id.buttonEditExpense);
        buttonDelete = (Button) findViewById(R.id.buttonDeleteExpense);
        buttonShow = (Button) findViewById(R.id.buttonShowExpenses);
        buttonFinish = (Button) findViewById(R.id.buttonFinish);

        buttonAdd.setOnClickListener(this);
        buttonEdit.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);
        buttonShow.setOnClickListener(this);
        buttonFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonAddExpense) {
            Intent intent = new Intent(MainActivity.this,AddExpenseActivity.class);
            startActivityForResult(intent,EXPENSE_ADD);
        }
        else if (v.getId() == R.id.buttonEditExpense) {
            if(expenses.isEmpty()) {
                Toast.makeText(getApplicationContext(),"No Expenses available, Add an Expense",Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(MainActivity.this,EditExpenseActivity.class);
                intent.putExtra(EXPENSES_KEY,expenses);
                startActivityForResult(intent,EXPENSE_EDIT);
            }
        }
        else if (v.getId() == R.id.buttonDeleteExpense) {
            if(expenses.isEmpty()) {
                Toast.makeText(getApplicationContext(),"No Expenses available, Add an Expense",Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(MainActivity.this,DeleteExpense.class);
                intent.putExtra(EXPENSES_KEY,expenses);
                startActivityForResult(intent,EXPENSE_DELETE);
            }
        }
        else if (v.getId() == R.id.buttonShowExpenses) {
            if(expenses.isEmpty()) {
                Toast.makeText(getApplicationContext(),"No Expenses available, Add an Expense",Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(MainActivity.this,ShowExpensesActivity.class);
                intent.putExtra(EXPENSES_KEY,expenses);
                startActivity(intent);
            }
        }
        else if (v.getId() == R.id.buttonFinish) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EXPENSE_ADD)
        {
            if(resultCode == RESULT_OK)
            {
                Expense newexp =  data.getExtras().getParcelable(MainActivity.NEWEXP_KEY);
                expenses.add(newexp);
                Collections.sort(expenses);
                Toast.makeText(getApplicationContext(),"Successfully Added",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Failed to Add",Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == EXPENSE_EDIT)
        {
            if(resultCode == RESULT_OK)
            {
                ArrayList updated_expenses = (ArrayList<Expense>) data.getExtras().getSerializable(EXPENSES_KEY);
                expenses = updated_expenses;
                Collections.sort(expenses);
                Toast.makeText(getApplicationContext(),"Successfully updated",Toast.LENGTH_SHORT).show();

            }
            else
            {
                Toast.makeText(getApplicationContext(),"Failed to update",Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == EXPENSE_DELETE)
        {
            if(resultCode == RESULT_OK)
            {
                ArrayList updated_expenses = (ArrayList<Expense>) data.getExtras().getSerializable(EXPENSES_KEY);
                expenses = updated_expenses;
                Collections.sort(expenses);
                Toast.makeText(getApplicationContext(),"Successfully deleted",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Failed to delete",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
