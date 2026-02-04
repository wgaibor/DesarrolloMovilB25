package com.lemas.asistincialemas.api;

import com.lemas.asistincialemas.model.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    // Auth endpoints
    @POST("api/v1/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    // Estudiante endpoints
    @GET("api/v1/estudiantes/mi-qr")
    Call<QrResponse> obtenerMiQr();

    @GET("api/v1/estudiantes/mis-asistencias")
    Call<List<AsistenciaResponse>> obtenerMisAsistencias(
            @Query("estado") String estado,
            @Query("desde") String desde,
            @Query("hasta") String hasta
    );

    // Docente endpoints
    @GET("api/v1/docentes/mis-cursos")
    Call<List<CursoResponse>> obtenerMisCursos();

    @GET("api/v1/docentes/cursos/{cursoId}/estudiantes")
    Call<List<EstudianteResponse>> obtenerEstudiantesCurso(@Path("cursoId") Long cursoId);

    @POST("api/v1/docentes/asistencia")
    Call<AsistenciaResponse> registrarAsistencia(@Body RegistrarAsistenciaRequest request);

    @GET("api/v1/docentes/cursos/{cursoId}/asistencias")
    Call<List<AsistenciaResponse>> obtenerAsistenciasCurso(
            @Path("cursoId") Long cursoId,
            @Query("estado") String estado,
            @Query("fecha") String fecha
    );

    // Representante endpoints
    @GET("api/v1/representantes/mis-estudiantes")
    Call<List<EstudianteResponse>> obtenerMisEstudiantes();

    @GET("api/v1/representantes/estudiantes/{estudianteId}/asistencias")
    Call<List<AsistenciaResponse>> obtenerAsistenciasEstudiante(
            @Path("estudianteId") Long estudianteId,
            @Query("estado") String estado,
            @Query("desde") String desde,
            @Query("hasta") String hasta
    );
}
