package com.example.mcumgr;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.content.Context;

import io.runtime.mcumgr.McuMgrBleTransport;
import io.runtime.mcumgr.McuMgrDfu;

public class McumgrPlugin extends CordovaPlugin {

    private static final String TAG = "McumgrPlugin";
    private Context context;

    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();
        context = cordova.getActivity().getApplicationContext();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("performDfu".equals(action)) {
            JSONObject params = args.getJSONObject(0);
            String deviceAddress = params.getString("deviceAddress");
            String filePath = params.getString("filePath");

            performDfu(deviceAddress, filePath, callbackContext);
            return true;
        }

        if (action.equals("testLog")) {
            android.util.Log.d("McumgrPlugin", "Test log from Mcumgr Cordova Plugin");
            callbackContext.success("Logged from plugin");
            return true;
        }
        return false;
    }

    private void performDfu(String deviceAddress, String filePath, CallbackContext callbackContext) {
        cordova.getThreadPool().execute(() -> {
            try {
                Log.d(TAG, "Starting DFU for device: " + deviceAddress + " with file: " + filePath);

                McuMgrBleTransport transport = new McuMgrBleTransport(context, deviceAddress);
                McuMgrDfu dfu = new McuMgrDfu(transport);

                dfu.dfu(filePath, new McuMgrDfu.Callback() {
                    @Override
                    public void onProgressChanged(int progress) {
                        Log.d(TAG, "DFU Progress: " + progress + "%");
                    }

                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "DFU Success");
                        callbackContext.success("DFU completed successfully");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "DFU Error", e);
                        callbackContext.error("DFU failed: " + e.getMessage());
                    }
                });

            } catch (Exception e) {
                Log.e(TAG, "Error performing DFU", e);
                callbackContext.error("DFU Exception: " + e.getMessage());
            }
        });
    }
}
