package com.example.infozarataxi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VolleyApi {

    public class Respuesta {
        Boolean Correcto;
    }

    public class Taxista {
        int Id;
        int Licencia;
        String Email;
    }

    public class RespuestaAlta extends Respuesta {
        Taxista Taxista;
    }

    public class RespuestaRegistro extends Respuesta {
        int CarreraInterna;
    }

    protected static void peticionAlServidor(final Context context, final String url, final Trip trip, final Intent intent, final String licencia, final String email) {
        final SharedPreferences  prefe = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefe.edit();
        final MyAsyncTask tarea1;

        tarea1 = new MyAsyncTask(context);
        tarea1.execute();

        StringRequest stringRequest;

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        XmlParserCreator parserCreator = new XmlParserCreator() {
                            @Override
                            public XmlPullParser createParser() {
                                try {
                                    return XmlPullParserFactory.newInstance().newPullParser();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        };
                        GsonXml gsonXml = new GsonXmlBuilder()
                                .setXmlParserCreator(parserCreator)
                                .create();

                        String xml = response;
                        if (url.compareTo(Constants.urlAlta) == 0) {

                            RespuestaAlta ra = gsonXml.fromXml(xml, RespuestaAlta.class);

                            if (ra.Correcto) {
                                tarea1.cancel(true);
                                if (!prefe.getString("licencia", "").isEmpty()) {
                                    editor.putString("email", email);

                                } else {

                                    editor.putString("licencia", licencia);
                                    editor.putInt("idCarrera", 1);

                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);;
                                    context.startActivity(intent);

                                }

                                editor.commit();
                                Toast.makeText(context, "Registro Correcto", Toast.LENGTH_SHORT).show();

                            }
                        }

                        if (url.compareTo(Constants.urlRegistro) == 0) {

                            RespuestaRegistro rr = gsonXml.fromXml(xml, RespuestaRegistro.class);

                            //Recuperar Trips
                            Trip trip = new Trip();
                            ArrayList<Trip> Trips = trip.recuperarTrips(context);
                            ArrayList<Trip> TripsFinal = new ArrayList();

                            //Comprobar respuesta
                            if (rr != null) {
                                if (Trips.size() > 0) {
                                    for (int x = 0; x < Trips.size(); x++) {
                                        Trip tripRecuperado = Trips.get(x);

                                        if (tripRecuperado.getCarreraInterna().equalsIgnoreCase(String.valueOf(rr.CarreraInterna))) {
                                            if (!rr.Correcto) {
                                                tripRecuperado.setIntentosTrip();
                                                TripsFinal.add(tripRecuperado);
                                            } else {
                                                Toast.makeText(context, "registro guardado correctamente", Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            //Comprobacion extra para evitar guardar datos innecesarios
                                            if (!tripRecuperado.getAceppt() && tripRecuperado.getIntentosTrip() < 3) {
                                                TripsFinal.add(tripRecuperado);
                                            }
                                        }
                                    }
                                }
                                tarea1.cancel(true);
                            }

                            //Volvemos a guardar los tripsRecuperados
                            trip.guardarTrips(context, TripsFinal);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:

                params.put(Constants.usuarioTexto, Constants.usuarioValor);
                params.put(Constants.passwordTexto, Constants.passwordValor);
                params.put(Constants.licencia, Constants.licenseValue);

                if (url.equalsIgnoreCase(Constants.urlAlta)) {

                    params.put(Constants.email, email);

                } else {

                    params.put(Constants.carrerainterna, trip.getCarreraInterna());
                    params.put(Constants.salida_fechahora, trip.getSalidaFechahora());
                    params.put(Constants.salida_latitud, trip.getSalidaLatitud());
                    params.put(Constants.salida_longuitud, trip.getSalidaLonguitud());
                    params.put(Constants.recogida_fechahora, trip.getRecogidaFechahora());
                    params.put(Constants.recogida_latitud, trip.getRecogidaLatitud());
                    params.put(Constants.recogida_longuitud, trip.getRecogidaLonguitud());
                    params.put(Constants.finalizacion_fechahora, trip.getFinalizacionFechahora());
                    params.put(Constants.finalizacion_latitud, trip.getFinalizacionLatitud());
                    params.put(Constants.finalizacion_longuitud, trip.getFinalizacionLonguitud());
                    params.put(Constants.preciofinal, trip.getPrecioFinal());

                }
                return params;

            }
        };

        Volley.newRequestQueue(context).add(stringRequest);

    }


}
