package com.practice.olegtojgildin.aidlpractice_meet_13;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String ACTION_AIDL = "com.practice.olegtojgildin.aidlpractice_meet_13.aidl.IDataInterface";

    private IDataInterface dataInterface;

    private EditText editText;
    private TextView readText;
    private Button saveText;
    private Button showText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initListener();

    }

    private void init() {
        editText = findViewById(R.id.editData);
        readText = findViewById(R.id.readData);
        saveText = findViewById(R.id.SaveDataBtn);
        showText = findViewById(R.id.ShowDataBtn);
    }

    private void initListener() {
        saveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dataInterface.saveDataText(editText.getText().toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        showText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    readText.setText(dataInterface.getDataText());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            dataInterface = IDataInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            dataInterface = null;
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mConnection);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = createExplicitIntent(this, new Intent(ACTION_AIDL));
        if (intent != null) {
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }

    }


    public Intent createExplicitIntent(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(intent, 0);

        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;

        ComponentName component = new ComponentName(packageName, className);

        Intent explicitIntent = new Intent(intent);
        explicitIntent.setComponent(component);

        return explicitIntent;
    }
}
