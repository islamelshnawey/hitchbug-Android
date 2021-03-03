package com.hitchbug.library.util;

import android.content.Intent;
import android.util.Log;

import com.hitchbug.library.BuildConfig;
import com.hitchbug.library.core.Hitchbug;
import com.hitchbug.library.core.investigation.AppInfo;
import com.hitchbug.library.core.investigation.Crash;
import com.hitchbug.library.model.SuggestGetSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonParseing {

    private static String TAG = "JsonParseing";
    double current_latitude, current_longitude;

    public JsonParseing() {
    }

    public JsonParseing(double current_latitude, double current_longitude) {
        this.current_latitude = current_latitude;
        this.current_longitude = current_longitude;
    }

    public String getParseJson(Crash mCrash, AppInfo mAppInfo) {

        List<SuggestGetSet> ListData = new ArrayList<SuggestGetSet>();

        String code = "";
        String status = "";
        String message = "";

        try {

            MultipartUtility multipart = new MultipartUtility(Config.BASE_URL + Hitchbug.APP_KEY, "utf-8");

            /* This is to add parameter values */
            multipart.addFormField("application_id", mAppInfo.applicationId);
            multipart.addFormField("android_version", mCrash.getDeviceInfo().getSdk());
            multipart.addFormField("app_version_code", String.valueOf(mAppInfo.versionCode));
            multipart.addFormField("app_version_name", mAppInfo.versionName);
            multipart.addFormField("device_features", "n/a");
            multipart.addFormField("device_id", "n/a");
            multipart.addFormField("environment", BuildConfig.BUILD_TYPE);
            multipart.addFormField("file_path", mCrash.getPlace());
            multipart.addFormField("logcat", mCrash.getReason());
            multipart.addFormField("package_name", mAppInfo.packageName);
            multipart.addFormField("phone_model", mCrash.getDeviceInfo().getManufacturer() + "\n" + mCrash.getDeviceInfo().getBrand()
                    + "\n" + mCrash.getDeviceInfo().getName()
                    + "\n" + mCrash.getDeviceInfo().getSdk());
            multipart.addFormField("report_id", String.valueOf(mCrash.getId()));
            multipart.addFormField("stack_trace", mCrash.getStackTrace());
            multipart.addFormField("user_comment", "n/a");
            multipart.addFormField("user_crash_date", String.valueOf(mCrash.getDate()));
            multipart.addFormField("user_email", mAppInfo.email);

            List<String> response = multipart.finish();

            for (String line : response) {

                // Get your server response here.
                System.out.println(line);

                JSONObject jsonResponse = new JSONObject(line);

                if (jsonResponse != null) {

                    Log.d("response=", jsonResponse.toString());

                    JSONObject jObj = new JSONObject(jsonResponse.toString());
                    status = recurseKeys(jObj, "status");
                    code = recurseKeys(jObj, "code");
                    message = recurseKeys(jObj, "message");

                    if (status.equals("true")) {
                        JSONObject jobject = jsonResponse.getJSONObject("data");

                        if (jobject.has("data")) {

                            if (jobject.has("report_id")) {
                                jobject = jobject.getJSONObject("report_id");

                            }

                        }

                    }

                } else {
                    Log.d(TAG, "Response Is Null !!");
                }

            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        return code;
    }

    public static String recurseKeys(JSONObject jObj, String findKey) throws JSONException {
        String finalValue = "";
        if (jObj == null) {
            return "";
        }

        Iterator<String> keyItr = jObj.keys();
        Map<String, String> map = new HashMap<>();

        while (keyItr.hasNext()) {
            String key = keyItr.next();
            try {
                map.put(key, jObj.getString(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        for (Map.Entry<String, String> e : (map).entrySet()) {
            String key = e.getKey();
            if (key.equalsIgnoreCase(findKey)) {
                return jObj.getString(key);
            }

            // read value
            Object value = jObj.get(key);

            if (value instanceof JSONObject) {
                finalValue = recurseKeys((JSONObject) value, findKey);
            }
        }

        // key is not found
        return finalValue;
    }
}