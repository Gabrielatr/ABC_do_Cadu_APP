package pm.ABCdoCadu.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.selects.select
import pm.ABCdoCadu.R

private const val IMG_TITLE = "param1"
private const val OPT_ONE = "param2"
private const val OPT_TWO = "param3"
private const val OPT_TREE = ""
private const val OPT_FOUR = ""
private const val CORRECT_OPT = "1"


class MultipleChoiceWithTextFragment : Fragment() {
    private var imgTitle: String? = null
    private var optOne: String? = null
    private var optTwo: String? = null
    private var optTree: String? = null
    private var optFour: String? = null
    private var correctOpt: String? = "1"
    lateinit var selectedOption : String
    var correctOption: Boolean = false

    interface OnDataPass {
        fun onDataPass(data: String, correctOption: Boolean)
    }

    private lateinit var dataPasser: OnDataPass

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
            correctOpt = it.getString(CORRECT_OPT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_multiple_choice_with_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Substitui o conteúdo das views pelo valor dos parametros
        view.findViewById<ImageView>(R.id.imgTextQuestion).setImageResource(R.drawable.ic_launcher_background)
        view.findViewById<ImageView>(R.id.imgTextQuestion).contentDescription = imgTitle
        view.findViewById<TextView>(R.id.txtOption1).text = optOne
        view.findViewById<TextView>(R.id.txtOption2).text = optTwo

        // Verifica se a opção 3 e 4 existem, atribui o texto e torna a opção visível
        if(optTree != "") {
            view.findViewById<TextView>(R.id.txtOption3).text = optTree
            view.findViewById<TextView>(R.id.txtOption3).visibility = View.VISIBLE
        }
        if(optFour != "") {
            view.findViewById<TextView>(R.id.txtOption4).text = optFour
            view.findViewById<TextView>(R.id.txtOption4).visibility = View.VISIBLE
        }

        // Verifica qual a opção correta e atribui o valor ao campo correto
        when(correctOpt) {
            "1" -> view.findViewById<TextView>(R.id.txtOption1).contentDescription = "correto"
            "2" -> view.findViewById<TextView>(R.id.txtOption2).contentDescription = "correto"
            "3" -> view.findViewById<TextView>(R.id.txtOption3).contentDescription = "correto"
            "4" -> view.findViewById<TextView>(R.id.txtOption4).contentDescription = "correto"
        }

    }

    fun selectedOption(view: View) {
        val itemSelected = view.findViewById<TextView>(view.id)
        selectedOption = itemSelected.text.toString()
        if (itemSelected.contentDescription == "correto") {
            correctOption = true
        }
    }

    fun checkAnswer(view: View) {
        dataPasser.onDataPass(selectedOption, correctOption)
    }

    companion object {
        @JvmStatic
        fun newInstance(img_title: String, opt_one: String, opt_two: String, opt_tree: String, opt_four: String, correct_opt: String) =
            MultipleChoiceWithTextFragment().apply {
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