package com.example.zenlauncher

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zenlauncher.data.AppRepository
import com.example.zenlauncher.model.AppItem
import com.example.zenlauncher.util.PreferenceHelper

class AppSelectionActivity : AppCompatActivity() {

    private lateinit var editSearch: EditText
    private lateinit var containerApps: LinearLayout
    private lateinit var btnSave: Button
    private lateinit var btnSelectAll: Button
    private lateinit var btnDeselectAll: Button
    private lateinit var textCount: TextView

    private var allApps = listOf<AppItem>()
    private val checkBoxes = mutableMapOf<String, CheckBox>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_selection)

        editSearch = findViewById(R.id.editSearch)
        containerApps = findViewById(R.id.containerApps)
        btnSave = findViewById(R.id.btnSave)
        btnSelectAll = findViewById(R.id.btnSelectAll)
        btnDeselectAll = findViewById(R.id.btnDeselectAll)
        textCount = findViewById(R.id.textCount)

        loadApps()

        btnSave.setOnClickListener { saveSelection() }
        btnSelectAll.setOnClickListener { setAll(true) }
        btnDeselectAll.setOnClickListener { setAll(false) }

        editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterApps(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun loadApps() {
        allApps = AppRepository.getAllInstalledApps(this)
        val selected = PreferenceHelper.getSelectedApps(this)
        renderApps(allApps, selected)
    }

    private fun renderApps(apps: List<AppItem>, selected: Set<String>) {
        containerApps.removeAllViews()
        checkBoxes.clear()

        for (app in apps) {
            val checkBox = CheckBox(this).apply {
                text = app.label
                tag = app.packageName
                isChecked = app.packageName in selected
                setPadding(16, 20, 16, 20)
                textSize = 16f
                setTextColor(getColor(R.color.text_primary))
            }
            checkBoxes[app.packageName] = checkBox
            containerApps.addView(checkBox)
        }
        updateCount()
    }

    private fun filterApps(query: String) {
        val selected = PreferenceHelper.getSelectedApps(this)
        val filtered = if (query.isEmpty()) allApps
        else allApps.filter { it.label.contains(query, ignoreCase = true) }

        containerApps.removeAllViews()
        for (app in filtered) {
            val checkBox = CheckBox(this).apply {
                text = app.label
                tag = app.packageName
                isChecked = checkBoxes[app.packageName]?.isChecked ?: (app.packageName in selected)
                setPadding(16, 20, 16, 20)
                textSize = 16f
                setTextColor(getColor(R.color.text_primary))
            }
            checkBoxes[app.packageName] = checkBox
            containerApps.addView(checkBox)
        }
    }

    private fun setAll(checked: Boolean) {
        for ((_, cb) in checkBoxes) cb.isChecked = checked
        updateCount()
    }

    private fun saveSelection() {
        val selected = mutableSetOf<String>()
        for ((pkg, cb) in checkBoxes) {
            if (cb.isChecked) selected.add(pkg)
        }
        PreferenceHelper.setSelectedApps(this, selected)
        Toast.makeText(this, "${selected.size} apps saved", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun updateCount() {
        val count = checkBoxes.count { it.value.isChecked }
        textCount.text = "$count selected"
    }
}
