package com.example.zenlauncher

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zenlauncher.util.PreferenceHelper

class WidgetPickerActivity : AppCompatActivity() {

    companion object {
        private const val APPWIDGET_HOST_ID = 1024
        private const val REQUEST_CREATE_WIDGET = 3001
    }

    private lateinit var appWidgetManager: AppWidgetManager
    private lateinit var appWidgetHost: AppWidgetHost
    private var selectedWidgetId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appWidgetManager = AppWidgetManager.getInstance(this)
        appWidgetHost = AppWidgetHost(this, APPWIDGET_HOST_ID)
        appWidgetHost.startListening()

        val providers = appWidgetManager.installedProviders
        val names = providers.map { it.loadLabel(packageManager) + " (" + it.provider.packageName + ")" }.toMutableList()
        names.add(0, "No Widget")

        val listView = ListView(this)
        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, names)

        listView.setOnItemClickListener { _, _, position, _ ->
            if (position == 0) {
                PreferenceHelper.clearWidget(this)
                Toast.makeText(this, "Widget removed", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
                return@setOnItemClickListener
            }
            val info = providers[position - 1]
            val widgetId = appWidgetHost.allocateAppWidgetId()
            selectedWidgetId = widgetId

            val success = appWidgetManager.bindAppWidgetIdIfAllowed(widgetId, info.provider)
            if (success) {
                PreferenceHelper.setSelectedWidgetId(this, widgetId)
                Toast.makeText(this, "Widget added!", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            } else {
                try {
                    val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_PICK)
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                    startActivityForResult(intent, REQUEST_CREATE_WIDGET)
                } catch (e: Exception) {
                    PreferenceHelper.setSelectedWidgetId(this, widgetId)
                    Toast.makeText(this, "Widget selected", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }

        setContentView(listView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CREATE_WIDGET && resultCode == RESULT_OK) {
            PreferenceHelper.setSelectedWidgetId(this, selectedWidgetId)
            Toast.makeText(this, "Widget configured!", Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        appWidgetHost.stopListening()
    }
}
