package pm.ABCdoCadu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import pm.ABCdoCadu.adapter.ExerciseAdapter
import pm.ABCdoCadu.adapter.QuestionAdapter
import pm.ABCdoCadu.adapter.TextQuestionAdapter
import pm.ABCdoCadu.databinding.ActivityCategoryBinding
import pm.ABCdoCadu.databinding.ActivityQuestionsBinding
import pm.ABCdoCadu.model.Question
import pm.ABCdoCadu.`object`.Questions


class QuestionsActivity : AppCompatActivity() {

    private lateinit var questionsList: ArrayList<Question>
    private var currentPosition : Int = 1

    private val binding by lazy {
        ActivityQuestionsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

        val exerciseId = intent.getIntExtra("exercise_id", 0)
        questionsList = Questions.getQuestions(this, exerciseId)
        Log.d("Numero de perguntas", "${questionsList.size}")
        setCurrentQuestion()
    }

    private fun setCurrentQuestion(){
        val question: Question = questionsList[currentPosition - 1]

        // Change the layout according to the type of question
        val type = question.type_name
        changeTypeLayout(type, question)


    }

    private fun changeTypeLayout(type: String, question : Question){
        when(type){
            "Múltipla-escolha com imagens" ->{
                // Hide the image title
                binding.txtQuestionTitle.text = question.title
                binding.imgQuestion.visibility = View.INVISIBLE
            }

            "Múltipla-escolha com texto" ->
            {
                binding.txtQuestionTitle.visibility = View.INVISIBLE
            }
        }

        // Hide the image title
        binding.txtQuestionTitle.text = question.title
        binding.imgQuestion.visibility = View.INVISIBLE

        // Usar o Adapter para associar os dados encontrados à RecyclerView
        val adapter = TextQuestionAdapter(questionsList)
        binding.recyclerViewOptions.setAdapter(adapter)
    }



/*
    private fun getQuestionsFromAPI(exerciseId : Int){

        // Inicializar a RequestQueue e definir o URL do pedido
        val queue = Volley.newRequestQueue(this)
        val url = "https://esan-tesp-ds-paw.web.ua.pt/tesp-ds-g5/projeto/api/question/questions_of_exercise.php?id=$exerciseId"

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
                        var Question: Question = Question()

                        // Atribui as propriedades
                        Question.id = Integer.parseInt(questionObject.getString("id"))
                        Question.title = questionObject.getString("title")
                        Question.type_name = questionObject.getString("type_name")
                        Question.answer_01 = questionObject.getString("answer_01")
                        Question.answer_02 = questionObject.getString("answer_02")
                        Question.answer_03 = questionObject.getString("answer_03")
                        Question.answer_04 = questionObject.getString("answer_04")
                        Question.correct_answer = Integer.parseInt(questionObject.getString("correct_answer"))

                        //Adiciona o objeto ao ArrayList
                        questions.add(Question)
                    }
                    checkType()
                    //Check type
                    //Fill content
                    //If multiple choice with images
                    // Load images for all answers different than ""
                    //If multiple choice with text
                    // Load image for title
                    //If complete text
                    // Load image for title
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show() })

        // Adicionar o pedido à RequestQueue.
        queue.add(stringRequest)
    }

    private fun checkType(){
        questions.forEach {
            when(it.type_name){
                "Múltipla-escolha com imagens" -> supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragmentContainerView, MultipleChoiceWithImagesFragment() )
                    .commit()
                "Múltipla-escolha com texto" -> fillMultipleChoiceWithText(it)
            }
        }

    }

    private fun fillMultipleChoiceWithImages(question : Question){
        findViewById<TextView>(R.id.MCWI_title).text = question.title
        val option1 = findViewById<ImageButton>(R.id.MCWI_img1)
        val option2 = findViewById<ImageButton>(R.id.MCWI_img2)
        val option3 = findViewById<ImageButton>(R.id.MCWI_img3)
        val option4 = findViewById<ImageButton>(R.id.MCWI_img4)

        //Load images for op 1 and 2

        if(question.answer_03 == ""){
            option3.visibility = View.GONE
            return
        }

        //Load image for op 3

        if (question.answer_04 == ""){
            option4.visibility = View.GONE
            return
        }

        //Load image for op 4
    }

    private fun fillMultipleChoiceWithText(question : Question){
        findViewById<TextView>(R.id.MCWI_title).text = question.title
        findViewById<Button>(R.id.MCWT_option1).text = question.answer_01
        findViewById<Button>(R.id.MCWT_option2).text = question.answer_02
        findViewById<Button>(R.id.MCWT_option3).text = question.answer_03
        findViewById<Button>(R.id.MCWT_option4).text = question.answer_04
        val imgTitle = findViewById<ImageView>(R.id.MCWT_imgTitle)

        //Load images for title

    }*/

}