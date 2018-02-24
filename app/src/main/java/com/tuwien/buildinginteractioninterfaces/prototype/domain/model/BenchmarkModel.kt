package com.tuwien.buildinginteractioninterfaces.prototype.domain.model

import android.arch.persistence.room.*
import com.tuwien.buildinginteractioninterfaces.prototype.data.room.CustomConverters
import com.tuwien.buildinginteractioninterfaces.prototype.data.room.OptionsConverters
import com.tuwien.buildinginteractioninterfaces.prototype.data.room.StringBufferConverters
import com.tuwien.buildinginteractioninterfaces.prototype.util.Benchmarks
import java.io.Serializable
import java.util.*

@Entity(tableName = "benchmark")
class BenchmarkModel() : Serializable {

    @TypeConverters(OptionsConverters::class)
    @ColumnInfo(name = "options")
    lateinit var options: OptionsModel

    @ColumnInfo(name = "keyboard-app")
    lateinit var keyboardApp: String

    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0

    @ColumnInfo(name = "correct_chars")
    var correctChars: Int = 0
        set(value) {
            field = value
            updateKeystrokesPerChar()
            updateCharsPerSecond()
        }

    @ColumnInfo(name = "correct_words")
    var correctWords: Int = 0
        set(value) {
            field = value
            updateWordsPerMinute()
            updateWordsPerSec()
        }
    @ColumnInfo(name = "errors")
    var errors: Int = 0

    @ColumnInfo(name = "total_words")
    var totalWords: Int = 0

    @ColumnInfo(name = "time_elapsed")
    var timeElapsed:Long = 0 // In miliseconds
        set(value) {
            field = value
            updateWordsPerMinute()
            updateKeystrokesPerSecond()
            updateCharsPerSecond()
            updateWordsPerSec()
        }

    @ColumnInfo(name = "backspace")
    var backspace: Int = 0

    @ColumnInfo(name = "keystrokes")
    var keystrokes: Int = 0
        set(value) {
            field = value
            updateKeystrokesPerSecond()
            updateKeystrokesPerChar()
        }
    @ColumnInfo(name = "characters")
    var characters: Int = 0

    @TypeConverters(CustomConverters::class)
    @ColumnInfo(name = "timestamp")
    var timestamp: Date = Calendar.getInstance().getTime()

    @TypeConverters(StringBufferConverters::class)
    @ColumnInfo(name = "input_stream")
    var inputStream: StringBuffer = StringBuffer()

    @TypeConverters(StringBufferConverters::class)
    @ColumnInfo(name = "transcribed_string")
    var transcribedString: StringBuffer = StringBuffer()

    //Entry rates
    @ColumnInfo(name = "words_per_minute")
    var wordsPerMinute: Float = 0.0f

    @ColumnInfo(name = "keystrokes_per_second")
    var keystrokesPerSecond: Float = 0.0f

    //Error rates
    @ColumnInfo(name = "keystrokes_per_char")
    var keystrokesPerChar: Double = 0.0

    @ColumnInfo(name = "minimum_string_distance_error_rate")
    var minimumStringDistanceErrorRate: Double = 0.0

    //Custom entry rates
    @ColumnInfo(name = "chars_per_sec")
    var charsPerSec: Float = 0.0f

    @ColumnInfo(name = "words_per_sec")
    var wordsPerSec: Float = 0.0f

    fun updateKeystrokesPerChar(){
        keystrokesPerChar = if(keystrokes == 0 || correctChars == 0) 0.0 else Benchmarks.keystrokesPerChar(keystrokes,correctChars)
    }

    fun updateWordsPerMinute(){
        wordsPerMinute = if (timeElapsed == 0L || correctWords == 0) 0.0f else correctWords.toFloat()/ timeElapsed * 60 * 1000
    }

    fun updateKeystrokesPerSecond(){
        keystrokesPerSecond = if (timeElapsed == 0L || keystrokes == 0) 0.0f else (keystrokes.toFloat() - 1) / timeElapsed * 1000
    }

    fun updateCharsPerSecond(){
        charsPerSec = if(timeElapsed == 0L || correctChars == 0) 0.0f else correctChars.toFloat() / timeElapsed * 1000
    }

    fun updateWordsPerSec(){
        wordsPerSec = if(timeElapsed == 0L || correctChars == 0) 0.0f else correctWords.toFloat() / timeElapsed * 1000
    }

    fun addToTranscribedString(word: String){
        transcribedString.append(word + "\n")
    }

    fun addToInputStream(word: String){
        inputStream.append(word + "\n")
    }

    override fun toString(): String {
        return "BenchmarkModel(" +
                "options=$options,\n" +
                "timestamp=$timestamp,\n" +
                "keyboardApp=$keyboardApp,\n" +
                "correctChars=$correctChars,\n" +
                "correctWords=$correctWords,\n" +
                "errors=$errors,\n" +
                "charsPerSec=$charsPerSec,\n" +
                "wordsPerSec=$wordsPerSec,\n" +
                "totalWords=$totalWords,\n" +
                "timeElapsed=$timeElapsed,\n" +
                "backspace=$backspace,\n" +
                "keystrokes=$keystrokes,\n" +
                "characters=$characters,\n" +
                "wordsPerMinute=$wordsPerMinute,\n" +
                "wordsPerSec=$wordsPerSec,\n" +
                "keystrokesPerSecond=$keystrokesPerSecond,\n" +
                "keystrokesPerChar=$keystrokesPerChar,\n" +
                "minimumStringDistanceErrorRate=$minimumStringDistanceErrorRate,\n" +
                "inputString=$inputStream,\n" +
                "transcribedString=$transcribedString,\n" +
                ")"
    }


}