package com.github.c4ertoba3.todos.data.remote

import android.util.Log
import com.github.c4ertoba3.todos.todo.TodoItem
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.UUID
import java.util.concurrent.TimeUnit

class TodoRemoteDataSource(private val authToken: String) {

    private val gson = Gson()
    private val deviceId = UUID.randomUUID().toString()
    private var currentRevision: Long = 0L

    private val authInterceptor = okhttp3.Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $authToken")
            .build()
        chain.proceed(request)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    private val api: TodoApi = Retrofit.Builder()
        .baseUrl("https://hive.mrdekk.ru/todo/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(TodoApi::class.java)

    suspend fun fetchTodos(): List<TodoItem> {
        val response = api.getAll()
        currentRevision = response.revision
        Log.d("TODO_REMOTE", "Загружено с сервера, ревизия: $currentRevision")

        return response.objects.map { storageObj ->
            gson.fromJson(storageObj.payload, TodoItem::class.java)
        }
    }

    // --- Создание ---
    suspend fun pushTodo(item: TodoItem) {
        val storageObj = createStorageObject(item)
        try {
            val response = api.create(currentRevision, storageObj)
            currentRevision = response.revision
            Log.d("TODO_REMOTE", "Создано на сервере, новая ревизия: $currentRevision")
        } catch (e: HttpException) {
            if (e.code() == 400) {
                Log.e("TODO_REMOTE", "Конфликт ревизий при создании. Обновляем список...")
                fetchTodos()
            }
            throw e
        }
    }

    // --- Обновление ---
    suspend fun updateTodo(item: TodoItem) {
        val storageObj = createStorageObject(item)
        try {
            val response = api.update(item.uid, currentRevision, storageObj)
            currentRevision = response.revision
            Log.d("TODO_REMOTE", "Обновлено на сервере, новая ревизия: $currentRevision")
        } catch (e: HttpException) {
            if (e.code() == 400) fetchTodos()
            throw e
        }
    }

    // --- Удаление ---
    suspend fun deleteTodo(uid: String) {
        try {
            val response = api.delete(uid, currentRevision)
            currentRevision = response.revision
            Log.d("TODO_REMOTE", "Удалено на сервере, новая ревизия: $currentRevision")
        } catch (e: HttpException) {
            if (e.code() == 400) fetchTodos()
            throw e
        }
    }

    private fun createStorageObject(item: TodoItem): StorageObject {
        val now = System.currentTimeMillis() / 1000
        return StorageObject(
            id = item.uid,
            payload = gson.toJsonTree(item),
            createdAt = now,
            changedAt = now,
            lastUpdatedBy = deviceId
        )
    }
}