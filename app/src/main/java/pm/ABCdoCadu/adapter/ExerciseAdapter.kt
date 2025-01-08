package pm.ABCdoCadu.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import pm.ABCdoCadu.QuestionsActivity
import pm.ABCdoCadu.R
import pm.ABCdoCadu.model.Exercise

class ExerciseAdapter(
    private val exercises: ArrayList<Exercise>,
    private val exerciseClickListener: OnExerciseClickListener) :
    RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    interface OnExerciseClickListener {
        fun onExerciseClick(exercise: Exercise)
    }

    // Definição do Holder
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Identificar as Views que vão apresentar os dados
        var txt_name: TextView = itemView.findViewById<TextView>(R.id.txtExerciseTitle)
        var img: ImageView = itemView.findViewById<ImageView>(R.id.imgExercise)

        // Atribui os valores às Views e adiciona o evento de clique
        fun bind(item: Exercise, clickListener: OnExerciseClickListener) {
            // Atribui o valor do nome
            txt_name.text = item.title

            // Carrega a imagem
            Picasso.get()
                .load(item.imgURL)
                .into(img)

            // Adiciona o evento de clique na categoria
            itemView.setOnClickListener {
                clickListener.onExerciseClick(item)
            }
        }
    }

    // Metodo que cria as novas Views (item)
    // Invocado pelo gestor de layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)
        return ViewHolder(itemView)
    }

    // Metodo que devolve a dimensão do conjunto de dados
    override fun getItemCount(): Int {
        return exercises.size
    }

    // Metodo que renderiza cada item na RecyclerView
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(exercises[position], exerciseClickListener)
    }
}