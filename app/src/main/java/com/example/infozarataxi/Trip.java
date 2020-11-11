package com.example.infozarataxi;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Trip implements Serializable {


    private int intentosTrip= 0;
    private String carreraInterna = "";
    private String salidaFechahora = "";
    private String salidaLatitud = "";
    private String salidaLonguitud = "";
    private String recogidaFechahora = "";
    private String recogidaLatitud = "";
    private String recogidaLonguitud = "";
    private String finalizacionFechahora = "";
    private String finalizacionLatitud = "";
    private String finalizacionLonguitud = "";
    private String precioFinal = "";

    public boolean getAceppt() {
        return aceppt;
    }

    private boolean aceppt = false;

    public String getCarreraInterna() {
        return carreraInterna;
    }

    public int getIntentosTrip() {
        return intentosTrip;
    }

    public void setIntentosTrip() {
        intentosTrip += 1;
    }

    public String getSalidaFechahora() {
        return salidaFechahora;
    }

    public void setSalidaFechahora(String salidaFechahora) {
        this.salidaFechahora = salidaFechahora;
    }

    public String getSalidaLatitud() {
        return salidaLatitud;
    }

    public void setSalidaLatitud(String salidaLatitud) {
        this.salidaLatitud = salidaLatitud;
    }

    public String getSalidaLonguitud() {
        return salidaLonguitud;
    }

    public void setSalidaLonguitud(String salidaLonguitud) {
        this.salidaLonguitud = salidaLonguitud;
    }

    public String getRecogidaFechahora() {
        return recogidaFechahora;
    }

    public void setRecogidaFechahora(String recogidaFechahora) {
        this.recogidaFechahora = recogidaFechahora;
    }

    public String getRecogidaLatitud() {
        return recogidaLatitud;
    }

    public void setRecogidaLatitud(String recogidaLatitud) {
        this.recogidaLatitud = recogidaLatitud;
    }

    public String getRecogidaLonguitud() {
        return recogidaLonguitud;
    }

    public void setRecogidaLonguitud(String recogidaLonguitud) {
        this.recogidaLonguitud = recogidaLonguitud;
    }

    public String getFinalizacionFechahora() {
        return finalizacionFechahora;
    }

    public void setFinalizacionFechahora(String finalizacionFechahora) {
        this.finalizacionFechahora = finalizacionFechahora;
    }

    public String getFinalizacionLatitud() {
        return finalizacionLatitud;
    }

    public void setFinalizacionLatitud(String finalizacionLatitud) {
        this.finalizacionLatitud = finalizacionLatitud;
    }

    public String getFinalizacionLonguitud() {
        return finalizacionLonguitud;
    }

    public void setFinalizacionLonguitud(String finalizacionLonguitud) {
        this.finalizacionLonguitud = finalizacionLonguitud;
    }

    public String getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(String precioFinal) {
        this.precioFinal = precioFinal;
    }

    public void setCarreraInterna(int idCarreraInterna) {
        this.carreraInterna = Integer.toString(idCarreraInterna);
    }

    public ArrayList<Trip> recuperarTrips(Context context){
        ArrayList<Trip> Trips;
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("Trips", "");
        Trips = gson.fromJson(json, new TypeToken<ArrayList<Trip>>(){}.getType());

        return Trips;
    }

    public boolean guardarTrip(Context context, Trip trip){
        boolean correcto = false;
        try{
            ArrayList<Trip> Trips;
            Trips = recuperarTrips(context);

            //Inicializamos si tutto va benne
            if(Trips == null){
                Trips = new ArrayList<Trip>();
            }

            //Metemos el trip actual
            Trips.add(trip);

            SharedPreferences appSharedPrefs2 = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            SharedPreferences.Editor prefsEditor2 = appSharedPrefs2.edit();
            Gson gson2 = new Gson();
            String json2 = gson2.toJson(Trips);
            prefsEditor2.putString("Trips", json2);
            prefsEditor2.commit();

            correcto = true;
        }catch(Exception e){
        }
        return correcto;
    }

    public boolean enviarTrips(Context context){
        boolean correcto = false;
        try{
            ArrayList<Trip> Trips;
            Trips = recuperarTrips(context);

            //Inicializamos si tutto va benne
            if(Trips == null){
                Trips = new ArrayList<Trip>();
            }

            if (Trips.size() > 0) {

                Iterator<Trip> it = Trips.iterator();

                while (it.hasNext()) {
                    Trip trip = it.next();
                    if(!trip.aceppt || trip.intentosTrip <3){
                        //No hace falta pasarle el email ni la licencia porque las recuperamos de las preferencias, previamente asignadas a las Costantes.
                        new VolleyApi().peticionAlServidor(context, Constants.urlRegistro, trip, null, "","");

                    }else{
                        Trips.remove(trip);
                    }
                }
            }

            correcto = true;
        }catch(Exception e){
        }
        return correcto;
    }

    public boolean guardarTrips(Context context, ArrayList<Trip> Trips){
        boolean correcto = false;
        try{
            if(Trips == null){
                Trips = new ArrayList<Trip>();
            }

            SharedPreferences appSharedPrefs2 = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
            SharedPreferences.Editor prefsEditor2 = appSharedPrefs2.edit();
            Gson gson2 = new Gson();
            String json2 = gson2.toJson(Trips);
            prefsEditor2.putString("Trips", json2);
            prefsEditor2.commit();

            correcto = true;
        }catch(Exception e){
        }
        return correcto;
    }
}








