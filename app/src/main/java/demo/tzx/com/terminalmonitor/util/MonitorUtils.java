package demo.tzx.com.terminalmonitor.util;

import android.app.ActivityManager;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.RequiresApi;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created by 呃呃呃 on 2017-06-13.
 */

public class MonitorUtils {

    public static int defaultState=1;
    public static int changeState=2;


    public static String getInformation() {
        //Android : Letv,Letv X501,23,6.0
        String os = "Android : " + Build.MANUFACTURER+","+ Build.MODEL+ ","
                + Build.VERSION.RELEASE;
    return os;
    }
    // 实时获取CPU当前频率（单位KHZ）
    public static String getCurCpuFreq() {
        String result = "N/A";
        try {
            FileReader fr = new FileReader(
                    "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            result = text.trim();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Double.parseDouble(result)/1000/1000+"GH";

    }
    // 获取CPU最大频率（单位KHZ）
    // "/system/bin/cat" 命令行
    // "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" 存储最大频率的文件的路径
    public static String getMaxCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = { "/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }

        return Double.parseDouble(result.trim())/1000/1000+"GH";
    }

    //当前cpu率
    public static String getProcessCpuRate()
    {

        float totalCpuTime1 = getTotalCpuTime();
        float processCpuTime1 = getAppCpuTime();
        try
    {
        Thread.sleep(360);

    }
    catch (Exception e)
    {
    }

        float totalCpuTime2 = getTotalCpuTime();
        float processCpuTime2 = getAppCpuTime();

        float cpuRate = 100 * (processCpuTime2 - processCpuTime1)
                / (totalCpuTime2 - totalCpuTime1);

        return cpuRate+"";
    }


    public static long getTotalCpuTime()
    { // 获取系统总CPU使用时间
        String[] cpuInfos = null;
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/stat")), 1000);
            String load = reader.readLine();
            reader.close();
            cpuInfos = load.split(" ");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        long totalCpu = Long.parseLong(cpuInfos[2])
                + Long.parseLong(cpuInfos[3]) + Long.parseLong(cpuInfos[4])
                + Long.parseLong(cpuInfos[6]) + Long.parseLong(cpuInfos[5])
                + Long.parseLong(cpuInfos[7]) + Long.parseLong(cpuInfos[8]);
        return totalCpu;
    }

    public static long getAppCpuTime()
    { // 获取应用占用的CPU时间
        String[] cpuInfos = null;
        try
        {
            int pid = android.os.Process.myPid();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/" + pid + "/stat")), 1000);
            String load = reader.readLine();
            reader.close();
            cpuInfos = load.split(" ");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        long appCpuTime = Long.parseLong(cpuInfos[13])
                + Long.parseLong(cpuInfos[14]) + Long.parseLong(cpuInfos[15])
                + Long.parseLong(cpuInfos[16]);
        return appCpuTime;
    }





    // 7、mac地址和开机时间

    public static String getOtherInfo(Context mContext) {
        String other = "null";
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getMacAddress() != null) {
            other = wifiInfo.getMacAddress();
        } else {
            other = "Fail";
        }
        return other;
    }

    /**
     * 获得SD卡总大小
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getSDTotalSize(Context mContext,int state) {
        File sdDir = null;
        Log.e("TAG","-------------"+ Environment.getExternalStorageState());
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if   (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }else{
            return  getInternalMemorySize(mContext,state);
        }
        StatFs stat = new StatFs(sdDir.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        if(state==1){
            return blockSize * totalBlocks+"";
        }
        return Formatter.formatFileSize(mContext, blockSize * totalBlocks);
    }

    /**
     * 获取手机SD卡总大小
     *
     * @param context
     * @return 以M,G为单位的容量
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getInternalMemorySize(Context context,int state) {
        File file = Environment.getDataDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long blockSizeLong = statFs.getBlockSizeLong();
        long blockCountLong = statFs.getBlockCountLong();
        long size = blockCountLong * blockSizeLong;
        if(state==1){
            return size+"";
        }
        return Formatter.formatFileSize(context, size);
    }
    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getSDAvailableSize(Context mContext ,int state) {
        File path=null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if   (sdCardExist)
        {
            path = Environment.getExternalStorageDirectory();//获取跟目录
        }else{
            return  getAvailableInternalMemorySize(mContext,state);
        }
         path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        if(state==1){
           return blockSize * availableBlocks+"";
        }
        return Formatter.formatFileSize(mContext, blockSize * availableBlocks);
    }
    /**
     * 获取手机SD卡可用存储空间
     * @param context
     * @return 以M,G为单位的容量
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getAvailableInternalMemorySize(Context context,int state) {
        File file = Environment.getDataDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long availableBlocksLong = statFs.getAvailableBlocksLong();
        long blockSizeLong = statFs.getBlockSizeLong();
        if(state==1){
            return availableBlocksLong
                    * blockSizeLong+"";
        }
        return Formatter.formatFileSize(context, availableBlocksLong
                * blockSizeLong);
    }
    /**
     * 获取手机SD卡使用的大小
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String  getSDUseSize(Context mContext){
        long SDAvailableSize=Long.valueOf( getSDAvailableSize(mContext,defaultState));
        long SDTotalSize=Long.valueOf(getSDTotalSize(mContext,defaultState));
        return (SDTotalSize-SDAvailableSize)*100/SDTotalSize+"";
    }





    /**
     * 获得机身内存总大小
     *
     * @return
     */

    public static String getTotalMemory(Context mContext,int state){
        try {
            File file = new File("/proc/meminfo");
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String totalRam = br.readLine();
            StringBuffer sb = new StringBuffer();
            //MemTotal:  513000 KB
            char[] cs = totalRam.toCharArray();
            for (char c : cs) {
                if (c >= '0' && c <= '9') {
                    sb.append(c);
                }
            }
            long result = Long.parseLong(sb.toString()) * 1024;//因为读出来的是KB,所以这里转换为B方便后面的格式化
            if(state==1){
               return result+"";
            }
            return Formatter.formatFileSize(mContext,result);//字节
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * 获取手机可用的剩余运行内存
     * @return 字节数
     */
    public static String getAvailMemory(Context mContext,int state){
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo=new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        if(state==1){
           return memoryInfo.availMem+"";
        }
        return Formatter.formatFileSize(mContext, memoryInfo.availMem);
    }
    /**
     * 获取手机使用的内存
     * @return 字节数
     */
    public static String getUseMemory(Context mContext){
    long totalMemory=Long.valueOf(getTotalMemory(mContext,defaultState));
    long availMemory=Long.valueOf(getAvailMemory(mContext,defaultState));
        return  ""+(totalMemory-availMemory)*100/totalMemory;
    }



}
