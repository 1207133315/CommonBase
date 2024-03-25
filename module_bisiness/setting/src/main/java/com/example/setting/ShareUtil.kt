package com.example.setting

import android.content.Context
import android.content.Intent
import android.net.Uri

object ShareUtil {
    fun share(context: Context) {
        val msg = context?.resources?.getString(com.liningkang.ui.R.string.share_msg)
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            "$msg \n https://play.google.com/store/apps/details?id=${context?.packageName}"
        )
        context?.startActivity(
            Intent.createChooser(
                shareIntent,
                context?.resources?.getString(com.liningkang.ui.R.string.share)
            )
        )
    }
    fun goGooglePlayScore(context: Context, packageName: String = context.packageName) {
        val intent = Intent(Intent.ACTION_VIEW)
        try {
            intent.data =
                Uri.parse("https://play.google.com/store/apps/details?id=${packageName}")
        } catch (e: Exception) {
            e.printStackTrace()
            intent.data = Uri.parse("market://details?id=${packageName}")
        }
        context?.startActivity(intent)
    }


}