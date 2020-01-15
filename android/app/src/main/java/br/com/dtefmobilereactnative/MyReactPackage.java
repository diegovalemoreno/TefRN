package br.com.linx.dtefmobilereactnative;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


// RN Packages are sets of native modules, native views and other native resources.
// They may also reference JS modules, no idea how.
// This is exposed to JS via the ReactNativeHost which lives in the Application
public class MyReactPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> result = new ArrayList<>();
        result.add(new ToastModule(reactContext));
        result.add(new PhoneInfoModule(reactContext));
        result.add(new RNDtefmobilernModule(reactContext));
        return result;
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}
