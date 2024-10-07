package com.example.cp5_kotlin.ui.listartarefa

import android.app.AlertDialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.cp5_kotlin.R
import com.example.cp5_kotlin.bancodedados.DatabaseHelper
import com.example.cp5_kotlin.model.Tarefa
import com.google.android.material.button.MaterialButton

class ListarTarefaFragment : Fragment() {

    private lateinit var listViewTarefas: ListView
    private lateinit var dbHelper: DatabaseHelper
    private val listaTarefas: MutableList<Tarefa> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_listar_tarefa, container, false)

        // Inicializando o ListView e o DatabaseHelper
        listViewTarefas = view.findViewById(R.id.listViewTarefas)
        dbHelper = DatabaseHelper(requireContext())

        // Chamando o método para listar tarefas
        listarTarefas()

        return view
    }

    private fun listarTarefas() {
        listaTarefas.clear()

        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABELA_TAREFAS,
            null, // seleciona todas as colunas
            null, // sem cláusula WHERE
            null,
            null,
            null,
            null
        )

        Log.d("ListarTarefas", "Cursor size: ${cursor.count}")

        if (cursor.moveToFirst()) {
            do {
                try {
                    // Verificando se as colunas estão corretas
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.ID_TAREFA))
                    val titulo = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TITULO))
                    val data = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.DATA_TAREFA))
                    val descricao = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.DESCRICAO))
                    val local = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.LOCAL_TAREFA))
                    val hora = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.HORA_TAREFA))

                    // Log para cada tarefa encontrada
                    Log.d("ListarTarefas", "Tarefa encontrada: ID: $id, Título: $titulo")

                    // Adicionando a tarefa na lista
                    listaTarefas.add(Tarefa(id, titulo, data, descricao, local, hora))

                } catch (e: Exception) {
                    Log.e("ListarTarefas", "Erro ao obter tarefa: ${e.message}")
                }
            } while (cursor.moveToNext())
        } else {
            Log.d("ListarTarefas", "Nenhuma tarefa encontrada")
        }

        cursor.close()
        db.close()

        // Verificando se a lista está populada corretamente
        Log.d("ListarTarefas", "Total de tarefas listadas: ${listaTarefas.size}")

        // Exibir tarefas no ListView
        val adapter = TarefaAdapter(requireContext(), listaTarefas, ::onEditarClick, ::onDeletarClick)
        listViewTarefas.adapter = adapter
    }

    // Função de callback para editar a tarefa
    private fun onEditarClick(tarefa: Tarefa) {
        Log.d("ListarTarefas", "Editar tarefa ID: ${tarefa.id}")
        // Implemente a lógica para editar a tarefa
    }

    // Função de callback para deletar a tarefa
    private fun onDeletarClick(tarefa: Tarefa) {
        Log.d("ListarTarefas", "Deletar tarefa ID: ${tarefa.id}")
        val db = dbHelper.writableDatabase
        val whereClause = "${DatabaseHelper.ID_TAREFA} = ?"
        val whereArgs = arrayOf(tarefa.id.toString())

        val deletedRows = db.delete(DatabaseHelper.TABELA_TAREFAS, whereClause, whereArgs)
        db.close()

        if (deletedRows > 0) {
            Log.d("ListarTarefas", "Tarefa deletada com sucesso")
            listarTarefas() // Atualiza a lista após a exclusão
        } else {
            Log.e("ListarTarefas", "Erro ao deletar tarefa")
        }
    }

    private fun showEditTaskDialog(tarefa: Tarefa) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Editar Tarefa")

        // Configurando o EditText
        val input = EditText(requireContext())
        input.setText(tarefa.titulo) // Define o texto atual da tarefa
        builder.setView(input)

        builder.setPositiveButton("Salvar") { dialog, _ ->
            val novoTitulo = input.text.toString()

            if (novoTitulo.isNotEmpty()) {
                tarefa.titulo = novoTitulo // Atualiza o título da tarefa
                atualizarTarefaNoBanco(tarefa) // Chama a função de atualização no banco
                listarTarefas() // Atualiza a lista após a edição
            }

            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }


    private fun atualizarTarefaNoBanco(tarefa: Tarefa) {
        val db = DatabaseHelper(requireContext()).writableDatabase
        val contentValues = ContentValues().apply {
            put(DatabaseHelper.TITULO, tarefa.titulo)
            put(DatabaseHelper.DATA_TAREFA, tarefa.data)
            put(DatabaseHelper.DESCRICAO, tarefa.descricao)
            put(DatabaseHelper.LOCAL_TAREFA, tarefa.local)
            put(DatabaseHelper.HORA_TAREFA, tarefa.hora)
        }

        db.update(DatabaseHelper.TABELA_TAREFAS, contentValues, "${DatabaseHelper.ID_TAREFA} = ?", arrayOf(tarefa.id.toString()))
        db.close()
    }

}
