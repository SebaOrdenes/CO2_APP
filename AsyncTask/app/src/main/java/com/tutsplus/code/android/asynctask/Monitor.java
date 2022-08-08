package com.tutsplus.code.android.asynctask;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Monitor {
     private ArrayList apps;
     private ArrayList porcentajeBateria;


     public Monitor(Apps app, int porcBateria){  //constructor
          apps= new ArrayList<>();
          porcentajeBateria= new ArrayList();
          apps.add(app.listaApps);
          porcentajeBateria.add(porcBateria);
     }


     public void anadirApps(Apps app){
          apps.add(app.listaApps);
     }

     public void anadirPorcentajeBateria(int  porcjeBateria){
          porcentajeBateria.add(porcjeBateria);
     }






}
