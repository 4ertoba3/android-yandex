package com.github.c4ertoba3.todos.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos")
    fun observeAll(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE uid = :uid")
    suspend fun getById(uid: String): TodoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(todos: List<TodoEntity>)

    @Delete
    suspend fun delete(todo: TodoEntity)

    @Query("DELETE FROM todos WHERE uid = :uid")
    suspend fun deleteById(uid: String)

    @Query("DELETE FROM todos")
    suspend fun clearAll()
}