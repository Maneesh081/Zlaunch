package com.example.zenlauncher

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.example.zenlauncher.util.PreferenceHelper
import java.io.File
import java.io.FileOutputStream

class SettingsActivity : AppCompatActivity() {

    companion object {
        private const val REQ_WP = 4001
        private const val REQ_AUTH = 4002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<Button>(R.id.btnSelectApps).setOnClickListener {
            startActivity(Intent(this, AppSelectionActivity::class.java))
        }

        findViewById<SwitchMaterial>(R.id.switchFocus).apply {
            isChecked = PreferenceHelper.isFocusMode(this@SettingsActivity)
            setOnCheckedChangeListener { _, c -> PreferenceHelper.setFocusMode(this@SettingsActivity, c) }
        }

        // Wallpaper - use ACTION_OPEN_DOCUMENT for persistable permission
        findViewById<Button>(R.id.btnWallpaper).setOnClickListener {
            val i = Intent(Intent.ACTION_OPEN_DOCUMENT)
            i.addCategory(Intent.CATEGORY_OPENABLE)
            i.type = "image/*"
            startActivityForResult(i, REQ_WP)
        }

        findViewById<Button>(R.id.btnClearWallpaper).setOnClickListener {
            val old = PreferenceHelper.getWallpaperName(this)
            if (old.isNotEmpty()) File(filesDir, old).delete()
            PreferenceHelper.setWallpaperName(this, "")
            Toast.makeText(this, "Wallpaper cleared", Toast.LENGTH_SHORT).show()
        }

        // Wallpaper brightness
        val seek = findViewById<SeekBar>(R.id.seekBrightness)
        val textB = findViewById<TextView>(R.id.textBrightness)
        seek.progress = PreferenceHelper.getWpBrightness(this)
        textB.text = "Brightness: " + PreferenceHelper.getWpBrightness(this) + "%"
        seek.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, p: Int, f: Boolean) {
                PreferenceHelper.setWpBrightness(this@SettingsActivity, p)
                textB.text = "Brightness: " + p + "%"
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        // Clock style
        val rg = findViewById<RadioGroup>(R.id.rgClockStyle)
        when (PreferenceHelper.getClockStyle(this)) {
            "thin" -> rg.check(R.id.rbThin)
            "light" -> rg.check(R.id.rbLight)
            "bold" -> rg.check(R.id.rbBold)
            "mono" -> rg.check(R.id.rbMono)
            "digital" -> rg.check(R.id.rbDigital)
        }
        rg.setOnCheckedChangeListener { _, id ->
            val s = when (id) {
                R.id.rbThin -> "thin"
                R.id.rbLight -> "light"
                R.id.rbBold -> "bold"
                R.id.rbMono -> "mono"
                R.id.rbDigital -> "digital"
                else -> "thin"
            }
            PreferenceHelper.setClockStyle(this, s)
        }

        // Clock colors
        val colors = mapOf(
            R.id.btnColorWhite to "#F5F5F5",
            R.id.btnColorGreen to "#00E676",
            R.id.btnColorRed to "#FF5252",
            R.id.btnColorBlue to "#448AFF",
            R.id.btnColorYellow to "#FFD740",
            R.id.btnColorPink to "#FF80AB"
        )
        for ((id, color) in colors) {
            findViewById<Button>(id).setOnClickListener {
                PreferenceHelper.setClockColor(this, color)
                Toast.makeText(this, "Color updated", Toast.LENGTH_SHORT).show()
            }
        }

        // Widget
        findViewById<Button>(R.id.btnWidget).setOnClickListener {
            startActivity(Intent(this, WidgetPickerActivity::class.java))
        }

        // Switch to Default Launcher
        findViewById<Button>(R.id.btnSwitchLauncher).setOnClickListener {
            val i = Intent(this, PasswordAuthActivity::class.java)
            startActivityForResult(i, REQ_AUTH)
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }
    }

    override fun onActivityResult(req: Int, res: Int, data: Intent?) {
        super.onActivityResult(req, res, data)
        if (req == REQ_WP && res == RESULT_OK) {
            val uri = data?.data ?: return
            try {
                val input = contentResolver.openInputStream(uri) ?: return
                val fileName = "wp_${System.currentTimeMillis()}.jpg"
                val file = File(filesDir, fileName)
                FileOutputStream(file).use { input.copyTo(it) }
                input.close()
                // Delete old wallpaper
                val old = PreferenceHelper.getWallpaperName(this)
                if (old.isNotEmpty()) { try { File(filesDir, old).delete() } catch (e: Exception) {} }
                PreferenceHelper.setWallpaperName(this, fileName)
                Toast.makeText(this, "Wallpaper set!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
        if (req == REQ_AUTH && res == PasswordAuthActivity.RESULT_AUTH_SUCCESS) {
            // User authenticated - finish settings activity so default launcher can take over
            finish()
        }
    }
}
