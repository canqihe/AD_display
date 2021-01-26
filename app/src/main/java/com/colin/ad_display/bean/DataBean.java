package com.colin.ad_display.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import util.Constants;
import util.FileUtils;

public class DataBean {
    public Integer imageRes;
    public String imageUrl;
    public String title;
    public int viewType;

    public Integer getImageRes() {
        return imageRes;
    }

    public void setImageRes(Integer imageRes) {
        this.imageRes = imageRes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DataBean(Integer imageRes, String title, int viewType) {
        this.imageRes = imageRes;
        this.title = title;
        this.viewType = viewType;
    }

    public DataBean(String imageUrl, String title, int viewType) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.viewType = viewType;
    }

    public static List<DataBean> getAd1() {
        List<DataBean> list = new ArrayList<>();
        list.add(new DataBean("https://s3.ax1x.com/2020/12/15/rMsesS.jpg", null, 1));
        list.add(new DataBean("https://s3.ax1x.com/2020/12/15/rMs3R0.jpg", null, 1));
        list.add(new DataBean("https://www.apple.com.cn/home/promos/watch-se/images/tile__cauwwcyyn9hy_large_2x.jpg", null, 1));
        list.add(new DataBean("https://s3.ax1x.com/2020/12/15/rMsdo9.jpg", null, 1));
        list.add(new DataBean("https://s3.ax1x.com/2020/12/15/rMs5Wt.jpg", null, 1));
        return list;
    }

    public static List<DataBean> getAd2() {
        List<DataBean> list = new ArrayList<>();
        list.add(new DataBean("https://img.zcool.cn/community/013de756fb63036ac7257948747896.jpg", null, 1));
        list.add(new DataBean("https://img.zcool.cn/community/01639a56fb62ff6ac725794891960d.jpg", null, 1));
        list.add(new DataBean("https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg", null, 1));
        list.add(new DataBean("https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg", null, 1));
        list.add(new DataBean("https://img.zcool.cn/community/016a2256fb63006ac7257948f83349.jpg", null, 1));
        return list;
    }

    /***
     * 获取顶部广告图
     * @return
     */
    public static List<DataBean> getImgTopFromSDcard() {
        List<DataBean> list = new ArrayList<>();
        List<File> fileList = FileUtils.listFilesInDir(Constants.FILE_IMG_PATH_TOP);
        for (int i = 0; i < fileList.size(); i++) {
            list.add(new DataBean(fileList.get(i).getAbsolutePath(), fileList.get(i).getName(), 1));
        }
        return list;
    }

    /***
     * 从sd卡获取左侧广告图
     * @return
     */
    public static List<DataBean> getLeftBannerFromSdCard() {
        List<DataBean> list = new ArrayList<>();
        List<File> fileList = FileUtils.listFilesInDir(Constants.FILE_IMG_PATH_LEFT);
        for (int i = 0; i < fileList.size(); i++) {
            list.add(new DataBean(fileList.get(i).getAbsolutePath(), fileList.get(i).getName(), 1));
        }
        return list;
    }
    /***
     * 从sd卡获取右侧广告图
     * @return
     */
    public static List<DataBean> getRightBannerFromSdCard() {
        List<DataBean> list = new ArrayList<>();
        List<File> fileList = FileUtils.listFilesInDir(Constants.FILE_IMG_PATH_RIGHT);
        for (int i = 0; i < fileList.size(); i++) {
            list.add(new DataBean(fileList.get(i).getAbsolutePath(), fileList.get(i).getName(), 1));
        }
        return list;
    }

    /***
     * 获取sd卡视频
     * @return
     */
    public static List<DataBean> getVideosFromSDcard() {
        List<DataBean> list = new ArrayList<>();
        List<File> fileList = FileUtils.listFilesInDir(Constants.FILE_VIDEO_PATH);
        for (int i = 0; i < fileList.size(); i++) {
            list.add(new DataBean(fileList.get(i).getAbsolutePath(), fileList.get(i).getName(), 1));
        }
        return list;
    }


    public static List<String> getColors(int size) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(getRandColor());
        }
        return list;
    }

    /**
     * 获取十六进制的颜色代码.例如  "#5A6677"
     * 分别取R、G、B的随机值，然后加起来即可
     *
     * @return String
     */
    public static String getRandColor() {
        String R, G, B;
        Random random = new Random();
        R = Integer.toHexString(random.nextInt(256)).toUpperCase();
        G = Integer.toHexString(random.nextInt(256)).toUpperCase();
        B = Integer.toHexString(random.nextInt(256)).toUpperCase();

        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;

        return "#" + R + G + B;
    }
}
