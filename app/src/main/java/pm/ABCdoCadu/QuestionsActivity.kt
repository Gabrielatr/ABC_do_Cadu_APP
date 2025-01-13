package pm.ABCdoCadu

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import pm.ABCdoCadu.fragment.MultipleChoiceWithTextFragment
import pm.ABCdoCadu.fragment.ResultsFragment
import pm.ABCdoCadu.model.Question
import pm.ABCdoCadu.`object`.Questions


class QuestionsActivity : AppCompatActivity(), MultipleChoiceWithTextFragment.OnDataPass {

    private var questionsList: ArrayList<Question> = ArrayList()
    private var currentPosition : Int = 1
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
        Log.d("Id da pergutna", exerciseId.toString())
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
                    setCurrentQuestion()
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
        val question: Question = questionsList[currentPosition - 1]

        Log.d("Pergunta atual", question.title)
        Log.d("Tipo da pergunta", question.type_name)
        Log.d("Resposta 1", question.answer_01)
        Log.d("Resposta 2", question.answer_02)
        Log.d("Resposta 3", question.answer_03)
        Log.d("Resposta 4", question.answer_04)
        Log.d("Resposta correta", question.correct_answer)

        // Change the layout according to the type of question
        getImageURL(question.title, question.type_name, question)
    }

    private fun changeTypeLayout(type: String, question : Question, imageURL: String = "", images: List<String> = listOf()){
        when(type){
            "Múltipla-escolha com texto" ->{
                currentFragment = MultipleChoiceWithTextFragment.newInstance(
                    imageURL, question.answer_01, question.answer_02, question.answer_03, question.answer_04, question.correct_answer)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, currentFragment)
                    .commit()
            }

        }
        if (type != "Múltipla-escolha com texto"){
            currentPosition++
            setCurrentQuestion()
        }
    }

    private fun getImageURL(title : String, type: String, question: Question){

        // Inicializar a RequestQueue e definir o URL do pedido
        val queue = Volley.newRequestQueue(this)
        val searchURL = "https://api.arasaac.org/api/pictograms/pt/search/"
        val imagesURL = "https://static.arasaac.org/pictograms/"

        // Define a URL para pesquisa
        var titleURL = searchURL + title

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
                    changeTypeLayout(type, question, imgURL)

                } catch (e: JSONException) {
                    Log.d("** Error **", e.toString())
                    e.printStackTrace()
                }
            },
            { Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show() })

        // Adicionar o pedido à RequestQueue.
        queue.add(stringRequest)
    }

    private fun getImagesURL(images : List<String>, type: String, question: Question){
        // Inicializar a RequestQueue e definir o URL do pedido
        val queue = Volley.newRequestQueue(this)
        val searchURL = "https://api.arasaac.org/api/pictograms/pt/search/"
        val imagesAPIURL = "https://static.arasaac.org/pictograms/"

        // Define a variável das urls das imagens
        var imagesURL : List<String> = listOf()

        for (image in images){
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
                        val id = element.getInt("_id")
                        val res = imagesURL + id + "/" + id + "_300.png"
                        imagesURL.plus(res)

                        Log.d("Question Image: ", "$title $imagesURL")

                        if(imagesURL.size == images.size){
                            changeTypeLayout(type, question, "", imagesURL)
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
            finished()
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

    fun redirectToExercises(view: View){
        val intent = Intent(this, ExerciseActivity::class.java)
        startActivity(intent)
    }

}