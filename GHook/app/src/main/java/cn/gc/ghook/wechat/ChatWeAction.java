package cn.gc.ghook.wechat;

import android.util.Log;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by 宫成 on 2019-12-04 16:25.
 */
public class ChatWeAction {
    private static final String weChatLogClassName = "com.tencent.mm.xlog.app.XLogSetup";
    private static final String weChatLogMethodName = "keep_setupXLog";
    private static final String TAG = ChatWeAction.class.getSimpleName();

    private XC_LoadPackage.LoadPackageParam loadPackageParam;

    public ChatWeAction(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        this.loadPackageParam = loadPackageParam;
        weChatLogOpen();

    }

    private void weChatLogOpen() {
        XposedHelpers.findAndHookMethod(
                weChatLogClassName,
                loadPackageParam.classLoader,
                weChatLogMethodName,
                boolean.class, String.class, String.class, Integer.class, Boolean.class, Boolean.class, String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        param.args[5] = true;
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Log.i(TAG, "is weChatLogOpen: " + param.args[5]);
                    }
                });
    }
}
