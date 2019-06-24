package com.yakovlaptev.university;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.yakovlaptev.university.entity.Survey;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CallWebService extends AsyncTask<String, Void, String> {

    View view;
    PieChart mPieChart;
    PieChart mPieChart2;

    private String SOAP_ACTION;
    private String METHOD_NAME;
    private String PARAMETER_NAME;
    private String REQUESTVALUE;
    private Context context;

    private final DefaultHttpClient httpClient = new DefaultHttpClient();


    public CallWebService(String action, String method, String parameter_name, String requestValue, View view, PieChart mPieChart, PieChart mPieChart2,Context context) {
        this.SOAP_ACTION = action;
        this.METHOD_NAME = method;
        this.PARAMETER_NAME = parameter_name;
        this.REQUESTVALUE = requestValue;
        this.view = view;
        this.mPieChart = mPieChart;
        this.mPieChart2 = mPieChart2;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPostExecute(String s) {
        XmlPullParser parser;
        List result = new ArrayList<>();
        try {
            parser = createXmlPullParser(s);
            result = parseXML(parser);
            //Log.i("result", result.toString());
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
        String[] resultArray = new String[result.size()];
        for (int k = 0; k < result.size(); k++) {
            resultArray[k] = result.get(k).toString();
        }
        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, resultArray);
        if (view.getClass() == ListView.class) {
            ListView list = (ListView) view;
            list.setAdapter(adapter);
        } else {
            Spinner spinner = (Spinner) view;
            spinner.setAdapter(adapter);
        }
        if (mPieChart != null && mPieChart2 != null ) {
            Random rnd = new Random();
            Map<String, Integer> hashMapForAnswer1 = new HashMap<String, Integer>();
            Map<String, Integer> hashMapForAnswer2 = new HashMap<String, Integer>();
            for (int k = 0; k < result.size(); k++) {
                Object object = result.get(k);
                if (object.getClass() == Survey.class) {
                    Survey survey = (Survey) object;
                    if (hashMapForAnswer1.containsKey(survey.getAnswer1())) {
                        hashMapForAnswer1.put(survey.getAnswer1(), hashMapForAnswer1.get(survey.getAnswer1()) + 1);
                    } else {
                        hashMapForAnswer1.put(survey.getAnswer1(), 1);
                    }
                    Log.i("hashMapForPie", String.valueOf(hashMapForAnswer1.get(survey.getAnswer1())));
                    if (hashMapForAnswer2.containsKey(survey.getAnswer2())) {
                        hashMapForAnswer2.put(survey.getAnswer2(), hashMapForAnswer2.get(survey.getAnswer2()) + 1);
                    } else {
                        hashMapForAnswer2.put(survey.getAnswer2(), 1);
                    }
                }
            }
            for (String key : hashMapForAnswer1.keySet()) {
                mPieChart.addPieSlice(new PieModel(key, hashMapForAnswer1.get(key), new Random().nextInt()));
            }
            mPieChart.setTooltipText("Вопрос 1");
            mPieChart.startAnimation();
            for (String key : hashMapForAnswer2.keySet()) {
                mPieChart2.addPieSlice(new PieModel(key, hashMapForAnswer2.get(key), new Random().nextInt()));
            }
            mPieChart.setTooltipText("Вопрос 2");

            mPieChart2.startAnimation();
        }

    }

    @Override
    protected String doInBackground(String... params) {
        String URL = "http://192.168.137.126:8080/ws";
        String NAMESPACE = "http://192.168.137.126:8080/soapservice";
        String envelope = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tns=\"" + NAMESPACE + "\">" +
                "<soapenv:Body>" +
                "<tns:" + this.METHOD_NAME + ">" +
                "<tns:" + this.PARAMETER_NAME + ">" + this.REQUESTVALUE + "</tns:" + this.PARAMETER_NAME + ">" +
                "</tns:" + this.METHOD_NAME + ">" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>";
        HttpParams httpParams = httpClient.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
        HttpConnectionParams.setSoTimeout(httpParams, 15000);
        HttpProtocolParams.setUseExpectContinue(httpClient.getParams(), true);
        HttpPost httppost = new HttpPost(URL);
        httppost.setHeader("soapaction", this.SOAP_ACTION);
        httppost.setHeader("Content-Type", "text/xml; charset=utf-8");
        String responseString = "";
        try {
            HttpEntity entity = new StringEntity(envelope);
            httppost.setEntity(entity);
            ResponseHandler<String> rh = new ResponseHandler<String>() {
                public String handleResponse(HttpResponse response) throws IOException {
                    HttpEntity entity = response.getEntity();
                    StringBuilder out = new StringBuilder();
                    byte[] b = EntityUtils.toByteArray(entity);
                    out.append(new String(b, 0, b.length));
                    return out.toString();
                }
            };
            responseString = httpClient.execute(httppost, rh);
            Log.i("responseString", responseString);
        } catch (Exception e) {
            Log.v("exception", e.toString());
        }
        httpClient.getConnectionManager().shutdown();
        return responseString;
    }

    @NonNull
    private XmlPullParser createXmlPullParser(String s) throws XmlPullParserException {
        XmlPullParser parser = Xml.newPullParser();
        InputStream inputStream = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, null);
        return parser;
    }

    private List parseXML(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Survey> surveys = new ArrayList<>();
        List<String> items = new ArrayList<>();
        int eventType = parser.getEventType();
        Survey survey = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = "";
            if (eventType == XmlPullParser.START_DOCUMENT) {
                surveys = new ArrayList<>();
            } else if (eventType == XmlPullParser.START_TAG) {
                name = parser.getName();
                if (name.equals("ns2:value")) {
                    survey = new Survey();
                } else if (name.equals("ns2:item")) {
                    items.add(parser.nextText());
                } else if (survey != null) {
                    switch (name) {
                        case "ns2:id": {
                            String value = parser.nextText();
                            survey.setId(Integer.parseInt(value));
                            break;
                        }
                        case "ns2:institute": {
                            String value = parser.nextText();
                            survey.setInstitute(value);
                            break;
                        }
                        case "ns2:department": {
                            String value = parser.nextText();
                            survey.setDepartment(value);
                            break;
                        }
                        case "ns2:specialtyCode": {
                            String value = parser.nextText();
                            survey.setSpecialtyCode(value);
                            break;
                        }
                        case "ns2:specialty": {
                            String value = parser.nextText();
                            survey.setSpecialty(value);
                            break;
                        }
                        case "ns2:curse": {
                            String value = parser.nextText();
                            survey.setCurse(Integer.parseInt(value));
                            break;
                        }
                        case "ns2:studyForm": {
                            String value = parser.nextText();
                            survey.setStudyForm(value);
                            break;
                        }
                        case "ns2:answer1": {
                            String value = parser.nextText();
                            survey.setAnswer1(value);
                            break;
                        }
                        case "ns2:answer2": {
                            String value = parser.nextText();
                            survey.setAnswer2(value);
                            break;
                        }
                    }
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                name = parser.getName();
                if (name.equalsIgnoreCase("ns2:value") && survey != null) {
                    surveys.add(survey);
                }
            }
            eventType = parser.next();
        }

        if (surveys.size() > 0) {
            return surveys;
        } else {
            return items;
        }
    }

}