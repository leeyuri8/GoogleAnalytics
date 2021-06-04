package com.yrabdelrhmn.googleanalytics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private FirebaseAnalytics myFirebaseAnalytics;
    FirebaseFirestore db;
    long timeEntry,timeLeave,timeSpent;
    String userID;
    String categoryName;
    Button foodButton , clothesButton ,electronicButton;
    Intent intent;
    @Override
    protected void onRestart() {
        super.onRestart();
        // init class Service in package utility
        Date serviceDate=new Service().calender();
        timeEntry =serviceDate.getTime();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myFirebaseAnalytics.getAppInstanceId().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<String> task) {
                if(task.isSuccessful()){
                    userID = task.getResult();
                    Log.e("time","timeSpwnt"+"");
                    Log.e("userid",userID);
                    HashMap<String,Object> detailsSession=new HashMap<>();
                    detailsSession.put("timePerSecond",timeSpent);
                    detailsSession.put("userId",userID);
                    detailsSession.put("pageName",categoryName);

                    CollectionReference collectionReference=db.collection("detailsSessionInScreen");
                    Task<DocumentReference> documentReferenceTask= collectionReference.add(detailsSession);
                    documentReferenceTask.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("success","uploadSession");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("failure","failureUploadSession");
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });


                }else if(task.isCanceled()){
                    Log.d("failure","failureGetUserIdSoNoSaveToFireStore");
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        foodButton = findViewById(R.id.food);
        clothesButton = findViewById(R.id.clothes);
        electronicButton = findViewById(R.id.electronic);
        categoryName = this.getClass().getSimpleName();

        myFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        db=FirebaseFirestore.getInstance();
        Date serviceDate=new Service().calender();
        timeEntry =serviceDate.getTime();
        Log.e("timeEntry",timeEntry+"");
        new Service().trackScreen(myFirebaseAnalytics,"Category Screen");
        foodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ProductsActivity.class);
                intent.putExtra("productsFood","food");
                timeLeave = System.currentTimeMillis();
                timeSpent = ((timeLeave - timeEntry)/1000);
                Log.e("timeTaken",timeSpent+"");
                new Service().selectContent(
                        myFirebaseAnalytics,
                        String.valueOf(new Random().nextInt(10000)),
                        "foodCategory","clickButton");
                startActivity(intent);
            }
        });
        clothesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this,ProductsActivity.class);
                intent.putExtra("productsClothes","clothes");
                timeLeave = System.currentTimeMillis();
                timeSpent = ((timeLeave - timeEntry)/1000);
                Log.e("timeTaken",timeSpent+"");

//               firebase analytics select content
                new Service().selectContent(
                        myFirebaseAnalytics,
                        String.valueOf(new Random().nextInt(10000)),
                        "Clothes","clickButton");
                startActivity(intent);
            }
        });
        electronicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this,ProductsActivity.class);
                intent.putExtra("productsElectronic","electronic");
                timeLeave = System.currentTimeMillis();
                timeSpent = ((timeLeave - timeEntry)/1000);
                Log.e("timeTaken",timeSpent+"");
                new Service().selectContent(
                        myFirebaseAnalytics,
                        String.valueOf(new Random().nextInt(10000)),
                        "electronic","clickButton");
                startActivity(intent);
            }
        });

    }


}