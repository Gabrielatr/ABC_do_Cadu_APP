package pm.ABCdoCadu

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.media.RouteListingPreference
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import pm.ABCdoCadu.adapter.ExerciseAdapter
import pm.ABCdoCadu.adapter.TextQuestionAdapter
import pm.ABCdoCadu.databinding.ActivityCategoryBinding
import pm.ABCdoCadu.databinding.ActivityQuestionsBinding
import pm.ABCdoCadu.fragment.MultipleChoiceWithImagesFragment
import pm.ABCdoCadu.fragment.MultipleChoiceWithTextFragment
import pm.ABCdoCadu.fragment.ResultsFragment
import pm.ABCdoCadu.model.Question
import pm.ABCdoCadu.`object`.Questions


class QuestionsActivity : AppCompatActivity(), MultipleChoiceWithTextFragment.OnDataPass, MultipleChoiceWithImagesFragment.OnDataPass {

    private var questionsList: ArrayList<Question> = ArrayList()
    private var currentPosition : Int = 0
    private lateinit var correctOption: String
    private lateinit var currentFragment : Fragment
    var results: ArrayList<Boolean> = ArrayList()

    private val binding by lazy {
        ActivityQuestionsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

        val exerciseId = intent.getIntExtra("exercise_id", 0)
        fillQuestionsList(exerciseId)
    }

