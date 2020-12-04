package com.example.allab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class search extends AppCompatActivity{
    final private static String TAG = "ALLAB";
    Button camera;
    Button gallery;
    ImageView a;
    File image;
    final static int TAKE_PICTURE = 1;
    String mCurrentPhotoPath;
    final static int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_CODE = 0;
    Button upload;
    AppCompatDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        camera = findViewById(R.id.camera);
        a = findViewById(R.id.imageView_test);
        gallery = findViewById(R.id.gallery);
        upload = findViewById(R.id.upload);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한 설정 완료");
            } else {
                Log.d(TAG, "권한 설정 요청");
                ActivityCompat.requestPermissions(search.this, new String[]
                        {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.camera:
                        //Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //startActivityForResult(cameraIntent, TAKE_PICTURE);
                        dispatchTakePictureIntent();
                        break;
                }
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUpload.Send_Server(image);
                startProgress();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), view.class);
                        startActivity(intent);
                    }
                },10000);

            }
        });
    }
    public void startProgress() {

        progressON(this,"Loading...");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressOFF();
            }
        }, 10000);


    }
    //권한 요청
    //@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }

    //촬영한 영상 가져오는 부분
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        try {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO: {
                    File file = new File(mCurrentPhotoPath);
                    Bitmap bitmap;
                    if (Build.VERSION.SDK_INT >= 29) //빌드 버전에 따라 다르게해줘야됨
                    {
                        ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), Uri.fromFile(file));
                        try {
                            bitmap = ImageDecoder.decodeBitmap(source);
                            if (bitmap != null) {
                                a.setImageBitmap(bitmap);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                            if (bitmap != null) {
                                a.setImageBitmap(bitmap);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri uri = intent.getData();
                    InputStream in = getContentResolver().openInputStream(intent.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    a.setImageBitmap(img);
                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }

    //이미지 파일로 저장
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        image = File.createTempFile(
                imageFileName, ".jpeg", storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //카메라 인텐트 실행
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.mydomain.FileProvider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    public void progressON(Activity activity, String message) {

        if (activity == null || activity.isFinishing()) {
            return;
        }


        if (progressDialog != null && progressDialog.isShowing()) {
            progressSET(message);
        } else {

            progressDialog = new AppCompatDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.progress_loading);
            progressDialog.show();

        }


        final ImageView img_loading_frame = (ImageView) progressDialog.findViewById(R.id.iv_frame_loading);
        final AnimationDrawable frameAnimation = (AnimationDrawable) img_loading_frame.getBackground();
        img_loading_frame.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });

        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }


    }
    public void progressSET(String message) {

        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }

        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }

    }
    public void progressOFF() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


}