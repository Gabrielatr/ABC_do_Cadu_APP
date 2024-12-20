package pm.ABCdoCadu

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class LetMeGuestActivity : AppCompatActivity() {

    private lateinit var textToSpeech: TextToSpeech
    private lateinit var speechRecognizer: SpeechRecognizer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_let_me_guest)

        // Inicializa o TTS
        initTTS()

        // Inicializa o SpeechRecognizer
        recognize()
    }

    // TTS
    private fun initTTS() {
        textToSpeech = TextToSpeech(this){
            status ->
                if(status == TextToSpeech.SUCCESS) {

                    val result = textToSpeech.setLanguage(Locale("pt", "BR"))
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(this, "Erro na linguagem", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    Toast.makeText(this, "Erro ao iniciar TTS", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun recognize(){
        // Inicializa o SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale("pt", "BR"))
        }

        // Botão para iniciar o reconhecimento de voz
        val button = findViewById<ImageButton>(R.id.btn_micro)

        button.setOnClickListener {
            /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 1)
            } else {*/
                speechRecognizer.setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        Toast.makeText(this@LetMeGuestActivity, "Fale agora...", Toast.LENGTH_SHORT)
                            .show()
                    }

                    //TODO

                    override fun onBeginningOfSpeech() {}

                    override fun onRmsChanged(rmsdB: Float) {}

                    override fun onBufferReceived(buffer: ByteArray?) {}

                    override fun onEndOfSpeech() {}

                    override fun onError(error: Int) {
                        Toast.makeText(this@LetMeGuestActivity, "Erro: $error", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onResults(results: Bundle?) {
                        val matches =
                            results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        if (matches != null && matches.isNotEmpty()) {
                            val spokenText = matches[0]
                            Toast.makeText(
                                this@LetMeGuestActivity,
                                "Você disse: $spokenText",
                                Toast.LENGTH_SHORT
                            ).show()
                            findViewById<TextView>(R.id.txt_recognizedText).text = spokenText
                            speak(spokenText) // Fala o que foi reconhecido
                        }
                    }

                    override fun onPartialResults(partialResults: Bundle?) {}

                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
                speechRecognizer.startListening(intent)

        }
    }


    private fun speak(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }


    override fun onDestroy() {
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
        textToSpeech.shutdown()
        super.onDestroy()
    }

    fun redirectToHome(view: View){
        startActivity(Intent(this,HomeActivity::class.java))
    }
}