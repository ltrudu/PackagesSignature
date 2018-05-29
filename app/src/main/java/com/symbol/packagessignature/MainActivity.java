package com.symbol.packagessignature;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.Signature;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final int RC_PERMISSION_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(verifyStoragePermissions(this))
            getPackagesSignature();

    }

    private void getPackagesSignature()
    {
        List<Package> packageList = getPackagesData();
        savePackageList(packageList);
        finish();
    }

    private List<Package> getPackagesData() {
        PackageManager packageManager = getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
        List<Package> packageList = new ArrayList<>(packageInfoList.size());
        for (PackageInfo packageInfo : packageInfoList) {
            Package packageItem = new Package();
            packageItem.version = packageInfo.versionName;
            packageItem.versioncode = packageInfo.versionCode;
            packageItem.packageName = packageInfo.packageName;
            packageItem.firstInstallTime = new Date(packageInfo.firstInstallTime);
            packageItem.lastUpdateTime = new Date(packageInfo.lastUpdateTime);

            if(packageInfo.applicationInfo != null)
            {
                packageItem.sourceDir = packageInfo.applicationInfo.sourceDir;
            }
            else
            {
                packageItem.sourceDir = "";
            }

            try {
                ActivityInfo[] activityInfo = getPackageManager().getPackageInfo(packageInfo.packageName, PackageManager.GET_ACTIVITIES).activities;
                if(activityInfo != null)
                {
                    for(ActivityInfo actinfo : activityInfo)
                    {
                        packageItem.activities.add(actinfo.name);
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }


            try {
                ActivityInfo[] activityInfo = getPackageManager().getPackageInfo(packageInfo.packageName, PackageManager.GET_RECEIVERS).receivers;
                if(activityInfo != null)
                {
                    for(ActivityInfo actinfo : activityInfo)
                    {
                        packageItem.receivers.add(actinfo.name);
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            try {
                ServiceInfo[] activityInfo = getPackageManager().getPackageInfo(packageInfo.packageName, PackageManager.GET_SERVICES).services;
                if(activityInfo != null)
                {
                    for(ServiceInfo actinfo : activityInfo)
                    {
                        packageItem.services.add(actinfo.name);
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            try {
                ProviderInfo[] activityInfo = getPackageManager().getPackageInfo(packageInfo.packageName, PackageManager.GET_PROVIDERS).providers;
                if(activityInfo != null)
                {
                    for(ProviderInfo actinfo : activityInfo)
                    {
                        packageItem.providers.add(actinfo.name);
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            try {
                PermissionInfo[] activityInfo = getPackageManager().getPackageInfo(packageInfo.packageName, PackageManager.GET_PERMISSIONS).permissions;
                if(activityInfo != null)
                {
                    for(PermissionInfo actinfo : activityInfo)
                    {
                        packageItem.permissions.add(actinfo.name);
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            packageList.add(packageItem);
            ApplicationInfo ai = null;
            try {
                ai = packageManager.getApplicationInfo(packageInfo.packageName, PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (ai == null) {
                continue;
            }
            CharSequence appName = packageManager.getApplicationLabel(ai);
            if (appName != null) {
                packageItem.name = appName.toString();
            }

            Signature[] signatures = null;
            try {
                signatures = packageManager.getPackageInfo(packageInfo.packageName, PackageManager.GET_SIGNATURES).signatures;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if(signatures != null && signatures.length > 0)
            {
                packageItem.packageSignature = signatures[0].toCharsString();
            }

        }
        return packageList;
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public boolean verifyStoragePermissions(Activity activity) {


        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            requestWriteExternalStoragePermission();
        }
        else
        {
           return true;
        }
        return false;
    }

    private void requestWriteExternalStoragePermission() {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Inform and request")
                    .setMessage("You need to enable permissions, bla bla bla")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_PERMISSION_WRITE_EXTERNAL_STORAGE);
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_PERMISSION_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RC_PERMISSION_WRITE_EXTERNAL_STORAGE: {
                boolean allPermissionsGranted = true;
                // If request is cancelled, the result arrays are empty.
                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED)
                        allPermissionsGranted = false;
                }
                if(allPermissionsGranted)
                {
                    getPackagesSignature();
                }
            }
            break;
        }
    }

    private void savePackageList(List<Package> packageList)
    {
        File extStore = Environment.getExternalStorageDirectory();
        File signFileJSON = new File(extStore, "packagesInfo.json");
        if(signFileJSON.exists())
            signFileJSON.delete();

        Type listOfTestObject = new TypeToken<List<Package>>(){}.getType();
        Gson gson = new GsonBuilder().registerTypeAdapter(Package.class, new PackageJSONSerializer()).setPrettyPrinting().create();
        String signaturesJSON = gson.toJson(packageList, listOfTestObject);
        // Write JSON file
        try {
            FileWriter out = new FileWriter(signFileJSON);
            out.write(signaturesJSON);
            out.close();
        }
        catch (IOException e) {
            Log.e("PackageSignatures", "File write failed: " + e.toString());
        }
    }
}
