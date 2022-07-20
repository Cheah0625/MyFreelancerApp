package com.example.myfreelancer;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationAPIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAZwaiW9Y:APA91bF1B2RoMPLYm-Y_hJWDH9m8ZoXHoBUKUeJkHUHP2FZHVcAn9ikad3fl8A8ZTgm0MXV2LwokNjzF4L9SimNKSgsCbk6nI8HjyEYMOjPi5mBvxY8CDp-XQ-KudyzMjXT-NhnsYh1-"
            }
    )

    @POST("fcm/send")
    Call<NotificationMyResponse> sendNotification(@Body NotificationSender body);
}


