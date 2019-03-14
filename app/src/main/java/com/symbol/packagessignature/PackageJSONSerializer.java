package com.symbol.packagessignature;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Trudu Laurent on 19/05/2017.
 */

public class PackageJSONSerializer implements JsonSerializer<Package> {

    @Override
    public JsonElement serialize(Package packageInfo, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        object.add("name", context.serialize(packageInfo.name));
        object.add("version", context.serialize(packageInfo.version));
        object.add("versioncode", context.serialize(packageInfo.versioncode));
        object.add("sourcedir", context.serialize(packageInfo.sourceDir));
        object.add("packageName", context.serialize(packageInfo.packageName));
        object.add("firstInstallTime", context.serialize(packageInfo.firstInstallTime));
        object.add("lastUpdateTime", context.serialize(packageInfo.lastUpdateTime));
        object.add("activities", context.serialize(packageInfo.activities));
        object.add("receivers", context.serialize(packageInfo.receivers));
        object.add("services", context.serialize(packageInfo.services));
        object.add("providers", context.serialize(packageInfo.providers));
        object.add("permissions", context.serialize(packageInfo.permissions));
        object.add("packageSignature", context.serialize(packageInfo.packageSignature));
        object.add("packageSignatureHex", context.serialize(packageInfo.packageSignatureHex));

        return object;
    }

}











