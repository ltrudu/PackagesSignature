package com.symbol.packagessignature;

import android.content.pm.ActivityInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Trudu Laurent on 19/05/2017.
 */

public class Package {
    public String               name;
    public String               version;
    public int                  versioncode;
    public String               sourceDir;
    public String               packageName;
    public String               packageSignature    = null;
    public Date                 firstInstallTime;
    public Date                 lastUpdateTime;
    public List<String>         activities          = new ArrayList<String>();
    public List<String>         receivers           = new ArrayList<String>();
    public List<String>         services            = new ArrayList<String>();
    public List<String>         providers           = new ArrayList<String>();
    public List<String>         permissions         = new ArrayList<String>();
}
