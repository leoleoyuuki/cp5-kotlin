package com.example.cp5_kotlin.bancodedados

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, "todolist", null, 1
) {
    companion object {
        const val TABELA_TAREFAS = "tarefas"
        const val ID_TAREFA = "id_tarefa"
        const val TITULO = "titulo"
        const val DATA_TAREFA = "dt_tarefa"
        const val DESCRICAO = "descricao"
        const val LOCAL_TAREFA = "local_tarefa"
        const val HORA_TAREFA = "hora_tarefa" // Corrigido para HORA_TAREFA
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = """
            CREATE TABLE IF NOT EXISTS $TABELA_TAREFAS (
                $ID_TAREFA INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                $TITULO VARCHAR(100),
                $DATA_TAREFA TEXT,
                $DESCRICAO TEXT,
                $LOCAL_TAREFA TEXT,
                $HORA_TAREFA TEXT
            );
        """.trimIndent()

        try {
            db?.execSQL(sql)
            Log.i("db_info", "Tabela criada com sucesso")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("db_info", "Erro ao criar tabela: ${e.message}")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABELA_TAREFAS")
        onCreate(db)
    }
}
