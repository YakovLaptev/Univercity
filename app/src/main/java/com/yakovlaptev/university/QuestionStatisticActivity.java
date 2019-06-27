package com.yakovlaptev.university;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.charts.ValueLineChart;

public class QuestionStatisticActivity extends AppCompatActivity {

    ValueLineChart valueLineChart1;
    ValueLineChart valueLineChart2;
    TextView textView4;
    TextView textView5;
    TextView textView6;

    String SOAP_ACTION = "";
    String METHOD_NAME = "";
    String PARAMETER_NAME = "";
    String REQUESTVALUE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_statistic);
        final Context context = this;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        valueLineChart1 = (ValueLineChart)findViewById(R.id.valueLineChart1);
        valueLineChart2 = (ValueLineChart)findViewById(R.id.valueLineChart2);
        textView6 = (TextView) findViewById(R.id.textView6);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView4 = (TextView) findViewById(R.id.textView4);

        SOAP_ACTION = "getAllResponse";
        METHOD_NAME = "getAllRequest";
        PARAMETER_NAME = "fieldName";

        new CallWebService(SOAP_ACTION, METHOD_NAME, PARAMETER_NAME, "answer1", null, valueLineChart1, null, context).execute();
        new CallWebService(SOAP_ACTION, METHOD_NAME, PARAMETER_NAME, "answer1", null, null, valueLineChart2, context).execute();
        new CallWebService("getAllQuestionResponse", "getAllQuestionRequest", "", "", null, textView6, textView4, context).execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
