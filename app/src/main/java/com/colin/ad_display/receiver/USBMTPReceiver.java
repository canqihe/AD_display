package com.colin.ad_display.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Environment;
import android.util.Log;

import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.partition.Partition;

import java.io.IOException;

import util.SmartToastUtils;

import static util.Constants.READ_USB_DEVICE_PERMISSION;


public class USBMTPReceiver extends BroadcastReceiver {

    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        final String action = intent.getAction();
        switch (action) {
            case UsbManager.ACTION_USB_DEVICE_ATTACHED://插上USB设备
                SmartToastUtils.showShort(context, "USB设备已连接");
                UsbDevice find_USB_Device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                //设备不为空
                if (find_USB_Device != null) {
                    // 检查权限
                    permissionRequest(mContext);
                } else {
                    SmartToastUtils.showShort(mContext, "findUsb is null");
                }
                break;
            case UsbManager.ACTION_USB_DEVICE_DETACHED://断开USB设备
                SmartToastUtils.showShort(context, "USB设备已断开");
                try {
                    UsbDevice removedDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (removedDevice != null) {
                        UsbMassStorageDevice usbMassStorageDevice = getUsbMass(removedDevice);
                        if (usbMassStorageDevice != null) {
                            usbMassStorageDevice.close();
                        }
                    }
                } catch (Exception e) {
                    SmartToastUtils.showShort(mContext, "USB断开异常" + e.toString());
                }
                break;
            case READ_USB_DEVICE_PERMISSION: //自定义读取USB 设备信息
                Log.e("读取", "USB已获取权限");
                UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                // 检查U盘权限
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    if (usbDevice != null) {
                        //读取USB 设备
                        readDevice(getUsbMass(usbDevice));
                    } else {
                        SmartToastUtils.showShort(mContext, "没有插入U盘");
                    }
                } else {
                    SmartToastUtils.showShort(mContext, "未获取到U盘权限");
                    Log.e("读取", "未获取到U盘权限");
                    permissionRequest(mContext);
                }
                break;
        }
    }

    /**
     * 进行U盘读写权限的申请
     */
    private void permissionRequest(Context context) {
        Log.e("读取", "开始申请设备权限");
        try {
            // 设备管理器
            UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
            // 获取 U 盘存储设备
            UsbMassStorageDevice[] storageDevices = UsbMassStorageDevice.getMassStorageDevices(context.getApplicationContext());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, new Intent(READ_USB_DEVICE_PERMISSION), 0);
            if (storageDevices.length == 0) {
                SmartToastUtils.showShort(context, "请插入可用的U盘");
            } else {
                //可能有几个 一般只有一个 因为大部分手机只有1个otg插口
                for (UsbMassStorageDevice device : storageDevices) {
                    if (usbManager.hasPermission(device.getUsbDevice())) {
                        Log.e("读取", "USB直接获取权限");
                        readDevice(device);
                    } else {
                        // 进行权限申请
                        usbManager.requestPermission(device.getUsbDevice(), pendingIntent);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("读取", "申请权限异常:" + e.toString());
        }

    }

    private void readDevice(UsbMassStorageDevice device) {
        Log.e("读取", "开始读取设备信息");
        try {
            device.init();
        } catch (IOException e) {
            Log.e("读取", "device.init() error" + e.toString());
            return;
        }
        UsbDevice usbDevice = device.getUsbDevice();
        if (device.getPartitions().size() == 0) {
            Log.e("读取", "未读取到分区");
            return;
        }
        // 设备分区
        Partition partition = device.getPartitions().get(0);
        // 文件系统
        FileSystem currentFs = partition.getFileSystem();
        // 获取 U 盘的根目录
        UsbFile mRootFolder = currentFs.getRootDirectory();

//        // 获取 U 盘的容量
//        long capacity = currentFs.getCapacity();
//        // 获取 U 盘的剩余容量
//        long freeSpace = currentFs.getFreeSpace();
//        // 获取 U 盘的标识
//        String volumeLabel = currentFs.getVolumeLabel();

//        if(mRootFolder.isDirectory()){
//            SmartToastUtils.showShort(mContext,"这是一个文件夹");
//        }else{
//            SmartToastUtils.showShort(mContext,"这不是一个文件夹");
//        }
//        if(mRootFolder.isRoot()){
//            SmartToastUtils.showShort(mContext,"这是根目录");
//        }else{
//            SmartToastUtils.showShort(mContext,"这不是根目录");
//        }
        readAllPicFileFromUSBDevice(mRootFolder, currentFs);
    }

    private void readAllPicFileFromUSBDevice(UsbFile usbFile, FileSystem fileSystem) {
        try {
            UsbFile[] usbFileList = usbFile.listFiles();
            for (UsbFile usbFileItem : usbFileList) {
                if (!usbFileItem.isDirectory()) {
                    String FileEnd = usbFileItem.getName().substring(usbFileItem.getName().lastIndexOf(".") + 1,
                            usbFileItem.getName().length()).toLowerCase();
                    if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("mp4")) {
                        Log.e("读取", "文件名称=" + usbFileItem.getName() + "文件大小=" + usbFileItem.getLength());
//                        FileUtils.saveToPhoneDevice(usbFileItem, fileSystem);
                    }
                } else {
                    readAllPicFileFromUSBDevice(usbFileItem, fileSystem);
                }
            }
        } catch (IOException e) {
            Log.e("读取", "遍历USB文件异常");
        }
    }

    /**
     * USBDevice 转换成UsbMassStorageDevice 对象
     */
    private UsbMassStorageDevice getUsbMass(UsbDevice usbDevice) {
        UsbMassStorageDevice[] storageDevices = UsbMassStorageDevice.getMassStorageDevices(mContext);
        for (UsbMassStorageDevice device : storageDevices) {
            if (usbDevice.equals(device.getUsbDevice())) {
                return device;
            }
        }
        return null;
    }
}
