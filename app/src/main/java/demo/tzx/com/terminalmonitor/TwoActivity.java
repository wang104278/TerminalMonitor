package demo.tzx.com.terminalmonitor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import demo.tzx.com.terminalmonitor.util.ServiceUtils;


public class TwoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServiceUtils.stopService();
        System.exit(0);
    }
}
