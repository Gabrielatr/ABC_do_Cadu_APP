package pm.ABCdoCadu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import pm.ABCdoCadu.R
import pm.ABCdoCadu.model.Word

class WordsAdapter : RecyclerView.Adapter<WordsAdapter.ViewHolder> {

    // Definição do construtor do Apdater e da lista dos objetos com os dados
    private var words: ArrayList<Word>

    constructor(words: ArrayList<Word>) {
        this.words = words
    }

    // Definição do Holder
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Identificar as Views que vão apresentar os dados
        var txt_name: TextView = itemView.findViewById<TextView>(R.id.txt_word)
        var img: ImageView = itemView.findViewById<ImageView>(R.id.img_word)
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
        val item: Word = words.get(position)
        holder.txt_name.setText(item.name)

        Picasso.get()
            .load(item.imgURL)
            .into(holder.img)
    }
}