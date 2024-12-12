package pm.ABCdoCadu

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import pm.ABCdoCadu.adapter.CategoryAdapter
import pm.ABCdoCadu.model.Category

class DictOfCategoriesActivity : AppCompatActivity() {

    lateinit var categories: java.util.ArrayList<Category>
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dict_categories)

        recyclerView = findViewById<RecyclerView>(R.id.CategoriesView)

        // Escolher uma das opções para o layout do RecyclerView :
        // - LinearLayoutManager
        // - GridLayoutManager
        // - StaggeredGridLayoutManager
        recyclerView.setHasFixedSize(true)

        // Obter a relação de distritos por HTTP
        categories = ArrayList<Category>()


        // Inicializar a RequestQueue e definir o URL do pedido
        val queue = Volley.newRequestQueue(this)
        val url = "https://esan-tesp-ds-paw.web.ua.pt/tesp-ds-g5/projeto/api/category/read.php"


        // Solicitar uma string de resposta a um pedido por URL
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val c = jsonArray.getJSONObject(i)
                        val d: Category = Category()
                        d.name = c.getString("name")
                        categories.add(d)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                // Usar o Adapter para associar os dados à RecyclerView
                val adapter: CategoryAdapter = CategoryAdapter(categories)
                recyclerView.setAdapter(adapter)
            },
            { Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show() })

        // Adicionar o pedido à RequestQueue.
        queue.add(stringRequest)
    }
}