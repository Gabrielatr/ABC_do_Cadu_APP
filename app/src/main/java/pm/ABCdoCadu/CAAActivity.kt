package pm.ABCdoCadu

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import pm.ABCdoCadu.adapter.WordsAdapter
import pm.ABCdoCadu.model.Word
import java.util.Locale
import kotlin.system.exitProcess

class CAAActivity : AppCompatActivity(), WordsAdapter.OnWordClickListener {

    private val opcoes = listOf("Eu", "Sentimentos", "Conversa", "Desejos", "Cores", "Animais", "Necessidades básicas", "Família", "Comida", "Frutas", "Objetos")
    private val eu = listOf("eu", "estou bem", "estou mal", "quero", "quero mais", "não quero", "preciso de ajuda", "não preciso de ajuda")
    private val conversa = listOf("olá", "adeus", "por favor", "obrigado", "desculpa", "sim", "não", "quem", "o quê", "onde", "quando", "porquê", "como", "você", "tu", "posso ajudar-te?")
    private val sentimentos = listOf("feliz", "triste", "cansado", "com fome", "com sede", "com calor", "com frio", "com sono")
    private val cores = listOf("vermelho", "azul", "amarelo", "verde", "laranja", "roxo", "preto", "branco", "cinzento", "castanho", "cor de rosa", "lilás")
    private val necessidades = listOf("fazer xixi", "fazer coco", "tomar banho", "escovar os dentes", "pentear o cabelo", "vestir", "despir", "dormir", "comer", "beber")
    private val animais = listOf("Leão", "Papagaio", "Cão", "Gato", "Elefante", "Girafa", "Macaco", "Pato", "Peixe", "Tigre", "Urso", "Zebra", "Dinossauro", "Coelho")
    private val familia = listOf("mãe", "pai", "irmã", "irmão", "avó", "avô", "tio", "tia", "primo", "prima", "sobrinho", "sobrinha", "filho", "filha")
    private val desejo = listOf("comer", "beber", "dormir", "ver TV", "ouvir música", "ler", "passear", "correr", "pular", "brincar", "ir à escola", "desenhar", "pintar", "praia", "cinema", "piscina", "andar de carro", "andar de bicicleta", "zoológico")
    private val comida = listOf("Chocolate", "Bolacha", "Pão", "Leite", "Água", "Sumo", "Sopa", "Peixe", "Carne", "Fruta", "Legumes", "Bolos", "Gelado", "Pizza", "Hambúrguer", "Batatas fritas", "Ovos", "Arroz", "Massa", "Pão de forma", "Iogurte", "Queijo", "Fiambre", "Manteiga", "Azeite", "Sal", "Açúcar", "Café", "Chá", "Cereais" )
    private val frutas = listOf("Maçã", "Banana", "Uva", "Cereja", "Abacate", "Laranja", "Pêra", "Pêssego", "Ananás", "Melancia", "Morango", "Kiwi", "Manga", "Maracujá", "Limão", "Framboesa", "Mirtilo", "Amora", "Romã", "Tangerina", "Coco", "Melão", "Ameixa", "Nectarina", "Clementina", "Figo", "Papaia", "Damasco", "Goiaba", "Toranja", "Tâmara", "Carambola", "Pitanga", "Jabuticaba", "Acerola", "Caju", "Graviola", "Jaca", "Mamão", "Uvaia", "Açaí", "Cajá")
    private val objetos = listOf("Telemóvel", "Tablet", "Computador", "Rato", "Teclado", "Monitor", "Caneta", "Lápis", "Borracha", "Caderno", "Livro", "Revista", "Jornal", "Óculos", "Relógio", "Anel", "Colar", "Brincos", "Pulseira", "Mala", "Carteira", "Chave", "Mochila", "Guarda-chuva", "Copo", "Prato", "Garfo", "Faca", "Colher", "Talheres", "Panela", "Frigideira", "Forno", "Fogão", "Microondas", "Máquina de lavar roupa", "Máquina de lavar loiça", "Aspirador", "Vassoura", "Esfregona", "Balde", "Pá", "Escova", "Pente", "Tesoura", "Lanterna", "Fósforos", "Vela", "Lâmpada", "Candeeiro", "Interruptor", "Tomada", "Ficha", "Cabo", "Carregador", "Bateria", "Pilhas", "Comando", "Televisão", "Rádio", "Coluna", "Auscultadores", "Microfone")
    private val wordsCAA = ArrayList<Word>()
    private var phase: List<String> = listOf("")
    private var wordCategory = "opcoes"
    private var previousCategory = ""
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WordsAdapter
    private lateinit var txtPhase: TextView
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caaactivity)

        /* TextToSpeech */
        // Inicializa o TextToSpeech
        initTTS()

        recyclerView = findViewById<RecyclerView>(R.id.CAAView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.setHasFixedSize(true)

        txtPhase = findViewById<TextView>(R.id.txtPhase)
        txtPhase.text = phase.joinToString(separator = " ")

        changeCategory()
    }

    /* RecyclerView Content */
    private fun loadData(cat : List<String>){

        Log.d("** Informação **", "A carregar dados da categoria $wordCategory")

        // Inicializar a RequestQueue e definir o URL do pedido
        val queue = Volley.newRequestQueue(this)
        val images_url = "https://static.arasaac.org/pictograms/"

        // Limpa a listagem de palavras
        wordsCAA.clear()

        // Percorre cada palavra da categoria e atribui os valores ao array responsável pelo RecyclerView
        for (word in cat) {
            // URL para pesquisar a palavra
            val words_url = "https://api.arasaac.org/api/pictograms/pt/search/$word"

            // Solicitar uma string de resposta a um pedido por URL
            val stringRequest = StringRequest(
                Request.Method.GET, words_url,
                { response ->
                    try {
                        //Obtem as respostas da pesquisa na API
                        val jsonArray = JSONArray(response)

                        //Para a primeira correspondência, obtem o seu id, nome, descrição e url da imagem
                        val wordJson = jsonArray.getJSONObject(0)
                        val id = wordJson.getInt("_id")

                        // Cria o objeto Word
                        val w: Word = Word()
                        w.name = word

                        w.imgURL = images_url + "/" + id + "/" + id + "_300.png"

                        // Adiciona na listagem de palavras que vão para a RecyclerView
                        wordsCAA.add(w)

                        if (w == wordsCAA.last()) {
                            uploadToRecyclerView()
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

    private fun uploadToRecyclerView(){
        // Usar o Adapter para associar os dados à RecyclerView
        adapter = WordsAdapter(wordsCAA, this)
        recyclerView.setAdapter(adapter)
    }


    /* Phase Construction */
    private fun changeCategory(word: String = ""){

        Log.d("** Informação **", "Categoria $wordCategory")
        if(wordCategory != previousCategory){
            Log.d("** Informação **", "A mudar de categoria para $wordCategory")
            when (wordCategory) {
                "opcoes" -> {
                    previousCategory = wordCategory
                    wordCategory = "opcoes"
                    loadData(opcoes)          // Carrega a lista
                }
                "Eu" -> {
                    previousCategory = wordCategory
                    wordCategory = "Eu"
                    loadData(eu)             // Carrega a lista
                }
                "Sentimentos" -> {
                    previousCategory = wordCategory
                    wordCategory = "Sentimentos"
                    loadData(sentimentos)    // Carrega a lista
                }
                "Desejos" -> {
                    previousCategory = wordCategory
                    wordCategory = "Desejos"
                    loadData(desejo)         // Carrega a lista
                }
                "Cores" -> {
                    previousCategory = wordCategory
                    wordCategory = "Cores"
                    loadData(cores)          // Carrega a lista
                }
                "Necessidades básicas" -> {
                    previousCategory = wordCategory
                    wordCategory = "Necessidades básicas"
                    loadData(necessidades)   // Carrega a lista
                }
                "Animais" -> {
                    previousCategory = wordCategory
                    wordCategory = "Animais"
                    loadData(animais)        // Carrega a lista
                }
                "Família" -> {
                    previousCategory = wordCategory
                    wordCategory = "Familia"
                    loadData(familia)        // Carrega a lista
                }
                "Conversa" -> {
                    previousCategory = wordCategory
                    wordCategory = "Conversa"
                    loadData(conversa)       // Carrega a lista
                }
                "Comida" -> {
                    previousCategory = wordCategory
                    wordCategory = "Comida"
                    loadData(comida)         // Carrega a lista
                }
                "Frutas" -> {
                    previousCategory = wordCategory
                    wordCategory = "Frutas"
                    loadData(frutas)         // Carrega a lista
                }
                "Objetos" -> {
                    previousCategory = wordCategory
                    wordCategory = "Objetos"
                    loadData(objetos)        // Carrega a lista
                }
                else -> {
                    Log.d("** Informação **", "Fim das categorias")
                    Toast.makeText(this, "Parabéns! Você ao final da categoria!", Toast.LENGTH_SHORT).show()
                    wordsCAA.clear()
                    uploadToRecyclerView()
                }
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

    override fun onWordClick(word: Word) {
        Log.d("** Informação no clique **", word.name)

        if(word.name !in opcoes){
            phase = phase.plus(word.name)
            txtPhase.text = phase.joinToString(separator = " ")
            speak(txtPhase)
        }else{
            wordCategory = word.name
        }
        Log.d("** Informação no clique **", phase.joinToString(separator = " "))
        Log.d("** Informação no clique **", txtPhase.text.toString())
        changeCategory(word.name)
    }

    /* Main Buttons */

    fun goToHomeCategories(view: View){
        wordCategory = "opcoes"
        changeCategory()
    }

    fun clearLastWord(view: View){
        phase = phase.dropLast(1)
        txtPhase.text = phase.joinToString(separator = " ")
    }

    fun clearPhase(view: View){
        phase = listOf("")
        txtPhase.text = phase.joinToString(separator = " ")
    }

    /* Navegation */

    fun redirectToHome(view: View){
        startActivity(Intent(this, HomeActivity::class.java))
    }

    /* TTS */
    private fun initTTS() {
        textToSpeech = TextToSpeech(this){
                status ->
            if(status == TextToSpeech.SUCCESS) {

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

    fun speak(view: View){
        textToSpeech.speak(phase.joinToString(separator = " "), TextToSpeech.QUEUE_FLUSH, null, "")
    }

}