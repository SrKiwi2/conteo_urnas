<div align="center">

# 🗳️ Sistema de Conteo de Urnas

### Plataforma electoral de registro y seguimiento de votos en tiempo real

*Visualiza el avance de una elección en vivo: votos por candidato, recinto y mesa, al instante.*

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)](https://www.thymeleaf.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![WebSocket](https://img.shields.io/badge/WebSocket-En_Vivo-010101?style=for-the-badge&logo=socket.io&logoColor=white)]()
[![Servidor Privado](https://img.shields.io/badge/Deploy-Servidor_Institucional-2C3E50?style=for-the-badge&logo=linux&logoColor=white)]()

[🐛 Reportar Bug](https://github.com/Srkiwi2/conteo_urnas/issues) · [✨ Solicitar Feature](https://github.com/Srkiwi2/conteo_urnas/issues)

</div>

---

## 📸 Capturas del Sistema

> 🖼️ *Screenshots próximamente...*

---

## 🧩 ¿Qué problema resuelve?

El conteo de votos en elecciones institucionales suele ser un proceso lento, opaco y propenso a errores cuando se realiza de forma manual. La información tarda en llegar, los reportes se generan horas después y no existe visibilidad en tiempo real del avance electoral.

**El Sistema de Conteo de Urnas** digitaliza y transparenta todo ese proceso: los operadores registran los votos desde cada mesa y recinto, y cualquier autoridad o veedor puede ver el avance de la elección **en vivo y en directo**, con totales actualizados al instante.

---

## ✨ Funcionalidades Principales

### 🗳️ Registro Electoral
- [x] Registro de **candidatos** participantes en la elección
- [x] Configuración de **recintos** electorales
- [x] Configuración de **mesas** de votación por recinto
- [x] Registro de votos emitidos por mesa y recinto
- [x] Control de votos nulos y blancos

### 📡 Seguimiento en Vivo (Tiempo Real)
- [x] Visualización **en vivo** del conteo de votos vía **WebSocket**
- [x] Actualización automática de totales sin necesidad de recargar la página
- [x] Progreso de votación por candidato en tiempo real
- [x] Porcentajes y tendencias actualizadas al instante

### 📊 Reportes Electorales
- [x] Reporte de resultados por **recinto**
- [x] Reporte de resultados por **mesa**
- [x] Reporte general consolidado de la elección
- [x] Historial completo del proceso de conteo
- [x] Exportación de actas y resultados finales

### ⚙️ Administración del Proceso
- [x] Gestión de usuarios y operadores por mesa
- [x] Control de apertura y cierre del proceso electoral
- [x] Trazabilidad de cada registro ingresado
- [x] Roles diferenciados (administrador, operador, veedor)

---

## 📡 Panel de Resultados en Vivo

```
┌─────────────────────────────────────────────────────────────┐
│              🗳️ RESULTADOS EN TIEMPO REAL                    │
│                                                             │
│  Mesas computadas: 12 / 20        Votos registrados: 1,240  │
│                                                             │
│  ┌───────────────────────────────────────────────────────┐  │
│  │  🟦 Candidato A  ████████████████████░░░░░  64%  794  │  │
│  │  🟥 Candidato B  ████████████░░░░░░░░░░░░░  31%  384  │  │
│  │  ⬜ Votos Blancos ██░░░░░░░░░░░░░░░░░░░░░░   3%   37  │  │
│  │  ❌ Votos Nulos   █░░░░░░░░░░░░░░░░░░░░░░░   2%   25  │  │
│  └───────────────────────────────────────────────────────┘  │
│                                                             │
│  Por Recinto:  [Recinto A ▼]    Por Mesa: [Todas ▼]         │
│                                                             │
│         ⚡ Actualización automática vía WebSocket            │
└─────────────────────────────────────────────────────────────┘
```

---

## 🔄 Flujo del Proceso Electoral

```
  ⚙️ Administrador              📋 Operador de Mesa          👁️ Veedor / Autoridad
        │                               │                            │
        ▼                               │                            │
  Configura elección:                   │                            │
  candidatos, recintos,                 │                            │
  mesas y operadores                    │                            │
        │                               ▼                            │
        │                    Recibe acta física          Observa pantalla
        │                    de la mesa y registra       de resultados
        │                    los votos en el sistema     en tiempo real
        │                               │                            │
        └───────────────────────────────┘                            │
                         │                                           │
                         ▼                                           │
              Sistema procesa el voto ──────────────────────────────►│
              y actualiza totales vía WebSocket                      │
                         │                                           ▼
                         │                               📊 Ve resultados
                         ▼                               actualizados al instante
              Base de datos PostgreSQL
              registra con trazabilidad completa
```

---

## 🛠️ Stack Tecnológico

| Capa | Tecnología |
|------|-----------|
| **Backend** | Java 17 + Spring Boot 3 |
| **Frontend** | Thymeleaf + HTML/CSS/JS |
| **Base de Datos** | PostgreSQL |
| **Tiempo Real** | WebSocket (STOMP) |
| **Infraestructura** | Servidor privado institucional |
| **Build** | Maven |

---

## 🚀 Instalación y Ejecución Local

### Prerrequisitos

- [Java 17+](https://adoptium.net/)
- [Maven 3.8+](https://maven.apache.org/)
- [PostgreSQL 14+](https://www.postgresql.org/download/)

### Pasos

**1. Clona el repositorio**
```bash
git clone https://github.com/Srkiwi2/conteo_urnas.git
cd conteo_urnas
```

**2. Crea la base de datos**
```sql
CREATE DATABASE conteo_urnas;
```

**3. Configura la aplicación**

Edita `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/conteo_urnas
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA
spring.jpa.hibernate.ddl-auto=update
```

**4. Ejecuta el proyecto**
```bash
mvn clean install
mvn spring-boot:run
```

**5. Accede al sistema**
```
http://localhost:8080
```

---

## 📁 Estructura del Proyecto

```
conteo_urnas/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/conteournas/
│   │   │       ├── controller/      # Controladores MVC
│   │   │       ├── model/           # Candidato, Mesa, Recinto, Voto
│   │   │       ├── repository/      # Repositorios JPA
│   │   │       ├── service/         # Lógica de conteo electoral
│   │   │       └── websocket/       # Transmisión de resultados en vivo
│   │   └── resources/
│   │       ├── templates/
│   │       │   ├── admin/           # Configuración del proceso
│   │       │   ├── operador/        # Registro de votos por mesa
│   │       │   └── resultados/      # Panel de resultados en vivo
│   │       ├── static/              # CSS, JS, imágenes
│   │       └── application.properties
├── pom.xml
└── README.md
```

---

## 👥 Roles del Sistema

| Rol | Responsabilidad |
|-----|----------------|
| ⚙️ **Administrador** | Configura candidatos, recintos, mesas y usuarios del sistema |
| 📋 **Operador de Mesa** | Ingresa los votos físicos de su mesa al sistema |
| 👁️ **Veedor / Autoridad** | Visualiza los resultados en tiempo real (solo lectura) |

---

## 👤 Autor

**Srkiwi2**

[![GitHub](https://img.shields.io/badge/GitHub-Srkiwi2-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/Srkiwi2)

---

## 📄 Licencia

Desarrollado como sistema de apoyo para procesos electorales institucionales.
Todos los derechos reservados © 2024 Srkiwi2.

---

<div align="center">

*Transparencia y tecnología al servicio de la democracia institucional — Bolivia 🇧🇴*

</div>
