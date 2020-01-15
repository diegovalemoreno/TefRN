package br.com.linx.dtefmobilereactnative;

import android.os.Build;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Created by lacevedo on 2018/1/25.
 */

// The sole purpose of this module is learn to use Promises in React-Native
public class PhoneInfoModule extends ReactContextBaseJavaModule {
    public static final String PHONE_BRAND = "phoneBrand";
    public static final String PHONE_MODEL= "phoneModel";
    public static final String PHONE_CODENAME = "phoneCodename";
    public static final String OS_NAME = "osName";
    public static final String OS_VERSION = "osVersion";


    public PhoneInfoModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return PhoneInfoModule.class.getSimpleName();
    }

    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        Map<String, Object> constants = new HashMap<>();

        constants.put("PHONE_BRAND", PHONE_BRAND);
        constants.put("PHONE_MODEL", PHONE_MODEL);
        constants.put("OS_NAME", OS_NAME);
        constants.put("OS_VERSION", OS_VERSION);
        constants.put("PHONE_CODENAME", PHONE_CODENAME);

        return constants;
    }

    @ReactMethod
    public void getPhoneInfo(Promise promise) {
        if (promise != null) {
            WritableMap info = Arguments.createMap();
            info.putString(PHONE_BRAND, Build.BRAND);
            info.putString(PHONE_MODEL, Build.MODEL);
            info.putString(OS_NAME, "Android");
            info.putString(OS_VERSION, String.valueOf(Build.VERSION.SDK_INT));
            info.putString(PHONE_CODENAME, String.valueOf(Build.DEVICE));

            promise.resolve(info);
        }
    }
}
