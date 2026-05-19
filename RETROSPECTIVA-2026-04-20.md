# Retrospectiva de Sesion — 2026-04-20
### API REST Spring Boot - CRUD de Tareas

## Resumen / Overview
Se construyo desde cero una API REST con Spring Boot 3.2.5 para gestionar un CRUD completo de lista de tareas, respaldada por base de datos SQL (H2 en desarrollo/test, MySQL disponible para produccion). Se crearon 12 tests de integracion que cubren todos los endpoints y casos limite. **Todos los tests pasaron exitosamente (13/13, 0 fallos).**

## Proceso de instalacion / Installation

### Prerequisitos detectados en el sistema
- **Java:** JDK 17.0.16 (Oracle) ya instalado en `C:\Program Files\Java\jdk-17`
- **Maven:** No estaba instalado. Se descargo Maven 3.9.6 via wrapper script a `~/.m2/wrapper/dists/apache-maven-3.9.6/`

### Estructura del proyecto creada
```
api-spring-boot-tareas/
├── pom.xml
├── mvnw / mvnw.cmd              (Maven Wrapper)
├── .mvn/wrapper/
│   └── maven-wrapper.properties
├── src/main/java/com/tareas/api/
│   ├── ApiTareasApplication.java     (Main class)
│   ├── model/
│   │   └── Tarea.java                (Entidad JPA)
│   ├── repository/
│   │   └── TareaRepository.java      (Spring Data JPA)
│   ├── service/
│   │   └── TareaService.java         (Logica de negocio)
│   └── controller/
│       └── TareaController.java      (REST endpoints)
├── src/main/resources/
│   ├── application.properties
│   └── schema.sql
└── src/test/java/com/tareas/api/
    ├── ApiTareasApplicationTests.java
    └── TareaIntegrationTest.java     (12 tests de integracion)
```

## Guia paso a paso para ejecutar el proyecto / Step-by-Step Guide

### Paso 1: Verificar prerequisitos

Asegurate de tener **Java 17** instalado:

```bash
java -version
# Esperado: java version "17.x.x"
```

Si no lo tienes, descargalo desde https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html

### Paso 2: Clonar o ubicar el proyecto

```bash
cd D:/Master-IA-Dev/04-Bloque4/1-4-10-api-spring-boot-tareas
```

### Paso 3: Configurar Maven

**Opcion A — Si ya tienes Maven instalado globalmente:**
```bash
mvn --version
# Si funciona, puedes saltar al paso 4
```

**Opcion B — Si NO tienes Maven instalado (usar el wrapper incluido):**

Desde Git Bash:
```bash
# Descargar Maven 3.9.6 automaticamente
curl -fsSL "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip" -o /tmp/maven.zip
unzip -qo /tmp/maven.zip -d "$HOME/.m2/wrapper/dists/"

# Agregar Maven al PATH de la sesion actual
export PATH="$HOME/.m2/wrapper/dists/apache-maven-3.9.6/bin:$PATH"
export JAVA_HOME="/c/Program Files/Java/jdk-17"

# Verificar
mvn --version
```

### Paso 4: Compilar el proyecto

```bash
mvn clean compile
```

Esto descargara todas las dependencias (Spring Boot, H2, etc.) y compilara el codigo. La primera vez puede tardar unos minutos.

### Paso 5: Ejecutar los tests de integracion

```bash
mvn test
```

Resultado esperado:
```
Tests run: 13, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### Paso 6: Levantar la aplicacion

```bash
mvn spring-boot:run
```

La aplicacion arrancara en el puerto **8080**. Veras en consola:
```
Started ApiTareasApplication in X.XXX seconds
```

### Paso 7: Probar los endpoints

Abre otra terminal y prueba con `curl`:

```bash
# Crear una tarea
curl -X POST http://localhost:8080/api/tareas \
  -H "Content-Type: application/json" \
  -d '{"titulo": "Comprar leche", "descripcion": "Ir al supermercado"}'

# Listar todas las tareas
curl http://localhost:8080/api/tareas

# Obtener tarea por ID (reemplaza 1 con el ID real)
curl http://localhost:8080/api/tareas/1

# Actualizar una tarea
curl -X PUT http://localhost:8080/api/tareas/1 \
  -H "Content-Type: application/json" \
  -d '{"titulo": "Comprar leche y pan", "descripcion": "Ir al supermercado", "completada": true}'

# Filtrar solo tareas completadas
curl http://localhost:8080/api/tareas/estado/true

# Filtrar solo tareas pendientes
curl http://localhost:8080/api/tareas/estado/false

