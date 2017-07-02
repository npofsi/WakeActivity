package net.npofsi.tool.awakeactivity;
import android.content.*;

public class Untils
{
    public static Intent AwakeActivity(Context context,String activity){
        Intent intent=new Intent();
        intent.setComponent(new ComponentName(context,activity));
        context.startActivity(intent);
        return intent;
    }
}
