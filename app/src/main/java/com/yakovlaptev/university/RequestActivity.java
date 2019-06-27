package com.yakovlaptev.university;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.w3c.dom.Text;

public class RequestActivity extends AppCompatActivity {

    Button button;
    ListView list;
    Spinner spinner;
    PieChart mPieChart;
    PieChart mPieChart2;
    TextView textView6;
    TextView textView4;

    String SOAP_ACTION = "";
    String METHOD_NAME = "";
    String PARAMETER_NAME = "";
    String REQUESTVALUE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        final Context context = this;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        button = (Button)findViewById(R.id.button);
        list = (ListView)findViewById(R.id.listView);
        spinner = (Spinner) findViewById(R.id.spinner);
        mPieChart = (PieChart) findViewById(R.id.piechart);
        mPieChart2 = (PieChart) findViewById(R.id.piechart2);
        textView6 = (TextView) findViewById(R.id.textView6);
        textView4 = (TextView) findViewById(R.id.textView4);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selected = spinner.getSelectedItem().toString();
                PARAMETER_NAME = getIntent().getStringExtra("request");
                switch (getIntent().getStringExtra("request")) {
                    case "specialtyCode":
                        PARAMETER_NAME = "code";
                        SOAP_ACTION = "getAnswersByCodeResponse";
                        METHOD_NAME = "getAnswersByCodeRequest";
                        break;
                    case "curse":
                        SOAP_ACTION = "getAnswersByCurseResponse";
                        METHOD_NAME = "getAnswersByCurseRequest";
                        break;
                    case "institute":
                        SOAP_ACTION = "getAnswersByInstituteResponse";
                        METHOD_NAME = "getAnswersByInstituteRequest";
                        break;
                    case "specialty":
                        SOAP_ACTION = "getAnswersBySpecialtyResponse";
                        METHOD_NAME = "getAnswersBySpecialtyRequest";
                        break;
                    case "department":
                        SOAP_ACTION = "getAnswersByDepartmentResponse";
                        METHOD_NAME = "getAnswersByDepartmentRequest";
                        break;
                    case "studyForm":
                        SOAP_ACTION = "getAnswersByStudyFormResponse";
                        METHOD_NAME = "getAnswersByStudyFormRequest";
                        break;
                }
                REQUESTVALUE = selected;
                Log.i("selected", selected);
                new CallWebService(SOAP_ACTION, METHOD_NAME, PARAMETER_NAME, REQUESTVALUE, list, mPieChart, mPieChart2, context).execute();
                new CallWebService("getAllQuestionResponse", "getAllQuestionRequest", "", "", null, textView6, textView4, context).execute();
            }
        });

        SOAP_ACTION = "getAllResponse";
        METHOD_NAME = "getAllRequest";
        PARAMETER_NAME = "fieldName";
        REQUESTVALUE = getIntent().getStringExtra("request");

        new CallWebService(SOAP_ACTION, METHOD_NAME, PARAMETER_NAME, REQUESTVALUE, spinner, null, null, context).execute();
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