# Eliminar una tarea
curl -X DELETE http://localhost:8080/api/tareas/1
```

### Paso 8: Acceder a la consola H2 (opcional)

Abre en el navegador: http://localhost:8080/h2-console

Configuracion de conexion:
- **JDBC URL:** `jdbc:h2:mem:tareasdb`
- **User:** `sa`
- **Password:** *(dejar vacio)*

### Paso 9: Detener la aplicacion

Presiona `Ctrl+C` en la terminal donde esta corriendo.

---

## Comandos ejecutados / Commands Run

```bash
# Crear estructura de directorios
mkdir -p src/main/java/com/tareas/api/{controller,model,repository,service} \
         src/main/resources src/test/java/com/tareas/api

# Descargar Maven 3.9.6 (no estaba instalado)
curl -fsSL "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip" -o /tmp/maven.zip
unzip -qo /tmp/maven.zip -d "$HOME/.m2/wrapper/dists/"

# Ejecutar tests
export PATH="$HOME/.m2/wrapper/dists/apache-maven-3.9.6/bin:$PATH"
export JAVA_HOME="/c/Program Files/Java/jdk-17"
mvn test
```

## Levantar y detener la aplicacion / Running & Stopping

```bash
# Configurar entorno (si Maven no esta en PATH)
export PATH="$HOME/.m2/wrapper/dists/apache-maven-3.9.6/bin:$PATH"
export JAVA_HOME="/c/Program Files/Java/jdk-17"

# Levantar la aplicacion (puerto 8080 por defecto)
mvn spring-boot:run

# O usando el wrapper
./mvnw spring-boot:run

# Detener: Ctrl+C en la terminal

# Ejecutar solo los tests
mvn test
```

## URLs de prueba / Test URLs

Con la aplicacion levantada en local:

| Metodo | URL | Descripcion |
|--------|-----|-------------|
| GET | http://localhost:8080/api/tareas | Listar todas las tareas |
| GET | http://localhost:8080/api/tareas/{id} | Obtener tarea por ID |
| GET | http://localhost:8080/api/tareas/estado/true | Filtrar tareas completadas |
| GET | http://localhost:8080/api/tareas/estado/false | Filtrar tareas pendientes |
| POST | http://localhost:8080/api/tareas | Crear nueva tarea |
| PUT | http://localhost:8080/api/tareas/{id} | Actualizar tarea existente |
| DELETE | http://localhost:8080/api/tareas/{id} | Eliminar tarea |
| — | http://localhost:8080/h2-console | Consola H2 (JDBC URL: jdbc:h2:mem:tareasdb) |

### Ejemplo de body para POST/PUT:
```json
{
  "titulo": "Comprar leche",
  "descripcion": "Ir al supermercado",
  "completada": false
}
```

## Problemas encontrados / Problems & Solutions

| Problema | Solucion |
|----------|----------|
| Maven no instalado en el sistema (`mvn: command not found`) | Se descargo Maven 3.9.6 manualmente a `~/.m2/wrapper/dists/` y se agrego al PATH. Tambien se crearon scripts `mvnw` y `mvnw.cmd` como wrapper. |
| El script `mvnw.cmd` fallo al crear directorios (`El sistema no puede encontrar la ruta especificada`) | Se uso bash directamente con `curl` + `unzip` para descargar Maven, evitando los problemas del script batch con rutas de Windows. |

## Resultados y conclusiones / Results & Conclusions

### Lo que funciono
- **13/13 tests pasaron** (1 context load + 12 tests de integracion) en 38.9 segundos
- Arquitectura limpia en capas: Controller → Service → Repository → Entity
- H2 en memoria funciona perfectamente para tests de integracion con `@SpringBootTest(webEnvironment = RANDOM_PORT)`
- Validacion con `@Valid` y Bean Validation funciona correctamente (test de titulo vacio retorna 400)

### Tests de integracion implementados
1. Crear tarea (201 Created)
2. Crear tarea sin titulo (400 Bad Request)
3. Listar todas las tareas
4. Obtener tarea por ID existente
5. Obtener tarea por ID no existente (404)
6. Actualizar tarea existente
7. Actualizar tarea no existente (404)
8. Eliminar tarea existente (204 No Content)
9. Eliminar tarea no existente (404)
10. Filtrar por estado: solo completadas
11. Filtrar por estado: solo pendientes
12. Flujo CRUD completo integrado (crear → leer → actualizar → eliminar → verificar eliminacion)

### Para mejorar en futuras sesiones
- Configurar perfil de produccion con MySQL real (`application-prod.properties`)
- Agregar paginacion en el listado de tareas
- Agregar manejo global de excepciones con `@ControllerAdvice`
- Considerar agregar Swagger/OpenAPI para documentacion automatica
