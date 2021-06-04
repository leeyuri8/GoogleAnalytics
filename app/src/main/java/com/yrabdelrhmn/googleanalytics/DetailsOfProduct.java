package com.yrabdelrhmn.googleanalytics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;

public class DetailsOfProduct extends AppCompatActivity {
TextView productName;
TextView productDesc;
ImageView imageView;
private FirebaseAnalytics myFirebaseAnalytics;
String categoryName;
long timeEntry,timeLeave,timeSpent;
String userId;
FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_of_products);
        productName = findViewById(R.id.productName);
        productDesc = findViewById(R.id.productDescription);
        imageView = findViewById(R.id.productImage);

        categoryName = this.getClass().getSimpleName();
        myFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        db = FirebaseFirestore.getInstance();

        Date serviceDate= new Service().calender();
        timeEntry = serviceDate.getTime();

        Product product= (Product) getIntent().getExtras().getSerializable("DATA_PRODUCT");
        productName.setText(product.getProductName());
        productDesc.setText(product.getProductDetails());
        imageView.setImageResource(R.drawable.labtop);

        new Service().trackScreen(myFirebaseAnalytics,"Product"+product.getProductName()+"Screen");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Date serviceDate = new Service().calender();
        timeEntry =serviceDate.getTime();

    }

    @Override
    protected void onStop() {
        super.onStop();
        timeLeave = System.currentTimeMillis();
        timeSpent = ((timeLeave-timeEntry)/1000);
        myFirebaseAnalytics.getAppInstanceId().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<String> task) {
                if(task.isSuccessful()){
                    userId = task.getResult();
                    HashMap<String,Object> session = new HashMap<>();
                    session.put("timePerSecond",timeSpent);
                    session.put("userId",userId);
                    session.put("userName",categoryName);
                    CollectionReference cR = db.collection("detailsSessionInScreen");
                    Task<DocumentReference> dR = cR.add(session);
                    dR.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("success","uploadSession");

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Log.d("failure","failureUploadSession");
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }else if (task.isCanceled()){
                    Log.d("failure","failureGetUserId");
                }
            }
        });
    }
}