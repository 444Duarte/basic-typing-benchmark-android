package com.tuwien.buildinginteractioninterfaces.typingbenchmark.presentation

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.R
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.data.local.TwelveDictsDictionaryRepository
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.data.local.room.RoomDatabase
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.Benchmarker
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.GameInteractor
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.GameInteractor.Callback
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.model.OptionsModel
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.DictionaryRepository
import kotlinx.android.synthetic.main.activity_play_game.*

@Suppress("MemberVisibilityCanBePrivate")
class PlayGame : AppCompatActivity() {

    lateinit var dictionaryRepository: DictionaryRepository
    lateinit var game: GameInteractor
    lateinit var options: OptionsModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_game)

        options = intent.extras["OPTIONS"] as OptionsModel

        setOptions()

        start_button.setOnClickListener { restartGame() }
        pause_button.setOnClickListener { pauseGame() }
        continue_button.setOnClickListener { continueGame() }
        restart_button.setOnClickListener { restartGame() }
        benchmark_button.setOnClickListener { viewBenchmark() }

        keyboard_input.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
            }
        }

    }

    fun setOptions(){
        // By default it already comes with auto-correct
        if(!options.autoCorrect){
            findViewById<EditText>(R.id.keyboard_input).inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        }

        /*when(options.typeGame){
            OptionsModel.TypeGame.TIME -> {}
            OptionsModel.TypeGame.NUM_WORDS -> {}
            OptionsModel.TypeGame.NUM_ERRORS -> {}
            OptionsModel.TypeGame.NUM_CORRECT_WORDS -> {}
            OptionsModel.TypeGame.NO_END -> {}
        }*/

        dictionaryRepository = when(options.source){
            OptionsModel.Source.TWELVE_DICTS -> TwelveDictsDictionaryRepository(this)
            else -> {
                TwelveDictsDictionaryRepository(this)
            }
        }


    }

    fun focusOnInputAndShowKeyboard(){
        keyboard_input.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(keyboard_input, InputMethodManager.SHOW_IMPLICIT)
    }

    fun restartGame(){
        start_button.visibility = View.GONE
        pause_menu.visibility = View.GONE
        pause_button.visibility =  View.VISIBLE
        keyboard_input.visibility = View.VISIBLE
        continue_button.isEnabled = true

        updateStatsTextViews(0f,0f,0f,0f,0,0)
        focusOnInputAndShowKeyboard()

        //To prevent from bugging with game type TIME
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()

        val gameCallback = object: Callback {
            override fun finishGame() {
                this@PlayGame.finishGame()
            }

            override fun updateWords(currentWord: String?, nextWord: String?) {
                if (currentWord != null) {
                    if (nextWord != null) {
                        updateWordsTextViews(currentWord,nextWord)
                    }
                }
            }
        }

        val benchmarkerCallback = Benchmarker.Callback { wpm, ksps, kspc, msdErrorRate, correctWords, failedWords -> updateStatsTextViews(wpm, ksps, kspc, msdErrorRate,correctWords, failedWords) }

        val keyboardApp = Settings.Secure.getString(getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD)

        val benchmarkRepository = RoomDatabase.instance.getDatabase(applicationContext).benchmarkDao()

        game = GameInteractor(gameCallback, benchmarkerCallback,
                dictionaryRepository,
                benchmarkRepository,
                chronometer,
                options,
                keyboardApp)

        keyboard_input.addTextChangedListener(game)
        keyboard_input.setText("")
    }

    fun finishGame(){
        pauseGame()
        continue_button.isEnabled = false
        Toast.makeText(this, getString(R.string.game_over), Toast.LENGTH_SHORT).show()
        keyboard_input.removeTextChangedListener(game)
    }

    fun pauseGame(){
        chronometer.stop()
        pause_button.visibility =  View.GONE
        pause_menu.visibility = View.VISIBLE
        keyboard_input.visibility = View.INVISIBLE
        game.pauseGame()
    }

    fun continueGame(){
        chronometer.start()
        pause_menu.visibility = View.GONE
        pause_button.visibility =  View.VISIBLE
        keyboard_input.visibility = View.VISIBLE
    }

    fun updateWordsTextViews(currentWord: String, nextWord: String){
        current_word.text = currentWord
        next_word.text = nextWord
    }

    fun updateStatsTextViews(wpm: Float, ksps: Float,kspc: Float, msdErrorRate:Float, correctWords: Int, failedWords: Int){
        wpm_stat.text = wpm.toString()
        ksps_stat.text = ksps.toString()
        kspc_stat.text = kspc.toString()
        msd_error_rate_stat.text = msdErrorRate.toString()
        correct_words.text = correctWords.toString()
        failed_words.text = failedWords.toString()
    }

    fun viewBenchmark(){
        val intent = Intent(this, BenchmarkActivity::class.java)
        intent.putExtra("BENCHMARK", game.benchmarker.benchmark)
        startActivity(intent)
    }
}