package net.npofsi.tool.awakeactivity;
import android.app.*;
import android.os.*;
import android.content.*;

public class StopServiceActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        Intent intent= new Intent(StopServiceActivity.this,WakeActivityService.class);
        
            //if(isServiceRunning(ctx,"net.npofsi.tool.awakeactivity.WakeActivityService")){      
                stopService(intent);
           //     }
           this.finish();
    }
    
}
