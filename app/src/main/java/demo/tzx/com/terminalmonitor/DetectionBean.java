package demo.tzx.com.terminalmonitor;

/**
 * Created by 呃呃呃 on 2017-06-15.
 */

public class DetectionBean {
    //sd卡总大小
    private String sdTotalSize;
    //sd卡剩余大小
    private String sdAvailableSize;
    //获得机身内存总大小
    private String totalMemory;
    //获取手机可用的剩余运行内存
    private String availMemory;
    //当前cpu率
    private String processCpuRate;
    //实时获取CPU当前频率（单位KHZ）
    private String curCpuFreq;
    //启动时间
    private String startEnd;
    //手机名称，版本号
    private String operatingSystem;

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getSdTotalSize() {
        return sdTotalSize;
    }

    public void setSdTotalSize(String sdTotalSize) {
        this.sdTotalSize = sdTotalSize;
    }

    public String getSdAvailableSize() {
        return sdAvailableSize;
    }

    public void setSdAvailableSize(String sdAvailableSize) {
        this.sdAvailableSize = sdAvailableSize;
    }

    public String getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(String totalMemory) {
        this.totalMemory = totalMemory;
    }

    public String getAvailMemory() {
        return availMemory;
    }

    public void setAvailMemory(String availMemory) {
        this.availMemory = availMemory;
    }

    public String getProcessCpuRate() {
        return processCpuRate;
    }

    public void setProcessCpuRate(String processCpuRate) {
        this.processCpuRate = processCpuRate;
    }

    public String getCurCpuFreq() {
        return curCpuFreq;
    }

    public void setCurCpuFreq(String curCpuFreq) {
        this.curCpuFreq = curCpuFreq;
    }

    public String getStartEnd() {
        return startEnd;
    }

    public void setStartEnd(String startEnd) {
        this.startEnd = startEnd;
    }

    @Override
    public String toString() {
        return "DetectionBean{" +
                "sdTotalSize='" + sdTotalSize + '\'' +
                ", sdAvailableSize='" + sdAvailableSize + '\'' +
                ", totalMemory='" + totalMemory + '\'' +
                ", availMemory='" + availMemory + '\'' +
                ", processCpuRate='" + processCpuRate + '\'' +
                ", curCpuFreq='" + curCpuFreq + '\'' +
                ", startEnd='" + startEnd + '\'' +
                '}';
    }
}
