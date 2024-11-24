package pm.ABCdoCadu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import pm.ABCdoCadu.model.Category

class DictOfCategoriesActivity : AppCompatActivity() {

    lateinit var categorias: java.util.ArrayList<Category>
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dict_categories)

        /*
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Escolher uma das opções para o layout do RecyclerView :
        // - LinearLayoutManager
        // - GridLayoutManager
        // - StaggeredGridLayoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(GridLayoutManager(this))


        // Obter a relação de distritos por HTTP
        categorias = ArrayList<Category>()


        // Inicializar a RequestQueue e definir o URL do pedido
        val queue = Volley.newRequestQueue(this)
        val url = "https://esan-tesp-ds-paw.web.ua.pt/nveloso/api/getDestinos.php"


        // Solicitar uma string de resposta a um pedido por URL
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val c = jsonArray.getJSONObject(i)
                        val d: Destino = Destino()
                        d.destino = c.getString("destino")
                        d.preco = c.getDouble("preco")
                        d.imgURL = c.getString("img")
                        destinos.add(d)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                // Usar o Adapter para associar os dados à RecyclerView
                val adapter: DestinosAdapter = DestinosAdapter(destinos)
                recyclerView.setAdapter(adapter)
            },
            { Toast.makeText(this@MainActivity, "Erro", Toast.LENGTH_SHORT).show() })

        // Adicionar o pedido à RequestQueue.
        queue.add(stringRequest)*/
    }
}