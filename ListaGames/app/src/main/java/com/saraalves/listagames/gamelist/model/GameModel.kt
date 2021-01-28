package com.saraalves.listagames.gamelist.model

data class GameModel(
    val nome: String = "",
    val dataLancamento: String? = "",
    val descricao: String = "",
    val imgUrl: String = ""
)