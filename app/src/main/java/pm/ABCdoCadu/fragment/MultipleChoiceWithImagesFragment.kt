package pm.ABCdoCadu.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import pm.ABCdoCadu.R

private const val IMG_TITLE = "param0"
private const val OPT_ONE = "param1"
private const val OPT_TWO = "param2"
private const val OPT_TREE = "param3"
private const val OPT_FOUR = "param4"
private const val CORRECT_OPT = "1"


class MultipleChoiceWithImagesFragment : Fragment() {
    private var imgTitle: String? = null
    private var optOne: String? = null
    private var optTwo: String? = null
    private var optTree: String? = null
    private var optFour: String? = null
    private var correctOpt: String = "1"
    lateinit var selectedOption : String
    private lateinit var dataPasser: OnDataPass

    interface OnDataPass {
        fun onDataPass(selectedOption: String, correctOption: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as OnDataPass
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imgTitle = it.getString(IMG_TITLE)
            optOne = it.getString(OPT_ONE)
            optTwo = it.getString(OPT_TWO)
            optTree = it.getString(OPT_TREE)
            optFour = it.getString(OPT_FOUR)
            correctOpt = it.getString(CORRECT_OPT).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_multiple_choice_with_images, container, false)
    }

    @SuppressLint("ResourceAsColor", "UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Separa as views
        val title = view.findViewById<TextView>(R.id.txtImageTitle)
        val btnOption1 = view.findViewById<ImageView>(R.id.imgOpt1)
        val btnOption2 = view.findViewById<ImageView>(R.id.imgOpt2)
        val btnOption3 = view.findViewById<ImageView>(R.id.imgOpt3)
        val btnOption4 = view.findViewById<ImageView>(R.id.imgOpt4)
        val btnCheckAnswer = view.findViewById<Button>(R.id.btnImageQuestion)

        // Substitui o conteúdo das views pelo valor dos parametros
        title.text = imgTitle

        Log.d("imgTitle-Image", imgTitle.toString())
        Log.d("optOne-Image", optOne.toString())

        // Carrega as imagens
        Picasso.get()
            .load(optOne)
            .into(btnOption1)
        Picasso.get()
            .load(optTwo)
            .into(btnOption2)

        // Verifica se a opção 3 e 4 existem, atribui o texto e torna a opção visível
        if(optTree != "") {
            Picasso.get()
                .load(optTree)
                .into(btnOption3)
            btnOption3.visibility = View.VISIBLE
        }
        if(optFour != "") {
            Picasso.get()
                .load(optFour)
                .into(btnOption4)
            btnOption4.visibility = View.VISIBLE
        }

        // Adiciona o evento de clique nas opções
        btnOption1.setOnClickListener {
            selectedOption = "1"
        }
        btnOption2.setOnClickListener {
            selectedOption = "2"
        }
        btnOption3.setOnClickListener {
            selectedOption = "3"
        }
        btnOption4.setOnClickListener {
            selectedOption = "4"
        }
        btnCheckAnswer.setOnClickListener {
            checkAnswer(view)
        }
    }

    private fun checkAnswer(view: View) {
        dataPasser.onDataPass(selectedOption, correctOpt)
    }

    companion object {
        @JvmStatic
        fun newInstance(img_title: String, opt_one: String, opt_two: String, opt_tree: String, opt_four: String, correct_opt: String) =
            MultipleChoiceWithImagesFragment().apply {
                arguments = Bundle().apply {
                    putString(IMG_TITLE, img_title)
                    putString(OPT_ONE, opt_one)
                    putString(OPT_TWO, opt_two)
                    putString(OPT_TREE, opt_tree)
                    putString(OPT_FOUR, opt_four)
                    putString(CORRECT_OPT, correct_opt)
                }
            }
    }
}