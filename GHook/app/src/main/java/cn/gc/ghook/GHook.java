package cn.gc.ghook;

import cn.gc.ghook.wechat.ChatWe;
import cn.gc.ghook.wechat.ChatWeAction;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 代理hook类
 *
 * @author 宫成
 * @date 2019-12-04 16:04
 */
public class GHook implements IXposedHookLoadPackage, IXposedHookZygoteInit {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        String packageName = loadPackageParam.packageName;
        ClassLoader classLoader = loadPackageParam.classLoader;
        //hook微信
        hookWeChat(packageName, classLoader,loadPackageParam);
    }

    private void hookWeChat(String packageName, ClassLoader classLoader, XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (ChatWe.weChatPackageName.equals(packageName)) {
            XposedBridge.log("=========Loaded app: " + packageName);
            ChatWeAction action = new ChatWeAction(loadPackageParam);
        }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
//        this.sharedPreferences =new XSharedPreferences(modulePackageName, "default");
    }
}
