package top.libreeze.path.forbid.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import top.libreeze.path.forbid.R
import top.libreeze.path.forbid.bean.AppFile

class AppFileAdapter(private val context: Context, private val data: MutableList<AppFile>) :
    RecyclerView.Adapter<AppFileAdapter.ClassHolder>() {


    private lateinit var onItemClickListener: OnItemClickListener<AppFile>
    private lateinit var onLongItemClickListener: OnItemLongClickListener<AppFile>

    fun setOnItemClickListener(listener: OnItemClickListener<AppFile>) {
        this.onItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener<AppFile>) {
        this.onLongItemClickListener = listener
    }


    class ClassHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.itemIconImg)
        val filename: TextView = itemView.findViewById(R.id.itemAppFilenameTv)
        val filesize: TextView = itemView.findViewById(R.id.itemAppFilesizeTv)
        val baseView: LinearLayout = itemView.findViewById(R.id.itemBaseView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassHolder {
        val inflate = LayoutInflater.from(context).inflate(R.layout.item_app_file, parent, false)
        return ClassHolder(inflate)
    }

    @SuppressLint("SetTextI18n", "ResourceType")
    override fun onBindViewHolder(holder: ClassHolder, position: Int) {
        val appFile = data[position]
        holder.filename.text = appFile.filename
        holder.filesize.text = appFile.mebibyte
        holder.baseView.setOnClickListener {
            onItemClickListener.onItemClicked(position, appFile)
        }
        holder.baseView.setOnLongClickListener {
            onLongItemClickListener.longClick(holder.baseView, position, appFile)
            true
        }
        val icon: Drawable? = if (appFile.isDirectory) {
            context.getDrawable(R.drawable.ic_baseline_folder_24)
        } else {
            context.getDrawable(R.drawable.ic_baseline_insert_drive_file_24)
        }
        holder.icon.setImageDrawable(icon)

        if (appFile.permission == 500) {
            holder.filename.setTextColor(Color.RED)
        } else {
            holder.filename.setTextColor(
                context.getColor(R.color.black)
            )
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}