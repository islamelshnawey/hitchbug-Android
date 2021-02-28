package com.hitchbug.library.util;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hitchbug.library.core.Hitchbug;
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
    //private UserModel mUser;

    public SendCrashDetails(Crash crash,/* UserModel user,*/ SendCrashListener sendCrashListener) {
        mListner = sendCrashListener;
        mCrash = crash;
        //mUser = user ;
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            main();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    private void main() throws IOException {

        URL url = new URL(Config.BASE_URL+ Hitchbug.APP_KEY);

        Map<String, Object> params2 = new LinkedHashMap<>();
        params2.put("application_id", "1");
        params2.put("android_version", android.os.Build.VERSION.SDK_INT);
        params2.put("app_version_code", "BuildConfig.VERSION_CODE");
        params2.put("app_version_name", "BuildConfig.VERSION_NAME");
        params2.put("device_features", "n/a");
        params2.put("device_id", "n/a");
        params2.put("environment", "BuildConfig.BUILD_TYPE");
        params2.put("file_path", mCrash.getPlace());
        params2.put("logcat", mCrash.getReason());
        params2.put("package_name", "BuildConfig.APPLICATION_ID");
        params2.put("phone_model", mCrash.getDeviceInfo().getManufacturer() + "\n" + mCrash.getDeviceInfo().getBrand()
                + "\n" + mCrash.getDeviceInfo().getName()
                + "\n" + mCrash.getDeviceInfo().getSdk());
        params2.put("report_id", mCrash.getId());
        params2.put("stack_trace", mCrash.getStackTrace());
        params2.put("user_comment", "n/a");
        params2.put("user_crash_date", mCrash.getDate());
        params2.put("user_email", "mUser.getId()");

        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonNode = mapper.valueToTree(params2);
        String jsonInputString = jsonNode.toString();
        byte[] input = jsonInputString.getBytes("utf-8");

        System.out.println(jsonNode);

        StringBuilder postData = new StringBuilder();

        for (Map.Entry<String, Object> param : params2.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        byte[] postDataBytes = postData.toString().getBytes("utf-8");

        System.out.println("Post Data ==+==+==+==");
        System.out.println(postData);

        // From the above URL object,
        // we can invoke the openConnection method to get the HttpURLConnection object.
        // We can't instantiate HttpURLConnection directly, as it's an abstract class:
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        //To send request content, let's enable the URLConnection object's doOutput property to true.
        //Otherwise, we'll not be able to write content to the connection output stream:
        con.setDoOutput(true);
        con.setDoInput(true);
        // To send a POST request, we'll have to set the request method property to POST:
        con.setRequestMethod("POST");
        //set the timeout in milliseconds
        //con.setConnectTimeout(7000);
        // Set the Request Content-Type Header Parameter
        // Set “content-type” request header to “application/json” to send the request content in JSON form.
        // This parameter has to be set to send the request body in JSON format.
        // Failing to do so, the server returns HTTP status code “400-bad request”.
        con.setRequestProperty("Content-Type", "application/json");
        //con.setRequestProperty("Content-Length", Integer.toString(input.length));
        con.setRequestProperty("Host", "www.hitchbug.com");
        //Set Response Format Type
        //Set the “Accept” request header to “application/json” to read the response in the desired format:
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Connection", "Keep-Alive");

        System.out.println(con.getRequestProperties());

        try (OutputStream os = con.getOutputStream()) {
            //byte[] input = jsonNode.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int code = con.getResponseCode();
        System.out.println(code);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
            mListner.onFinsish(response.toString());

        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }
}
