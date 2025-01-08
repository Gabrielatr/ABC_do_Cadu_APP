package pm.ABCdoCadu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pm.ABCdoCadu.R
import pm.ABCdoCadu.model.Question

class TextQuestionAdapter : RecyclerView.Adapter<TextQuestionAdapter.ViewHolder> {

    // Definição do construtor do Adapter e da lista dos objetos com os dados
    private var questions: ArrayList<Question>

    constructor(questions: ArrayList<Question>) {
        this.questions = questions
    }

    // Definição do Holder
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Identificar as Views que vão apresentar os dados
        var txtOption: TextView = itemView.findViewById<TextView>(R.id.txtOption2)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_text_option, parent, false)
        return ViewHolder(itemView)
    }


    // Metodo que devolve a dimensão do conjunto de dados
    override fun getItemCount(): Int {
        return questions.size
    }

    // Metodo que renderiza cada item na RecyclerView
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Question = questions[position]
        holder.txtOption.text = item.title

        /*Picasso.get()
            .load(item.imgURL)
            .into(holder.img)*/
    }


}