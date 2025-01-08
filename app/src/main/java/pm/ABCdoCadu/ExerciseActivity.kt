package pm.ABCdoCadu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import pm.ABCdoCadu.adapter.ExerciseAdapter
import pm.ABCdoCadu.model.Exercise


class ExerciseActivity : AppCompatActivity(), ExerciseAdapter.OnExerciseClickListener {

    private lateinit var exercises: ArrayList<Exercise>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExerciseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        // Inicializa o recyclerview
        recyclerView = findViewById<RecyclerView>(R.id.ExerciseRecyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(this))

        // Coleta as informações para a recyclerview
        exercises = ArrayList<Exercise>()
        getExercisesFromAPI()
    }

    private fun getExercisesFromAPI(){

        // Inicializar a RequestQueue e definir o URL do pedido
        val queue = Volley.newRequestQueue(this)
        val url = "https://esan-tesp-ds-paw.web.ua.pt/tesp-ds-g5/projeto/api/exercise/read.php"

        // Solicitar uma string de resposta a um pedido por URL
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    // Faz a leitura do retorno alojado dentro de records
                    val res = JSONObject(response)
                    val exerciseList = res.getJSONArray("records")

                    for (i in 0 until exerciseList.length()) {

                        // Obtem a categoria
                        val exerciseObject = exerciseList.getJSONObject(i)

                        //Cria o objeto exercise
                        var exercise: Exercise = Exercise()

                        // Atribui as propriedades
                        exercise.id = Integer.parseInt(exerciseObject.getString("id"))
                        exercise.title = exerciseObject.getString("title")
                        exercise.category = exerciseObject.getString("category_name")

                        //Adiciona o objeto ao ArrayList
                        exercises.add(exercise)
                    }
                    getExercisesImagesFromAPI()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show() })

        // Adicionar o pedido à RequestQueue.
        queue.add(stringRequest)
    }

    private fun getExercisesImagesFromAPI(){
        
        // Inicializar a RequestQueue e definir o URL do pedido
        val queue = Volley.newRequestQueue(this)
        val searchURL = "https://api.arasaac.org/api/pictograms/pt/search/"
        val imagesURL = "https://static.arasaac.org/pictograms/"

        // Percorre todas as categorias
        exercises.forEach {
            //Define a URL para pesquisa
            var exerciseURL = searchURL + it.category

            // Faz a requisição para encontrar a imagem de cada categoria
            val stringRequest = StringRequest(
                Request.Method.GET, exerciseURL,
                { response ->
                    Log.d("Image API: ", "Entrei")
                    try {
                        //Obtem as respostas da pesquisa na API
                        val jsonArray = JSONArray(response)

                        //Para cada resposta obtem o seu id e constroi a url de acesso
                        //para obter a primeira imagem que corresponde ao nome do exercício
                        val element = jsonArray.getJSONObject(0)
                        val id = element.getInt("_id")
                        val imgURL = imagesURL + "/" + id + "/" + id + "_300.png"

                        //Atribui o valor ao objeto
                        it.imgURL = imgURL
                        Log.d("Exercise Image: ", it.title + " " + it.imgURL)

                        if (it == exercises.last()){
                            displayDataWhenFinished()
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

    override fun onExerciseClick(exercise: Exercise) {
        Log.d("** Informação **", "Clique no exercício : " + exercise.title)

        // Inicia a nova Activity e passa o item clicado
        val intent = Intent(this, QuestionsActivity::class.java)
        intent.putExtra("exercise_id", exercise.id)
        startActivity(intent)
    }

    private fun displayDataWhenFinished(){
        Log.d("** Informação **", "A colocar no recyclerview")
        // Usar o Adapter para associar os dados encontrados à RecyclerView
        adapter = ExerciseAdapter(exercises, this)
        recyclerView.setAdapter(adapter)
    }

    fun goToHome(view: View){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }



}