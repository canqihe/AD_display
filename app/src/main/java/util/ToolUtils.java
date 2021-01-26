package util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Colin
 * on 12/18/20
 * E-mail: hecanqi168@gmail.com
 */
public class ToolUtils {
    private ToolUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 关闭IO
     *
     * @param closeables closeables
     */
    public static void closeIO(Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 安静关闭IO
     *
     * @param closeables closeables
     */
    public static void closeIOQuietly(Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public static void showToast(Context context, String stringTx) {
        popToast(context, stringTx, Toast.LENGTH_SHORT);
    }

    public static void popToast(Context context, String toastText, int during) {
        if (context == null) {
            return;
        }
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing())
                return;
        }
        Toast sToast = Toast.makeText(context, toastText, during);
        sToast.show();
    }

    /**
     * 导航栏，状态栏隐藏
     *
     * @param activity
     */
    public static void NavigationBarStatusBar(Activity activity, boolean hasFocus) {
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    /***
     * 时间
     * @return
     */
    public static String getTime() {
        return new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(new Date());
    }

    /***
     * 日期
     * @return
     */
    public static String getDate() {
        return new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA).format(new Date());
    }

    /**
     * 判断是否有外置SD卡挂载
     *
     * @return
     */
    public static boolean isExternalSdMount() {
        List<String> extPath = getAllExterSdcardPath();
        for (int i = 0; i < extPath.size(); i++) {
            if (!extPath.get(i).equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
                Log.e("读取path", extPath.get(i));
                return true;
            }
        }
        return false;
    }


    /**
     * 查找所有sd卡
     *
     * @return
     */
    public static List<String> getAllExterSdcardPath() {
        List<String> SdList = new ArrayList<String>();
        // 得到路径
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                // 将常见的linux分区过滤掉
                if (line.contains("secure"))
                    continue;
                if (line.contains("asec"))
                    continue;
                if (line.contains("media"))
                    continue;
                if (line.contains("system") || line.contains("cache")
                        || line.contains("sys") || line.contains("data")
                        || line.contains("tmpfs") || line.contains("shell")
                        || line.contains("root") || line.contains("acct")
                        || line.contains("proc") || line.contains("misc")
                        || line.contains("obb")) {
                    continue;
                }

                if (line.contains("fat") || line.contains("fuse") || (line
                        .contains("ntfs"))) {

                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        String path = columns[1];
                        if (path != null && !SdList.contains(path) && path.contains("sd")) {
                            SdList.add(columns[1]);
                            Log.e("读取", "路径 " + path);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return SdList;
    }

}
