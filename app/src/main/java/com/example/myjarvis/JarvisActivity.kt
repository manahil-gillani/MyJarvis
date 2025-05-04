package com.example.myjarvis

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.AlarmClock
import android.provider.MediaStore
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import java.util.*

class JarvisActivity : AppCompatActivity() {

    private lateinit var tts: TextToSpeech
    private lateinit var wakeRecognizer: SpeechRecognizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jarvis)

        val jarvisAnim = findViewById<LottieAnimationView>(R.id.jarvisAnimation)
        val userCommand = findViewById<TextView>(R.id.userCommand)
        val jarvisReply = findViewById<TextView>(R.id.jarvisReply)

        // Start Lottie animation
        jarvisAnim.playAnimation()

        // Setup TTS
        tts = TextToSpeech(this) {
            if (it == TextToSpeech.SUCCESS) {
                tts.language = Locale.US
            }
        }

        // Setup Wake Word Listener
        wakeRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        val wakeIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }

        wakeRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                val heard = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0)?.lowercase() ?: ""
                if ("jarvis" in heard || "hey jarvis" in heard) {
                    speak("Yes, I'm listening.")
                    listenForCommand(userCommand, jarvisReply)
                } else {
                    restartWakeWordListening()
                }
            }

            override fun onError(error: Int) {
                restartWakeWordListening()
            }

            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

                wakeRecognizer.startListening(wakeIntent)
    }

    private fun restartWakeWordListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }

        Handler(Looper.getMainLooper()).postDelayed({
            wakeRecognizer.startListening(intent)
        }, 1000)
    }

    private fun listenForCommand(userCommand: TextView, jarvisReply: TextView) {
        val recognizer = SpeechRecognizer.createSpeechRecognizer(this)
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }

        recognizer.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                val text = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0) ?: ""
                userCommand.text = text
                val reply = handleCommand(text)
                jarvisReply.text = reply
                speak(reply)
            }

            override fun onError(error: Int) {
                speak("I didn't catch that.")
                restartWakeWordListening()
            }

            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        recognizer.startListening(intent)
    }

    private fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun handleCommand(command: String): String {
        return when {
            "alarm" in command.lowercase() -> {
                val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                    putExtra(AlarmClock.EXTRA_HOUR, 8)
                    putExtra(AlarmClock.EXTRA_MINUTES, 0)
                }
                startActivity(intent)
                "Setting an alarm for 8 AM"
            }

            "bluetooth" in command.lowercase() -> {
                startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS))
                "Opening Bluetooth settings"
            }

            "camera" in command.lowercase() -> {
                startActivity(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                "Opening camera"
            }

            "call" in command.lowercase() -> {
                val intent = Intent(Intent.ACTION_CALL).apply {
                    data = Uri.parse("tel:123456789")
                }
                startActivity(intent)
                "Calling 123456789"
            }

            "sms" in command.lowercase() -> {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("sms:123456789")
                    putExtra("sms_body", "Hello from JARVIS")
                }
                startActivity(intent)
                "Sending SMS to 123456789"
            }

            else -> "Sorry, I didn't understand that."
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.shutdown()
        wakeRecognizer.destroy()
    }
}
