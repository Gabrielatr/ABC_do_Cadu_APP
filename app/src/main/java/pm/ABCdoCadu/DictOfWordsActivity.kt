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
import pm.ABCdoCadu.adapter.WordsAdapter
import pm.ABCdoCadu.model.Category
import pm.ABCdoCadu.model.Word

class DictOfWordsActivity : AppCompatActivity() {

    lateinit var words: java.util.ArrayList<Word>
    lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dict_words)

        //RecyclerView
        recyclerView = findViewById<RecyclerView>(R.id.RecyclerView)

        recyclerView.setHasFixedSize(true)

        // Obter a relação de palavras por HTTP
        words = ArrayList<Word>()


        // Inicializar a RequestQueue e definir o URL do pedido
        val queue = Volley.newRequestQueue(this)
        val words_url = "https://api.arasaac.org/api/pictograms/pt/search/Frutas"
        val images_url = "https://static.arasaac.org/pictograms/"


        // Solicitar uma string de resposta a um pedido por URL
        val stringRequest = StringRequest(
            Request.Method.GET, words_url,
            { response ->
                try {
                    //Obtem as respostas da pesquisa na API
                    val jsonArray = JSONArray(response)

                    //Para cada resposta obtem o seu id, nome e descrição
                    for (i in 0 until jsonArray.length()) {
                        val id = jsonArray.getJSONObject(i).getString("_id")
                        val keywords = jsonArray.getJSONObject(i).getJSONArray("keywords")


                        val w: Word = Word()
                        w.name = keywords.getJSONObject(0).getString("keyword")
                        w.description =
                            if(keywords.getJSONObject(0).has("description")) {
                                keywords.getJSONObject(0).getString("description")
                            } else {""}
                        w.imgURL = images_url + id + "_300.png"

                        words.add(w)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show() })

        // Usar o Adapter para associar os dados à RecyclerView
        val adapter: WordsAdapter = WordsAdapter(words)
        recyclerView.setAdapter(adapter)

        // Adicionar o pedido à RequestQueue.
        queue.add(stringRequest)
    }
}