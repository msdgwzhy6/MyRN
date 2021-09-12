package com.myrn;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.facebook.react.bridge.WritableArray;
import com.myrn.utils.PhoneInfo;
import com.myrn.utils.RNConvert;

import static com.myrn.utils.MathUtil.getRandomString;

public class RNBridge extends ReactContextBaseJavaModule {
  public static final String PREFIX = getRandomString(6) + "_";

  @NonNull
  @NotNull
  @Override
  public String getName() {
    return "RNBridge";
  }

  @Nullable
  @org.jetbrains.annotations.Nullable
  @Override
  public Map<String, Object> getConstants() {
    HashMap<String, Object> map = new HashMap<>();
    map.put("prefix", PREFIX);
    map.put("model", PhoneInfo.getPhoneModel());
    return map;
  }

  @ReactMethod
  public void log(String content) {
    Log.d("MY_RN_LOG", content);
  }

  @ReactMethod
  public void getAllComponent(Promise promise) {
    WritableArray array = Arguments.createArray();
    ArrayList<RNDBHelper.Result> results = RNApplication.mReactNativeDB.selectAll();
    for (RNDBHelper.Result result : results) {
      array.pushMap(RNConvert.obj2Map(result));
    }
    promise.resolve(array);
  }

  @ReactMethod
  public void openFromAssets(String bundleName, String moduleName, @Nullable Integer statusBarMode) {
    Activity activity = getActivity();
    if (activity == null) return;
    Intent intent = new Intent(activity, MainActivity.class);
    Bundle bundle = createBundle(bundleName, moduleName, statusBarMode);
    intent.putExtras(bundle);
    activity.startActivity(intent);
  }

  public Bundle createBundle(String bundleName, String moduleName, @Nullable Integer statusBarMode) {
    Bundle bundle = new Bundle();
    bundle.putString("moduleName", moduleName);
    bundle.putInt("statusBarMode", statusBarMode == null ? 0 : statusBarMode);
    bundle.putString("bundleName", bundleName);
    return bundle;
  }

  public static Activity getActivity () {
    try {
      @SuppressLint("PrivateApi") Class activityThreadClass = Class.forName("android.app.ActivityThread");
      Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(
              null);
      Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
      activitiesField.setAccessible(true);
      Map activities = (Map) activitiesField.get(activityThread);
      for (Object activityRecord : activities.values()) {
        Class activityRecordClass = activityRecord.getClass();
        Field pausedField = activityRecordClass.getDeclaredField("paused");
        pausedField.setAccessible(true);
        if (!pausedField.getBoolean(activityRecord)) {
          Field activityField = activityRecordClass.getDeclaredField("activity");
          activityField.setAccessible(true);
          Activity activity = (Activity) activityField.get(activityRecord);
          return activity;
        }
      }
    } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }
}
