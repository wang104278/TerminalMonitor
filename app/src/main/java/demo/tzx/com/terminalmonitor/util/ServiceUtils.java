package demo.tzx.com.terminalmonitor.util;

import android.content.Context;
import android.content.Intent;

import demo.tzx.com.terminalmonitor.service.MonitorService;


/**
 * Created by 呃呃呃 on 2017-06-15.
 */

public class ServiceUtils {

    public static Context mContext;


    public static void init(Context context) {
        mContext = context;
    }

    public static void startService() {
        Intent intent = new Intent(mContext,MonitorService.class);
        intent.putExtra("posNum","--------TAG------");
        intent.putExtra("posName","--------TAG------");
        intent.putExtra("registeredTime","--------TAG------");
        intent.putExtra("posType","--------TAG------");

        mContext.startService(intent);

    }

    public static void stopService() {
        Intent intent = new Intent(mContext,MonitorService.class);
        mContext.stopService(intent);
    }



}
