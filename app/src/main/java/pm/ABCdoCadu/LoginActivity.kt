package pm.ABCdoCadu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns.EMAIL_ADDRESS
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import org.json.JSONObject
import pm.ABCdoCadu.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private var progressBar: ProgressBar? = null
    private lateinit var auth: FirebaseAuth

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setProgressBar(binding.progressBarLogin)

        // Buttons
        with(binding) {
            btnLogin.setOnClickListener {
                val email = binding.inputEmail.text.toString()
                val password = binding.inputPassword.text.toString()
                signIn(email, password)
            }
            btnRegister.setOnClickListener {
                redirectToRegister()
            }
        }

        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    private fun holdLogin(){
        if (binding.checkBox.isChecked) {
            getSharedPreferences("pmLogin", Context.MODE_PRIVATE)
                .edit()
                .putBoolean("login",true)
                .apply()
        }
    }

    public override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }

        showProgressBar()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                    startActivity(Intent(this,HomeActivity::class.java))
                } else {

                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        this,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }

                if (!task.isSuccessful) {
                    Toast.makeText(this, R.string.status_sign_in_failed, Toast.LENGTH_SHORT).show()
                }
                hideProgressBar()
            }
        holdLogin()
    }

    /*private fun sendEmailVerification() {
        // Disable button
        binding.verifyEmailButton.isEnabled = false

        // Send verification email
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener { task ->
                // Re-enable button
                binding.verifyEmailButton.isEnabled = true

                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Verification email sent to ${user.email} ",
                        Toast.LENGTH_SHORT,
                    ).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(
                        this,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }*/

    private fun reload() {
        auth.currentUser!!.reload().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                updateUI(auth.currentUser)
            } else {
                Log.e(TAG, "reload", task.exception)
            }
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

    fun redirectToRegister(){
        startActivity(Intent(this,RegisterActivity::class.java))
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
}