package net.edrop.edrop_employer;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import net.edrop.edrop_employer.model.Model;

import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
    private static Application instance2;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        //极光推送
        JPushInterface.setDebugMode(true);//打开调试模式
        JPushInterface.init(this);

        instance2 = this;
        Fresco.initialize(this);
        //初始化环信easeui
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);//设置需要同意后才能接受邀请
        EMClient.getInstance().init(this, options);
        //初始化数据模型层类
        Model.getInstance().init(this);
        //初始化全局上下文
        mContext = this;

    }

    public static Application getInstance2(){
        return instance2;
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return processName;
    }


    //获取全局上下文对象
    public static Context getGlobalApplication(){
        return mContext;
    }
}
