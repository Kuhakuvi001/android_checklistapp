package com.example.checklistapp

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.checklistapp.Adapter.ChecklistItemAdapter
import com.example.checklistapp.Model.ChecklistItemModel
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_list_checklist.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListChecklist : AppCompatActivity() {

    lateinit var adapter:ChecklistItemAdapter
    var listitem:MutableList<ChecklistItemModel> = ArrayList()

    var TOKEN = ""
    var ID = ""

    lateinit var datauser: SharedPreferences
    lateinit var context:Context

    val activity:Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_checklist)
        var nama = intent.getStringExtra("name")
        ID = intent.getStringExtra("id").toString()
        var list = JSONArray(intent.getStringExtra("list"))

        context = this
        datauser = getSharedPreferences("datauser", Context.MODE_PRIVATE)
        TOKEN = datauser.getString("token", "").toString()



        for(i in 0 until list.length()){
            var datajson = list.getJSONObject(i)
            listitem.add(
                ChecklistItemModel(
                    datajson.getInt("id"),
                    datajson.getString("name"),
                    datajson.getBoolean("itemCompletionStatus")
                )
            )
        }

        adapter = ChecklistItemAdapter(this, listitem)

        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.setHasFixedSize(true)
        recyclerview.adapter = adapter

        textnama.setText(nama)

        buttonsimpan.setOnClickListener{
            validation()
        }

        val broadcast_reciever: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(arg0: Context, intent: Intent) {
                val action = intent.action
                if (action == "finish") {
                    finish()
                }
            }
        }
        registerReceiver(broadcast_reciever, IntentFilter("finish"))
    }

    fun validation(){
        var name = inputname.text.toString().trim()
        if(name.isEmpty()){
            Toast.makeText(context, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }else{
            callapi_save(name)
        }
    }

    fun callapi_update(id: String, position: Int){
        VariableNet()
            .getApiService()
            .updateitem(id, "Bearer " + TOKEN)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    try {
                        var dataobject = JSONObject(response.body().toString())

                        if (dataobject.getString("errorMessage") == "null") {
                            Toast.makeText(context, "Berhasil update", Toast.LENGTH_SHORT).show()
                            listitem.get(position).setStatus()
                            adapter.notifyDataSetChanged()
                        } else {
                            var message = dataobject.getString("errorMessage")
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Log.d("TAGGAR", "ERROR " + e.message)
                        Toast.makeText(context, VariableNet().TEXT_ERROR, Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("TAGGAR", "ERROR " + t.message)
                    Toast.makeText(context, VariableNet().TEXT_ERROR, Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun callapi_hapus(id: String, position: Int){
        VariableNet()
            .getApiService()
            .deleteitem(id, "Bearer " + TOKEN)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    try {
                        var dataobject = JSONObject(response.body().toString())

                        if (dataobject.getString("errorMessage") == "null") {
                            Toast.makeText(context, "Berhasil menghapus", Toast.LENGTH_SHORT).show()
                            listitem.removeAt(position)
                            adapter.notifyDataSetChanged()
                        } else {
                            var message = dataobject.getString("errorMessage")
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Log.d("TAGGAR", "ERROR " + e.message)
                        Toast.makeText(context, VariableNet().TEXT_ERROR, Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("TAGGAR", "ERROR " + t.message)
                    Toast.makeText(context, VariableNet().TEXT_ERROR, Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun callapi_save(nama: String){
        var datajson = JsonObject()
        datajson.addProperty("checklistId", ID)
        datajson.addProperty("itemName", nama)

        VariableNet()
            .getApiService()
            .additem("Bearer " + TOKEN, datajson)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    try {
                        var dataobject = JSONObject(response.body().toString())

                        if (dataobject.getString("errorMessage") == "null") {
                            var datajson = dataobject.getJSONObject("data")
                            Toast.makeText(context, "Berhasil menyimpan", Toast.LENGTH_LONG).show()
                            listitem.add(
                                ChecklistItemModel(
                                    datajson.getInt("id"),
                                    datajson.getString("name"),
                                    datajson.getBoolean("itemCompletionStatus")
                                )
                            )

                            adapter.notifyDataSetChanged()
                        } else {
                            var message = dataobject.getString("errorMessage")
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        Log.d("TAGGAR", "ERROR " + e.message)
                        Toast.makeText(context, VariableNet().TEXT_ERROR, Toast.LENGTH_LONG).show()
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("TAGGAR", "ERROR " + t.message)
                    Toast.makeText(context, VariableNet().TEXT_ERROR, Toast.LENGTH_LONG).show()
                }
            })
    }
}