package pm.ABCdoCadu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import pm.ABCdoCadu.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

    fun redirectToCategoriesList(view: View){
        val intent = Intent(this,CategoryActivity::class.java)
        intent.putExtra("modo", "lista")
        startActivity(intent)
    }

    fun redirectToCaa(view: View){
        val intent = Intent(this,CategoryActivity::class.java)
        intent.putExtra("modo", "caa")
        startActivity(intent)
    }

    fun redirectToLetMeGuest(view: View){
        startActivity(Intent(this,LetMeGuestActivity::class.java))
    }

    fun redirectToExercises(view: View){
        startActivity(Intent(this, ExerciseActivity::class.java))
    }

    fun changeTheme(view: View){
        if(binding.switch1.isActivated) {
            setTheme(R.style.Theme_DarkTheme)
        }else{
            setTheme(R.style.Theme_LightTheme)
        }
    }

    fun signOut(view: View) {
        val sharedPreferences = this.getSharedPreferences("pmLogin", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("login", false)
        editor.apply()

        startActivity(Intent(this, LoginActivity::class.java))
    }
}