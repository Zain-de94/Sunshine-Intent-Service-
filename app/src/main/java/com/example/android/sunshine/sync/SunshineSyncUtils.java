package com.example.android.sunshine.sync;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

public class SunshineSyncUtils {


    public static void startImmediateSync(@NonNull final Context context){

        Intent intentToSyncImmediately = new Intent(context ,
                SunshineSyncIntentService.class);

        context.startService(intentToSyncImmediately);


    }





}
