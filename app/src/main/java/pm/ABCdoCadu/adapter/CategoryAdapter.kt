package pm.ABCdoCadu.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import pm.ABCdoCadu.R
import pm.ABCdoCadu.model.Category


class CategoryAdapter(
    private val categories: ArrayList<Category>,
    private val categoryClickListener: OnCategoryClickListener
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    interface OnCategoryClickListener {
        fun onCategoryClick(category: String)
    }

    // Definição do Holder
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Identificar as Views que vão apresentar os dados
        var txt_name: TextView = itemView.findViewById<TextView>(R.id.txtCategory)
        var img: ImageView = itemView.findViewById<ImageView>(R.id.imgCategory)

        // Atribui os valores às Views e adiciona o evento de clique
        fun bind(category: Category, clickListener: OnCategoryClickListener) {
            // Atribui o valor do nome
            txt_name.text = category.name

            // Carrega a imagem
            Picasso.get()
                .load(category.imgURL)
                .into(img)

            // Adiciona o evento de clique na categoria
            itemView.setOnClickListener {
                clickListener.onCategoryClick(category.name)
            }
        }
    }

    // Metodo que cria as novas Views (item)
    // Invocado pelo gestor de layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(itemView)
    }

    // Metodo que devolve a dimensão do conjunto de dados
    override fun getItemCount(): Int {
        return categories.size
    }

    // Metodo que renderiza cada item na RecyclerView
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position], categoryClickListener)
    }
}