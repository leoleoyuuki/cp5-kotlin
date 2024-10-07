package com.example.cp5_kotlin.ui.listartarefa

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.cp5_kotlin.R
import com.example.cp5_kotlin.model.Tarefa
import com.google.android.material.button.MaterialButton

class TarefaAdapter(
    context: Context,
    private val tarefas: List<Tarefa>,
    private val onEditarClick: (Tarefa) -> Unit,
    private val onDeletarClick: (Tarefa) -> Unit
) : ArrayAdapter<Tarefa>(context, 0, tarefas) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_tarefa, parent, false)

        val tarefa = tarefas[position]

        // Referenciando as views do layout
        val tituloTarefa = view.findViewById<TextView>(R.id.tituloTarefa)
        val botaoEditar = view.findViewById<MaterialButton>(R.id.botaoEditar)
        val botaoDeletar = view.findViewById<MaterialButton>(R.id.botaoDeletar)

        // Definindo os valores
        tituloTarefa.text = tarefa.titulo

        // Ações dos botões
        botaoEditar.setOnClickListener {
            onEditarClick(tarefa)
        }

        botaoDeletar.setOnClickListener {
            onDeletarClick(tarefa)
        }

        return view
    }
}
