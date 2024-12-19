package pm.ABCdoCadu

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import pm.ABCdoCadu.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

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

    fun changeTheme(view: View){
        if(binding.switch1.isActivated) {
            setTheme(R.style.Theme_DarkTheme)
        }else{
            setTheme(R.style.Theme_LightTheme)
        }
    }
}