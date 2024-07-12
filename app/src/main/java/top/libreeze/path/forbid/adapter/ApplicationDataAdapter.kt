package top.libreeze.path.forbid.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import top.libreeze.path.forbid.R
import top.libreeze.path.forbid.bean.ApplicationData

class ApplicationDataAdapter(private val context: Context, private var data: MutableList<ApplicationData>): RecyclerView.Adapter<ApplicationDataAdapter.ClassView>() {

    class ClassView(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val baseView:LinearLayout = itemView.findViewById(R.id.itemBaseView)
        val icon:ImageView = itemView.findViewById(R.id.itemIconImg)
        val title:TextView = itemView.findViewById(R.id.itemAppNameTv)
        val packageName:TextView = itemView.findViewById(R.id.itemAppPackageNameTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassView {
        val inflate =
            LayoutInflater.from(context).inflate(R.layout.item_application_data, parent, false)
        return ClassView(inflate)
    }

    override fun onBindViewHolder(holder: ClassView, position: Int) {
        val applicationData = data[position]
        holder.icon.setImageDrawable(applicationData.appIcon)
        holder.title.text = applicationData.appName
        holder.packageName.text = applicationData.applicationInfo.packageName
        holder.baseView.setOnClickListener {
            this.itemClickListener.onItemClicked(position, applicationData)
        }
        if (applicationData.isSystemApp) {
            holder.title.setTextColor(Color.parseColor("#F44336"))
        } else {
            holder.title.setTextColor(Color.BLACK)
        }
    }

    private lateinit var itemClickListener:OnItemClickListener<ApplicationData>

    fun setOnItemClickListener(listener: OnItemClickListener<ApplicationData>) {
        this.itemClickListener = listener
    }

    fun setDataSource(list: MutableList<ApplicationData>) {
        this.data = list
    }

    override fun getItemCount(): Int {
        return data.size
    }
}