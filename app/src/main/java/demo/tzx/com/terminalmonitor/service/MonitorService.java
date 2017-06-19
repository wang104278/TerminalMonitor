package demo.tzx.com.terminalmonitor.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import demo.tzx.com.terminalmonitor.DetectionBean;
import demo.tzx.com.terminalmonitor.util.MonitorUtils;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;


/**
 * Created by 呃呃呃 on 2017-06-15.
 */

public class MonitorService extends Service {
    private Subscription subscribe;
    //倒计时
    public int time = 5;
    private long startEnd;
    private DetectionBean detectionBean;
    private Thread myThread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    public void MyMethod() {
        if(detectionBean==null)detectionBean=new DetectionBean();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            detectionBean.setSdTotalSize(MonitorUtils.getSDTotalSize(this));
            detectionBean.setSdAvailableSize(MonitorUtils.getSDAvailableSize(this));
        }


        detectionBean.setTotalMemory(MonitorUtils.getTotalMemory(this));
        detectionBean.setAvailMemory(MonitorUtils.getAvailMemory(this));
        detectionBean.setProcessCpuRate(MonitorUtils.getProcessCpuRate());
        detectionBean.setCurCpuFreq(MonitorUtils.getCurCpuFreq());
        detectionBean.setStartEnd(String.valueOf(startEnd));
        detectionBean.setOperatingSystem(MonitorUtils.getInformation());

       /* Log.e("TAG","sdk总容量--------------------"+b);
        Log.e("TAG","获得sd卡剩余容量，即可用大小---"+a);
        Log.e("TAG","获得机身内存总大小------------"+d);
        Log.e("TAG","获得机身可用内存--------------"+e);*/

        Log.e("TAG","------"+detectionBean.toString());
        if(myThread!=null)myThread=null;
        myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                send();
            }
        });
        myThread.start();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("TAG", "-------service-----我在启动");
        startEnd=System.currentTimeMillis();
        startTimer();
        intent.getStringExtra("posNum");
        intent.getStringExtra("posName");
        intent.getStringExtra("registeredTime");
        intent.getStringExtra("posType");
        return START_NOT_STICKY;



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != subscribe) {
            subscribe.unsubscribe();
        }
    }

    private Observer<Long> observer = new Observer<Long>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(Long aLong) {
            Log.e("TAG","------倒计时------"+aLong);
            if(aLong==time){
                MyMethod();

            }

        }
    };
    private void startTimer() {
        subscribe = rx.Observable.interval(0, 1, TimeUnit.SECONDS).take(time+1).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }
    private String result = ""; // 声明一个代表显示内容的字符串

    public void send() {
        String target = "http://www.baidu.com";   //要提交的目标地址
        URL url;
        try {
            url = new URL(target);
            HttpURLConnection urlConn = (HttpURLConnection) url
                    .openConnection(); // 创建一个HTTP连接
            urlConn.setRequestMethod("POST"); // 指定使用POST请求方式
            urlConn.setReadTimeout(3000);
            urlConn.setConnectTimeout(5000);
            urlConn.setDoInput(true); // 向连接中写入数据
            urlConn.setDoOutput(true); // 从连接中读取数据
            urlConn.setUseCaches(false); // 禁止缓存
            urlConn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded"); // 设置内容类型
            DataOutputStream out = new DataOutputStream(urlConn.getOutputStream()); // 获取输出流
            String param = "nickname="
                    + URLEncoder.encode("zhangsan", "utf-8")
                    + "&content="
                    + URLEncoder.encode("lisi", "utf-8"); //连接要提交的数据
            out.writeBytes(param);//将要传递的数据写入数据输出流
            out.flush();    //输出缓存
            out.close();    //关闭数据输出流
            // 判断是否响应成功
            if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader in = new InputStreamReader(
                        urlConn.getInputStream()); // 获得读取的内容
                BufferedReader buffer = new BufferedReader(in); // 获取输入流对象
                String inputLine = null;
                while ((inputLine = buffer.readLine()) != null) {
                    result += inputLine + "\n";
                }
                buffer.close();
                in.close(); //关闭字符输入流
            }else{
                Log.e("TAG","连接失败");
            }
            urlConn.disconnect();   //断开连接
            startTimer();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("TAG",result);
    }

}
