package com.colin.ad_display;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.colin.ad_display.bean.DataBean;
import com.universalvideoview.UniversalVideoView;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import util.Constants;
import util.FileUtils;
import util.ToolUtils;

public class MainActivity extends AppCompatActivity {

    private Banner mBannerTop, mBannerLeft, mBannerRight;
    private UniversalVideoView mVideoView;
    private String videoPath;
    private ArrayList<String> videoPathList = new ArrayList<>();
    private int playIndex;
    public Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //保持亮屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            getWindow().setAttributes(attributes);
        }

        // Activity启动后就锁定为启动时的方向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        //时间
        timer = new Timer();
        timer.schedule(new RemindTask(), 0, 1000);

        mBannerTop = findViewById(R.id.bannerTop);
        mBannerLeft = findViewById(R.id.bannerLeft);
        mBannerRight = findViewById(R.id.bannerRight);
        mVideoView = findViewById(R.id.videoView);

        //顶部广告图
        mBannerTop.setAdapter(new BannerImageAdapter<DataBean>(DataBean.getImgTopFromSDcard()) {
            @Override
            public void onBindView(BannerImageHolder holder, DataBean data, int position, int size) {
                Glide.with(holder.itemView)
                        .load(data.imageUrl)

                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                        .into(holder.imageView);
            }
        });
        mBannerTop.setLoopTime(10000);
//        mBannerTop.setPageTransformer(new ZoomOutPageTransformer());  //切换效果

        //左侧广告图
        mBannerLeft.setAdapter(new BannerImageAdapter<DataBean>(DataBean.getLeftBannerFromSdCard()) {
            @Override
            public void onBindView(BannerImageHolder holder, DataBean data, int position, int size) {
                Glide.with(holder.itemView)
                        .load(data.imageUrl)

                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                        .into(holder.imageView);
            }
        });
        mBannerLeft.setLoopTime(3000);
//        mBannerLeft.setPageTransformer(new DepthPageTransformer());


        //右侧广告图
        mBannerRight.setAdapter(new BannerImageAdapter<DataBean>(DataBean.getRightBannerFromSdCard()) {
            @Override
            public void onBindView(BannerImageHolder holder, DataBean data, int position, int size) {
                Glide.with(holder.itemView)
                        .load(data.imageUrl)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                        .into(holder.imageView);
            }
        });
        mBannerLeft.setLoopTime(5000);

//        videoPathList.add("http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4");
//        videoPathList.add("http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-10_10-20-26.mp4");
//        videoPathList.add("http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-21_16-41-07.mp4");

        List<File> fileList = FileUtils.listFilesInDir(Constants.FILE_VIDEO_PATH);
        for (int i = 0; i < fileList.size(); i++) {
            videoPathList.add(fileList.get(i).getAbsolutePath());
        }

        start();

        //播放完成
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next();
            }
        });

    }

    public void start() {
        mVideoView.setVideoPath(videoPathList.get(playIndex));
        mVideoView.start();
        Log.e("正在播放", "第" + playIndex + "个视频");
    }

    public void next() {
        if (playIndex + 1 < videoPathList.size()) {
            playIndex++;
            start();
        } else {
            playIndex = 0;
            start();
        }

    }


    /***
     * 计时器
     */
    class RemindTask extends TimerTask {
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });

            if (ToolUtils.getTime().equals("01:01:01")) {

            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

}