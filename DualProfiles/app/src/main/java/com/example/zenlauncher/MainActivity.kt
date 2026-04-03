package com.example.zenlauncher

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zenlauncher.adapter.AppListAdapter
import com.example.zenlauncher.data.AppRepository
import com.example.zenlauncher.data.QuoteRepository
import com.example.zenlauncher.util.AnimationHelper
import com.example.zenlauncher.util.PreferenceHelper
import java.io.File

class MainActivity : AppCompatActivity() {
    companion object { private const val AWID = 1024 }
    private lateinit var textTime: TextView
    private lateinit var textDate: TextView
    private lateinit var textQuote: TextView
    private lateinit var recyclerApps: RecyclerView
    private lateinit var focusOverlay: LinearLayout
    private lateinit var floatingAppList: LinearLayout
    private lateinit var dragHandle: LinearLayout
    private lateinit var clockArea: LinearLayout
    private lateinit var quoteArea: LinearLayout
    private lateinit var btnFocus: ImageButton
    private lateinit var btnSettings: ImageButton
    private lateinit var btnExitFocus: Button
    private lateinit var textFocusTimer: TextView
    private lateinit var imageWallpaper: ImageView
    private lateinit var wallpaperOverlay: View
    private lateinit var widgetArea: FrameLayout
    private lateinit var adapter: AppListAdapter
    private val handler = Handler(Looper.getMainLooper())
    private var clockR: Runnable? = null
    private var focusR: Runnable? = null
    private var focusStart = 0L
    private lateinit var awm: AppWidgetManager
    private lateinit var awh: AppWidgetHost
    private var dragStartX = 0f
    private var dragStartY = 0f
    private var viewStartX = 0f
    private var viewStartY = 0f
    private var isDragging = false

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(R.layout.activity_main)
        awm = AppWidgetManager.getInstance(this)
        awh = AppWidgetHost(this, AWID)
        awh.startListening()
        initViews(); setupRV(); setupAllDrag(); loadWP(); startClk(); showQ(); applyCS(); restoreAllPos(); checkFM()
    }
    override fun onResume() { super.onResume(); loadApps(); applyCS(); loadWP(); checkFM() }
    override fun onDestroy() { super.onDestroy(); clockR?.let{handler.removeCallbacks(it)}; focusR?.let{handler.removeCallbacks(it)}; awh.stopListening() }

    private fun initViews() {
        textTime = findViewById(R.id.textTime)
        textDate = findViewById(R.id.textDate)
        textQuote = findViewById(R.id.textQuote)
        recyclerApps = findViewById(R.id.recyclerApps)
        focusOverlay = findViewById(R.id.focusOverlay)
        floatingAppList = findViewById(R.id.floatingAppList)
        dragHandle = findViewById(R.id.dragHandle)
        clockArea = findViewById(R.id.clockArea)
        quoteArea = findViewById(R.id.quoteArea)
        btnFocus = findViewById(R.id.btnFocus)
        btnSettings = findViewById(R.id.btnSettings)
        btnExitFocus = findViewById(R.id.btnExitFocus)
        textFocusTimer = findViewById(R.id.textFocusTimer)
        imageWallpaper = findViewById(R.id.imageWallpaper)
        wallpaperOverlay = findViewById(R.id.wallpaperOverlay)
        widgetArea = findViewById(R.id.widgetArea)
        btnFocus.setOnClickListener{toggleFM()}
        btnSettings.setOnClickListener{startActivity(Intent(this, SettingsActivity::class.java))}
        btnExitFocus.setOnClickListener{exitFM()}
    }

    private fun setupRV() {
        adapter = AppListAdapter(emptyList()){pkg -> packageManager.getLaunchIntentForPackage(pkg.packageName)?.let{startActivity(it)}}
        recyclerApps.layoutManager = LinearLayoutManager(this)
        recyclerApps.adapter = adapter
    }
    private fun loadApps() { adapter.updateList(AppRepository.getSelectedApps(this)) }

    // ALL ELEMENTS DRAGGABLE
    private fun setupAllDrag() {
        makeDraggable(clockArea, clockArea, "clock")
        makeDraggable(quoteArea, quoteArea, "quote")
        makeDraggable(floatingAppList, dragHandle, "applist")
    }
    private fun makeDraggable(target: View, handle: View, key: String) {
        handle.setOnTouchListener(object: View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> { dragStartX = event.rawX; dragStartY = event.rawY; viewStartX = target.translationX; viewStartY = target.translationY; isDragging = false; return true }
                    MotionEvent.ACTION_MOVE -> {
                        val dx = event.rawX - dragStartX; val dy = event.rawY - dragStartY
                        if (Math.abs(dx) > 8 || Math.abs(dy) > 8) isDragging = true
                        target.translationX = viewStartX + dx; target.translationY = viewStartY + dy; return true
                    }
                    MotionEvent.ACTION_UP -> {
                        if (isDragging) {
                            when (key) {
                                "clock" -> PreferenceHelper.setClockPos(this@MainActivity, target.translationX, target.translationY)
                                "quote" -> PreferenceHelper.setQuotePos(this@MainActivity, target.translationX, target.translationY)
                                "applist" -> PreferenceHelper.setAppListPos(this@MainActivity, target.translationX, target.translationY)
                            }
                        }; return isDragging
                    }
                }; return false
            }
        })
    }

    private fun restoreAllPos() {
        restorePos(clockArea, PreferenceHelper.getClockX(this), PreferenceHelper.getClockY(this))
        restorePos(quoteArea, PreferenceHelper.getQuoteX(this), PreferenceHelper.getQuoteY(this))
        restorePos(floatingAppList, PreferenceHelper.getAppListX(this), PreferenceHelper.getAppListY(this))
    }
    private fun restorePos(view: View, x: Float, y: Float) { if (x != -1f) view.translationX = x; if (y != -1f) view.translationY = y }

    private fun loadWP() {
        val name = PreferenceHelper.getWallpaperName(this)
        if (name.isNotEmpty()) {
            val file = File(filesDir, name)
            if (file.exists()) {
                try {
                    val bmp = BitmapFactory.decodeFile(file.absolutePath)
                    imageWallpaper.setImageBitmap(bmp)
                    imageWallpaper.visibility = View.VISIBLE
                    wallpaperOverlay.visibility = View.VISIBLE
                    applyBrightness()
                } catch (e: Exception) {
                    imageWallpaper.visibility = View.GONE
                    wallpaperOverlay.visibility = View.GONE
                }
            } else {
                imageWallpaper.visibility = View.GONE
                wallpaperOverlay.visibility = View.GONE
            }
        } else {
            imageWallpaper.visibility = View.GONE
            wallpaperOverlay.visibility = View.GONE
        }
    }

    private fun applyBrightness() {
        val brightness = PreferenceHelper.getWpBrightness(this)
        val alpha = (255 * (100 - brightness) / 100).coerceIn(0, 255)
        wallpaperOverlay.background.alpha = alpha
    }

    private fun applyCS() {
        val s = PreferenceHelper.getClockStyle(this); val c = PreferenceHelper.getClockColor(this)
        try { textTime.setTextColor(Color.parseColor(c)) } catch (e: Exception) {}
        when (s) {
            "thin" -> { textTime.typeface = Typeface.create("sans-serif-thin", Typeface.NORMAL); textTime.textSize = 72f }
            "light" -> { textTime.typeface = Typeface.create("sans-serif-light", Typeface.NORMAL); textTime.textSize = 68f }
            "bold" -> { textTime.typeface = Typeface.create("sans-serif", Typeface.BOLD); textTime.textSize = 72f }
            "mono" -> { textTime.typeface = Typeface.MONOSPACE; textTime.textSize = 64f }
            "digital" -> { textTime.typeface = Typeface.create("monospace", Typeface.BOLD); textTime.textSize = 80f }
        }
    }

    private fun startClk() { clockR = object: Runnable { override fun run() { updateClk(); handler.postDelayed(this, 1000) } }; handler.post(clockR!!) }
    private fun updateClk() {
        val n = java.util.Calendar.getInstance()
        textTime.text = String.format("%02d:%02d", n.get(11), n.get(12))
        val d = arrayOf("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday")
        val m = arrayOf("January","February","March","April","May","June","July","August","September","October","November","December")
        textDate.text = d[n.get(7)-1] + ", " + m[n.get(2)] + " " + n.get(5)
    }

    private fun showQ() { textQuote.text = QuoteRepository.getDailyQuote().text }

    private fun toggleFM() { if (PreferenceHelper.isFocusMode(this)) exitFM() else enterFM() }
    private fun enterFM() { PreferenceHelper.setFocusMode(this, true); focusStart = System.currentTimeMillis(); startFT(); checkFM() }
    private fun exitFM() { PreferenceHelper.setFocusMode(this, false); stopFT(); checkFM() }
    private fun startFT() { focusR = object: Runnable { override fun run() { updateFT(); handler.postDelayed(this, 1000) } }; handler.post(focusR!!) }
    private fun stopFT() { focusR?.let { handler.removeCallbacks(it) }; focusR = null }
    private fun updateFT() { val s = (System.currentTimeMillis() - focusStart) / 1000; textFocusTimer.text = String.format("%02d:%02d:%02d", s/3600, (s%3600)/60, s%60) }
    private fun checkFM() {
        val f = PreferenceHelper.isFocusMode(this)
        if (f) { AnimationHelper.fadeOut(floatingAppList, 300); AnimationHelper.fadeIn(focusOverlay, 300); btnFocus.alpha = 1f; if (focusR == null) startFT() }
        else { AnimationHelper.fadeOut(focusOverlay, 300); AnimationHelper.fadeIn(floatingAppList, 300); btnFocus.alpha = 0.5f }
    }
    override fun onBackPressed() {}
}
