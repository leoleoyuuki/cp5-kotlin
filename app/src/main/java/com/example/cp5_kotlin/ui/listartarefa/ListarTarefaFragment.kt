package com.example.cp5_kotlin.ui.listartarefa

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cp5_kotlin.R
import com.example.cp5_kotlin.bancodedados.DatabaseHelper
import com.example.cp5_kotlin.model.Tarefa

class ListarTarefaFragment : Fragment() {

    private lateinit var listViewTarefas: ListView
    private lateinit var btnAtualizar: Button
    private lateinit var bancoDados: DatabaseHelper
    private var listaTarefas = mutableListOf<Tarefa>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_listar_tarefa, container, false)

        listViewTarefas = view.findViewById(R.id.listViewTarefas)
        btnAtualizar = view.findViewById(R.id.btnAtualizar)

        bancoDados = DatabaseHelper(requireContext())
        listarTarefas()

        btnAtualizar.setOnClickListener {
            listarTarefas()
        }

        return view
    }

    private fun listarTarefas() {
        listaTarefas.clear()
        val cursor = bancoDados.readableDatabase.query(
            DatabaseHelper.TABELA_TAREFAS,
            null,
            null,
            null,
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_TAREFA))
            val titulo = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TITULO))
            val data = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DATA_TAREFA))
            val descricao = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DESCRICAO))
            val local = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LOCAL_TAREFA))
            val hora = cursor.getString(cursor.getColumnIndex(DatabaseHelper.HORA_TAREFA))

            listaTarefas.add(Tarefa(id, titulo, data, descricao, local, hora))
        }

        cursor.close()

        val adapter = ArrayAdapter(requireContext(), R.layout.item_tarefa, listaTarefas)
        listViewTarefas.adapter = adapter

        listViewTarefas.setOnItemClickListener { _, view, position, _ ->
            // Exibe os botões de editar e deletar
            val tarefa = listaTarefas[position]
            view.findViewById<Button>(R.id.btnEditar).setOnClickListener {
                // Implementar a lógica de edição aqui
                Toast.makeText(requireContext(), "Editar: ${tarefa.titulo}", Toast.LENGTH_SHORT).show()
            }

            view.findViewById<Button>(R.id.btnDeletar).setOnClickListener {
                // Implementar a lógica de deleção aqui
                deletarTarefa(tarefa.id)
            }
        }
    }

    private fun deletarTarefa(id: Int) {
        bancoDados.writableDatabase.delete(DatabaseHelper.TABELA_TAREFAS, "${DatabaseHelper.ID_TAREFA} = ?", arrayOf(id.toString()))
        Toast.makeText(requireContext(), "Tarefa deletada", Toast.LENGTH_SHORT).show()
        listarTarefas() // Atualiza a lista
    }
}
