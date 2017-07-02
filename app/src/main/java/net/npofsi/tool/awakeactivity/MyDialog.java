package net.npofsi.tool.awakeactivity;

import java.util.ArrayList;
import java.util.List;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.app.*;
import android.support.v4.text.*;

/**
 * 弹出所有应用列表
 * @author kfb
 *  
 */
public class MyDialog extends Dialog //implements OnItemClickListener
{
    private Context context = null;

    private GridView appGridView = null;

    //用来记录应用程序的信息
    List<AppsItemInfo> list = null;

    private PackageManager pManager;

    private GetAllApps AllApps= null;

    private baseAdapter adapter = null;

    private int filter = 0;

    //public ArrayList<AppsItemInfo> 
    /**
     * 显示应用程序列表的Dialog
     * @param context
     * @param theme
     */
    public MyDialog(Context context, int filter, int theme) {
        super(context, theme);
        setContentView(R.layout.dialog_apps);
        this.context = context;
        this.filter = filter;
        
        //设置dialog的透明度，高度和宽度
        Window w = getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.alpha = 1.0f;//设置当前对话框的透明度

        appGridView = (GridView) this.findViewById(R.id.gridview);

        // 获取图片、应用名、包名
        pManager = context.getPackageManager();
        AllApps = new GetAllApps(context);

        List<PackageInfo> appList = null;

        appGridView.setNumColumns(4);
        lp.width = (int)(w.getWindowManager().getDefaultDisplay().getWidth()*0.9);
        appList = AllApps.getAllApps(this.filter);

        w.setAttributes(lp);

        this.list = new ArrayList<AppsItemInfo>();

        for (int i = 0; i < appList.size(); i++) {
            PackageInfo pinfo = appList.get(i);
            AppsItemInfo shareItem = new AppsItemInfo();
            // 设置图片
            shareItem.setIcon(pManager.getApplicationIcon(pinfo.applicationInfo));
            // 设置应用程序名字
            shareItem.setLabel(pManager.getApplicationLabel(pinfo.applicationInfo).toString());
            // 设置应用程序的包名
            shareItem.setPackageName(pinfo.applicationInfo.packageName);

            this.list.add(shareItem);

        }

        adapter = new baseAdapter();
        appGridView.setAdapter(adapter);
        appGridView.setOnItemClickListener(null);

        // Dialog按对话框外面取消操作
        this.setCanceledOnTouchOutside(true);
    }

    static class ViewHolder{
        ImageView icon;
        TextView label;
    }

    private class baseAdapter extends BaseAdapter {
        LayoutInflater inflater = LayoutInflater.from(context);

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return (list).size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder;
            if (convertView == null) {
                // 使用View的对象itemView与R.layout.item关联
                convertView = inflater.inflate(R.layout.dialog_item, null);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.apps_image);
                holder.label = (TextView) convertView.findViewById(R.id.apps_textview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.icon.setImageDrawable(list.get(position).getIcon());
            holder.label.setText(list.get(position).getLabel().toString());

            return convertView;

        }//end if

    }

    public Dialog setIL(OnItemClickListener IC){
        appGridView.setOnItemClickListener(IC);
        return this;
    }
    /*
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        
        
        //通过包名打开应用程序
        Intent intent = new Intent();              
        //获取intent  
        intent = packageManager.getLaunchIntentForPackage(pName);
        context.startActivity(intent);
        
    }*/

}
