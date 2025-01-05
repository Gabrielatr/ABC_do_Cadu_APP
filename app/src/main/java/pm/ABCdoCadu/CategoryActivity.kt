package pm.ABCdoCadu

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ImageView
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
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import pm.ABCdoCadu.adapter.CategoryAdapter
import pm.ABCdoCadu.adapter.ExerciseAdapter
import pm.ABCdoCadu.databinding.ActivityCategoryBinding
import pm.ABCdoCadu.model.Category

class CategoryActivity : AppCompatActivity() {

    private lateinit var textToSpeech: TextToSpeech
    lateinit var categories: java.util.ArrayList<Category>
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: CategoryAdapter
    var viewMod: Boolean = false

    private val binding by lazy {
        ActivityCategoryBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        // Definir o modo de tela
        val modo = intent.getStringExtra("modo")

        if (modo == "caa"){
            changeMod(binding.btnCAA3)
        }

        // Recycler View Content

        recyclerView = findViewById<RecyclerView>(R.id.CategoriesView)
        recyclerView.setHasFixedSize(true)

        // Obter a relação de distritos por HTTP
        categories = ArrayList<Category>()

        getCategoriesFromAPI()

    }

    private fun getCategoriesImagesFromAPI(){

        // Inicializar a RequestQueue e definir o URL do pedido
        val queue = Volley.newRequestQueue(this)
        val searchURL = "https://api.arasaac.org/api/pictograms/pt/search/"
        val imagesURL = "https://static.arasaac.org/pictograms/"

        // Percorre todas as categorias
        categories.forEach {
            //Define a URL para pesquisa
            var exerciseURL = searchURL + it.name

            // Faz a requisição para encontrar a imagem de cada categoria
            val stringRequest = StringRequest(
                Request.Method.GET, exerciseURL,
                { response ->
                    Log.d("Image API: ", "Entrei")
                    try {
                        //Obtem as respostas da pesquisa na API
                        val jsonArray = JSONArray(response)

                        //Para cada resposta obtem o seu id e constroi a url de acesso
                        //para obter a primeira imagem que corresponde ao nome do exercício
                        val element = jsonArray.getJSONObject(0)
                        val id = element.getInt("_id")
                        val imgURL = imagesURL + "/" + id + "/" + id + "_300.png"

                        //Atribui o valor ao objeto
                        it.imgURL = imgURL
                        Log.d("Exercise Image: ", it.name + " " + it.imgURL)

                        if (it == categories.last()){
                            displayDataWhenFinished()
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                { Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show() })

            // Adicionar o pedido à RequestQueue.
            queue.add(stringRequest)
        }
    }

    private fun getCategoriesFromAPI(){
        // Inicializar a RequestQueue e definir o URL do pedido
        val queue = Volley.newRequestQueue(this)
        val url = "https://esan-tesp-ds-paw.web.ua.pt/tesp-ds-g5/projeto/api/category/read.php"

        // Solicitar uma string de resposta a um pedido por URL
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                Log.d("A pegar as categorias: ","Entrei")
                try {
                    // Faz a leitura do retorno alojado dentro de records
                    val res = JSONObject(response)
                    val records = res.getJSONArray("records")

                    for (i in 0 until records.length()) {

                        // Obtem a categoria
                        val c = records.getJSONObject(i)

                        //Cria o objeto Category
                        var d: Category = Category()

                        // Atribui as propriedades
                        d.id = Integer.parseInt(c.getString("id"))
                        d.name = c.getString("name")
                        
                        //Adiciona o objeto ao ArrayList
                        categories.add(d)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                getCategoriesImagesFromAPI()
            },
            { Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show() })

        // Adicionar o pedido à RequestQueue.
        queue.add(stringRequest)
    }

    private fun displayDataWhenFinished(){
        // Usar o Adapter para associar os dados encontrados à RecyclerView
        adapter = CategoryAdapter(categories)
        recyclerView.setAdapter(adapter)
    }
    
    fun onClickCategory(view: View) {
        val txt = view as TextView
        val intent = Intent(this, WordsActivity::class.java)
        intent.putExtra("category", txt.text)
        startActivity(intent)
    }

    fun changeMod(view: View){

        if(viewMod){
            binding.LinearLayoutCAA.visibility = INVISIBLE
            binding.btnCAA3.setImageResource(R.drawable.ic_caa)
        }else {
            binding.LinearLayoutCAA.visibility = VISIBLE
            binding.btnCAA3.setImageResource(R.drawable.ic_dict)
        }


        // Obtem as constraints do recyclerview categoria
        /*val constraintLayout = findViewById<ConstraintLayout>(R.id.CategoriesView)
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)*/

        // Altera as constraints e adiciona o campo do CAA
        //constraintSet.connect(R.id.CategoriesView, ConstraintSet.TOP, R.id.Linear_layout_CAA, ConstraintSet.TOP, 0)


        //Altera a função
        /*binding.btnCAA3.setOnClickListener{
            closeCAA(view)
        }*/

    }


    fun redirectToHome(view: View){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}