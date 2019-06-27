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
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.charts.ValueLineChart;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class QuestionStatisticActivity extends AppCompatActivity {

    ValueLineChart valueLineChart1;
    ValueLineChart valueLineChart2;
    TextView textView4;
    TextView textView5;
    TextView textView6;

    String SOAP_ACTION = "";
    String METHOD_NAME = "";
    String PARAMETER_NAME = "";

    Button export_button;
    ConstraintLayout container_page;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_statistic);
        final Context context = this;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        valueLineChart1 = (ValueLineChart) findViewById(R.id.valueLineChart1);
        valueLineChart2 = (ValueLineChart) findViewById(R.id.valueLineChart2);
        textView6 = (TextView) findViewById(R.id.textView6);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView4 = (TextView) findViewById(R.id.textView4);
        export_button = findViewById(R.id.export_button);
        container_page = findViewById(R.id.container_page);

        SOAP_ACTION = "getAllResponse";
        METHOD_NAME = "getAllRequest";
        PARAMETER_NAME = "fieldName";

//        new CallWebService(SOAP_ACTION, METHOD_NAME, PARAMETER_NAME, "answer1", null, valueLineChart1, null, context).execute();
//        new CallWebService(SOAP_ACTION, METHOD_NAME, PARAMETER_NAME, "answer1", null, null, valueLineChart2, context).execute();
//        new CallWebService("getAllQuestionResponse", "getAllQuestionRequest", "", "", null, textView6, textView4, context).execute();

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
                Toast.makeText(QuestionStatisticActivity.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }
    }
}
