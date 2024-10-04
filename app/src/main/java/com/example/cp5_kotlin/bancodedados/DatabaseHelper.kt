package com.example.cp5_kotlin.bancodedados

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log



    class DatabaseHelper(context:Context) : SQLiteOpenHelper(
        //1.Context
        //2.Nome do banco de dados
        //3.CursorFactory
        //4.versão

        context,"todolist",null,1
    ) {
        companion object{
            const val TABELA_TAREFAS = "tarefas"
            const val ID_TAREFA = "id_tarefa"
            const val TITULO = "titulo"
            const val DATA_TAREFA = "dt_tarefa"
            const val DESCRICAO = "descricao"
            const val LOCAL_TAREFA = "local_tarefa"
            const val HORA_TAREA = "hora_tarefa"

        }
        override fun onCreate(db: SQLiteDatabase?) {
            //É executado um única vez, quando o app é instalado
            val sql = "CREATE TABLE IF NOT EXISTS $TABELA_TAREFAS(" +
                    "$ID_TAREFA INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "$TITULO VARCHAR(100)," +
                    "$DATA_TAREFA TEXT," +
                    "$DESCRICAO TEXT," +
                    "$LOCAL_TAREFA TEXT," +
                    "$HORA_TAREA TEXT" +
                    ");"

            try{
                db?.execSQL(sql)
                Log.i("db_info","Tabela criada com sucesso")
            }catch (e:Exception){
                e.printStackTrace()
                Log.i("db_info","Error ao criar tabela")
            }

        }

        override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
            //É executado quando há mudança de versão do banco
        }

    }
