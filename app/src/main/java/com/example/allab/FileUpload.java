package com.example.allab;

import android.app.DownloadManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.os.Looper.getMainLooper;

public class FileUpload {
    static String data;
    static String Id;
    public static void Send_Server(File file){
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file",file.getName(),RequestBody.create(MultipartBody.FORM,file))
                .build();
        Request request = new Request.Builder()
                .url("https://0f0e146e0b89.ngrok.io/upload")
                //.url("https://48f65fbdef8b.ngrok.io/upload")
                //.url("https://8f020555f179.ngrok.io/upload")
                .post(requestBody)
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.MINUTES)
                .readTimeout(30,TimeUnit.MINUTES)
                .build();


        client.newCall(request).enqueue((new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse( Call call,Response response) throws IOException {

                //Log.d("Test : ",response.body().string());
                Log.d("Test2 : ",response.body().toString());
                JSONObject json = null;
                try {
                    json = new JSONObject(response.body().string());
                    Id = json.getString("0");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e){
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }

                data = response.body().toString();
            }
        }));
    }
}
