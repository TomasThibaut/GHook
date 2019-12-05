package cn.gc.ghook

import android.content.Context
import android.util.Log
import dalvik.system.PathClassLoader
import org.junit.Test
import java.io.File

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val context = App.mApp.createPackageContext("cn.gc.ghook",
            Context.CONTEXT_INCLUDE_CODE or Context.CONTEXT_IGNORE_SECURITY)
        val apkPath = context.packageCodePath
        val file = File(apkPath)
        val loader = PathClassLoader(file.absolutePath, ClassLoader.getSystemClassLoader())
        Log.i("ExampleUnitTest", "2: $apkPath")

        val clazz = Class.forName("cn.gc.ghook.GHook",true,loader)
        Log.i("ExampleUnitTest", "3")
    }
}
