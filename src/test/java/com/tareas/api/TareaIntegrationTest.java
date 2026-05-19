package com.tareas.api;

import com.tareas.api.model.Tarea;
import com.tareas.api.repository.TareaRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TareaIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TareaRepository tareaRepository;

    @BeforeEach
    void limpiarBaseDeDatos() {
        tareaRepository.deleteAll();
    }

    @Test
    @Order(1)
    void crearTarea_debeRetornar201YTareaCreada() {
        Tarea tarea = new Tarea("Comprar leche", "Ir al supermercado");

        ResponseEntity<Tarea> response = restTemplate.postForEntity(
                "/api/tareas", tarea, Tarea.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getTitulo()).isEqualTo("Comprar leche");
        assertThat(response.getBody().getDescripcion()).isEqualTo("Ir al supermercado");
        assertThat(response.getBody().isCompletada()).isFalse();
        assertThat(response.getBody().getFechaCreacion()).isNotNull();
    }

    @Test
    @Order(2)
    void crearTarea_sinTitulo_debeRetornar400() {
        Tarea tarea = new Tarea();
        tarea.setDescripcion("Sin título");

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/tareas", tarea, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Order(3)
    void listarTodas_debeRetornarListaDeTareas() {
        tareaRepository.save(new Tarea("Tarea 1", "Desc 1"));
        tareaRepository.save(new Tarea("Tarea 2", "Desc 2"));

        ResponseEntity<Tarea[]> response = restTemplate.getForEntity(
                "/api/tareas", Tarea[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    @Order(4)
    void obtenerPorId_existente_debeRetornarTarea() {
        Tarea guardada = tareaRepository.save(new Tarea("Tarea test", "Descripción test"));

        ResponseEntity<Tarea> response = restTemplate.getForEntity(
                "/api/tareas/" + guardada.getId(), Tarea.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitulo()).isEqualTo("Tarea test");
    }

    @Test
    @Order(5)
    void obtenerPorId_noExistente_debeRetornar404() {
        ResponseEntity<Tarea> response = restTemplate.getForEntity(
                "/api/tareas/9999", Tarea.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(6)
    void actualizarTarea_existente_debeRetornarTareaActualizada() {
        Tarea guardada = tareaRepository.save(new Tarea("Original", "Desc original"));

        Tarea actualizada = new Tarea("Modificada", "Desc modificada");
        actualizada.setCompletada(true);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Tarea> request = new HttpEntity<>(actualizada, headers);

        ResponseEntity<Tarea> response = restTemplate.exchange(
                "/api/tareas/" + guardada.getId(), HttpMethod.PUT, request, Tarea.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitulo()).isEqualTo("Modificada");
        assertThat(response.getBody().getDescripcion()).isEqualTo("Desc modificada");
        assertThat(response.getBody().isCompletada()).isTrue();
    }

    @Test
    @Order(7)
    void actualizarTarea_noExistente_debeRetornar404() {
        Tarea actualizada = new Tarea("No existe", "Desc");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Tarea> request = new HttpEntity<>(actualizada, headers);

        ResponseEntity<Tarea> response = restTemplate.exchange(
                "/api/tareas/9999", HttpMethod.PUT, request, Tarea.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(8)
    void eliminarTarea_existente_debeRetornar204() {
        Tarea guardada = tareaRepository.save(new Tarea("A eliminar", "Desc"));

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/tareas/" + guardada.getId(), HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(tareaRepository.findById(guardada.getId())).isEmpty();
    }

    @Test
    @Order(9)
    void eliminarTarea_noExistente_debeRetornar404() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/tareas/9999", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(10)
    void filtrarPorEstado_debeRetornarSoloCompletadas() {
        Tarea pendiente = new Tarea("Pendiente", "Desc");
        tareaRepository.save(pendiente);

        Tarea completada = new Tarea("Completada", "Desc");
        completada.setCompletada(true);
        tareaRepository.save(completada);

        ResponseEntity<Tarea[]> response = restTemplate.getForEntity(
                "/api/tareas/estado/true", Tarea[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getTitulo()).isEqualTo("Completada");
    }

    @Test
    @Order(11)
    void filtrarPorEstado_debeRetornarSoloPendientes() {
        Tarea pendiente = new Tarea("Pendiente", "Desc");
        tareaRepository.save(pendiente);

        Tarea completada = new Tarea("Completada", "Desc");
        completada.setCompletada(true);
        tareaRepository.save(completada);

        ResponseEntity<Tarea[]> response = restTemplate.getForEntity(
                "/api/tareas/estado/false", Tarea[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getTitulo()).isEqualTo("Pendiente");
    }

    @Test
    @Order(12)
    void crudCompleto_flujoIntegrado() {
        // Crear
        Tarea nueva = new Tarea("Tarea CRUD", "Test flujo completo");
        ResponseEntity<Tarea> createResponse = restTemplate.postForEntity(
                "/api/tareas", nueva, Tarea.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long id = createResponse.getBody().getId();

        // Leer
        ResponseEntity<Tarea> readResponse = restTemplate.getForEntity(
                "/api/tareas/" + id, Tarea.class);
        assertThat(readResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(readResponse.getBody().getTitulo()).isEqualTo("Tarea CRUD");

        // Actualizar
        Tarea modificada = new Tarea("Tarea CRUD Actualizada", "Actualizado");
        modificada.setCompletada(true);
        HttpEntity<Tarea> updateRequest = new HttpEntity<>(modificada);
        ResponseEntity<Tarea> updateResponse = restTemplate.exchange(
                "/api/tareas/" + id, HttpMethod.PUT, updateRequest, Tarea.class);
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody().isCompletada()).isTrue();

        // Eliminar
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/tareas/" + id, HttpMethod.DELETE, null, Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Verificar eliminación
        ResponseEntity<Tarea> verifyResponse = restTemplate.getForEntity(
                "/api/tareas/" + id, Tarea.class);
        assertThat(verifyResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
