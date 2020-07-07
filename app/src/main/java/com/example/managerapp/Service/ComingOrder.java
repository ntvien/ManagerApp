package com.example.managerapp.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.managerapp.Common;
import com.example.managerapp.Supplier.SupplierHomePage;
import com.example.managerapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ComingOrder extends Service implements ChildEventListener {

    DatabaseReference orderList;
    public ComingOrder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        orderList = FirebaseDatabase.getInstance().getReference("Order/CurrentOrder/List");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        orderList.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        if(snapshot.child("supplierID").getValue().equals(Common.supplier.getSupplierID())
                && snapshot.child("status").getValue().equals("0")){
            Intent startIntent = new Intent(this, SupplierHomePage.class);
            PendingIntent content = PendingIntent.getActivity(getBaseContext(), 0,startIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), "n");
            builder.setAutoCancel((true))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setTicker("ManagerApp")
                    .setContentInfo("")
                    .setContentText("You have new order")
                    .setContentIntent(content)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setSmallIcon(R.drawable.food);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getBaseContext());

            managerCompat.notify(0,builder.build());
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
