package com.github.c4ertoba3.todos.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE todos_new (
                uid TEXT PRIMARY KEY NOT NULL,
                text TEXT NOT NULL,
                importance INTEGER NOT NULL,
                color INTEGER NOT NULL,
                deadline INTEGER,
                isDone INTEGER NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            INSERT INTO todos_new (uid, text, importance, color, deadline, isDone)
            SELECT uid, text, 
                CASE importance 
                    WHEN 'LOW' THEN 0 
                    WHEN 'BASIC' THEN 1 
                    WHEN 'HIGH' THEN 2 
                    ELSE 1 
                END, 
                color, deadline, isDone 
            FROM todos
        """.trimIndent())

        db.execSQL("DROP TABLE todos")
        db.execSQL("ALTER TABLE todos_new RENAME TO todos")
    }
}