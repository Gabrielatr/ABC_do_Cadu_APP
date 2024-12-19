package pm.ABCdoCadu

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import pm.ABCdoCadu.adapter.CategoryAdapter
import pm.ABCdoCadu.databinding.ActivityCategoryBinding
import pm.ABCdoCadu.model.Category

class CategoryActivity : AppCompatActivity() {

    private lateinit var textToSpeech: TextToSpeech
    lateinit var categories: java.util.ArrayList<Category>
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: CategoryAdapter

    private val binding by lazy {
        ActivityCategoryBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        // Definir o modo de tela
        val modo = intent.getStringExtra("modo")

        if (modo == "caa"){
            openCAA(null)
        }

        recyclerView = findViewById<RecyclerView>(R.id.CategoriesView)
        recyclerView.setHasFixedSize(true)

        // Obter a relação de distritos por HTTP
        categories = ArrayList<Category>()


        //api_php()
        manual()
    }

    private fun gerarURL(a: Category) : Category{
        val queue = Volley.newRequestQueue(this)
        val searchUrl = "https://api.arasaac.org/api/pictograms/pt/search/" + a.name
        val imagesUrl = "https://static.arasaac.org/pictograms/"

        val stringRequest = StringRequest(
            Request.Method.GET, searchUrl,
            { response ->
                try {

                    //Obtem as respostas da pesquisa na API
                    val jsonArray = JSONArray(response)
                    val obj = jsonArray.getJSONObject(0)
                    val id = obj.getInt("_id")
                    val url = imagesUrl + id + "/" + id + "_300.png"
                    //a.imgURL = url
                }catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show() }
        )

        queue.add(stringRequest)
        return a
    }

    private fun manual(){
        //Cria o objeto Category
        var d: Category = Category()
        d.id = 0
        d.name = "Frutas"

        //Adiciona o objeto ao ArrayList
        categories.add(d)

        //Cria o objeto Category
        var a: Category = Category()
        a.id = 1
        a.name = "Lugares"

        //Toast.makeText(this, a.imgURL, Toast.LENGTH_SHORT).show()

        //Adiciona o objeto ao ArrayList
        categories.add(a)

        // Usar o Adapter para associar os dados à RecyclerView
        adapter = CategoryAdapter(categories)
        recyclerView.setAdapter(adapter)
    }

    fun api_php(){
        // Inicializar a RequestQueue e definir o URL do pedido
        val queue = Volley.newRequestQueue(this)
        val url = "https://esan-tesp-ds-paw.web.ua.pt/tesp-ds-g5/projeto/api/category/read.php"

        // Solicitar uma string de resposta a um pedido por URL
        var stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {

                    val jsonObject = JSONObject(response)
                    val records = jsonObject.getJSONArray("records")

                    for (i in 0 until records.length()) {
                        val c = records.getJSONObject(i)

                        //Cria o objeto Category
                        val d: Category = Category()
                        d.id = Integer.parseInt(c.getString("id"))
                        d.name = c.getString("name")
                        //d.imgURL = getimageURL(d.name)

                        //Adiciona o objeto ao ArrayList
                        categories.add(d)
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                // Usar o Adapter para associar os dados à RecyclerView
                adapter = CategoryAdapter(categories)
                recyclerView.setAdapter(adapter)
            },
            { Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show() })

        // Adicionar o pedido à RequestQueue.
        queue.add(stringRequest)
    }


    private fun getimageURL(name: String): String {
        return "antiga forma que funcionavaaaa"
    }

    fun onClickCategory(view: View) {
        val txt = view as TextView
        val intent = Intent(this, WordsActivity::class.java)
        intent.putExtra("category", txt.text)
        startActivity(intent)
    }

    fun openCAA(view: View?){

        //Altera a visibilidade
        binding.LinearLayoutCAA.visibility = VISIBLE

        // Obtem as constraints do recyclerview categoria
        val constraintLayout = findViewById<ConstraintLayout>(R.id.CategoriesView)
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        // Altera as constraints e adiciona o campo do CAA
        constraintSet.connect(R.id.CategoriesView, ConstraintSet.TOP, R.id.Linear_layout_CAA, ConstraintSet.TOP, 0)

        //Altera a imagem
        binding.btnCAA3.setImageResource(R.drawable.ic_dict)

        //Altera a função
        /*binding.btnCAA3.setOnClickListener{
            closeCAA(view)
        }*/

    }

    fun closeCAA(view: View?){

        //Altera a visibilidade
        binding.LinearLayoutCAA.visibility = INVISIBLE

        // Obtem as constraints do recyclerview categoria
        val constraintLayout = findViewById<ConstraintLayout>(R.id.CategoriesView)
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        // Altera as constraints e remove o campo do CAA
        constraintSet.connect(R.id.CategoriesView, ConstraintSet.TOP, R.id.toolbar3, ConstraintSet.TOP, 0)

        //Altera a imagem
        binding.btnCAA3.setImageResource(R.drawable.ic_caa)

        //Altera a função
        /*binding.btnCAA3.setOnClickListener{
            openCAA(view)
        }*/
    }

    fun redirectToHome(view: View){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}