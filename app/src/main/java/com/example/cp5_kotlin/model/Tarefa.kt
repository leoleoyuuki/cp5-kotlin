package com.example.cp5_kotlin.model

data class Tarefa(
    val id: Int,
    var titulo: String,
    val data: String,
    val descricao: String,
    val local: String,
    val hora: String
)
