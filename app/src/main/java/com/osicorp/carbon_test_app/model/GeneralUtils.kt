package com.osicorp.carbon_test_app.model

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager.BadTokenException
import android.widget.Toast
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.osicorp.carbon_test_app.R
import com.osicorp.carbon_test_app.view.activities.ExitActivity

class GeneralUtils {


    companion object{
        private const val APP_PREFS_NAME = "com.osicorp.carbon_test_app.app_pref"
        private var appPref: SharedPreferences? = null
        private var uiHandler: Handler? = null

        @JvmStatic
        fun message(context: Context, message: String?) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        @JvmStatic
        fun getAppPref(context: Context): SharedPreferences? {
            if (appPref == null) appPref = context.getSharedPreferences(
                APP_PREFS_NAME,
                Context.MODE_PRIVATE
            )
            return appPref
        }

        @JvmStatic
        fun getHandler(): Handler? {
            if (uiHandler== null){
                uiHandler = Handler(Looper.getMainLooper())
            }
            return uiHandler
        }

        @JvmStatic
        fun exitApp(activity: Activity) {
            activity.finishAndRemoveTask()
            val intent = Intent(activity, ExitActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra("EXIT", true)
            activity.startActivity(intent)
        }

        @JvmStatic
        fun transitionActivity(oldActivity: Activity, newActivity: Class<*>?) {
            oldActivity.finishAndRemoveTask()
            oldActivity.startActivity(Intent(oldActivity, newActivity))
            Animatoo.animateFade(oldActivity)
        }

        @JvmStatic
        fun transitionActivity(oldActivity: Activity, intent: Intent?) {
            oldActivity.finishAndRemoveTask()
            oldActivity.startActivity(intent)
            Animatoo.animateFade(oldActivity)
        }

        @JvmStatic
        fun showAlertMessage(activity: Activity, title: String?, message: String?) {
            val alertDialog: AlertDialog.Builder
            alertDialog = AlertDialog.Builder(
                activity,
                android.R.style.Theme_DeviceDefault_Dialog_Alert
            )

            alertDialog.setCancelable(true)
                .setIcon(R.mipmap.ic_launcher).setMessage(message)
                .setTitle(title).setNeutralButton("Ok",
                    { dialog, i -> dialog.dismiss() })
            getHandler()?.post {
                try {
                    alertDialog.show()
                } catch (e: BadTokenException) {
                    e.printStackTrace()
                    Log.w(activity.javaClass.simpleName, e.fillInStackTrace())
                }
            }
        }

        fun resizeBitmap(source: Bitmap, maxLength: Int): Bitmap? {
            return try {
                if (source.height >= source.width) {
                    if (source.height <= maxLength) { // if image already smaller than the required height
                        return source
                    }
                    val aspectRatio = source.width.toDouble() / source.height.toDouble()
                    val targetWidth = (maxLength * aspectRatio).toInt()
                    Bitmap.createScaledBitmap(source, targetWidth, maxLength, false)
                } else {
                    if (source.width <= maxLength) { // if image already smaller than the required height
                        return source
                    }
                    val aspectRatio = source.height.toDouble() / source.width.toDouble()
                    val targetHeight = (maxLength * aspectRatio).toInt()
                    Bitmap.createScaledBitmap(source, maxLength, targetHeight, false)
                }
            } catch (e: Exception) {
                source
            }
        }
    }

}