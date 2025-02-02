package pm.ABCdoCadu

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import pm.ABCdoCadu.adapter.WordsAdapter
import pm.ABCdoCadu.model.Word
import java.util.Locale

class WordsActivity : AppCompatActivity(), WordsAdapter.OnWordClickListener {

    private lateinit var textToSpeech: TextToSpeech
    lateinit var words: java.util.ArrayList<Word>
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: WordsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_words)

        val category = intent.getStringExtra("category") ?: "Nada"
        Log.d("** Informação na activity **", category)

        /* RecyclerView */

        // Layout
        recyclerView = findViewById<RecyclerView>(R.id.RecyclerView)
        recyclerView.setHasFixedSize(true)

        // Inicializa o array de palavras
        words = ArrayList<Word>()

        // Fazer request a API para obter as palavras
        getDataFromAPI(category)

        /* TextToSpeech */

        // Inicializa o TextToSpeech
        initTTS()

    }

    // API
    private fun getDataFromAPI(cat : String){
        // Inicializar a RequestQueue e definir o URL do pedido
        val queue = Volley.newRequestQueue(this)
        val words_url = "https://api.arasaac.org/api/pictograms/pt/search/$cat"
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
                        val word = jsonArray.getJSONObject(i)
                        val id = word.getInt("_id")
                        val keywords = word.getJSONArray("keywords")

                        val w: Word = Word()
                        w.imgURL = images_url + "/" + id + "/" + id + "_300.png"

                        //Para cada palavra obtem o seu nome e descrição (se disponível)
                        for (k in 0 until keywords.length()) {
                            val wordKeys = keywords.getJSONObject(k)
                            w.name = wordKeys.getString("keyword")

                            if (wordKeys.has("description")) {
                                w.description = wordKeys.getString("meaning")
                            }
                        }

                        //Adiciona o objeto ao ArrayList
                        words.add(w)
                    }

                    // Usar o Adapter para associar os dados à RecyclerView
                    adapter = WordsAdapter(words, this)
                    recyclerView.setAdapter(adapter)

                } catch (e: JSONException) {
                    Log.d("** Error **", e.toString())
                    e.printStackTrace()
                }
            },
            { Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show() })

        // Adicionar o pedido à RequestQueue.
        queue.add(stringRequest)
    }

    // TTS
    private fun initTTS() {
        textToSpeech = TextToSpeech(this){
            status ->
                if(status == TextToSpeech.SUCCESS) {

                    val result = textToSpeech.setLanguage(Locale("pt", "PT"))
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(this, "Erro na linguagem", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    Toast.makeText(this, "Erro ao iniciar TTS", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onDestroy() {
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
        textToSpeech.shutdown()
        super.onDestroy()
    }

    fun goBack(view: View){
        startActivity(Intent(this,CategoryActivity::class.java))
    }

    override fun onWordClick(word: Word) {
        textToSpeech.speak(word.name, TextToSpeech.QUEUE_FLUSH, null, "")
    }

}