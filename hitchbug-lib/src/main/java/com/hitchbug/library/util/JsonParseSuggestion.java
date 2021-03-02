package com.hitchbug.library.util;

import android.content.Intent;
import android.util.Log;

import com.hitchbug.library.BuildConfig;
import com.hitchbug.library.core.Hitchbug;
import com.hitchbug.library.core.investigation.AppInfo;
import com.hitchbug.library.core.investigation.Crash;
import com.hitchbug.library.model.SuggestGetSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParseSuggestion {
    double current_latitude, current_longitude;

    public JsonParseSuggestion() {
    }

    public JsonParseSuggestion(double current_latitude, double current_longitude) {
        this.current_latitude = current_latitude;
        this.current_longitude = current_longitude;
    }

    public String getParseJsonWCF(Crash mCrash, AppInfo mAppInfo) {

        List<SuggestGetSet> ListData = new ArrayList<SuggestGetSet>();
        String code = "";

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
            multipart.addFormField("user_email", "n/a");

            List<String> response = multipart.finish();

            for (String line : response) {
                // Get your server response here.
                System.out.println(line);

                JSONObject jsonResponse = new JSONObject(line);

                if (jsonResponse != null) {

                    Log.d("response=", jsonResponse.toString());

                    boolean status = jsonResponse.getJSONObject("").getBoolean("status");

                    String message = jsonResponse.getJSONObject("").getString("message");
                    code = jsonResponse.getJSONObject("").getString("code");

                    if (status) {
                        JSONObject jobject = jsonResponse.getJSONObject("data");

                        if (jobject.has("data")) {

                            if (jobject.has("report_id")) {
                                jobject = jobject.getJSONObject("report_id");

                            }


                        }

                    }


                } else {

                    Log.d("", "");
                }

            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return code;

    }

}