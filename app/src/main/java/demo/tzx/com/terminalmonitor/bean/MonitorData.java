package demo.tzx.com.terminalmonitor.bean;

/**
 * @author xstar
 *         <p>
 *         请求data
 *         </P>
 * @since 2015/5/21
 */
public class MonitorData<T> {
    private long t;
    private String secret;
    private String oper;
    private String type;
    private String tenancy_id;
    private String store_id;
    private T data;
    private String organ_code;
    private boolean success;
    private int code;
    private String source;
    private String msg;

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MonitorData() {
        // t = System.currentTimeMillis();
        secret = "0";
        msg = "";
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getOrgan_code() {
        return organ_code;
    }

    public void setOrgan_code(String organ_code) {
        this.organ_code = organ_code;
    }

    public long getT() {
        return t;
    }

    public void setT(long t) {
        this.t = t;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }


    public String getTenancy_id() {
        return tenancy_id;
    }

    public void setTenancy_id(String tenancy_id) {
        this.tenancy_id = tenancy_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }



    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
