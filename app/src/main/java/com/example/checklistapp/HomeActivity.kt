package com.example.checklistapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.checklistapp.Adapter.Checklist
import com.example.checklistapp.Model.ChecklistModel
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_home_acticity.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class HomeActivity : AppCompatActivity() {
    var listitem:MutableList<ChecklistModel> = ArrayList()
    lateinit var adapter:Checklist
    var TOKEN = ""

    lateinit var datauser: SharedPreferences

    lateinit var context:Context
    override fun onResume() {
        callapi()
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_acticity)

        context = this

        datauser = getSharedPreferences("datauser", Context.MODE_PRIVATE)
        TOKEN = datauser.getString("token", "").toString()

        adapter = Checklist(this, listitem)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.setHasFixedSize(true)
        recyclerview.adapter = adapter

        buttonsimpan.setOnClickListener{
            validation()
        }
    }

    fun validation() {
        var name = inputname.text.toString().trim()
        if(name.isEmpty()){
            Toast.makeText(context, "Nama tidak boleh kosong", Toast.LENGTH_LONG).show()
        }else{
            callapi_save(name)
        }
    }

    fun callapi(){
        VariableNet()
            .getApiService()
            .getchecklist("Bearer "+TOKEN)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    try {
                        var dataobject = JSONObject(response.body().toString())

                        if (dataobject.getString("errorMessage") == "null") {
                            var dataarray = dataobject.getJSONArray("data")
                            listitem.clear()
                            for(i in 0 until dataarray.length()){
                                var datajson = dataarray.getJSONObject(i)

                                var item = datajson.getString("items")
                                if (item == "null"){
                                    listitem.add(ChecklistModel(
                                        datajson.getInt("id"),
                                        datajson.getString("name"),
                                        JSONArray()))
                                }else{
                                    listitem.add(ChecklistModel(
                                        datajson.getInt("id"),
                                        datajson.getString("name"),
                                        datajson.getJSONArray("items")))
                                }

                            }

                            adapter.notifyDataSetChanged()

                        } else {
                            var message = dataobject.getString("errorMessage")
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        Log.d("TAGGAR","ERROR "+e.message)
                        Toast.makeText(context, VariableNet().TEXT_ERROR, Toast.LENGTH_LONG).show()
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("TAGGAR","ERROR "+t.message)
                    Toast.makeText(context, VariableNet().TEXT_ERROR, Toast.LENGTH_LONG).show()
                }
            })
    }

    fun callapi_hapus(id:String){
        VariableNet()
            .getApiService()
            .deletechecklist(id, "Bearer "+TOKEN)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    try {
                        var dataobject = JSONObject(response.body().toString())

                        if (dataobject.getString("errorMessage") == "null") {
                            Toast.makeText(context, "Berhasil menghapus", Toast.LENGTH_LONG).show()
                            callapi()
                        } else {
                            var message = dataobject.getString("errorMessage")
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        Log.d("TAGGAR","ERROR "+e.message)
                        Toast.makeText(context, VariableNet().TEXT_ERROR, Toast.LENGTH_LONG).show()
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("TAGGAR","ERROR "+t.message)
                    Toast.makeText(context, VariableNet().TEXT_ERROR, Toast.LENGTH_LONG).show()
                }
            })
    }

    fun callapi_save(nama:String){
        var datajson = JsonObject()
        datajson.addProperty("name", nama)

        VariableNet()
            .getApiService()
            .savechecklist("Bearer "+TOKEN, datajson)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    try {
                        var dataobject = JSONObject(response.body().toString())

                        if (dataobject.getString("errorMessage") == "null") {
                            Toast.makeText(context, "Berhasil menyimpan", Toast.LENGTH_LONG).show()
                            callapi()
                        } else {
                            var message = dataobject.getString("errorMessage")
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        Log.d("TAGGAR","ERROR "+e.message)
                        Toast.makeText(context, VariableNet().TEXT_ERROR, Toast.LENGTH_LONG).show()
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d("TAGGAR","ERROR "+t.message)
                    Toast.makeText(context, VariableNet().TEXT_ERROR, Toast.LENGTH_LONG).show()
                }
            })
    }
}