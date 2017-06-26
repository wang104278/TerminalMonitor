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

    /**
     *
     * @param httpUrl 地址
     * @param tenancy_id 商户编号
     * @param organ_code 机构编号
     * @param registeredTime 设备注册时间
     * @param equipmentType 设备类型
     * @param devices_ip Mac地址
     */
    public static void startService(String httpUrl,String tenancy_id,String organ_code,String registeredTime,String equipmentType,String devices_ip) {
        Intent intent = new Intent(mContext,MonitorService.class);
        intent.putExtra("httpUrl",httpUrl);
        intent.putExtra("tenancy_id",tenancy_id);
        intent.putExtra("organ_code",organ_code);
        intent.putExtra("registeredTime",registeredTime);
        intent.putExtra("equipmentType",equipmentType);
        intent.putExtra("devices_ip",devices_ip);
        mContext.startService(intent);

    }

    public static void stopService() {
        Intent intent = new Intent(mContext,MonitorService.class);
        mContext.stopService(intent);
    }



}
