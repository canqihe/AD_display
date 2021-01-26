package com.colin.ad_display;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.colin.ad_display.receiver.USBMTPReceiver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import util.SmartToastUtils;

import static util.Constants.LEFT_BANNER;
import static util.Constants.READ_USB_DEVICE_PERMISSION;
import static util.Constants.RIGHT_BANNER;
import static util.Constants.TOP_BANNER;
import static util.Constants.VIDEO_BANNER;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.btn_top_banner)
    TextView btnTopBanner;
    @BindView(R.id.btn_left_banner)
    TextView btnLeftBanner;
    @BindView(R.id.btn_right_banner)
    TextView btnRightBanner;
    @BindView(R.id.btn_video_banner)
    TextView btnVideoBanner;
    @BindView(R.id.start_play)
    RelativeLayout startPlay;
    @BindView(R.id.import_data)
    RelativeLayout importData;
    @BindView(R.id.rb_oneline)
    RadioButton rbOneline;
    @BindView(R.id.rb_offline)
    RadioButton rbOffline;

    //USB MTP 设备监控广播
    USBMTPReceiver usbmtpReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        //注册USB设备广播
        registerUSBReceiver();

    }


    private void importData() {

        sendUSBBroadcast();
    }

    @OnClick({R.id.btn_top_banner, R.id.btn_left_banner, R.id.btn_right_banner, R.id.btn_video_banner, R.id.start_play, R.id.import_data})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_top_banner:
                startActivity(new Intent(SettingsActivity.this, ResourceActivity.class).putExtra("RESOURCE_TYPE", TOP_BANNER));
                break;
            case R.id.btn_left_banner:
                startActivity(new Intent(SettingsActivity.this, ResourceActivity.class).putExtra("RESOURCE_TYPE", LEFT_BANNER));
                break;
            case R.id.btn_right_banner:
                startActivity(new Intent(SettingsActivity.this, ResourceActivity.class).putExtra("RESOURCE_TYPE", RIGHT_BANNER));
                break;
            case R.id.btn_video_banner:
                startActivity(new Intent(SettingsActivity.this, ResourceActivity.class).putExtra("RESOURCE_TYPE", VIDEO_BANNER));
                break;
            case R.id.start_play:
                if (rbOffline.isChecked())
                    startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                else Toast.makeText(SettingsActivity.this, "暂不支持在线模式", Toast.LENGTH_SHORT).show();
                break;
            case R.id.import_data:
                importData();
                break;
        }
    }

    //发送USB广播
    private void sendUSBBroadcast() {
        //发送广播
        Intent intent = new Intent(READ_USB_DEVICE_PERMISSION);
        //发送标准广播
        sendBroadcast(intent);
    }

    /*****
     * 动态注册USB 设备监听
     * */
    private void registerUSBReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        //自定义USB设备读取照片
        intentFilter.addAction(READ_USB_DEVICE_PERMISSION);
        //USB连接状态发生变化时产生的广播
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        USBMTPReceiver usbmtpReceiver = new USBMTPReceiver();
        registerReceiver(usbmtpReceiver, intentFilter);
    }

    /***
     * 请求读写权限
     * ****/
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static int REQUEST_PERMISSION_CODE = 1;

    private void requestReadAndWriteAccess() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SettingsActivity.this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
    }

    //读写权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                SmartToastUtils.showShort(SettingsActivity.this, "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (usbmtpReceiver != null) {
            //取消注册USB设备广播
            unregisterReceiver(usbmtpReceiver);
        }
    }

}