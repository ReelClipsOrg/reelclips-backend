package org.arquitectura.reelclipsv2.shared.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.UUID;

@Service
public class SupabaseStorageService {

    private final WebClient webClient;

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${supabase.bucket.reels}")
    private String bucketReels;

    @Value("${supabase.bucket.imagenes}")
    private String bucketImagenes;

    public SupabaseStorageService() {
        this.webClient = WebClient.builder().build();
    }

    // Subir video de reel
    public String subirVideo(MultipartFile archivo) {
        return subir(archivo, bucketReels, "video/mp4");
    }

    // Subir imagen de perfil
    public String subirImagenPerfil(MultipartFile archivo) {
        return subir(archivo, bucketImagenes, archivo.getContentType());
    }

    // Eliminar archivo
    public void eliminar(String url, String bucket) {
        String nombreArchivo = extraerNombreArchivo(url);
        webClient.delete()
                .uri(supabaseUrl + "/storage/v1/object/" + bucket + "/" + nombreArchivo)
                .header("apikey", supabaseKey)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + supabaseKey)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    private String subir(MultipartFile archivo, String bucket, String contentType) {
        try {
            String nombreArchivo = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
            byte[] bytes = archivo.getBytes();

            webClient.post()
                    .uri(supabaseUrl + "/storage/v1/object/" + bucket + "/" + nombreArchivo)
                    .header("apikey", supabaseKey)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + supabaseKey)
                    .header(HttpHeaders.CONTENT_TYPE,
                            contentType != null ? contentType : "application/octet-stream")
                    .header("x-upsert", "true")
                    .bodyValue(bytes)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class)
                                    .flatMap(body -> reactor.core.publisher.Mono.error(
                                            new RuntimeException("Supabase error: " + body))))
                    .toBodilessEntity()
                    .block();

            return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + nombreArchivo;

        } catch (Exception e) {
            throw new RuntimeException("Error al subir archivo a Supabase: " + e.getMessage());
        }
    }

    private String extraerNombreArchivo(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
