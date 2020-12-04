package com.example.allab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.module.AppGlideModule;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class view extends AppCompatActivity {
TextView responn2;
    TextView name;
    TextView des1;
    TextView des2;
    TextView des3;
    String fname;
    String fDes1;
    String fDes2;
    String fDes3;
    String img_url;
    Context context;
    ImageView img_view;
    String test = "NO";
    private StorageReference mStorageRef;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String TAG = "FireBase_FireStore";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        context = getApplicationContext();
        name = findViewById(R.id.name);
        des1 = findViewById(R.id.des1);
        des2 = findViewById(R.id.des2);
        des3 = findViewById(R.id.des3);
        img_view = findViewById(R.id.img);
        db.collection(FirebaseID.Tablet)
                .whereEqualTo(FirebaseID.ID,FileUpload.Id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                fname = String.valueOf(document.get(FirebaseID.Name));
                                fDes1 = String.valueOf(document.get(FirebaseID.Des1));
                                fDes2 = String.valueOf(document.get(FirebaseID.Des2));
                                fDes3 = String.valueOf(document.get(FirebaseID.Des3));
                                img_url = String.valueOf(document.get(FirebaseID.Img));
                                fDes1 = fDes1.replace("bb","\n");
                                fDes2 = fDes2.replace("bb","\n");
                                fDes3 = fDes3.replace("bb","\n");
                                name.setText(fname);
                                des1.setText(fDes1);
                                des2.setText(fDes2);
                                des3.setText(fDes3);
                                mStorageRef = FirebaseStorage.getInstance().getReference().child("tablet/"+img_url);
                                mStorageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()){
                                            Glide.with(view.this)
                                                    .load(task.getResult())
                                                    .into(img_view);
                                        }else{
                                            Toast.makeText(view.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                Log.d(TAG,document.getId() +" => " + document.getData());
                            }

                        }
                        else{
                            Log.d(TAG,"ERROR getting documents: ", task.getException());
                        }
                    }
                });

    }

}