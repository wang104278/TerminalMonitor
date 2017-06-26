package demo.tzx.com.terminalmonitor.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import demo.tzx.com.terminalmonitor.bean.MonitorBean;
import demo.tzx.com.terminalmonitor.bean.MonitorData;
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
    private Thread myThread;
    private String tenancy_id;
    private String organ_code;
    private String registeredTime;
    private String equipmentType;
    private String devices_ip;
    private String GAUGE="GAUGE";
    private String COUNTER="COUNTER";
    private List<MonitorBean> monitorBeanList= new ArrayList<>();
    private String url="/falconDataAgent/upload";
    private String httpUrl="";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    public void MyMethod() {
        if(monitorBeanList.size()>0){
            monitorBeanList.clear();
        }
        MonitorBean monitorBean;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            monitorBean= getMonitorBean("memory.usage",MonitorUtils.getUseMemory(this),"TotalMemory="+MonitorUtils.getTotalMemory(this,MonitorUtils.changeState));
            monitorBeanList.add(monitorBean);
            monitorBean= getMonitorBean("disk.usage",MonitorUtils.getSDUseSize(this),"SDTotalSize="+MonitorUtils.getSDTotalSize(this,MonitorUtils.changeState));
            monitorBeanList.add(monitorBean);
        }
        monitorBean= getMonitorBean("cpu.usage",MonitorUtils.getProcessCpuRate(),"MaxCpuFreq="+MonitorUtils.getMaxCpuFreq());
        monitorBeanList.add(monitorBean);
        if(myThread!=null)myThread=null;
        myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                postJson();
            }
        });
        myThread.start();
    }
    private MonitorBean getMonitorBean(String metric ,String value,String tags){
        MonitorBean monitorBean=new MonitorBean();
        monitorBean.setEndpoint(tenancy_id+"-"+organ_code);
        monitorBean.setMetric("Android.Client."+metric);
        monitorBean.setTimestamp(String.valueOf(System.currentTimeMillis()));
        monitorBean.setStep("60");
        monitorBean.setValue(value);
        monitorBean.setCounterType(GAUGE);
        monitorBean.setTags("os="+MonitorUtils.getInformation()+","+tags);
        return monitorBean;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
    /**
     *
     *  tenancy_id 商户编号
     *  organ_code 机构编号
     *  registeredTime 设备注册时间
     *  equipmentType 设备类型
     *  devices_ip Mac地址
     */

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("TAG", "-------service-----我在启动");
        startEnd=System.currentTimeMillis();
        startTimer();
        tenancy_id = intent.getStringExtra("tenancy_id");
        httpUrl = intent.getStringExtra("httpUrl");
        organ_code = intent.getStringExtra("organ_code");
        registeredTime = intent.getStringExtra("registeredTime");
        equipmentType = intent.getStringExtra("equipmentType");
        devices_ip = intent.getStringExtra("devices_ip");
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
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");

    private void postJson() {
        //申明给服务端传递一个json串
        //创建一个OkHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        MonitorData data = getData(monitorBeanList);
        String param = new Gson().toJson(data); //连接要提交的数据
        Log.e("TAG",param);
        RequestBody requestBody = RequestBody.create(JSON, param);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(httpUrl+url)
                .post(requestBody)
                .build();
        //发送请求获取响应
        try {
            Response response=okHttpClient.newCall(request).execute();
            //判断请求是否成功
            if(response.isSuccessful()){
                //打印服务端返回结果
                Log.e("TAG",response.body().string());
            }else{
                Log.e("TAG","连接失败");
            }
            startTimer();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static <T> MonitorData<List<JsonObject>> getData( T t) {
        MonitorData data = new MonitorData();
        data.setType("FALCON");
        data.setOper("upload");
        data.setData(t);
        data.setSource("android_pad");
        return data;
    }
}