    private fun fillQuestionsList(idExerciselist: Int){
        // Inicializar a RequestQueue e definir o URL do pedido
        val queue = Volley.newRequestQueue(this)
        val url = "https://esan-tesp-ds-paw.web.ua.pt/tesp-ds-g5/projeto/api/question/questions_of_exercise.php?id=$idExerciselist"

        Log.d("** Informação na classe **", "ID do exercicio: $idExerciselist")

        // Solicitar uma string de resposta a um pedido por URL
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    // Faz a leitura do retorno alojado dentro de records
                    val res = JSONObject(response)
                    val questionList = res.getJSONArray("records")

                    for (i in 0 until questionList.length()) {

                        // Obtem a categoria
                        val questionObject = questionList.getJSONObject(i)
                        Log.d("Question Object", questionObject.toString())

                        // Se a questao pertence a este exercicio, adiciona na lista
                        if(questionObject.getString("exercise_id") == idExerciselist.toString()){
                            //Cria o objeto Question
                            var question: Question = Question()

                            // Atribui as propriedades
                            question.id = Integer.parseInt(questionObject.getString("id"))
                            question.title = questionObject.getString("title")
                            question.type_name = questionObject.getString("type_name")
                            question.answer_01 = questionObject.getString("answer_01")
                            question.answer_02 = questionObject.getString("answer_02")
                            question.answer_03 = questionObject.getString("answer_03")
                            question.answer_04 = questionObject.getString("answer_04")
                            question.correct_answer = questionObject.getString("correct_answer")

                            //Adiciona o objeto ao ArrayList
                            questionsList.add(question)
                        }
                    }
                    if (questionsList.size > 0){
                        setCurrentQuestion()
                    }else{
                        Toast.makeText(this, R.string.status_no_questions_for_exercise, Toast.LENGTH_SHORT).show()
                        redirectToExercises()
                    }
                    Log.d("Numero de perguntas", "${questionsList.size}")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show() })

        // Adicionar o pedido à RequestQueue.
        queue.add(stringRequest)
    }

    private fun setCurrentQuestion(){
        val question: Question = questionsList[currentPosition]

        Log.d("Pergunta atual", question.title)
        Log.d("Tipo da pergunta", question.type_name)
        Log.d("Resposta 1", question.answer_01)
        Log.d("Resposta 2", question.answer_02)
        Log.d("Resposta 3", question.answer_03)
        Log.d("Resposta 4", question.answer_04)
        Log.d("Resposta correta", question.correct_answer)

        // Change the layout according to the type of question
        when(question.type_name){
            "Múltipla-escolha com texto" ->{
                getImageURL(question)
            }
            "Múltipla-escolha com imagens" ->{
                getImagesURL(listOf(question.answer_01, question.answer_02, question.answer_03, question.answer_04), question)
            }
            else -> {
                Toast.makeText(this, "Erro ao carregar a questão: $currentPosition", Toast.LENGTH_SHORT).show()
                currentPosition++

                if (currentPosition == questionsList.size) {
                    finished()
                } else {
                    setCurrentQuestion()
                }
            }
        }
    }

    private fun changeTypeLayout(type: String, question : Question, imageURL: String = "", images: List<String> = listOf()){
        when(type){
            "Múltipla-escolha com texto" ->{
                Log.d("Type: ", "Múltipla-escolha com texto")
                currentFragment = MultipleChoiceWithTextFragment.newInstance(
                    imageURL, question.answer_01, question.answer_02, question.answer_03, question.answer_04, question.correct_answer)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, currentFragment)
                    .commit()
            }
            "Múltipla-escolha com imagens" ->{
                Log.d("Type: ", "Múltipla-escolha com imagens")
                currentFragment = MultipleChoiceWithImagesFragment.newInstance(
                    question.title, images[0], images[1], images[2], images[3], question.correct_answer)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, currentFragment)
                    .commit()
            }
            else -> {
                Toast.makeText(this, "Erro ao carregar a questão: $currentPosition", Toast.LENGTH_SHORT).show()
                currentPosition++

                if (currentPosition == questionsList.size) {
                    finished()
                } else {
                    setCurrentQuestion()
                }
            }
        }
    }

    private fun getImageURL(question: Question){
        // Inicializar a RequestQueue e definir o URL do pedido
        val queue = Volley.newRequestQueue(this)
        val searchURL = "https://api.arasaac.org/api/pictograms/pt/search/"
        val imagesURL = "https://static.arasaac.org/pictograms/"

        // Define a URL para pesquisa
        var titleURL = searchURL + question.title

        // Inicializa a variável da url da imagem
        var imgURL = ""

        // Faz a requisição para encontrar a url da questão
        val stringRequest = StringRequest(
            Request.Method.GET, titleURL,
            { response ->
                try {
                    //Obtem as respostas da pesquisa na API
                    val jsonArray = JSONArray(response)

                    //Para cada resposta obtem o seu id e constroi a url de acesso
                    //para obter a primeira imagem que corresponde ao nome do exercício
                    val element = jsonArray.getJSONObject(0)
                    val id = element.getInt("_id")
                    imgURL = imagesURL + id + "/" + id + "_300.png"

                    Log.d("Question Image: ", "$title $imgURL")
                    changeTypeLayout(question.type_name, question, imgURL)

                } catch (e: JSONException) {
                    Log.d("** Error **", e.toString())
                    e.printStackTrace()
                }
            },
            { Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show() })

        // Adicionar o pedido à RequestQueue.
        queue.add(stringRequest)
    }

    private fun getImagesURL(images : List<String>, question: Question){

        // Inicializar a RequestQueue e definir o URL do pedido
        val queue = Volley.newRequestQueue(this)
        val searchURL = "https://api.arasaac.org/api/pictograms/pt/search/"
        val imageURL = "https://static.arasaac.org/pictograms/"

        // Define a variável das urls das imagens
        var imagesURLs : MutableList<String> = mutableListOf("", "", "0", "0")

        for (image in images){
            Log.d("Image: ", image)
            // Define a URL para pesquisa
            var titleURL = searchURL + image

            // Faz a requisição para encontrar a url da questão
            val stringRequest = StringRequest(
                Request.Method.GET, titleURL,
                { response ->
                    try {
                        //Obtem as respostas da pesquisa na API
                        val jsonArray = JSONArray(response)

                        //Para cada resposta obtem o seu id e constroi a url de acesso
                        //para obter a primeira imagem que corresponde ao nome do exercício
                        val element = jsonArray.getJSONObject(0)
                        var id = element.getInt("_id")
                        var res = imageURL + id + "/" + id + "_300.png"
                        var index = images.indexOf(image)
                        imagesURLs[index] = res

                        Log.d("Question Image: ", "$image")
                        Log.d("Images URLS: ", "$imagesURLs Size: ${imagesURLs.size}")
                        Log.d("Image's Name: ", "$images Size: ${images.size}")

                        // Verifica se os campos obrigatorios estão preenchidos e os opcionais vazios
                        if(!imagesURLs.contains("0") && imagesURLs[0] != "" && imagesURLs[1] != ""){
                            changeTypeLayout(question.type_name, question, "", imagesURLs)
                        }

                    } catch (e: JSONException) {
                        Log.d("** Error **", e.toString())
                        e.printStackTrace()
                    }
                },
                { Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show() })

            // Adicionar o pedido à RequestQueue.
            queue.add(stringRequest)
        }
    }

    override fun onDataPass(selectedOption: String, correctOption: String) {
        if (selectedOption == correctOption){
            Toast.makeText(this, "Resposta correta", Toast.LENGTH_SHORT).show()
            currentPosition++

            if (currentPosition == questionsList.size) {
                finished()
            } else {
                setCurrentQuestion()
            }
        }else{
            Toast.makeText(this, "Resposta errada", Toast.LENGTH_SHORT).show()
        }
    }

    fun finished(){
        currentFragment = ResultsFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, currentFragment)
            .commit()
    }

    fun redirectToExercises(view: View? = null){
        val intent = Intent(this, ExerciseActivity::class.java)
        startActivity(intent)
    }

}