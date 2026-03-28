package com.tallbreadstick.cbsi_android_client

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tallbreadstick.cbsi_android_client.api.RetrofitClient
import com.tallbreadstick.cbsi_android_client.api.models.ChangePasswordRequest
import com.tallbreadstick.cbsi_android_client.api.models.GenericResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        RetrofitClient.init(applicationContext)

        val emailField = findViewById<EditText>(R.id.cp_email)
        val oldField = findViewById<EditText>(R.id.cp_old)
        val newField = findViewById<EditText>(R.id.cp_new)
        val btn = findViewById<Button>(R.id.btn_change_password)

        intent.getStringExtra("email")?.let { emailField.setText(it) }

        btn.setOnClickListener {
            val req = ChangePasswordRequest(
                emailField.text.toString(),
                oldField.text.toString(),
                newField.text.toString()
            )

            RetrofitClient.api.changePassword(req).enqueue(object : Callback<GenericResponse> {
                override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                    Toast.makeText(this@ChangePasswordActivity, response.body()?.message ?: "Password changed", Toast.LENGTH_LONG).show()
                    // navigate back to login
                    startActivity(Intent(this@ChangePasswordActivity, LoginActivity::class.java))
                    finish()
                }

                override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                    Toast.makeText(this@ChangePasswordActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
