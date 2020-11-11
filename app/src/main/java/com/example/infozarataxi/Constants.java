package com.example.infozarataxi;

class Constants {

    final static String urlAlta = "https://wszarataxi.nitax.net/WebService1.asmx/Alta";
    final static String urlRegistro = "https://wszarataxi.nitax.net/WebService1.asmx/Registro";

    final static String usuarioTexto = "usuario";
    final static String usuarioValor = "nitax";
    final static String passwordTexto = "password";
    final static String passwordValor = "1234";
    final static String licencia = "licencia";
    final static String email = "email";
    final static String carrerainterna = "carrerainterna";
    final static String salida_fechahora = "salida_fechahora";
    final static String salida_latitud = "salida_latitud";
    final static String salida_longuitud = "salida_longuitud";
    final static String recogida_fechahora = "recogida_fechahora";
    final static String recogida_latitud = "recogida_latitud";
    final static String recogida_longuitud = "recogida_longuitud";
    final static String finalizacion_fechahora = "finalizacion_fechahora";
    final static String finalizacion_latitud = "finalizacion_latitud";
    final static String finalizacion_longuitud = "finalizacion_longuitud";
    final static String preciofinal = "preciofinal";
    final static String accionUp = "up";
    final static String accionDown = "dowm";
    final static String sinAccion = "nothing";

    static String licenseValue = "";
    static String emailValue = "";

    static String textButton1 = "Pulsar el botón para iniciar nuevo servicio";
    static String textButton2 = "Pulsar el botón al recoger al cliente";
    static String textButton3 = "Pulsar el botón al dejar el cliente";

    public static void setLicenseValue(String licenseValue) {
        Constants.licenseValue = licenseValue;
    }

    public static void setEmailValue(String emailValue) {
        Constants.emailValue = emailValue;
    }

}