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

    fun redirectToDict(){
        startActivity(Intent(this,DictOfWordsActivity::class.java))
    }
}