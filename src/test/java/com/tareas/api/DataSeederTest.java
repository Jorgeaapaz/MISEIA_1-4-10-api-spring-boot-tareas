package com.tareas.api;

import com.tareas.api.model.Tarea;
import com.tareas.api.repository.TareaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests that verify the DataSeeder populates the database on startup.
 * Uses a dedicated Spring context (DirtiesContext) so the seeder always runs fresh,
 * independent of other test classes that may clear the database.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class DataSeederTest {

    @Autowired
    private TareaRepository tareaRepository;

    @Test
    void databaseShouldBeSeededOnStartup() {
        long count = tareaRepository.count();
        assertThat(count).isGreaterThanOrEqualTo(5);
    }

    @Test
    void atLeastOneTaskShouldBeCompleted() {
        List<Tarea> completed = tareaRepository.findAll()
                .stream()
                .filter(Tarea::isCompletada)
                .toList();
        assertThat(completed).isNotEmpty();
    }
}
