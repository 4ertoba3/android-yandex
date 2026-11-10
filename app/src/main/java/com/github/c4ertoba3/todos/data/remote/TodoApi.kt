package com.github.c4ertoba3.todos.data.remote

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import retrofit2.http.*

data class StorageObject(
    @SerializedName("id") val id: String,
    @SerializedName("payload") val payload: JsonElement,
    @SerializedName("created_at") val createdAt: Long,
    @SerializedName("changed_at") val changedAt: Long,
    @SerializedName("last_updated_by") val lastUpdatedBy: String,
)

data class ObjectsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("objects") val objects: List<StorageObject>,
    @SerializedName("revision") val revision: Long,
)

data class ObjectResponse(
    @SerializedName("status") val status: String,
    @SerializedName("object") val item: StorageObject,
    @SerializedName("revision") val revision: Long,
)

data class PatchResultItem(
    @SerializedName("id") val id: String,
    @SerializedName("action") val action: String,
    @SerializedName("object") val item: StorageObject?
)

data class PatchResponse(
    @SerializedName("status") val status: String,
    @SerializedName("results") val results: List<PatchResultItem>,
    @SerializedName("revision") val revision: Long
)

data class PatchRequest(
    @SerializedName("objects") val objects: List<StorageObject>
)

interface TodoApi {
    @GET("objects")
    suspend fun getAll(): ObjectsResponse

    @POST("objects")
    suspend fun create(
        @Header("X-Last-Known-Revision") revision: Long,
        @Body item: StorageObject,
    ): ObjectResponse

    @PUT("objects/{id}")
    suspend fun update(
        @Path("id") id: String,
        @Header("X-Last-Known-Revision") revision: Long,
        @Body item: StorageObject,
    ): ObjectResponse

    @DELETE("objects/{id}")
    suspend fun delete(
        @Path("id") id: String,
        @Header("X-Last-Known-Revision") revision: Long,
    ): ObjectResponse

    @PATCH("objects")
    suspend fun sync(
        @Header("X-Last-Known-Revision") revision: Long,
        @Body request: PatchRequest
    ): PatchResponse
}