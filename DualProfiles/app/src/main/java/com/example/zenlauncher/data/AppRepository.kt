package com.example.zenlauncher.data

import android.content.Context
import android.content.pm.PackageManager
import com.example.zenlauncher.model.AppItem
import com.example.zenlauncher.util.PreferenceHelper

object AppRepository {

    fun getAllInstalledApps(context: Context): List<AppItem> {
        val pm = context.packageManager
        val apps = pm.getInstalledApplications(0)
        return apps
            .filter { pm.getLaunchIntentForPackage(it.packageName) != null }
            .filter { it.packageName != context.packageName }
            .map { AppItem(it.packageName, it.loadLabel(pm).toString()) }
            .sortedBy { it.label.lowercase() }
    }

    fun getSelectedApps(context: Context): List<AppItem> {
        val selected = PreferenceHelper.getSelectedApps(context)
        val allApps = getAllInstalledApps(context)
        return allApps.filter { it.packageName in selected }
            .sortedBy { it.label.lowercase() }
    }
}
