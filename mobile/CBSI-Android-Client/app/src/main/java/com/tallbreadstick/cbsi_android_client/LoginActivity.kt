package com.tallbreadstick.cbsi_android_client

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tallbreadstick.cbsi_android_client.api.RetrofitClient
import com.tallbreadstick.cbsi_android_client.api.models.LoginRequest
import com.tallbreadstick.cbsi_android_client.api.models.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        RetrofitClient.init(applicationContext)

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val signIn = findViewById<Button>(R.id.btn_sign_in)
        val toRegister = findViewById<Button>(R.id.btn_go_register)

        signIn.setOnClickListener {
            val req = LoginRequest(email.text.toString(), password.text.toString())
            RetrofitClient.api.login(req).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val body = response.body()
                    if (body == null) {
                        Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (body.mustChangePassword == true) {
                        val i = Intent(this@LoginActivity, ChangePasswordActivity::class.java)
                        i.putExtra("email", email.text.toString())
                        startActivity(i)
                        return
                    }
                    body.token?.let { t ->
                        val prefs = getSharedPreferences("cbsi_prefs", MODE_PRIVATE)
                        prefs.edit().putString("cbsi_token", t).apply()
                    }
                    startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                    finish()
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        toRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
