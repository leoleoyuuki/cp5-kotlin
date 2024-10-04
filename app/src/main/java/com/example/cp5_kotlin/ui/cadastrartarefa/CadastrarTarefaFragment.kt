package com.example.cp5_kotlin.ui.cadastrartarefa

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.cp5_kotlin.R
import com.example.cp5_kotlin.bancodedados.DatabaseHelper



class CadastrarTarefaFragment : Fragment() {

    // Instancia o banco de dados usando requireContext()
    private val bancoDados by lazy {
        DatabaseHelper(requireContext())
    }

    private lateinit var editTitulo: EditText
    private lateinit var editDescricao: EditText
    private lateinit var editData: EditText
    private lateinit var editHora: EditText
    private lateinit var editLocal: EditText
    private lateinit var btnSalvar: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cadastrar_tarefa, container, false)

        // Inicializando elementos do layout
        editTitulo = view.findViewById(R.id.editTitulo)
        editDescricao = view.findViewById(R.id.editDescricao)
        editData = view.findViewById(R.id.editData)
        editHora = view.findViewById(R.id.editHora)
        editLocal = view.findViewById(R.id.editLocal)
        btnSalvar = view.findViewById(R.id.btnSalvar)

        // Ação do botão salvar
        btnSalvar.setOnClickListener {
            salvarTarefa()
        }

        return view
    }

    private fun salvarTarefa() {
        val titulo = editTitulo.text.toString()
        val descricao = editDescricao.text.toString()
        val data = editData.text.toString()
        val hora = editHora.text.toString()
        val local = editLocal.text.toString()

        try {
            val sql = "INSERT INTO tarefas VALUES(null,'$titulo','$data','$hora','$descricao','$local')"
            bancoDados.writableDatabase.execSQL(sql)
            Log.i("db_info", "Tarefa salva com sucesso!")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("db_info", "Erro ao salvar a tarefa")
        }
    }
}