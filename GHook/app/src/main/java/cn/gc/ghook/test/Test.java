package cn.gc.ghook.test;

import dalvik.system.PathClassLoader;
import de.robv.android.xposed.XposedBridge;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by 宫成 on 2019-12-05 16:35.
 */
public class Test {
    public void doTest(ClassLoader pathClassLoader) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Method forName = Class.class.getDeclaredMethod("forName", String.class, boolean.class, ClassLoader.class);
        forName.invoke(null, "cn.gc.ghook.GHook", true, pathClassLoader);

        Method forname2 = Class.class.getDeclaredMethod("forName", String.class);
//        forname2.invoke(null,"cn.gc.ghook.")
    }
}
