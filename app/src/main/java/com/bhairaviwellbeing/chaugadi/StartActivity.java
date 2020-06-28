package com.bhairaviwellbeing.chaugadi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;

public class StartActivity extends AppCompatActivity {

    Button login,register;
    FirebaseUser firebaseUser;
    private FirebaseRemoteConfig firebaseRemoteConfig =FirebaseRemoteConfig.getInstance();
    private HashMap<String,Object> firebaseDefaults;
    AppUpdateManager appUpdateManager;
    private static final int REQ_CODE_VERSION_UPDATE = 530;
    Task<AppUpdateInfo> appUpdateInfoTask;

    private InstallStateUpdatedListener installStateUpdatedListener;

    private final String LATEST_APP_VESION_KEY = "latest_app_version";

    /*
    * test Code
    * */

    Button temp;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQ_CODE_VERSION_UPDATE:
                if (resultCode != RESULT_OK) { //RESULT_OK / RESULT_CANCELED / RESULT_IN_APP_UPDATE_FAILED
 //                   Log.d("Update Actvity","Update flow failed! Result code: " + resultCode);
                    // If the update is cancelled or fails,
                    // you can request to start the update again.
                    unregisterInstallStateUpdListener();
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterInstallStateUpdListener();
    }

    private void unregisterInstallStateUpdListener() {
    }

    @Override
    protected void onStart() {
        super.onStart();


        firebaseDefaults = new HashMap<>();
        firebaseDefaults.put(LATEST_APP_VESION_KEY,getCurrentVersionCode());
        

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(12)
                .build();
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        firebaseRemoteConfig.setDefaultsAsync(firebaseDefaults);

        firebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Boolean> task) {
                        if(task.isSuccessful()){
                            firebaseRemoteConfig.activate();
                            Boolean updated = task.getResult();
 //                           Log.d("Updating Function", "Config params updated: " + updated);
 //                           Toast.makeText(StartActivity.this, "Fetch and activate succeeded",
 //                                   Toast.LENGTH_SHORT).show();
                            checkForUpdate();

                        } else {
//                            Toast.makeText(StartActivity.this, "Fetch failed",
//                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


//        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
//        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
//
//        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
//            @Override
//            public void onSuccess(AppUpdateInfo appUpdateInfo) {
//                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
//
//                }
//            }
//        });


    }

    private void startAppUpdateImmediate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    StartActivity.REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void startAppUpdateFlexible(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    StartActivity.REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void checkForUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        
        int latestAppVersion = (int) firebaseRemoteConfig.getDouble(LATEST_APP_VESION_KEY);

        if (latestAppVersion > getCurrentVersionCode()) {
            new AlertDialog.Builder(this).setTitle("Please Update the App")
                    .setMessage("A new version of this app is available. Please update it").setCancelable(false).show();

////            .setPositiveButton(
////                    "OK", new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialog, int which) {
////                            Toast
////                                    .makeText(StartActivity.this, "Take user to Google Play Store", Toast.LENGTH_SHORT)
////                                    .show();
////
////                            appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
////                                @Override
////                                public void onSuccess(AppUpdateInfo result) {
////                                    if(result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE){
////                                        if (result.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
////
////                                            // Before starting an update, register a listener for updates.
//////                                            appUpdateManager.registerListener(installStateUpdatedListener);
////                                            // Start an update.
////                                            startAppUpdateFlexible(result);
////                                        } else if (result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) ) {
////                                            // Start an update.
////                                            startAppUpdateImmediate(result);
////                                        }
////                                    }
////                                }
////                            });
//
//
//
//
//                        }
//                    })
        } else {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            if(firebaseUser != null){
                Intent intent = new Intent(StartActivity.this, TestActivity.class);
                startActivity(intent);
                finish();
            }
//            Toast.makeText(this,"This app is already upto date", Toast.LENGTH_SHORT).show();

        }
    }

    private int getCurrentVersionCode() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(),0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        temp = findViewById(R.id.temp);




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, RegisterActivity.class));
            }
        });

        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, MainActivity.class));
            }
        });

    }
}
