package net.npofsi.tool.awakeactivity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.*;
import java.util.*;
import android.widget.*;
import android.view.View.*;
import android.app.*;
import android.widget.AbsoluteLayout.*;
import android.view.*;
import android.os.*;
import android.app.ActivityManager.RunningServiceInfo;
import android.text.method.*;
import android.widget.AdapterView.*;
import android.content.pm.*;
public class MainActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private SharedPreferences.Editor spe;
    private Set<String> activity_list;
    private final Set<String> nuS=new TreeSet<String>();
    
    private int block_time;
    private EditText edt;
    private final Context ctx=this;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        sp = getSharedPreferences("u",MODE_ENABLE_WRITE_AHEAD_LOGGING | MODE_WORLD_WRITEABLE | MODE_MULTI_PROCESS);
        spe = sp.edit();
        activity_list=sp.getStringSet("activity_list",nuS);
        block_time=sp.getInt("block_time",10000);
        
        edt=(EditText) findViewById(R.id.block_time);
        edt.setText(""+block_time);
        
        loadActivityList();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Please add package.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                runOnUiThread(new Runnable(){
                    public void run(){
     //============================================================================================
                        final ProgressDialog abd=new ProgressDialog(ctx);
                        
                        abd.setTitle("Waiting...");
                        abd.create();
                        abd.show();
                        
    //============================================================================================      
    
                        Thread th=new Thread(new Runnable(){
                            public void run(){
                                final AUtils.Apps apps=new AUtils.Apps(ctx);
                                abd.dismiss();
                                runOnUiThread(new Runnable(){
                                    public void run(){
                                        LinearLayout lly=new LinearLayout(ctx);
                                        ScrollView scv=new ScrollView(ctx);
                                        lly.setOrientation(1);
                                        AlertDialog.Builder abbc=new AlertDialog.Builder(ctx);
                                        final AlertDialog ab=abbc.create();
                                        for(int i=0;i<apps.getAppsCount()-1;i++){
                                            LinearLayout lcy=new LinearLayout(ctx);
                                            lcy.setOrientation(0);
                                            ImageView imgv=new ImageView(ctx);
                                            imgv.setMaxHeight(140);
                                            imgv.setMaxWidth(140);
                                            TextView txtv=new TextView(ctx);
                                            txtv.setHint(apps.packNameList.get(i));
                                            txtv.setText(apps.labelList.get(i));
                                            txtv.setGravity(Gravity.LEFT|Gravity.CENTER);
                                            imgv.setImageDrawable(apps.iconList.get(i));
                                            txtv.setHeight(140);
                                            
                                            lcy.addView(imgv,140,140);
                                            lcy.addView(txtv);
                                            
                                            txtv.setOnClickListener(new OnClickListener(){

                                                    @Override
                                                    public void onClick(View p1)
                                                    {
                                                        // TODO: Implement this method
                                                        String name=((TextView)p1).getHint().toString();
                                                        
                                                        if(!("".equals(name))){
                                                            activity_list.add(name);
                                                            refreshSetting();
                                                            }
                                                        else{
                                                            Snackbar.make(p1, "Please type package name worked.", Snackbar.LENGTH_LONG)
                                                                .setAction("Action", null).show();
                                                       }
                                                        runOnUiThread(new Runnable(){

                                                                @Override
                                                                public void run()
                                                                {
                                                                    // TODO: Implement this method
                                                                    ab.dismiss();
                                                                }
                                                            });
                                                    }
                                            });
                                            lly.addView(lcy);
                                        }
                                        
                                        ab.setTitle("Choose an APP to start.");
                                        scv.addView(lly);
                                        scv.setPadding(5,10,5,10);
                                        ab.setView(scv);
                                        ab.show();
                                    }
                                });
                            }                        
                        });
                        th.start();

    //============================================================================================
    
                       } });
            }
        });
        
        ((ImageButton)findViewById(R.id.save)).setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                    // TODO: Implement this method
                    {
                        block_time=new Integer((edt).getText().toString());
                        refreshSetting();
                        Snackbar.make(p1, "Setting is saved.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                        
                    }
                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            
            
            LinearLayout lc=new LinearLayout(ctx);
            TextView ttv=new TextView(ctx);
            ttv.setText("    Maker:万能的N/P硅-npofsi©2017.6.24\n    感谢@xfy9326提供的技术支持\n    Tool:AIDE,ApkEditor,AS2AIDE\n    Email:npofsi@outlook.com\n");
            ttv.setLineSpacing(2,2);
            lc.addView(ttv);
            AlertDialog.Builder ab=new AlertDialog.Builder(ctx);
            ab.setTitle("About");
            ab.setView(lc);
            ab.setNegativeButton("ok", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface p1, int p2)
                    {
                        // TODO: Implement this method
                        
                        
                    }
                });
            ab.show();
            
            
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    public void refreshSetting(){
        spe.clear();
        spe.putInt("block_time",block_time);  
        //spe.commit();
        spe.putStringSet("activity_list",activity_list);
        spe.commit();
        edt.setText(""+block_time);
        loadActivityList();
    }
    
    public void loadActivityList(){
        final LinearLayout sv=(LinearLayout) findViewById(R.id.activity_list);
        sv.removeAllViews();
        int c=0;
        final LinearLayout[] lla=new LinearLayout[activity_list.size()];
        for(String i:activity_list){
            
            LinearLayout ll=new LinearLayout(ctx);
            lla[c]=ll;
            
            final TextView txv=new TextView(ctx);
            txv.setHint("=Activity=");
            txv.setGravity(Gravity.CENTER);
            txv.setText(i);
            txv.setId(c);
            txv.setTextSize(18);
            HorizontalScrollView hsv=new HorizontalScrollView(ctx);
            ImageButton imb=new ImageButton(ctx);
            imb.setImageDrawable(getDrawable(android.R.drawable.ic_menu_close_clear_cancel));
            imb.setOnClickListener(new OnClickListener(){

                    @Override
                    public void onClick(final View p1)
                    {
                        // TODO: Implement this method
                        runOnUiThread(new Runnable(){

                                @Override
                                public void run()
                                {
                                    // TODO: Implement this method
                                    
                                    sv.removeView(lla[txv.getId()]);
                                    activity_list.remove(txv.getText());
                                    refreshSetting();
                                }
                            });
                    }
                });
            hsv.addView(txv,LayoutParams.WRAP_CONTENT,120);
            ll.addView(hsv,LayoutParams.WRAP_CONTENT,120);
            hsv.setScrollContainer(false);
            hsv.setHorizontalScrollBarEnabled(false);
            txv.setHorizontalScrollBarEnabled(false);
            //txv.setHorizontallyScrolling(fal);
            txv.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            ll.addView(imb,120,120);
            ll.setGravity(Gravity.RIGHT);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            
            sv.addView(ll);
        }
    }
    
    public void start(View v){
        Intent intent= new Intent(MainActivity.this,WakeActivityService.class);
        try{
            if(isServiceRunning(ctx,"net.npofsi.tool.awakeactivity.WakeActivityService")){      
                stopService(intent);
                
                Snackbar.make(v, "Stopped...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            }else{
                startService(intent);
                Snackbar.make(v, "Started...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            }
        }catch(Exception err){}
        
        
    }
    public static boolean isServiceRunning(Context context,String serviceName){
        // 校验服务是否还存在
        ActivityManager am = (ActivityManager) context
            .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> services = am.getRunningServices(100);
        for (RunningServiceInfo info : services) {
            // 得到所有正在运行的服务的名称
            String name = info.service.getClassName();
            if (serviceName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onRestart()
    {
        // TODO: Implement this method
        super.onRestart();
        
    }

   

    
}
