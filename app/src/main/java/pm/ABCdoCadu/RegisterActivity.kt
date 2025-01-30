package pm.ABCdoCadu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns.EMAIL_ADDRESS
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import pm.ABCdoCadu.databinding.ActivityLoginBinding
import pm.ABCdoCadu.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private var progressBar: ProgressBar? = null
    private lateinit var auth: FirebaseAuth

    private val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setProgressBar(binding.registerProgressBar)

        // Buttons
        with(binding) {
            registerBtnLogin.setOnClickListener {
                redirectToLogin()
            }
            registerBtnRegister.setOnClickListener {
                val email = binding.inputEmail.text.toString()
                val password = binding.inputPassword.text.toString()
                createAccount(email, password)
            }

        }

        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    public override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun reload() {
        auth.currentUser!!.reload().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                updateUI(auth.currentUser)
            } else {
                Log.e(TAG, "reload", task.exception)
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount: $email")
        if (!validateForm()) {
            return
        }

        showProgressBar()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                    startActivity(Intent(this,LoginActivity::class.java))
                } else {

                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        this,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }

                hideProgressBar()
            }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = binding.inputEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, R.string.errorEmptyEmail, Toast.LENGTH_SHORT).show()
            valid = false
        }

        if(!EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, R.string.errorInvalidEmail, Toast.LENGTH_SHORT).show()
            valid = false
        }

        val password = binding.inputPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.errorEmptyPassword, Toast.LENGTH_SHORT).show()
            valid = false
        }
        if(password.length < 6){
            Toast.makeText(this, R.string.errorInvalidPassword, Toast.LENGTH_SHORT).show()
            valid = false
        }

        val confirmPassword = binding.inputConfirmPassword.text.toString()
        if(confirmPassword != password){
            Toast.makeText(this, R.string.errorPasswordNotMatch, Toast.LENGTH_SHORT).show()
            valid = false
        }

        return valid
    }

    private fun updateUI(user: FirebaseUser?) {
        hideProgressBar()
        if (user != null) {

            Log.d(
                "Login status: ", getString(
                    R.string.emailpassword_status_fmt,
                    user.email,
                    user.isEmailVerified
                )
            )

            Log.d("Firebase status: ", getString(R.string.firebase_status_fmt, user.uid))

        }
    }

    companion object {
        private const val TAG = "EmailPassword"
    }

    private fun setProgressBar(bar: ProgressBar) {
        progressBar = bar
    }

    private fun showProgressBar() {
        progressBar?.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar?.visibility = View.INVISIBLE
    }

    public override fun onStop() {
        super.onStop()
        hideProgressBar()
    }

    fun redirectToLogin(){
        startActivity(Intent(this,LoginActivity::class.java))
    }
}