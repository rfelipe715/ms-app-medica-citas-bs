# ms-app-medica-citas-bs

Capa **BS** (Business Service) del módulo **Citas**. Contiene la lógica de negocio de las citas y la **comunicación entre módulos**: antes de agendar/listar valida y enriquece cada cita consultando `ms-app-medica-pacientes-bs`. Delega la persistencia en `ms-app-medica-citas-db`.

| | |
|---|---|
| **Puerto** | `8091` |
| **Patrón** | Controller → Service → Client (Feign) |
| **Ruta base** | `/api/v1/citas` |
| **Llama a** | `citas-db` (8092) · `pacientes-bs` (8081) |
| **Pruebas** | `CitasServiceTest` — 8 pruebas JUnit 5 + Mockito + AssertJ (Given–When–Then), incluida la lógica de enriquecimiento remoto |

Manejo de errores remotos: un `FeignException.NotFound` se traduce a `404 CitaNotFoundException`; cualquier otro fallo de la capa DB a `502 ServicioNoDisponibleException`.

## Ejecución

```bash
# Con todo el ecosistema (recomendado), desde app-medica-et-fullstack-1/
docker compose up --build

# Individual
./mvnw spring-boot:run     # mvnw.cmd en Windows
./mvnw test
```
