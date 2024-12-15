package pm.ABCdoCadu

import android.graphics.drawable.Icon
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
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

class WordsActivity : AppCompatActivity() {

    private lateinit var textToSpeech: TextToSpeech
    lateinit var words: java.util.ArrayList<Word>
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: WordsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_words)

        // * RecyclerView *

        // Layout
        recyclerView = findViewById<RecyclerView>(R.id.RecyclerView)
        recyclerView.setHasFixedSize(true)

        // Inicializa o array de palavras
        words = ArrayList<Word>()

        // Fazer request a API para obter as palavras
        dataFromAPI()


        // * TextToSpeech *

        // Inicializa o TextToSpeech
        initTTS()

        // * CAA *

    }

    // API
    private fun dataFromAPI(){
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
                        val word = jsonArray.getJSONObject(i)
                        val id = word.getInt("_id")
                        val keywords = word.getJSONArray("keywords")


                        val w: Word = Word()
                        w.imgURL = images_url + "/" + id + "/" + id + "_300.png"

                        for (k in 0 until keywords.length()) {
                            val wordKeys = keywords.getJSONObject(k)
                            w.name = wordKeys.getString("keyword")

                            if (wordKeys.has("description")) {
                                w.description = wordKeys.getString("meaning")
                            }
                        }
                        words.add(w)
                    }

                    // Usar o Adapter para associar os dados à RecyclerView
                    adapter = WordsAdapter(words)
                    recyclerView.setAdapter(adapter)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show() })

        // Adicionar o pedido à RequestQueue.
        queue.add(stringRequest)
    }


    // TTS
    private fun initTTS() {
        Toast.makeText(this, "Inicializando TTS", Toast.LENGTH_SHORT).show()
        textToSpeech = TextToSpeech(this){
            status ->
                if(status == TextToSpeech.SUCCESS) {
                    Toast.makeText(this, "Definindo linguagem", Toast.LENGTH_SHORT).show()

                    val result = textToSpeech.setLanguage(Locale("pt", "BR"))
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(this, "Erro na linguagem", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    Toast.makeText(this, "Erro ao iniciar TTS", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun speak(view: View) {
        val text = view.findViewById<TextView>(R.id.txt_word).text.toString()
        Toast.makeText(this, "Speakando : $text", Toast.LENGTH_SHORT).show()

        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onDestroy() {
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
        textToSpeech.shutdown()
        super.onDestroy()
    }


    // CAA

    fun openCAA(view: View) {
        // Alterar imagem do botão para voltar
        val imgButton = view.findViewById<ImageButton>(R.id.btn_CAA)
        imgButton.setImageIcon(Icon.createWithResource(this, R.drawable.ic_back))

        //Abrir campo
        val caa = view.findViewById<LinearLayout>(R.id.Linear_layout_CAA)
        caa.visibility = View.VISIBLE

        val recyclerview = view.findViewById<RecyclerView>(R.id.RecyclerView)
        recyclerview.top = 100
    }

}