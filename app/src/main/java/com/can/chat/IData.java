package com.can.chat;

import android.content.Context;

import com.can.chat.Adapter.Recyleradap;
import com.can.chat.databinding.ActivityMainBinding;
import com.can.chat.model.User;

import java.util.ArrayList;
import java.util.Map;

public interface IData {

     void todb(Map<String,Object> messages);
     void pushNotification(Context context, String message);
     void getDataFromFirestore(ArrayList<User> users, ActivityMainBinding binding, Recyleradap adapter);
     void addToDbUserDeviceId(Context context, String us);
}
