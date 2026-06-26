package com.tareas.api;

import com.tareas.api.model.Tarea;
import com.tareas.api.repository.TareaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Seeds the database with initial mock task data on application startup.
 * Runs for all profiles (dev and prod). Insertion is idempotent:
 * data is only inserted when the table is empty.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private final TareaRepository tareaRepository;

    public DataSeeder(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    @Override
    public void run(String... args) {
        if (tareaRepository.count() == 0) {
            logger.info("Seeding initial task data...");

            Tarea t1 = new Tarea("Configurar entorno de desarrollo",
                    "Instalar JDK, Maven, Docker y configurar variables de entorno");
            t1.setCompletada(true);

            Tarea t2 = new Tarea("Implementar endpoint GET /tareas",
                    "Crear controller, service y repository para listar todas las tareas");
            t2.setCompletada(true);

            Tarea t3 = new Tarea("Agregar validaciones al modelo",
                    "Usar @NotBlank y @Size en los campos del modelo Tarea");

            Tarea t4 = new Tarea("Escribir pruebas de integración",
                    "Cubrir endpoints CRUD con MockMvc o TestRestTemplate");

            Tarea t5 = new Tarea("Documentar la API con Postman",
                    "Exportar colección Postman con todos los endpoints y ejemplos");

            tareaRepository.saveAll(List.of(t1, t2, t3, t4, t5));
            logger.info("Seeded {} tasks successfully.", tareaRepository.count());
        } else {
            logger.info("Database already contains data. Skipping seed.");
        }
    }
}
