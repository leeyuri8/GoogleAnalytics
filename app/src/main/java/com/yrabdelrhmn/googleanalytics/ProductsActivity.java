package com.yrabdelrhmn.googleanalytics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {
 String userId;
 String categoryName;
  RecyclerView rv;
RvAdapter rvAdapter;
FirebaseFirestore db;
long timeEntry , timeLeave,timeSpent;
private FirebaseAnalytics myFirebaseAnalytics;

    @Override
    protected void onRestart() {
        super.onRestart();
        Date serviceDate = new Service().calender();
        timeEntry = serviceDate.getTime();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);

        rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new GridLayoutManager(this,1));

        categoryName = this.getClass().getSimpleName();
        myFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        db = FirebaseFirestore.getInstance();
        Date serviceDate = new Service().calender();
        timeEntry= serviceDate.getTime();
   if(getIntent().getStringExtra("foodProducts")!=null){
       new Service().trackScreen(myFirebaseAnalytics,"Food Products Screen");
       rvAdapter = new RvAdapter(insertFoodProducts(),ProductsActivity.this,myFirebaseAnalytics);
       rv.setAdapter(rvAdapter);

   }else if(getIntent().getStringExtra("clothesProducts")!= null){
        new Service().trackScreen(myFirebaseAnalytics,"Clothes Products Screen");
       rvAdapter = new RvAdapter(insertClothesProducts(),ProductsActivity.this,myFirebaseAnalytics);
        rv.setAdapter(rvAdapter);
        }else if(getIntent().getStringExtra("ElectronicProducts")!=null){
          new Service().trackScreen(myFirebaseAnalytics,"Electronic Products Screen");
       rvAdapter = new RvAdapter(insertElectronicProducts(),ProductsActivity.this,myFirebaseAnalytics);
        rv.setAdapter(rvAdapter);
   }

    }

    @Override
    protected void onStop() {
        super.onStop();
        timeLeave = System.currentTimeMillis();
        timeSpent = ((timeLeave - timeEntry) / 1000);
        Log.e("timeLeave", timeLeave + "/" + timeEntry + "/" + timeSpent);
        myFirebaseAnalytics.getAppInstanceId().addOnCompleteListener(new OnCompleteListener<String>() {
                                                                         @Override
                                                                         public void onComplete(@NonNull @NotNull Task<String> task) {
                                                                             if (task.isSuccessful()) {
                                                                                 userId = task.getResult();
                                                                                 HashMap<String, Object> session = new HashMap<>();
                                                                                 session.put("timePerSecond", timeSpent);
                                                                                 session.put("userId", userId);
                                                                                 session.put("categoryName", categoryName);
                                                                                 CollectionReference cr = db.collection("sessionScreen");
                                                                                 Task<DocumentReference> drTask = cr.add(session);
                                                                                 drTask.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                     @Override
                                                                                     public void onSuccess(DocumentReference documentReference) {
                                                                                         Log.e("success", "uploadSession");
                                                                                     }

                                                                                 }).addOnFailureListener(new OnFailureListener() {
                                                                                     @Override
                                                                                     public void onFailure(@NonNull @NotNull Exception e) {
                                                                                         Log.d("failure", "failureUploadSession");
                                                                                         Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                                                                     }

                                                                                 });
                                                                             } else if (task.isCanceled()) {
                                                                                 Log.d("failure", "failureGetUserId");
                                                                             }
                                                                         }
                                                                     }
        );
    }


    List<Product> insertClothesProducts() {
List<Product> foodList = new ArrayList<Product>();
        foodList.add(new Product("1","image1","description1"));
        foodList.add(new Product("2","image1","description1"));
        foodList.add(new Product("3","image1","description1"));
        foodList.add(new Product("4","image1","description1"));
        foodList.add(new Product("5","image1","description1"));
        foodList.add(new Product("6","image1","description1"));
   return foodList;
    }

    List<Product> insertFoodProducts() {
        List<Product> clothesList = new ArrayList<>();
        clothesList.add(new Product("scarf","image","e"));
        clothesList.add(new Product("pant","image","e"));
        clothesList.add(new Product("top","image","e"));
        clothesList.add(new Product("shoes","image","e"));
        clothesList.add(new Product("abaya","image","e"));
        clothesList.add(new Product("hijab","image","e"));
        return clothesList;
    }

    List<Product> insertElectronicProducts() {
        List<Product> electronicList = new ArrayList<>();
        electronicList.add(new Product("laptop","","d1"));
        electronicList.add(new Product("iPhone","","d2"));
        electronicList.add(new Product("samsung","","d3"));
        electronicList.add(new Product("lenovo","","d4"));
        electronicList.add(new Product("dell","","d5"));
   return electronicList;
    }
      }