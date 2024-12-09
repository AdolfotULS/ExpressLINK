# ExpressLink - Sistema de Gestión de Envíos

## Descripción
ExpressLink es un sistema de gestión logística desarrollado en Java que permite el seguimiento y control integral de envíos de paquetes. El sistema utiliza una arquitectura MVC con interfaces gráficas específicas para cada rol de usuario (Cliente, Transportista, Sucursal) e incluye funcionalidades como seguimiento en tiempo real, gestión de entregas, y generación de reportes.

## Desarrolladores
- Ignacia Miranda
- Adolfo Toledo

## Tecnologías Utilizadas
- Java 17
- Java Swing para interfaces gráficas
- MySQL/MariaDB para persistencia de datos
- JUnit para pruebas unitarias

## Características Implementadas

### Módulo Cliente
- Seguimiento de envíos en tiempo real
- Historial de pedidos
- Panel de pedidos activos
- Gestión de perfil

### Módulo Sucursal
- Creación y gestión de pedidos
- Asignación de transportistas
- Monitoreo de entregas del día
- Panel de informes y estadísticas
- Gestión de paquetes en tránsito

### Módulo Transportista
- Visualización de pedidos asignados
- Registro de entregas
- Control de estados de entrega
- Historial de entregas realizadas

### Funcionalidades Generales
- Sistema de autenticación por roles
- Cálculo automático de costos basado en dimensiones
- Registro detallado de eventos (logs)
- Dashboards en tiempo real
- Validación de datos y manejo de errores

## Estados del Paquete
- PENDIENTE: Paquete registrado, pendiente de asignación
- EN_TRANSITO: En proceso de entrega
- ENTREGADO: Entrega completada exitosamente
- CANCELADO: Entrega cancelada

## Base de Datos
### Tablas Principales
- usuario
- sucursal
- transportista
- vehiculo
- paquete
- log_paquete
- log_sucursal
- log_transportista
- balance_sucursal

## Estructura del Proyecto
```
expresslink/
├── model/
│   ├── Entidad.java
│   ├── Persona.java
│   ├── Usuario.java
│   ├── Sucursal.java
│   ├── Transportista.java
│   ├── Paquete.java
│   └── ...
├── controllers/
│   ├── auth/
│   ├── cliente/
│   ├── sucursal/
│   └── transportista/
├── view/
│   ├── cliente/
│   ├── sucursal/
│   ├── transportista/
│   └── components/
└── utils/
    └── DatabaseConnection.java
```

## Configuración e Instalación
1. Requisitos previos:
   - JDK 17 o superior
   - MySQL/MariaDB
   - IDE Java (NetBeans/Eclipse/IntelliJ)

2. Pasos de instalación:
   ```bash
   git clone [URL del repositorio]
   cd expresslink
   ```

3. Configurar la base de datos:
   - Importar el script SQL proporcionado
   - Configurar credenciales en DatabaseConnection.java

4. Compilar y ejecutar el proyecto desde el IDE

## Contribución
Para contribuir al proyecto:
1. Crear un fork del repositorio
2. Crear una rama para nuevas funcionalidades (`git checkout -b feature/nueva-funcionalidad`)
3. Realizar cambios y commits
4. Enviar pull request

## Licencia
Este proyecto es software privado desarrollado para la Universidad de La Serena.

## Estado del Proyecto
Versión 1.0 completada - 2024
Universidad de La Serena

## Contacto
Para consultas sobre el proyecto:
- Ignacia Miranda 
- Adolfo Toledo
