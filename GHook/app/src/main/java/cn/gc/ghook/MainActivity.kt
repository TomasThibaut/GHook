package cn.gc.ghook

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cn.gc.ghook.test.Test
import dalvik.system.DexClassLoader
import dalvik.system.PathClassLoader
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val context = App.mApp.createPackageContext("cn.gc.ghook",
            Context.CONTEXT_INCLUDE_CODE or Context.CONTEXT_IGNORE_SECURITY)
        val apkPath = context.packageCodePath
        val file = File(apkPath)
        val loader = PathClassLoader(file.absolutePath, ClassLoader.getSystemClassLoader())
        Log.i("ExampleUnitTest", "2: $apkPath")
        Test().doTest(loader)
        Log.i("ExampleUnitTest", "3")
    }
}
