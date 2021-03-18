package com.example.checklistapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_registrasi.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class RegistrasiActivity : AppCompatActivity() {

    lateinit var context:Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrasi)

        context = this

        buttonregistrasi.setOnClickListener { validation() }
    }

    fun validation(){
        var email = inputemail.text.toString().trim()
        var username = inputusername.text.toString().trim()
        var password = inputpassword.text.toString().trim()

        if(email.isEmpty()){
            Toast.makeText(context, "Email tidak boleh kosong", Toast.LENGTH_LONG).show()
        }else if (username.isEmpty()){
            Toast.makeText(context, "Username tidak boleh kosong", Toast.LENGTH_LONG).show()
        }else if(password.isEmpty()){
            Toast.makeText(context, "Password tidak boleh kosong", Toast.LENGTH_LONG).show()
        }else{
            callapi(email, username, password)
        }

    }

    fun callapi(email:String, username: String, password: String){
        var datajson = JsonObject()
        datajson.addProperty("email", email)
        datajson.addProperty("username", username)
        datajson.addProperty("password", password)

        VariableNet()
            .getApiService()
            .register(datajson)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    try {
                        var dataobject = JSONObject(response.body().toString())

                        if (dataobject.getString("statusCode") == "2000") {
                            Toast.makeText(context, "Register Berhasil", Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            var message = dataobject.getString("errorMessage")
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, VariableNet().TEXT_ERROR, Toast.LENGTH_LONG).show()
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(context, VariableNet().TEXT_ERROR, Toast.LENGTH_LONG).show()
                }
            })
    }
}