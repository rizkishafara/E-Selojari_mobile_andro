package com.example.e_selojari.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerAPI {
    public static final String URL_DATA = "https://e-selojaritoko.000webhostapp.com/index.php/mobile";
    public static final String URL_DETAILJUAL = "https://e-selojaritoko.000webhostapp.com/mobile/create_detailjual.php";
    public static final String URL_JUAL = "https://e-selojaritoko.000webhostapp.com/index.php/mobile/Mobile/createJual";
    public static final String URL_LOGIN = "https://e-selojaritoko.000webhostapp.com/index.php/mobile/Mobile/login";
    public static final String URL_REGISTER = "https://e-selojaritoko.000webhostapp.com/index.php/mobile/Mobile/register";
    public static final String URL_UPDATE = "https://e-selojaritoko.000webhostapp.com/index.php/mobile/Mobile/updatePass";
    public static final String URL_DASHBOARD_JUAL = "https://e-selojaritoko.000webhostapp.com/mobile/view_data.php";
    public static final String URL_UPDATE_STATUS = "https://e-selojaritoko.000webhostapp.com/index.php/mobile/Mobile/updateStatus";
    //public static final String URL_RIWAYAT = "http://10.0.2.2/E-Selojari/index.php/mobile/Mobile/riwayat";

    public static final String URL_RIWAYAT = "https://e-selojaritoko.000webhostapp.com/mobile/riwayat.php";
}
