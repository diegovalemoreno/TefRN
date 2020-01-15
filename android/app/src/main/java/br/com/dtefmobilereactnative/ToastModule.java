package br.com.linx.dtefmobilereactnative;

import android.os.Handler;
import android.widget.Toast;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * This is the actual module that will be used by JS
 */

public class ToastModule extends ReactContextBaseJavaModule {
    private static final String DURATION_SHORT = "SHORT";
    private static final String DURATION_LONG = "LONG";

    // Do this
    public ToastModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    // NativeModule name in JS
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    // Exposes constants for JS
    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> result = new HashMap<>();
        result.put(DURATION_LONG, Toast.LENGTH_LONG);
        result.put(DURATION_SHORT, Toast.LENGTH_SHORT);
        return result;
    }

    // This method will be called from JS
    @ReactMethod
    public void show(String message, int duration) {
        Toast.makeText(getReactApplicationContext(), message, duration).show();
    }

    @ReactMethod
    public void showDelayed(final String message, final int duration, int delay) {
        showDelayed(message, duration, delay, null);
    }

    @ReactMethod
    public void showDelayed(final String message, final int duration, int delay, final Callback callback) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                show(message, duration);
                if (callback != null) {
                    callback.invoke();
                }
            }
        }, delay);
    }
}
