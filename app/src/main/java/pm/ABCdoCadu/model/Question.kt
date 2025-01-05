package pm.ABCdoCadu.model

class Question {
    var id : Int = 0
    lateinit var title : String
    lateinit var type_name : String
    var exercise_id : Int = 0
    lateinit var answer_01 : String
    lateinit var answer_02 : String
    var answer_03 : String = ""
    var answer_04 : String = ""
    var correct_answer : Int = 1
}