package com.colin.ad_display;

import android.app.Application;
import android.widget.Toast;

import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.converter.SerializableDiskConverter;

import util.Constants;
import util.FileUtils;


/**
 * Created by Colin
 * on 2020/8/20
 * E-mail: hecanqi168@gmail.com
 */
public class MyApplication extends Application {

    public static volatile MyApplication instance;

    public synchronized static MyApplication getInstance() {
        if (instance == null) {
            synchronized (MyApplication.class) {
                if (instance == null)
                    instance = new MyApplication();
            }
        }
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();

//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(this);

        EasyHttp.init(this);
        EasyHttp.getInstance()
                .debug("RxEasyHttp", true)
                .setReadTimeOut(60 * 1000)
                .setWriteTimeOut(60 * 1000)
                .setConnectTimeout(60 * 1000)
                .setRetryCount(3)//默认网络不好自动重试3次
                .setRetryDelay(500)//每次延时500ms重试
                .setRetryIncreaseDelay(500)//每次延时叠加500ms
                .setCacheDiskConverter(new SerializableDiskConverter())//默认缓存使用序列化转化
                .setCacheMaxSize(50 * 1024 * 1024)//设置缓存大小为50M
                .setCacheVersion(1)//缓存版本为1
                .setCertificates();//信任所有证书


        //创建文件夹
        if (!FileUtils.createOrExistsDir(Constants.FILE_IMG_PATH) || !FileUtils.createOrExistsDir(Constants.FILE_VIDEO_PATH))
            Toast.makeText(getApplicationContext(), "文件夹创建失败", Toast.LENGTH_SHORT).show();
    }

}
