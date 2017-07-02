package net.npofsi.tool.awakeactivity;
import android.app.*;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        CrashHandler.get().Catch(this);
        super.onCreate();
    }

}
