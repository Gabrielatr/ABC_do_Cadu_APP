package pm.ABCdoCadu

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import pm.ABCdoCadu.adapter.CategoryAdapter
import pm.ABCdoCadu.adapter.WordsAdapter
import pm.ABCdoCadu.model.Category

class CategoryActivity : AppCompatActivity() {

    private lateinit var textToSpeech: TextToSpeech
    lateinit var categories: java.util.ArrayList<Category>
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)


        recyclerView = findViewById<RecyclerView>(R.id.CategoriesView)
        recyclerView.setHasFixedSize(true)

        // Obter a relação de distritos por HTTP
        categories = ArrayList<Category>()


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
                        d.id = c.getInt("id")
                        d.name = c.getString("name")
                        d.imgURL = getimageURL(d.name)

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

    fun getimageURL(name: String): String {
        lateinit var url: String
        val queue = Volley.newRequestQueue(this)
        val search_url = "https://api.arasaac.org/api/pictograms/pt/search/$name"
        val images_url = "https://static.arasaac.org/pictograms/"

        var stringRequest = StringRequest(
            Request.Method.GET, search_url,
            { response ->
                try {

                    //Obtem as respostas da pesquisa na API
                    val jsonArray = JSONArray(response)
                    val obj = jsonArray.getJSONObject(0)
                    val id = obj.getInt("_id")
                    url = images_url + id + "/" + id + "_300.png"
                }catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show() }
        )

        queue.add(stringRequest)
        return url
    }

    fun onClickCategory(view: View) {
        val intent = Intent(this, WordsActivity::class.java)
        startActivity(intent)
    }
}