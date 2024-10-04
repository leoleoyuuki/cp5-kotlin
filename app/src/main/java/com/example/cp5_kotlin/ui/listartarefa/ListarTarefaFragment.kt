package com.example.cp5_kotlin.ui.listartarefa

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cp5_kotlin.R
import com.example.cp5_kotlin.bancodedados.DatabaseHelper
class ListarTarefaFragment : Fragment() {

    private lateinit var bancoDados: DatabaseHelper
    private lateinit var listView: ListView
    private lateinit var btnAtualizar: Button

    private val tarefas = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_listar_tarefa, container, false)

        bancoDados = DatabaseHelper(requireContext())
        listView = view.findViewById(R.id.listViewTarefas)
        btnAtualizar = view.findViewById(R.id.btnAtualizar)

        // Configurar o ListView
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, tarefas)
        listView.adapter = adapter

        // Atualizar a lista de tarefas
        atualizarLista()

        // Ação do botão atualizar
        btnAtualizar.setOnClickListener {
            atualizarLista()
        }

        listView.setOnItemClickListener { parent, view, position, id ->
            // Aqui você pode implementar a lógica para editar a tarefa
            val tarefa = tarefas[position]
            Toast.makeText(requireContext(), "Editar: $tarefa", Toast.LENGTH_SHORT).show()
        }

        listView.setOnItemLongClickListener { parent, view, position, id ->
            // Aqui você pode implementar a lógica para excluir a tarefa
            val tarefa = tarefas[position]
            excluirTarefa(tarefa)
            true
        }

        return view
    }

    private fun atualizarLista() {
        tarefas.clear() // Limpa a lista antes de atualizar
        try {
            val cursor = bancoDados.readableDatabase.rawQuery("SELECT * FROM ${DatabaseHelper.TABELA_TAREFAS}", null)
            val indiceTitulo = cursor.getColumnIndex(DatabaseHelper.TITULO)

            while (cursor.moveToNext()) {
                val titulo = cursor.getString(indiceTitulo)
                tarefas.add(titulo) // Adiciona o título à lista de tarefas
            }
            cursor.close()
            Log.i("db_info", "Lista atualizada com sucesso")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("db_info", "Erro ao atualizar lista")
        }
    }

    private fun excluirTarefa(titulo: String) {
        try {
            val sql = "DELETE FROM ${DatabaseHelper.TABELA_TAREFAS} WHERE ${DatabaseHelper.TITULO} = '$titulo'"
            bancoDados.writableDatabase.execSQL(sql)
            Log.i("db_info", "Tarefa excluída: $titulo")
            atualizarLista() // Atualiza a lista após a exclusão
            Toast.makeText(requireContext(), "Tarefa excluída: $titulo", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("db_info", "Erro ao excluir a tarefa")
        }
    }
}