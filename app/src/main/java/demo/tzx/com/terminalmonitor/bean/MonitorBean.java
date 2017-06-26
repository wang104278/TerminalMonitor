package demo.tzx.com.terminalmonitor.bean;

/**
 * Created by 呃呃呃 on 2017-06-22.
 */


/**
 * API详解
 * metric: 最核心的字段，代表这个采集项具体度量的是什么, 比如是cpu_idle呢，还是memory_free, 还是qps
 * endpoint: 标明Metric的主体(属主)，比如metric是cpu_idle，那么Endpoint就表示这是哪台机器的cpu_idle
 * timestamp: 表示汇报该数据时的unix时间戳，注意是整数，代表的是秒
 * value: 代表该metric在当前时间点的值，float64
 * step: 表示该数据采集项的汇报周期，这对于后续的配置监控策略很重要，必须明确指定。
 * counterType: 只能是COUNTER或者GAUGE二选一，前者表示该数据采集项为计时器类型，后者表示其为原值 (注意大小写)
 * GAUGE：即用户上传什么样的值，就原封不动的存储
 * COUNTER：指标在存储和展现的时候，会被计算为speed，即（当前值 - 上次值）/ 时间间隔
 * tags: 一组逗号分割的键值对, 对metric进一步描述和细化, 可以是空字符串. 比如idc=lg，比如service=xbox等，多个tag之间用逗号分割
 */
public class MonitorBean {

    private  String endpoint="";//商家-门店
    private  String metric="";//监控项
    private  String timestamp="";//数据采集的时间点,值为当前时间的秒数表示,在Java中可以通过System.currentTimeMillis()/1000拿到
    private  String step="";  //上报周期,单位为秒
    private  String value="";//数据值
    private  String counterType="";
    private  String tags="";//为此监控项打几个标签,最多8个,只支持有限的字符:"a to z,A to Z,0 to 9,-,_,.,/"和Unicode,暂时请不要包含中文

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCounterType() {
        return counterType;
    }

    public void setCounterType(String counterType) {
        this.counterType = counterType;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "MonitorBean{" +
                "endpoint='" + endpoint + '\'' +
                ", metric='" + metric + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", step='" + step + '\'' +
                ", value='" + value + '\'' +
                ", counterType='" + counterType + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }
}
