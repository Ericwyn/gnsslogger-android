package com.example.gnssloggerbutterflying.RinexFilefragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

/**
 * @Description: 打开文件工具类
 * @author: butterflying10
 * @time: 2020/3/18
 */
public class OpenFileUtil {
    /**
     * 使用自定义方法打开文件
     */
    public static void openFile(Activity activityFrom, File file) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //  此处注意替换包名，
            Uri contentUri = FileProvider.getUriForFile(activityFrom, "com.example.testfragment.fileprovider", file);
            Log.e("file_open", " uri   " + contentUri.getPath());
            intent.setDataAndType(contentUri, getMimeTypeFromFile(file));
//            intent.setDataAndType(contentUri, "image/*");
        } else {
            intent.setDataAndType(Uri.fromFile(file), getMimeTypeFromFile(file));//也可使用 Uri.parse("file://"+file.getAbsolutePath());
        }

        //以下设置都不是必须的
        intent.setAction(Intent.ACTION_VIEW);// 系统根据不同的Data类型，通过已注册的对应Application显示匹配的结果。
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//系统会检查当前所有已创建的Task中是否有该要启动的Activity的Task
        //若有，则在该Task上创建Activity；若没有则新建具有该Activity属性的Task，并在该新建的Task上创建Activity。
        intent.addCategory(Intent.CATEGORY_DEFAULT);//按照普通Activity的执行方式执行
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        activityFrom.startActivity(intent);
    }

    /**
     * 使用自定义方法获得文件的MIME类型
     */
    public static String getMimeTypeFromFile(File file) {
        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex > 0) {
            //获取文件的后缀名
            String end = fName.substring(dotIndex, fName.length()).toLowerCase(Locale.getDefault());
            //在MIME和文件类型的匹配表中找到对应的MIME类型。
            HashMap<String, String> map = MyMimeMap.getMimeMap();
            if (!TextUtils.isEmpty(end) && map.keySet().contains(end)) {
                type = map.get(end);
            }
        }
        Log.i("bqt", "我定义的MIME类型为：" + type);
        return type;
    }
    static class MyMimeMap {
        private static  final HashMap<String, String> mapSimple = new HashMap<>();
        private static  final HashMap<String, String> mapAll = new HashMap<>();
        /**
         *  常用"文件扩展名—MIME类型"匹配表。
         *  注意，此表并不全，也并不是唯一的，就像有人喜欢用浏览器打开TXT一样，你可以根据自己的爱好自定义。
         */
        public static  HashMap<String, String> getMimeMap() {
            if (mapSimple.size() == 0) {
                mapSimple.put(".20o", "text/html");
                mapSimple.put(".3gp", "video/3gpp");
                mapSimple.put(".apk", "application/vnd.android.package-archive");
                mapSimple.put(".asf", "video/x-ms-asf");
                mapSimple.put(".avi", "video/x-msvideo");
                mapSimple.put(".bin", "application/octet-stream");
                mapSimple.put(".bmp", "image/bmp");
                mapSimple.put(".c", "text/plain");
                mapSimple.put(".chm", "application/x-chm");
                mapSimple.put(".class", "application/octet-stream");
                mapSimple.put(".conf", "text/plain");
                mapSimple.put(".cpp", "text/plain");
                mapSimple.put(".doc", "application/msword");
                mapSimple.put(".docx", "application/msword");
                mapSimple.put(".exe", "application/octet-stream");
                mapSimple.put(".gif", "image/gif");
                mapSimple.put(".gtar", "application/x-gtar");
                mapSimple.put(".gz", "application/x-gzip");
                mapSimple.put(".h", "text/plain");
                mapSimple.put(".htm", "text/html");
                mapSimple.put(".html", "text/html");
                mapSimple.put(".jar", "application/java-archive");
                mapSimple.put(".java", "text/plain");
                mapSimple.put(".jpeg", "image/jpeg");
                mapSimple.put(".jpg", "image/jpeg");
                mapSimple.put(".js", "application/x-javascript");
                mapSimple.put(".log", "text/plain");
                mapSimple.put(".m3u", "audio/x-mpegurl");
                mapSimple.put(".m4a", "audio/mp4a-latm");
                mapSimple.put(".m4b", "audio/mp4a-latm");
                mapSimple.put(".m4p", "audio/mp4a-latm");
                mapSimple.put(".m4u", "video/vnd.mpegurl");
                mapSimple.put(".m4v", "video/x-m4v");
                mapSimple.put(".mov", "video/quicktime");
                mapSimple.put(".mp2", "audio/x-mpeg");
                mapSimple.put(".mp3", "audio/x-mpeg");
                mapSimple.put(".mp4", "video/mp4");
                mapSimple.put(".mpc", "application/vnd.mpohun.certificate");
                mapSimple.put(".mpe", "video/mpeg");
                mapSimple.put(".mpeg", "video/mpeg");
                mapSimple.put(".mpg", "video/mpeg");
                mapSimple.put(".mpg4", "video/mp4");
                mapSimple.put(".mpga", "audio/mpeg");
                mapSimple.put(".msg", "application/vnd.ms-outlook");
                mapSimple.put(".ogg", "audio/ogg");
                mapSimple.put(".pdf", "application/pdf");
                mapSimple.put(".png", "image/png");
                mapSimple.put(".pps", "application/vnd.ms-powerpoint");
                mapSimple.put(".ppt", "application/vnd.ms-powerpoint");
                mapSimple.put(".pptx", "application/vnd.ms-powerpoint");
                mapSimple.put(".prop", "text/plain");
                mapSimple.put(".rar", "application/x-rar-compressed");
                mapSimple.put(".rc", "text/plain");
                mapSimple.put(".rmvb", "audio/x-pn-realaudio");
                mapSimple.put(".rtf", "application/rtf");
                mapSimple.put(".sh", "text/plain");
                mapSimple.put(".tar", "application/x-tar");
                mapSimple.put(".tgz", "application/x-compressed");
                mapSimple.put(".txt", "text/plain");
                mapSimple.put(".wav", "audio/x-wav");
                mapSimple.put(".wma", "audio/x-ms-wma");
                mapSimple.put(".wmv", "audio/x-ms-wmv");
                mapSimple.put(".wps", "application/vnd.ms-works");
                mapSimple.put(".xml", "text/plain");
                mapSimple.put(".xls", "application/vnd.ms-excel");
                mapSimple.put(".xlsx", "application/vnd.ms-excel");
                mapSimple.put(".z", "application/x-compress");
                mapSimple.put(".zip", "application/zip");
                mapSimple.put("", "*/*");
            }
            return mapSimple;
        }

    }
}


