package pm.ABCdoCadu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import pm.ABCdoCadu.R
import pm.ABCdoCadu.adapter.CategoryAdapter.OnCategoryClickListener
import pm.ABCdoCadu.model.Category
import pm.ABCdoCadu.model.Word

class WordsAdapter(
    private val words: ArrayList<Word>,
    private val wordClickListener: OnWordClickListener
) : RecyclerView.Adapter<WordsAdapter.ViewHolder>() {

    interface OnWordClickListener {
        fun onWordClick(word: Word)
    }

    // Definição do Holder
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Identificar as Views que vão apresentar os dados
        var txt_name: TextView = itemView.findViewById<TextView>(R.id.txt_word)
        var img: ImageView = itemView.findViewById<ImageView>(R.id.imgExercise)

        // Atribui os valores às Views e adiciona o evento de clique
        fun bind(word: Word, clickListener: OnWordClickListener) {
            // Atribui o valor do nome
            txt_name.text = word.name

            // Carrega a imagem
            Picasso.get()
                .load(word.imgURL)
                .into(img)

            // Adiciona o evento de clique na categoria
            itemView.setOnClickListener {
                clickListener.onWordClick(word)
            }
        }
    }

    // Metodo que cria as novas Views (item)
    // Invocado pelo gestor de layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_word, parent, false)
        return ViewHolder(itemView)
    }

    // Metodo que devolve a dimensão do conjunto de dados
    override fun getItemCount(): Int {
        return words.size
    }

    // Metodo que renderiza cada item na RecyclerView
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(words[position], wordClickListener)
    }
}