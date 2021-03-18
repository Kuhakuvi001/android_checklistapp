package com.example.checklistapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.checklistapp.HomeActivity
import com.example.checklistapp.ListChecklist
import com.example.checklistapp.Model.ChecklistModel
import com.example.checklistapp.R
import kotlinx.android.synthetic.main.layoutchecklist.view.*
import org.json.JSONArray

class Checklist(
    val activity: HomeActivity,
    val listitem: List<ChecklistModel>): RecyclerView.Adapter<Checklist.ViewHolder>() {

    lateinit var context: Context
    class ViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){
        fun bindData(listitem: ChecklistModel){
            itemView.textnama.setText(listitem.nama)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.layoutchecklist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(listitem.get(position))
        holder.itemView.buttonhapus.setOnClickListener{
            activity.callapi_hapus(listitem.get(position).id.toString())
        }

        holder.itemView.buttonlayout.setOnClickListener{
            moveactivity(listitem.get(position).id.toString(), listitem.get(position).nama, listitem.get(position).item)
        }
    }

    override fun getItemCount(): Int {
        return listitem.size
    }

    fun moveactivity(id:String, name:String, listitem:JSONArray){
        var intent = Intent(context, ListChecklist::class.java)
        intent.putExtra("name", name)
        intent.putExtra("list", listitem.toString())
        intent.putExtra("id", id)

        context.startActivity(intent)
    }

}