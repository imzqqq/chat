package com.imzqqq.app

import android.content.Context
import android.content.Intent
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

fun openOssLicensesMenuActivity(context: Context) = context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
