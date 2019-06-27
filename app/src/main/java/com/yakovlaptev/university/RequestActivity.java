package com.yakovlaptev.university;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.eazegraph.lib.charts.PieChart;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

    Button export_button;
    ConstraintLayout container_page;
    Bitmap bitmap;

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
        export_button = findViewById(R.id.export_button);
        container_page = findViewById(R.id.container_page);

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

        export_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("size", " " + container_page.getWidth() + "  " + container_page.getWidth());
                bitmap = loadBitmapFromView(container_page, container_page.getWidth(), container_page.getHeight());
                createPdf();
            }
        });
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

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    private void createPdf() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels;
        float width = displaymetrics.widthPixels;

        int convertHighet = (int) hight, convertWidth = (int) width;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);

        String targetPdf = "storage/emulated/0/pdf_export_survey.pdf";
        File filePath;
        filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        document.close();
        Toast.makeText(this, "PDF of Scroll is created/", Toast.LENGTH_SHORT).show();

        //openGeneratedPDF();

    }

    private void openGeneratedPDF() {
        File file = new File("storage/emulated/0/pdf_export_survey.pdf");
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(RequestActivity.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }
    }
}
