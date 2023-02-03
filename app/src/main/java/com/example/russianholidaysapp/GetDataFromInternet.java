package com.example.russianholidaysapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class GetDataFromInternet extends AsyncTask <URL, Void, String> {

    private static final String TAG = "GetDataFromInternet";

    public interface AsyncResponse {
        void processFinished(String output);
    }

    public AsyncResponse delegate;

    public GetDataFromInternet(AsyncResponse delegate){
        this.delegate = delegate;
    }
    protected String getResponseFromHttpGetURL(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try{
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            String result;

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                result = scanner.next();
                return result;
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }

    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute: called");
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(URL[] url) {
        Log.d(TAG, "doInBackground: called");

        String result = null;
        URL urlQ = url[0];

        try {
            result = getResponseFromHttpGetURL(urlQ);
        } catch (IOException e){
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, "onPostExecute: called");
        Log.d(TAG, "onPostExecuteResult: "+result);

        delegate.processFinished(result);
    }


}