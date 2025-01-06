package pm.ABCdoCadu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import pm.ABCdoCadu.R
import pm.ABCdoCadu.model.Question
import pm.ABCdoCadu.model.Word

class QuestionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Definição do construtor do Adapter e da lista dos objetos com os dados
    private var questions: ArrayList<Question>

    constructor(questions: ArrayList<Question>) {
        this.questions = questions
    }

    // Definição do Holder
    /*class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Identificar as Views que vão apresentar os dados
        var txtTitle: TextView = itemView.findViewById<TextView>(R.id.txtQuestionTitle)
        var imgTitle: ImageView = itemView.findViewById<ImageView>(R.id.imgQuestion)
    }*/

    // Metodo que cria as novas Views (item)
    // Invocado pelo gestor de layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_text_option, parent, false)
                TextOptionViewHolder(view)
            }
            2 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_option, parent, false)
                ImageOptionViewHolder(view)
            }

            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_text_option, parent, false)
                TextOptionViewHolder(view)
            }
        }
    }

    // Metodo que devolve a dimensão do conjunto de dados
    override fun getItemCount(): Int {
        return questions.size
    }

    // Metodo que renderiza cada item na RecyclerView
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val question = questions[position]
        when (holder) {
            is TextOptionViewHolder -> holder.bind(question)
            is ImageOptionViewHolder -> holder.bind(question)
        }
        //val item: Question = questions.get(position)
        //holder.txtTitle.setText(item.title)

        /*Picasso.get()
            .load(item.imgURL)
            .into(holder.img)*/
    }

    // ViewHolder para o tipo Multipla-escolha com Texto
    class TextOptionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(question: Question) {
            // Vincule as views com os dados específicos para este tipo
            itemView.findViewById<TextView>(R.id.txtQuestionTitle).text = question.title
            //itemView.findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.ic_tipo1)
        }
    }

    // ViewHolder para o tipo Multipla-escolha com Imagens
    class ImageOptionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(question: Question) {
            // Vincule as views com os dados específicos para este tipo
            itemView.findViewById<ImageButton>(R.id.imgQuestion).setImageResource(R.drawable.ic_caa)
            //itemView.findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.ic_tipo1)
        }
    }

    // Retornar o tipo do item para determinar qual layout inflar
    override fun getItemViewType(position: Int): Int {
        val question = questions[position]
        return when (question.type_name) {
            "Múltipla-escolha com texto" -> 1
            "Múltipla-escolha com imagens" -> 2
            else -> 1 // Valor default, pode ser alterado
        }
    }

}