package pm.ABCdoCadu.`object`

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import pm.ABCdoCadu.model.Question

object Questions {

    private var questionsList = ArrayList<Question>()

    fun getQuestions(context: Context, idExerciselist : Int) : ArrayList<Question>{

        fillQuestionsList(context, idExerciselist)

        return questionsList
    }

    private fun fillQuestionsList(context : Context, idExerciselist: Int){

        // Inicializar a RequestQueue e definir o URL do pedido
        val queue = Volley.newRequestQueue(context)
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
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { Toast.makeText(context, "Erro", Toast.LENGTH_SHORT).show() })

        // Adicionar o pedido à RequestQueue.
        queue.add(stringRequest)
    }
}