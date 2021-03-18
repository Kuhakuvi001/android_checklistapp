package com.example.checklistapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.buttonregistrasi
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    lateinit var context:Context
    lateinit var datauser: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        datauser = getSharedPreferences("datauser", Context.MODE_PRIVATE)
        context = this

        buttonlogin.setOnClickListener {
            validation()
        }

        buttonregistrasi.setOnClickListener {
            moveactivity()
        }
    }

    fun moveactivity(){
        startActivity(Intent(this, RegistrasiActivity::class.java))
    }

    fun validation(){
        var username = inputusername.text.toString().trim()
        var password = inputpassword.text.toString().trim()

        if (username.isEmpty()){
            Toast.makeText(context, "Username tidak boleh kosong", Toast.LENGTH_LONG).show()
        }else if(password.isEmpty()){
            Toast.makeText(context, "Password tidak boleh kosong", Toast.LENGTH_LONG).show()
        }else{
            callapi(username, password)
        }

    }

    fun moveactivity_home(){
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    fun callapi(username: String, password: String){
        var datajson = JsonObject()
        datajson.addProperty("username", username)
        datajson.addProperty("password", password)

        VariableNet()
            .getApiService()
            .login(datajson)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    try {
                        var dataobject = JSONObject(response.body().toString())

                        if (dataobject.getString("statusCode") == "2000" || dataobject.getString("statusCode") == "2110") {
                            with(datauser.edit()) {
                                putString("token", dataobject.getJSONObject("data").getString("token"))
                                commit()
                            }
                            Toast.makeText(context, "Login Berhasil", Toast.LENGTH_LONG).show()
                            moveactivity_home()
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