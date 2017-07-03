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
    private String[] al;
    private int block_time=1000;
    private int i=-1;
    private NotificationCompat.Builder builder;
    private final Context ctx=this;
    private Handler h=null;
    private Runnable r;
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
        sp =ctx.getApplicationContext().getSharedPreferences("u",MODE_ENABLE_WRITE_AHEAD_LOGGING | MODE_WORLD_WRITEABLE | MODE_MULTI_PROCESS);
        spe = sp.edit();
        
        block_time=sp.getInt("block_time",1000);
        activity_list=sp.getStringSet("activity_list",new TreeSet<String>());
        al=new String[activity_list.size()];
        activity_list.toArray(al);
        
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
                   
               
                    Intent intent =null;
                        if(i>=al.length-1){i=-1;}
                        i++;
                    intent = ctx.getPackageManager().getLaunchIntentForPackage(al[i]);
                    
                    if(intent != null){
                        ctx.startActivity(intent);
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
    
    
}
