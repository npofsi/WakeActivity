package net.npofsi.tool.awakeactivity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 获得所有应用程序
 * @author kfb
 *
 */
public class GetAllApps {
    private Context context = null;

    public GetAllApps(Context context) {
        this.context = context;
    }

    /**
     * 获得所有的APPS
     * @param filter
     * @return
     */
    @SuppressWarnings("static-access")
    public List<PackageInfo> getAllApps(int filter) {
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pManager = context.getPackageManager();

        // 获取手机内所有应用
        List<PackageInfo> packlist = pManager.getInstalledPackages(0);

        for (int i = 0; i < packlist.size(); i++) {
            PackageInfo pak = (PackageInfo) packlist.get(i);
            String packageName = pak.applicationInfo.packageName;

            switch (filter) {
                case 0:             //所有应用
                    if (!filterErrorPackage(pManager, packageName)) {
                        apps.add(pak);
                    }
                    break;
                case 1:             //第三方应用
                    if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                        if (!filterErrorPackage(pManager, packageName)) {
                            apps.add(pak);
                        }
                    }
                    break;
                case 2:             //系统自带应用
                    if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) != 0) {
                        if (!filterErrorPackage(pManager, packageName)) {
                            apps.add(pak);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return apps;
    }

    /**
     * 判断当前包是否可以打开
     * @return
     */
    public boolean filterErrorPackage(PackageManager pManager, String packageName) {
        Intent intent = new Intent();
        intent = pManager.getLaunchIntentForPackage(packageName);

        //如果该程序不可启动（像系统自带的包，有很多是没有入口的）会返回NULL 
        if (intent == null) {
            return true;
        }

        return false;

    }
}
