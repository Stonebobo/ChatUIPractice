package com.littlestone.chatuipractice;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private Button btn_send;
    private RecyclerView recyclerView;
    private ImageView img_add;
    private ImageView img_camera;
    private ImageView img_photo;
    private ImageView img_express;
    private MyAdapter adapter;
    private List<Message> data = new ArrayList<>();

    //*******************************监听网络状态*********************
    //网络变化
    private NetworkChangeReceiver networkChangeReceiver;
    private IntentFilter intentFilter;
    //网络状态
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        bindView();
        init_data();
        initNetworkListener();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new MyAdapter(data, this);
        recyclerView.setAdapter(adapter);


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText.getText().toString().trim();
                if (s.equals("") == false) {
                    Message message = new Message(s, Message.TYPE_SEND);
                    data.add(message);
                    //当有新消息时，刷新RecyclerView
                    adapter.notifyItemInserted(data.size() - 1);
                    //将屏幕滚动到最新消息
                    recyclerView.scrollToPosition(data.size() - 1);
                    editText.setText("");

                } else {
                    Toast.makeText(MainActivity.this, "发送消息不能为空！", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

    public void bindView() {
        editText = findViewById(R.id.edit);
        btn_send = findViewById(R.id.btn_send);
        recyclerView = findViewById(R.id.rv);
        img_add = findViewById(R.id.img_add);
        img_camera = findViewById(R.id.img_camera);
        img_express = findViewById(R.id.img_express);
        img_photo = findViewById(R.id.img_photo);

    }

    public void init_data() {
        Message meg1 = new Message("Hello, 我叫白文磊", Message.TYPE_RECEIVE);
        data.add(meg1);
        Message meg2 = new Message("Hello, 我叫郭涵博", Message.TYPE_SEND);
        data.add(meg2);
    }

    public void initNetworkListener() {
        networkChangeReceiver = new NetworkChangeReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            //注意添加监听权限
            networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                Toast.makeText(MainActivity.this, "已联网", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "未联网,检查网络！", Toast.LENGTH_SHORT).show();
            }
        }
    }
}