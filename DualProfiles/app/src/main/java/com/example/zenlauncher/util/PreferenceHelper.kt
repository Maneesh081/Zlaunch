package com.example.zenlauncher.util

import android.content.Context

object PreferenceHelper {
    private const val PREFS = "zen_launcher_prefs"
    private fun prefs(ctx: Context) = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun getSelectedApps(c: Context): Set<String> = prefs(c).getStringSet("selected_apps", emptySet()) ?: emptySet()
    fun setSelectedApps(c: Context, a: Set<String>) { prefs(c).edit().putStringSet("selected_apps", a).apply() }
    fun isFocusMode(c: Context): Boolean = prefs(c).getBoolean("focus_mode", false)
    fun setFocusMode(c: Context, e: Boolean) { prefs(c).edit().putBoolean("focus_mode", e).apply() }
    fun getClockStyle(c: Context): String = prefs(c).getString("clock_style", "thin") ?: "thin"
    fun setClockStyle(c: Context, s: String) { prefs(c).edit().putString("clock_style", s).apply() }
    fun getClockColor(c: Context): String = prefs(c).getString("clock_color", "#F5F5F5") ?: "#F5F5F5"
    fun setClockColor(c: Context, cl: String) { prefs(c).edit().putString("clock_color", cl).apply() }
    fun getSelectedWidgetId(c: Context): Int = prefs(c).getInt("widget_id", -1)
    fun setSelectedWidgetId(c: Context, id: Int) { prefs(c).edit().putInt("widget_id", id).apply() }
    fun clearWidget(c: Context) { prefs(c).edit().remove("widget_id").apply() }

    // Wallpaper stored as file name in internal storage
    fun getWallpaperName(c: Context): String = prefs(c).getString("wallpaper_name", "") ?: ""
    fun setWallpaperName(c: Context, n: String) { prefs(c).edit().putString("wallpaper_name", n).apply() }

    // Wallpaper brightness overlay (0=dark, 100=bright/full)
    fun getWpBrightness(c: Context): Int = prefs(c).getInt("wp_brightness", 60)
    fun setWpBrightness(c: Context, b: Int) { prefs(c).edit().putInt("wp_brightness", b).apply() }

    // Draggable positions
    fun getWidgetX(c: Context): Float = prefs(c).getFloat("widget_x", -1f)
    fun getWidgetY(c: Context): Float = prefs(c).getFloat("widget_y", -1f)
    fun setWidgetPos(c: Context, x: Float, y: Float) { prefs(c).edit().putFloat("widget_x", x).putFloat("widget_y", y).apply() }
    fun getAppListX(c: Context): Float = prefs(c).getFloat("applist_x", -1f)
    fun getAppListY(c: Context): Float = prefs(c).getFloat("applist_y", -1f)
    fun setAppListPos(c: Context, x: Float, y: Float) { prefs(c).edit().putFloat("applist_x", x).putFloat("applist_y", y).apply() }
    fun getClockX(c: Context): Float = prefs(c).getFloat("clock_x", -1f)
    fun getClockY(c: Context): Float = prefs(c).getFloat("clock_y", -1f)
    fun setClockPos(c: Context, x: Float, y: Float) { prefs(c).edit().putFloat("clock_x", x).putFloat("clock_y", y).apply() }
    fun getQuoteX(c: Context): Float = prefs(c).getFloat("quote_x", -1f)
    fun getQuoteY(c: Context): Float = prefs(c).getFloat("quote_y", -1f)
    fun setQuotePos(c: Context, x: Float, y: Float) { prefs(c).edit().putFloat("quote_x", x).putFloat("quote_y", y).apply() }

    // Password for switching to default launcher
    fun getSwitchPassword(c: Context): String = prefs(c).getString("switch_password", "") ?: ""
    fun setSwitchPassword(c: Context, p: String) { prefs(c).edit().putString("switch_password", p).apply() }
    fun isSwitchPasswordSet(c: Context): Boolean = getSwitchPassword(c).isNotEmpty()
}
