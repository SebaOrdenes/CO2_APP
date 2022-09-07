package com.tutsplus.code.android.asynctask.Backend;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tutsplus.code.android.asynctask.User;

import java.util.HashMap;
import java.util.Map;

public class Database {
    String str;

    public Database(){  //constructor
        FirebaseFirestore db = FirebaseFirestore.getInstance();

    }

    public FirebaseFirestore generateInstance(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db;
    }

    public void insertInCollection(FirebaseFirestore db, String collectionPath,  Map<String, Object> data){

        db.collection(collectionPath)
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public String createUser(FirebaseFirestore db, String email){
        Map<String, Object> data = new HashMap<>();


        data.put("email", email);
        db.collection("usuarios")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                       // db.collection("usuarios").document(documentReference.getId()).set(data);

                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        str = "ERROR";
                    }
                });
     //   db.collection("usuarios").document(str).set("SOYUNID");

        return str; //retorna el id unica del usuario(documento)
    }


    public void getUsuarioPorId(FirebaseFirestore db, String id){
        DocumentReference docRef = db.collection("usuarios").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }





}