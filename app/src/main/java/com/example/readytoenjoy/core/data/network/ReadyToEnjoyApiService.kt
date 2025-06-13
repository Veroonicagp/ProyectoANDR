/**
 * @file ReadyToEnjoyApiService.kt
 * @brief Servicio principal de API REST para la aplicación
 * @details Define todos los endpoints de la API de ReadyToEnjoy usando Retrofit
 * @author ReadyToEnjoy Team
 * @version 1.0
 */

package com.example.readytoenjoy.core.data.network

import com.example.readytoenjoy.core.data.network.activity.model.ActivityListRawResponse
import com.example.readytoenjoy.core.data.network.activity.model.ActivityRawResponse
import com.example.readytoenjoy.core.data.network.activity.model.ActivityRequest
import com.example.readytoenjoy.core.data.network.activity.model.CreatedMediaItemResponse
import com.example.readytoenjoy.core.data.network.adevn.model.AdvenListRawResponse
import com.example.readytoenjoy.core.data.network.adevn.model.AdvenRawResponse
import com.example.readytoenjoy.core.data.network.adevn.model.AdvenRequest
import com.example.readytoenjoy.core.data.network.adevn.model.UserRequest
import com.example.readytoenjoy.core.data.network.adevn.model.userResponseLR
import com.example.readytoenjoy.core.data.network.adevn.model.LoginRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * @interface ReadyToEnjoyApiService
 * @brief Interface principal del servicio de API REST
 * @details Define todos los endpoints disponibles en la API de ReadyToEnjoy.
 *          Utiliza Retrofit para manejar las peticiones HTTP y serialización JSON.
 * @api
 */
interface ReadyToEnjoyApiService {

    // ============================================
    // ENDPOINTS DE REGISTRO Y LOGIN
    // ============================================

    /**
     * @brief Registra un nuevo usuario en el sistema
     * @details Endpoint POST para crear una nueva cuenta de usuario
     * @param userRequest Objeto con datos de registro del usuario
     * @return Response<userResponseLR> Respuesta con datos del usuario registrado y token
     */
    @POST("auth/local/register")
    suspend fun register(@Body userRequest: UserRequest): Response<userResponseLR>

    /**
     * @brief Autentica un usuario en el sistema
     * @details Endpoint POST para login con credenciales
     * @param loginData Objeto con credenciales de login (email/username y password)
     * @return Response<userResponseLR> Respuesta con token JWT y datos del usuario
     */
    @POST("auth/local")
    suspend fun login(@Body loginData: LoginRequest): Response<userResponseLR>

    /**
     * @brief Elimina un usuario específico del sistema
     * @details Endpoint DELETE para eliminar una cuenta de usuario
     * @param userId ID del usuario a eliminar
     * @return Response<Void> Confirmación de eliminación
     */
    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") userId: String): Response<Void>

    // ============================================
    // ENDPOINTS DE AVENTUREROS
    // ============================================

    /**
     * @brief Registra un nuevo aventurero en el sistema
     * @details Endpoint POST para crear un perfil de aventurero
     * @param advenRequest Datos del aventurero a registrar
     * @return Response<AdvenRawResponse> Respuesta con el aventurero creado
     */
    @POST("adventurers")
    suspend fun registerAdven(@Body advenRequest: AdvenRequest): Response<AdvenRawResponse>

    /**
     * @brief Obtiene el aventurero asociado a un ID de usuario
     * @details Endpoint GET para buscar aventurero por ID de usuario usando filtros
     * @param userId ID del usuario para buscar su aventurero asociado
     * @return Response<AdvenListRawResponse> Lista con el aventurero del usuario
     */
    @GET("adventurers")
    suspend fun getAdvenByUserId(@Query("filters[user]") userId: String): Response<AdvenListRawResponse>

    /**
     * @brief Obtiene todos los aventureros del sistema
     * @details Endpoint GET que retorna la lista completa de aventureros con media
     * @param populate Campos a popular, por defecto "media" para incluir imágenes
     * @return Response<AdvenListRawResponse> Lista de todos los aventureros
     */
    @GET("adventurers")
    suspend fun getAllAdvensFromSercice(@Query("populate") populate: String = "media"): Response<AdvenListRawResponse>

    /**
     * @brief Obtiene información detallada de un aventurero específico
     * @details Endpoint GET para obtener datos completos de un aventurero por ID
     * @param id ID del aventurero a obtener
     * @param populate Campos a popular, por defecto "media" para incluir imágenes
     * @return Response<AdvenRawResponse> Datos detallados del aventurero
     */
    @GET("adventurers/{id}")
    suspend fun readOneAdvenFromService(
        @Path("id") id: String,
        @Query("populate") populate: String = "media"
    ): Response<AdvenRawResponse>

