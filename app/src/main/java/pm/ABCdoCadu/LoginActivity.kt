package pm.ABCdoCadu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import pm.ABCdoCadu.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    fun login(){

        //RequestQueue
        val queue = Volley.newRequestQueue(this)
        val url = "https://esan-tesp-ds-paw.web.ua.pt/tesp-ds-g5/api/index.php"

        Toast.makeText(this, "Começa a fazer o login", Toast.LENGTH_SHORT).show()
        //POST request + get string response from the provided URL.
        val postRequest = object : StringRequest(Method.POST, url,
            { response ->
                var msg = response

                Toast.makeText(this, "Login trabalhando", Toast.LENGTH_SHORT).show()

                //If response is OK, do login
                if (response == "OK") {

                    //If checkbox is checked, save login in SharedPreference
                    if (binding.checkBox.isChecked) {
                        getSharedPreferences("pmLogin", Context.MODE_PRIVATE)
                            .edit()
                            .putBoolean("login",true)
                            .apply()
                        msg += " + SAVE"
                    }

                    Toast.makeText(this, "Login efetuado", Toast.LENGTH_SHORT).show()

                    //After login, redirect to Home activity
                    startActivity(Intent(this,HomeActivity::class.java))
                    finish()
                }

                binding.titleLogin.text = response
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
            },
            { error ->
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params: MutableMap<String,String> = HashMap()
                params["email"] = binding.editEmail.text.toString()
                params["password"] = binding.editPassword.text.toString()
                return params
            }
        }
        // Add the request to the RequestQueue.
        queue.add(postRequest)

    }


    fun redirect(){
        startActivity(Intent(this,HomeActivity::class.java))
    }
}