package com.physis.foot.stretching.http;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;


public class HttpAsyncTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "HttpRequester";
    private static final int connectTime = 4000;

    private String url;
    private String params = null;
    private String method = "POST";
    private String result = null;

    private OnResponseListener onResponseListener = null;

    public interface OnResponseListener{
        void onResponseListener(String url, String responseData);
    }

    public void setOnResponseListener(OnResponseListener listener){
        onResponseListener = listener;
    }


    public HttpAsyncTask(String url, String params){
        this.url = url;
        if(params != null)
            this.params = params;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();

            if(conn == null)
                return null;

            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod(method);
            conn.setConnectTimeout(connectTime);
            conn.setDoInput(true);

            if(params != null){
                conn.setDoOutput(true);
                OutputStreamWriter wr= new OutputStreamWriter(conn.getOutputStream());
                wr.write(params);
                wr.flush();
            }
            Log.e(TAG, " # HTTP Request > " +  url);

            int resCode = conn.getResponseCode();
            if(resCode != HttpURLConnection.HTTP_OK)
                return null;
            InputStream is = conn.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] byteBuffer = new byte[1536];
            byte[] byteData;
            int nLength;
            while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                baos.write(byteBuffer, 0, nLength);
            }

            byteData = baos.toByteArray();
            result = new String(byteData);
            Log.e(TAG, " # HTTP Response : " +  result);

            baos.close();
            is.close();
            conn.disconnect();

        } catch(Exception e) {
            Log.e(TAG, " # HTTP Error : " +  e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(onResponseListener != null)
            onResponseListener.onResponseListener(url, result);
    }
}
