package com.example.gnssloggerbtf.RinexFilefragments;

import android.content.Context;
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
    public static void openFile(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //  此处注意替换包名，

            Uri contentUri = FileProvider.getUriForFile(context,  context.getPackageName()+".fileprovider",file);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, getMimeTypeFromFile(file));
            Log.e("file_open", " uri   " + contentUri.getPath());
        } else {
            intent.setDataAndType(Uri.fromFile(file), getMimeTypeFromFile(file));//也可使用 Uri.parse("file://"+file.getAbsolutePath());
        }

        //以下设置都不是必须的
        intent.setAction(Intent.ACTION_VIEW);// 系统根据不同的Data类型，通过已注册的对应Application显示匹配的结果。
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//系统会检查当前所有已创建的Task中是否有该要启动的Activity的Task
        //若有，则在该Task上创建Activity；若没有则新建具有该Activity属性的Task，并在该新建的Task上创建Activity。
        intent.addCategory(Intent.CATEGORY_DEFAULT);//按照普通Activity的执行方式执行
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
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
                mapSimple.put(".20o", "text/plain");
                mapSimple.put(".20n", "text/plain");
                mapSimple.put(".20o", "text/html");
                mapSimple.put(".20n", "text/html");
                mapSimple.put("", "*/*");
            }
            return mapSimple;
        }
    }
}


