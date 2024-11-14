# ExpressLink - Sistema de Gestión de Envíos

## Descripción
ExpressLink es un sistema de gestión logística desarrollado en Java Swing que optimiza el proceso de envío de paquetes, desde la creación hasta la entrega final. El sistema proporciona una interfaz gráfica intuitiva para gestionar todos los aspectos del proceso logístico.

## Desarrolladores
- Ignacia Miranda
- Adolfo Toledo

## Tecnologías Utilizadas
- Java
- Swing (GUI)
- MySQL/MariaDB
- JUnit (Testing)

## Características Principales
- Sistema de seguimiento en tiempo real
- Cálculo automático de costos de envío
- Gestión de rutas optimizada
- Sistema de notificaciones integrado
- Generación de reportes y estadísticas
- Sistema de respaldo de información
- Gestión multiusuario con roles diferenciados

## Arquitectura del Sistema

### Módulos Principales
1. **Gestión de Usuarios**
   - Autenticación y autorización
   - Perfiles: Empresa, Cliente, Transportista, Sucursal

2. **Gestión de Pedidos**
   - Creación y seguimiento de envíos
   - Asignación de transportistas
   - Cálculo de costos
   - Estados del pedido

3. **Gestión de Sucursales**
   - Inventario de paquetes
   - Gestión de transportistas
   - Control de entregas y recogidas

4. **Sistema de Reportes**
   - Análisis de rendimiento
   - Estadísticas de envíos
   - Reportes financieros

## Estructura del Proyecto
```
expresslink/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── controllers/
│   │   │   ├── models/
│   │   │   ├── views/
│   │   │   └── utils/
│   │   └── resources/
│   └── test/
├── docs/
│   ├── diagrams/
│   └── specifications/
└── database/
    └── scripts/
```

## Estados del Pedido
1. INGRESADO
2. RECOLECTADO
3. EN_TRANSITO
4. EN_SUCURSAL_DESTINO
5. ASIGNADO_TRANSPORTISTA
6. EN_CAMINO
7. ENTREGADO
8. NO_ENTREGADO
9. CANCELADO

## Base de Datos
El sistema utiliza una base de datos relacional con las siguientes tablas principales:
- Usuarios
- Pedidos
- Sucursales
- Vehículos
- Transportistas
- Tracking

## Configuración del Entorno de Desarrollo

### Requisitos Previos
- JDK 17 o superior
- IDE (NetBeans/Eclipse/IntelliJ)
- MySQL/MariaDB
- Git

### Pasos de Instalación
1. Clonar el repositorio
2. Importar el proyecto en el IDE
3. Configurar la conexión a la base de datos
4. Ejecutar los scripts de base de datos
5. Compilar y ejecutar

## Convenciones de Código
- Nomenclatura en español
- Nombres de clases en PascalCase
- Nombres de métodos y variables en camelCase
- Documentación en español

## Testing
- Pruebas unitarias con JUnit
- Pruebas de integración para módulos críticos
- Pruebas de interfaz de usuario

## Contacto
Para consultas sobre el desarrollo:
- Ignacia Miranda: [email protegido]
- Adolfo Toledo: [email protegido]

## Estado del Proyecto
Proyecto en desarrollo activo - Universidad de La Serena, 2024
