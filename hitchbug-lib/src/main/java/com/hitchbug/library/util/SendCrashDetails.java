package com.hitchbug.library.util;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hitchbug.library.BuildConfig;
import com.hitchbug.library.core.Hitchbug;
import com.hitchbug.library.core.investigation.AppInfo;
import com.hitchbug.library.core.investigation.Crash;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Islam Elshnawey on 1/20/21.
 * <p>
 * is.elshnawey@gmail.com
 **/
public class SendCrashDetails extends AsyncTask<String, Void, String> {

    private SendCrashListener mListner;
    private Crash mCrash;
    private AppInfo mAppInfo;

    public SendCrashDetails(Crash crash, AppInfo appInfo, SendCrashListener sendCrashListener) {
        mListner = sendCrashListener;
        mCrash = crash;
        mAppInfo = appInfo;
    }

    @Override
    protected String doInBackground(String... params) {

        JsonParseing jp = new JsonParseing();
        String code;
        code = jp.getParseJson(mCrash, mAppInfo);
        mListner.onFinsish(code);

        return "";
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.e("TAG", result); // this is expecting a response code to be sent from your server upon receiving the POST data
    }

    public interface SendCrashListener {
        void onFinsish(String result);
    }

}
