package net.npofsi.tool.awakeactivity;
import android.content.*;
import android.content.pm.*;
import java.util.*;
import android.graphics.drawable.*;

public class AUtils
{
    public static Intent wakeActivity(Context context,String activity){
        Intent intent=new Intent();
        intent.setComponent(new ComponentName(context,activity));
        context.startActivity(intent);
        return intent;
    }
	public static String getComponentFileName(CharSequence namec){
		String[] str=namec.toString().split("\\u002E");
		return str[str.length-1];
	}
    public static class Apps{
        public static Context mctx;
        public static PackageManager mPackageManager;
        public static List<ApplicationInfo> appList;
        public static List<PackageInfo> packList;
        public static List<Drawable> iconList=new ArrayList<Drawable>();
        public static List<String> packNameList=new ArrayList<String>();
        public static List<CharSequence> labelList=new ArrayList<CharSequence>();
        
        public Apps(Context ctx){
            this.mctx=ctx;
            this.mPackageManager=ctx.getPackageManager();
            this.appList=mPackageManager.getInstalledApplications(0);
            this.packList=mPackageManager.getInstalledPackages(0);
            int appsCount=appList.size()-1;
            for(int ipn=0;ipn<appsCount;ipn++){
                this.packNameList.add(ipn,appList.get(ipn).packageName);
            }
            for(int iin=0;iin<appsCount;iin++){
                this.labelList.add(iin,mPackageManager.getApplicationLabel(appList.get(iin)));
            }
            for(int icn=0;icn<appsCount;icn++){
                this.iconList.add(icn,mPackageManager.getApplicationIcon(appList.get(icn)));
            }
        }
        public static PackageManager getPackageManager(){
            return mPackageManager;
        }
        public static int getAppsCount(){
            return appList.size();
        }
    }
	
}
