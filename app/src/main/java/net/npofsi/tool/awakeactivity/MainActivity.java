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
import android.graphics.*;
import android.widget.CompoundButton.*;
import android.content.pm.PackageManager.*;
import android.support.v7.*;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private SharedPreferences.Editor spe;
    private Set<String> activity_list;
	private Set<String> package_list;
    private final Context ctx=this;
    private final Set<String> nuS=new TreeSet<String>();
    private int block_time;
    private EditText edt,edc;
    private ActivityInfo[] activities;
	private ServiceInfo[] services;
	private float dip=2;
	private int times=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dip = ctx.getResources().getDisplayMetrics().density;
    
        sp = getSharedPreferences("u",MODE_ENABLE_WRITE_AHEAD_LOGGING | MODE_MULTI_PROCESS);
        spe = sp.edit();
        activity_list=sp.getStringSet("activity_list",nuS);
		package_list=sp.getStringSet("package_list",nuS);
        block_time=sp.getInt("block_time",10000);
        edt=(EditText) findViewById(R.id.block_time);
        edt.setText(""+block_time);
		times=sp.getInt("times",-1);
		edc=(EditText) findViewById(R.id.times);
		edc.setText(""+times);
        loadActivityList();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getText(R.string.action_add_package), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        
                
                runOnUiThread(new Runnable(){
                    public void run(){
     //============================================================================================
                        final ProgressDialog abd=new ProgressDialog(ctx);
                        
                        abd.setTitle(R.string.title_wait);
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
                                            imgv.setMaxHeight((int)Math.floor(140*dip));
                                            imgv.setMaxWidth((int)Math.floor(140*dip));
                                            TextView txtv=new TextView(ctx);
                                            txtv.setHint(apps.packNameList.get(i));
                                            txtv.setText(apps.labelList.get(i));
                                            txtv.setGravity(Gravity.LEFT|Gravity.CENTER);
                                            imgv.setImageDrawable(apps.iconList.get(i));
                                            txtv.setHeight((int)Math.floor(50*dip));
                                            lcy.addView(imgv,(int)Math.floor(50*dip),(int)Math.floor(50*dip));
                                            lcy.addView(txtv);
                                            txtv.setOnClickListener(new OnClickListener(){
                                                    @Override
                                                    public void onClick(View p1)
                                                    {
                                                        // TODO: Implement this method
                                                        final String name=((TextView)p1).getHint().toString();
                                                        //    activity_list.add(name);
                                                        //    refreshSetting();
                                                        
														runOnUiThread(new Runnable(){

                                                                @Override
                                                                public void run()
                                                                {
                                                                    ab.dismiss();
																		Thread h=new Thread(new Runnable(){ 
																			public void run(){
																				try
																				{
																					PackageInfo painfo=ctx.getPackageManager().getPackageInfo(name, PackageManager.GET_ACTIVITIES);
																				
																				
																					activities=painfo.activities;
																					PackageInfo psinfo=ctx.getPackageManager().getPackageInfo(name,PackageManager.GET_SERVICES);
																					services=psinfo.services;
																				}
																				catch (PackageManager.NameNotFoundException e)
																				{}
																		runOnUiThread(new Runnable(){public void run(){				
																		//try{
																		AlertDialog.Builder cbbc=new AlertDialog.Builder(ctx);
																		final AlertDialog cb=cbbc.create();
																		LinearLayout lcb=new LinearLayout(ctx);
																		ScrollView scb=new ScrollView(ctx);
																		scb.addView(lcb);
																		cb.setTitle(R.string.title_choose_component);
																		lcb.setPadding(5,5,5,5);
																		
																		lcb.setOrientation(1);
																		cb.setView(scb);
																			if(activities==null||services==null){
																				cb.setView(new TextView(ctx));
																				cb.setTitle(R.string.title_system_app);
																			}else{
																				
																				LinearLayout lcyg=new LinearLayout(ctx);
																				lcyg.setOrientation(0);
																				ImageView imgvg=new ImageView(ctx);
																				imgvg.setMaxHeight((int)Math.floor(140*dip));
																				imgvg.setMaxWidth((int)Math.floor(140*dip));
																				TextView txtvg=new TextView(ctx);
																				txtvg.setHint(";launch:"+name);
																				txtvg.setText("启动项:"+name);
																				txtvg.setGravity(Gravity.LEFT|Gravity.CENTER);
																				try{imgvg.setImageDrawable(apps.mPackageManager.getApplicationIcon(name));}catch(Exception err){}
																				txtvg.setHeight((int)Math.floor(50*dip));
																				lcyg.addView(imgvg,(int)Math.floor(50*dip),(int)Math.floor(50*dip));
																				lcyg.addView(txtvg);
																				lcyg.setPadding(5,0,5,0);
																				txtvg.setOnClickListener(new View.OnClickListener(){

																						@Override
																						public void onClick(View p1)
																						{
																							// TODO: Implement this method
																							cb.dismiss();
																							activity_list.add(";launch:"+name);
																							refreshSetting();
																						}
																					});
																				lcb.addView(lcyg);
																				
																		TextView tttx=new TextView(ctx);
																		tttx.setText("Activity:");
																		tttx.setPadding(15,15,0,0);
																		tttx.setTextSize(20);
																		lcb.addView(tttx);
																		if(activities.length!=0)for(int i=0;i<activities.length-1;i++){
																			LinearLayout lcy=new LinearLayout(ctx);
																			lcy.setOrientation(0);
																			ImageView imgv=new ImageView(ctx);
																			imgv.setMaxHeight((int)Math.floor(140*dip));
																			imgv.setMaxWidth((int)Math.floor(140*dip));
																			TextView txtv=new TextView(ctx);
																			txtv.setHint(activities[i].name);
																			txtv.setText(activities[i].loadLabel(ctx.getPackageManager())+"."+AUtils.getComponentFileName(activities[i].name));
																			txtv.setGravity(Gravity.LEFT|Gravity.CENTER);
																			try{imgv.setImageDrawable(activities[i].loadIcon(ctx.getPackageManager()));}catch(Exception err){}
																			txtv.setHeight((int)Math.floor(50*dip));
																			lcy.addView(imgv,(int)Math.floor(50*dip),(int)Math.floor(50*dip));
																			lcy.addView(txtv);
																			lcy.setPadding(5,0,5,0);
																			txtv.setOnClickListener(new View.OnClickListener(){

																					@Override
																					public void onClick(View p1)
																					{
																						// TODO: Implement this method
																						cb.dismiss();
																						activity_list.add(name+":"+((TextView)p1).getHint().toString());
																						refreshSetting();
																					}
																				});
																			lcb.addView(lcy);
																		}
																		/*TextView ttt=new TextView(ctx);
																		ttt.setText("Services:");
																		ttt.setPadding(15,15,0,0);
																		ttt.setTextSize(20);
																		lcb.addView(ttt);
																		if(services.length!=0)for(int i=0;i<services.length-1;i++){
																			LinearLayout lcy=new LinearLayout(ctx);
																			lcy.setOrientation(0);
																			ImageView imgv=new ImageView(ctx);
																			imgv.setMaxHeight((int)Math.floor(140*dip));
																			imgv.setMaxWidth((int)Math.floor(140*dip));
																			TextView txtv=new TextView(ctx);
																			txtv.setHint(services[i].name);
																			txtv.setText(services[i].loadLabel(ctx.getPackageManager()));
																			txtv.setGravity(Gravity.LEFT|Gravity.CENTER);
																			try{imgv.setImageDrawable(services[i].loadIcon(ctx.getPackageManager()));}catch(Exception err){}
																			txtv.setHeight((int)Math.floor(50*dip));
																			lcy.addView(imgv,(int)Math.floor(50*dip),(int)Math.floor(50*dip));
																			lcy.addView(txtv);
																			lcy.setPadding(5,0,5,0);
																			txtv.setOnClickListener(new View.OnClickListener(){

																					@Override
																					public void onClick(View p1)
																					{
																						// TODO: Implement this method
																						cb.dismiss();
																						
																						activity_list.add(name+":"+((TextView)p1).getHint().toString());
																						
																						refreshSetting();
																					}
																				});
																			lcb.addView(lcy);
																		}
																		*/
																		
																		}
																		cb.show();
																		//}catch(Exception err){ Toast.makeText(ctx,name+err.toString(),Toast.LENGTH_LONG).show();}
																		}});
																		
																			}});
																		h.start();
																	
                                                                }
                                                            });
														//toChooseComponents
														
                                                    }
                                            });
                                            lly.addView(lcy);
                                        }
                                        ab.setTitle(R.string.title_choose_app);
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
			ttv.setPadding(15,15,0,0);
            ttv.setLinksClickable(true);
            ttv.setLinkTextColor(Color.parseColor("#bf360c"));
            ttv.setText(R.string.part_about);
            ttv.setLineSpacing(2,2);
            lc.addView(ttv);
            AlertDialog.Builder ab=new AlertDialog.Builder(ctx);
            ab.setTitle(R.string.title_about);
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
		spe.putInt("times",times);
		spe.putStringSet("package_list",package_list);
        //spe.commit();
        spe.putStringSet("activity_list",activity_list);
        spe.commit();
        edt.setText(""+block_time);
		edc.setText(""+times);
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
            txv.setHint(getText(R.string.admin_hint));
            txv.setGravity(Gravity.RIGHT|Gravity.CENTER);
            txv.setText(i.split(":")[1]);
			txv.setHint(i);
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
                                    activity_list.remove(txv.getHint());
                                    refreshSetting();
                                }
                            });
                    }
                });
            hsv.addView(txv,LayoutParams.WRAP_CONTENT,Math.round(50*dip));
            ll.addView(hsv,LayoutParams.WRAP_CONTENT,Math.round(50*dip));
            hsv.setScrollContainer(false);
            hsv.setHorizontalScrollBarEnabled(false);
            txv.setHorizontalScrollBarEnabled(false);
            //txv.setHorizontallyScrolling(fal);
            txv.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            ll.addView(imb,Math.round(50*dip),Math.round(50*dip));
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
                
                Snackbar.make(v, getText(R.string.action_stop), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            }else{
                startService(intent);
                Snackbar.make(v, getText(R.string.action_start), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            }
			block_time=new Integer((edt).getText().toString());
			times=new Integer(edc.getText().toString());
        }catch(Exception err){}
		
        
        refreshSetting();
        //Snackbar.make(v, ".", Snackbar.LENGTH).setAction("Action", null).show();
        
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

   public void noText(){
	   Toast.makeText(ctx,R.string.no_text,Toast.LENGTH_LONG).show();
   }

    
}
