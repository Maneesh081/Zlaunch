package com.example.zenlauncher.adapter

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.example.zenlauncher.R
import com.example.zenlauncher.model.AppItem
import java.util.concurrent.ConcurrentHashMap

class AppListAdapter(
    private val onClick: (AppItem) -> Unit
) : ListAdapter<AppItem, AppListAdapter.ViewHolder>(AppDiffCallback()) {

    private val iconCache = ConcurrentHashMap<String, Drawable?>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.imageAppIcon)
        val name: TextView = view.findViewById(R.id.textAppName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app_grid, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val app = getItem(position)
        holder.name.text = app.label
        
        val cachedIcon = iconCache[app.packageName]
        if (cachedIcon != null) {
            holder.icon.setImageDrawable(cachedIcon)
        } else {
            holder.icon.setImageResource(android.R.drawable.sym_def_app_icon)
            holder.itemView.tag = app.packageName
            loadIconAsync(holder, app.packageName)
        }
        
        holder.itemView.setOnClickListener { onClick(app) }
    }

    private fun loadIconAsync(holder: ViewHolder, packageName: String) {
        holder.itemView.post {
            if (holder.itemView.tag != packageName) return@post
            try {
                val pm = holder.itemView.context.packageManager
                val icon: Drawable? = pm.getApplicationIcon(packageName)
                iconCache[packageName] = icon
                if (holder.itemView.tag == packageName) {
                    holder.icon.setImageDrawable(icon)
                }
            } catch (e: Exception) {
                iconCache[packageName] = null
            }
        }
    }

    fun updateApps(newApps: List<AppItem>) {
        submitList(newApps)
    }

    class AppDiffCallback : DiffUtil.ItemCallback<AppItem>() {
        override fun areItemsTheSame(oldItem: AppItem, newItem: AppItem): Boolean {
            return oldItem.packageName == newItem.packageName
        }

        override fun areContentsTheSame(oldItem: AppItem, newItem: AppItem): Boolean {
            return oldItem == newItem
        }
    }
}