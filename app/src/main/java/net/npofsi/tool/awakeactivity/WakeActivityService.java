package net.npofsi.tool.awakeactivity;
import android.app.*;
import android.os.*;
import android.content.*;
import android.support.v7.app.*;
import java.util.*;
import android.widget.*;

public class WakeActivityService extends Service
{
    private SharedPreferences sp;
    private SharedPreferences.Editor spe;
    private Set<String> activity_list=new TreeSet<String>();
	private Set<String> package_list=new TreeSet<String>();
    private String[] al;
	private String[] pl;
    private int block_time=1000;
    private int i=-1;
    private Boolean isOnce=false;
    private NotificationCompat.Builder builder;
    private final Context ctx=this;
    private Handler h=null;
    private Runnable r;
	private Service ctxs=this;
    @Override
    public IBinder onBind(Intent p1)
    {
        // TODO: Implement this method
        return null;
    }

    @Override
    public void onCreate()
    {
        // TODO: Implement this method
		
        super.onCreate();
		
        sp =ctx.getApplicationContext().getSharedPreferences("u",MODE_ENABLE_WRITE_AHEAD_LOGGING | MODE_MULTI_PROCESS);
        spe = sp.edit();
        
        block_time=sp.getInt("block_time",10000);
        activity_list=sp.getStringSet("activity_list",new TreeSet<String>());
		package_list=sp.getStringSet("package_list",new TreeSet<String>());
        al=new String[activity_list.size()];
        activity_list.toArray(al);
		pl=new String[package_list.size()];
		package_list.toArray(pl);
        isOnce=sp.getBoolean("isOnce",false);
        i=-1;
        
        builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("WakeActivity running...");
        builder.setContentText("点击以停止唤醒.");
        Intent intent = new Intent(this,StopServiceActivity.class/* MainActivity.class*/);
        PendingIntent pendingIntent = PendingIntent.getActivity
        (this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        builder.setContentIntent(pendingIntent);
//启动到前台
        //builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        startForeground(1, notification);
        
        
        
        
        /*
        *running service
        *main task at this
        *
        *
        */
        if(al.length!=0)
        runx();
        
        
    }

    public void runx(){
        (h=new Handler()).postDelayed((r=new Runnable(){
                
                @Override
                public void run()
                {
                    
                    // TODO: Implement this method
                    if(al.length!=0){
                   
               
                    //Intent intent =null;
                        if(i>=al.length-1){i=-1;
                        if(isOnce){
                            stop();
                        }}
                        i++;
                    final Intent intent = new Intent();
                    
                    if(intent != null){
                        
						ComponentName cn=new ComponentName(al[i].split(":")[0],
														   al[i].split(":")[1]);  
						intent.setComponent(cn);
						//getPackageManager().;
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						
						try{ctx.startActivity(intent);}catch(Exception err){print(al[i].split(":")[1]+" 不允许访问，请从列表中移除.");ctxs.stopSelf();}
						try{ctx.startService(intent);}catch(Exception err){print(al[i].split(":")[1]+" 不允许访问，请从列表中移除.");ctxs.stopSelf();}
                    }
                
                    runx();
                    
                    /*}catch(Exception err){
                        builder.setContentText("Error:"+err);
                        Toast.makeText(ctx,"Error:"+err,Toast.LENGTH_LONG).show();
                        //i=0;run();
                    }*/
                    }
                }
            }),block_time);
        
    }

    @Override
    public void onStart(Intent intent,int startId)
    {
        // TODO: Implement this method
        super.onStart(intent, startId);
        
        //return flags;
    }

    @Override
    public void onDestroy()
    {
        // TODO: Implement this method
        super.onDestroy();
        if(h!=null){

            h.removeCallbacks(r);
            h.sendEmptyMessage(0);
            this.stopSelf();
        }
        
    }
    public void stop(){
        this.stopSelf();
    }
    public void print(String str){
		Toast.makeText(ctx,str,Toast.LENGTH_LONG).show();
	}
}
