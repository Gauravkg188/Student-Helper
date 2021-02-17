package com.kg.studenthelper.Notification;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    private static String url="https://fcm.googleapis.com/";
    private static Retrofit retrofit=null;

    public static synchronized Retrofit getClient()
    {
        if(retrofit==null)
        {
            retrofit=new
                    Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

}
