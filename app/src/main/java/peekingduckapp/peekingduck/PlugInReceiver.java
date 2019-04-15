package peekingduckapp.peekingduck;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class PlugInReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.v("PlugInReceiver", "Action: " + action);
        Log.v("PlugInReceiver", "Context: " + context);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(action.equals("android.hardware.usb.action.USB_STATE")) {
                if(intent.getExtras().getBoolean("connected")) {
                    Toast.makeText(context, "USB Connected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "USB Disconnected", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            if(action.equals(Intent.ACTION_POWER_CONNECTED)) {
                Toast.makeText(context, "USB Connected", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "USB Disconnected", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
