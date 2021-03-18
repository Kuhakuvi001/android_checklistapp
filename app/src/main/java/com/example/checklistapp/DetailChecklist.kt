package com.example.checklistapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_detail_checklist.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailChecklist : AppCompatActivity() {

    var TOKEN = ""
    var ID = ""

    lateinit var datauser: SharedPreferences
    lateinit var context: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_checklist)

        context = this
        datauser = getSharedPreferences("datauser", Context.MODE_PRIVATE)
        TOKEN = datauser.getString("token", "").toString()

        ID = intent.getStringExtra("id").toString()
        callapi(ID)

        buttonsimpan.setOnClickListener{
            validation()
        }
    }

    fun validation(){
        var textname = inputname.text.toString().trim()
        if(textname.isEmpty()){
            Toast.makeText(context, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }else{
            callapi_gantinama(ID, textname)
        }
    }

    fun callapi(id: String){
        VariableNet()
            .getApiService()
            .getitem(id, "Bearer " + TOKEN)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    try {
                        var dataobject = JSONObject(response.body().toString())

                        if (dataobject.getString("errorMessage") == "null") {
                            var datajson = dataobject.getJSONObject("data")
                            inputname.setText(datajson.getString("name"))
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

    fun callapi_gantinama(id: String, nama: String){
        var datajson = JsonObject()
        datajson.addProperty("checklistId", id)
        datajson.addProperty("itemName", nama)

        VariableNet()
            .getApiService()
            .gantinama(id, "Bearer " + TOKEN, datajson)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    try {
                        var dataobject = JSONObject(response.body().toString())

                        if (dataobject.getString("errorMessage") == "null") {
                            Toast.makeText(context, "Berhasil ganti nama", Toast.LENGTH_SHORT).show()
                            val intent = Intent("finish")
                            sendBroadcast(intent)
                            finish()
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