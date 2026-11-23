package mx.udg.cucea.andynails.data.network

import mx.udg.cucea.andynails.data.model.*
import retrofit2.http.*

interface ApiService {
    // Clientes
    @GET("clientes/")
    suspend fun getClientes(@Query("skip") skip: Int = 0, @Query("limit") limit: Int = 100): List<Cliente>

    @POST("clientes/")
    suspend fun createCliente(@Body cliente: ClienteCreate): Cliente

    @GET("clientes/{id}")
    suspend fun getCliente(@Path("id") id: Int): Cliente

    @PUT("clientes/{id}")
    suspend fun updateCliente(@Path("id") id: Int, @Body cliente: ClienteCreate): Cliente

    @DELETE("clientes/{id}")
    suspend fun deleteCliente(@Path("id") id: Int): Map<String, String>

    // Empleados
    @GET("empleados/")
    suspend fun getEmpleados(@Query("skip") skip: Int = 0, @Query("limit") limit: Int = 100): List<Empleado>

    @POST("empleados/")
    suspend fun createEmpleado(@Body empleado: EmpleadoCreate): Empleado

    @GET("empleados/{id}")
    suspend fun getEmpleado(@Path("id") id: Int): Empleado

    @PUT("empleados/{id}")
    suspend fun updateEmpleado(@Path("id") id: Int, @Body empleado: EmpleadoCreate): Empleado

    @DELETE("empleados/{id}")
    suspend fun deleteEmpleado(@Path("id") id: Int): Map<String, String>

    // Citas
    @GET("citas/")
    suspend fun getCitas(@Query("skip") skip: Int = 0, @Query("limit") limit: Int = 100): List<Cita>

    @POST("citas/")
    suspend fun createCita(@Body cita: CitaCreate): Cita

    @GET("citas/{id}")
    suspend fun getCita(@Path("id") id: Int): Cita

    @PUT("citas/{id}")
    suspend fun updateCita(@Path("id") id: Int, @Body cita: CitaCreate): Cita

    @DELETE("citas/{id}")
    suspend fun deleteCita(@Path("id") id: Int): Map<String, String>

    // Servicios
    @GET("servicios/")
    suspend fun getServicios(@Query("skip") skip: Int = 0, @Query("limit") limit: Int = 100): List<Servicio>

    @POST("servicios/")
    suspend fun createServicio(@Body servicio: ServicioCreate): Servicio

    @GET("servicios/{id}")
    suspend fun getServicio(@Path("id") id: Int): Servicio

    @PUT("servicios/{id}")
    suspend fun updateServicio(@Path("id") id: Int, @Body servicio: ServicioCreate): Servicio

    @DELETE("servicios/{id}")
    suspend fun deleteServicio(@Path("id") id: Int): Map<String, String>

    // Puestos
    @GET("puestos/")
    suspend fun getPuestos(@Query("skip") skip: Int = 0, @Query("limit") limit: Int = 100): List<Puesto>

    @POST("puestos/")
    suspend fun createPuesto(@Body puesto: PuestoCreate): Puesto

    @GET("puestos/{id}")
    suspend fun getPuesto(@Path("id") id: Int): Puesto

    @PUT("puestos/{id}")
    suspend fun updatePuesto(@Path("id") id: Int, @Body puesto: PuestoCreate): Puesto

    @DELETE("puestos/{id}")
    suspend fun deletePuesto(@Path("id") id: Int): Map<String, String>

    // Especialidades
    @GET("especialidades/")
    suspend fun getEspecialidades(@Query("skip") skip: Int = 0, @Query("limit") limit: Int = 100): List<Especialidad>

    @POST("especialidades/")
    suspend fun createEspecialidad(@Body especialidad: EspecialidadCreate): Especialidad

    @GET("especialidades/{id}")
    suspend fun getEspecialidad(@Path("id") id: Int): Especialidad

    @PUT("especialidades/{id}")
    suspend fun updateEspecialidad(@Path("id") id: Int, @Body especialidad: EspecialidadCreate): Especialidad

    @DELETE("especialidades/{id}")
    suspend fun deleteEspecialidad(@Path("id") id: Int): Map<String, String>

    // Municipios
    @GET("municipios/")
    suspend fun getMunicipios(@Query("skip") skip: Int = 0, @Query("limit") limit: Int = 100): List<Municipio>

    @POST("municipios/")
    suspend fun createMunicipio(@Body municipio: MunicipioCreate): Municipio

    @GET("municipios/{id}")
    suspend fun getMunicipio(@Path("id") id: Int): Municipio

    @PUT("municipios/{id}")
    suspend fun updateMunicipio(@Path("id") id: Int, @Body municipio: MunicipioCreate): Municipio

    @DELETE("municipios/{id}")
    suspend fun deleteMunicipio(@Path("id") id: Int): Map<String, String>

    // Colonias
    @GET("colonias/")
    suspend fun getColonias(@Query("skip") skip: Int = 0, @Query("limit") limit: Int = 100): List<Colonia>

    @POST("colonias/")
    suspend fun createColonia(@Body colonia: ColoniaCreate): Colonia

    @GET("colonias/{id}")
    suspend fun getColonia(@Path("id") id: Int): Colonia

    @PUT("colonias/{id}")
    suspend fun updateColonia(@Path("id") id: Int, @Body colonia: ColoniaCreate): Colonia

    @DELETE("colonias/{id}")
    suspend fun deleteColonia(@Path("id") id: Int): Map<String, String>
}
