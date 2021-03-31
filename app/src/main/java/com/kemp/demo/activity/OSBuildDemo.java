package com.kemp.demo.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.kemp.demo.R;
import com.kemp.demo.utils.Tools;

public class OSBuildDemo extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);

        TextView textView = findViewById(R.id.tv_content);

        textView.setText(obtainVersions());
        textView.append("\r\n");
        textView.append(getScreenParames());
    }

    private String obtainVersions() {
        StringBuilder sb = new StringBuilder();
        sb.append("VERSIONS");
        sb.append("\r\n");
        sb.append("\n");

        sb.append("系统版本字符串：");
        sb.append(Build.VERSION.RELEASE);
        sb.append("\r\n");
        sb.append("系统的API：");
        sb.append(Build.VERSION.SDK_INT);
        sb.append("\r\n");
        sb.append("设备基板名称：");
        sb.append(Build.BOARD);
        sb.append("\r\n");
        sb.append("设备引导程序版本号：");
        sb.append(Build.BOOTLOADER);
        sb.append("\r\n");
        sb.append("设备品牌：");
        sb.append(Build.BRAND);
        sb.append("\r\n");
        sb.append("设备指令集名称（CPU的类型）：");
        sb.append(Build.CPU_ABI);
        sb.append("\r\n");
        sb.append("获取第二个指令集名称：");
        sb.append(Build.CPU_ABI2);
        sb.append("\r\n");
        sb.append("设备驱动名称：");
        sb.append(Build.DEVICE);
        sb.append("\r\n");
        sb.append("设备显示的版本包（在系统设置中显示为版本号）和ID一样：");
        sb.append(Build.DISPLAY);
        sb.append("\r\n");
        sb.append("设备的唯一标识：");
        sb.append(Build.FINGERPRINT);
        sb.append("\r\n");
        sb.append("设备硬件名称,一般和基板名称一样（BOARD）：");
        sb.append(Build.HARDWARE);
        sb.append("\r\n");
        sb.append("设备主机地址：");
        sb.append(Build.HOST);
        sb.append("\r\n");
        sb.append("设备版本号：");
        sb.append(Build.ID);
        sb.append("\r\n");
        sb.append("手机的型号 设备名称：");
        sb.append(Build.MODEL);
        sb.append("\r\n");
        sb.append("设备制造商：");
        sb.append(Build.MANUFACTURER);
        sb.append("\r\n");
        sb.append("整个产品的名称：");
        sb.append(Build.PRODUCT);
        sb.append("\r\n");
        sb.append("无线电固件版本号：");
        sb.append(Build.RADIO);
        sb.append("\r\n");
        sb.append("设备标签：");
        sb.append(Build.TAGS);
        sb.append("\r\n");
        sb.append("时间：");
        sb.append(Build.TIME);
        sb.append("\r\n");
        sb.append("设备版本类型：");
        sb.append(Build.TYPE);
        sb.append("\r\n");
        sb.append("设备用户名：");
        sb.append(Build.USER);
        sb.append("\r\n");
        sb.append("设备当前的系统开发代号：");
        sb.append(Build.VERSION.CODENAME);
        sb.append("\r\n");
        sb.append("系统源代码控制值：");
        sb.append(Build.VERSION.INCREMENTAL);
        sb.append("\r\n");

        return sb.toString();
    }

    /**
     * 获取屏幕分辨率等参数
     * android.view.DisplayInfo.logicalDensityDpi
     * android.view.DisplayInfo.physicalXDpi
     * android.view.DisplayInfo.physicalYDpi
     * @return
     */
    private String getScreenParames() {
        DisplayMetrics dm = Tools.getDisplayMetrics(this);

        StringBuilder sb = new StringBuilder();
        sb.append("屏幕分辨率：");
        sb.append(dm.heightPixels + "*" + dm.widthPixels);
        sb.append("\r\n");
        sb.append("屏幕密度：");//逻辑logicalDensityDpi
        sb.append(dm.densityDpi);
        sb.append("\r\n");
        sb.append("密度：");//= 屏幕密度 / 160
        sb.append(dm.density);
        sb.append("\r\n");
        sb.append("scale密度：");
        sb.append(dm.scaledDensity);
        sb.append("\r\n");
        sb.append("xdpi：");
        sb.append(dm.xdpi);//物理
        sb.append("\n");
        sb.append("ydpi：");
        sb.append(dm.ydpi);
        sb.append("\r\n");
        sb.append("物理尺寸：");
        sb.append(dm.heightPixels / dm.ydpi);
        sb.append("*");
        sb.append(dm.widthPixels / dm.xdpi);
        sb.append("(英寸)");
        sb.append("\r\n");
        sb.append("屏幕尺寸：");
        sb.append(Math.sqrt(dm.heightPixels / dm.ydpi * dm.heightPixels / dm.ydpi + dm.widthPixels / dm.xdpi * dm.widthPixels / dm.xdpi));
        sb.append("(英寸)");
        sb.append("\r\n");
        return sb.toString();
    }
}
