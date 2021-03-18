package com.example.checklistapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.checklistapp.DetailChecklist
import com.example.checklistapp.HomeActivity
import com.example.checklistapp.ListChecklist
import com.example.checklistapp.Model.ChecklistItemModel
import com.example.checklistapp.Model.ChecklistModel
import com.example.checklistapp.R
import kotlinx.android.synthetic.main.layoutchecklititem.view.*
import org.json.JSONArray

class ChecklistItemAdapter(
    val activity: ListChecklist,
    val listitem: List<ChecklistItemModel>): RecyclerView.Adapter<ChecklistItemAdapter.ViewHolder>() {

    lateinit var context: Context
    class ViewHolder(itemview: View): RecyclerView.ViewHolder(itemview){
        fun bindData(listitem: ChecklistItemModel){
            itemView.textnama.setText(listitem.nama)
            if(listitem.status){
                itemView.textstatus.setText("Complete")
            }else{
                itemView.textstatus.setText("Not Complete")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.layoutchecklititem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(listitem.get(position))
        holder.itemView.buttonhapus.setOnClickListener{
            activity.callapi_hapus(listitem.get(position).id.toString(), position)
        }
        holder.itemView.buttonupdate.setOnClickListener{
            activity.callapi_update(listitem.get(position).id.toString(), position)
        }

        holder.itemView.buttonlayout.setOnClickListener{
            moveactivity(listitem.get(position).id.toString())
        }
    }

    override fun getItemCount(): Int {
        return listitem.size
    }

    fun moveactivity(id:String){
        var intent = Intent(context, DetailChecklist::class.java)
        intent.putExtra("id", id)
        context.startActivity(intent)
    }

}