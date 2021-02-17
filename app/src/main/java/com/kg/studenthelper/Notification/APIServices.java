package com.kg.studenthelper.Notification;

import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIServices {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAFwLtv50:APA91bGIKz6U8E6wCs6xBKp6y6xWnSyaBky-YAt_w4FWMDRRmq1BBMC1Mzh3lS77aw7esA6liisc3cval7DjAUvX1VX8L0iwNU_9NmcQg2bri6hQm5vXWyV-tTO6VoN7z4wDqe8NsgNA"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);
}
