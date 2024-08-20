package com.glycin.ragvillage.ai

enum class QuestionType {
    CHAT,
    SHOPPING,
    SHOPPING_PAINTING,
    HISTORY;

    companion object {
        fun fromValueOrDefault(value: String): QuestionType {
            return entries.find { it.name == value.uppercase() } ?: CHAT
        }
    }
}