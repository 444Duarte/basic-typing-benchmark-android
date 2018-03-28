package com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.model

import android.arch.persistence.room.*
import java.io.Serializable


@Entity(tableName = "options")
class OptionsModel(
        var typeGame: TypeGame,
        var autoCorrect: Boolean,
        var skipOnFail: Boolean,
        var source: Source)
    : Serializable{

    var finishMark: Int = 0

    enum class TypeGame{
        TIME, NUM_WORDS, NUM_ERRORS, NUM_CORRECT_WORDS, NO_END
    }


    enum class Source{
        TEXT, TWELVE_DICTS
    }

    override fun toString(): String {
        return "OptionsModel(typeGame=$typeGame, autoCorrect=$autoCorrect, skipOnFail=$skipOnFail, source=$source, finishMark=$finishMark)"
    }

}