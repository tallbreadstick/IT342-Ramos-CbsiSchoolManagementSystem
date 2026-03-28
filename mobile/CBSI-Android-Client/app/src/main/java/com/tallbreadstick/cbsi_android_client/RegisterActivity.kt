package com.tallbreadstick.cbsi_android_client

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tallbreadstick.cbsi_android_client.api.RetrofitClient
import com.tallbreadstick.cbsi_android_client.api.models.RegisterRequest
import com.tallbreadstick.cbsi_android_client.api.models.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        RetrofitClient.init(applicationContext)

        val email = findViewById<EditText>(R.id.reg_email)
        val first = findViewById<EditText>(R.id.reg_first)
        val last = findViewById<EditText>(R.id.reg_last)
        val middle = findViewById<EditText>(R.id.reg_middle)
        val sex = findViewById<Spinner>(R.id.reg_sex)
        // populate sex spinner
        val sexAdapter = android.widget.ArrayAdapter.createFromResource(this, R.array.sex_options, android.R.layout.simple_spinner_item)
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sex.adapter = sexAdapter
        val dob = findViewById<EditText>(R.id.reg_dob)
        val perm = findViewById<EditText>(R.id.reg_perm)
        val curr = findViewById<EditText>(R.id.reg_curr)
        val btn = findViewById<Button>(R.id.btn_register)

        btn.setOnClickListener {
            val req = RegisterRequest(
                email.text.toString(),
                first.text.toString(),
                last.text.toString(),
                middle.text.toString().takeIf { it.isNotEmpty() },
                sex.selectedItem.toString(),
                dob.text.toString(),
                perm.text.toString().takeIf { it.isNotEmpty() },
                curr.text.toString().takeIf { it.isNotEmpty() }
            )

            RetrofitClient.api.register(req).enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    val b = response.body()
                    Toast.makeText(this@RegisterActivity, b?.message ?: "Registered", Toast.LENGTH_LONG).show()
                    b?.generatedPassword?.let { pw ->
                        Toast.makeText(this@RegisterActivity, "Temporary password: $pw", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