    /**
     * @brief Actualiza los datos de un aventurero existente
     * @details Endpoint PUT para modificar información de un aventurero
     * @param id ID del aventurero a actualizar
     * @param advenRequest Nuevos datos del aventurero
     * @return Response<AdvenRawResponse> Aventurero actualizado
     */
    @PUT("adventurers/{id}")
    suspend fun updateAdven(
        @Path("id") id: String,
        @Body advenRequest: AdvenRequest
    ): Response<AdvenRawResponse>

    /**
     * @brief Elimina un aventurero del sistema
     * @details Endpoint DELETE para eliminar un aventurero específico
     * @param id ID del aventurero a eliminar
     * @return Response<Void> Confirmación de eliminación
     */
    @DELETE("adventurers/{id}")
    suspend fun deleteAdven(@Path("id") id: String): Response<Void>

    // ============================================
    // ENDPOINTS DE ACTIVIDADES
    // ============================================

    /**
     * @brief Crea una nueva actividad en el sistema
     * @details Endpoint POST para crear una actividad
     * @param activity Datos de la nueva actividad a crear
     * @return Response<ActivityRawResponse> Actividad creada
     */
    @POST("activities")
    suspend fun cretaeActivities(@Body activity: ActivityRequest): Response<ActivityRawResponse>

    /**
     * @brief Obtiene las actividades de un aventurero específico
     * @details Endpoint GET para obtener actividades filtradas por ID de aventurero
     * @param advenId ID del aventurero para filtrar sus actividades
     * @param populate Campos a popular, por defecto "img" para incluir imágenes
     * @return Response<ActivityListRawResponse> Lista de actividades del aventurero
     */
    @GET("activities")
    suspend fun getAllMyActivitiesFromSercice(
        @Query("filters[advenId]") advenId: String,
        @Query("populate") populate: String = "img"
    ): Response<ActivityListRawResponse>

    /**
     * @brief Obtiene todas las actividades del sistema
     * @details Endpoint GET que retorna la lista completa de actividades
     * @param populate Campos a popular, por defecto "img" para incluir imágenes
     * @return Response<ActivityListRawResponse> Lista de todas las actividades
     */
    @GET("activities")
    suspend fun getAllActivitiesFromSercice(
        @Query("populate") populate: String = "img"
    ): Response<ActivityListRawResponse>

    /**
     * @brief Obtiene información detallada de una actividad específica
     * @details Endpoint GET para obtener datos completos de una actividad por ID
     * @param id ID de la actividad a obtener
     * @param populate Campos a popular, por defecto "img" para incluir imágenes
     * @return Response<ActivityRawResponse> Datos detallados de la actividad
     */
    @GET("activities/{id}")
    suspend fun readOneActFomService(
        @Path("id") id: String,
        @Query("populate") populate: String = "img"
    ): Response<ActivityRawResponse>

    /**
     * @brief Actualiza una actividad existente
     * @details Endpoint PUT para modificar los datos de una actividad
     * @param id ID de la actividad a actualizar
     * @param activityRequest Nuevos datos de la actividad
     * @return Response<ActivityRawResponse> Actividad actualizada
     */
    @PUT("activities/{id}")
    suspend fun updateActivity(
        @Path("id") id: String,
        @Body activityRequest: ActivityRequest
    ): Response<ActivityRawResponse>

    /**
     * @brief Elimina una actividad del sistema
     * @details Endpoint DELETE para eliminar una actividad específica
     * @param id ID de la actividad a eliminar
     * @return Response<Void> Confirmación de eliminación
     */
    @DELETE("activities/{id}")
    suspend fun deleteActivity(@Path("id") id: String): Response<Void>

    // ============================================
    // ENDPOINTS DE SUBIDA DE ARCHIVOS
    // ============================================

    /**
     * @brief Sube imágenes al servidor
     * @details Endpoint POST multipart para subir archivos de imagen al sistema.
     *          Utiliza PartMap para metadatos y MultipartBody.Part para el archivo.
     * @param partMap Map con metadatos del archivo (nombre, descripción, etc.)
     * @param files Archivo de imagen en formato MultipartBody.Part
     * @return Response<List<CreatedMediaItemResponse>> Lista de archivos subidos con sus URLs
     */
    @Multipart
    @POST("upload")
    suspend fun addImg(
        @PartMap partMap: MutableMap<String, RequestBody>,
        @Part files: MultipartBody.Part
    ): Response<List<CreatedMediaItemResponse>>
}