# Sistema de Login - Computec

## Descripción
Sistema de autenticación implementado con encriptación SHA256 para el sistema de gestión Computec.

## Características Implementadas

### 1. Encriptación SHA256
- Las contraseñas se almacenan en la base de datos usando hash SHA256
- Clase utilitaria `EncriptacionUtil` para manejar la encriptación
- Verificación segura de contraseñas

### 2. Sistema de Autenticación
- Login mediante DNI y contraseña
- Validación de credenciales contra la base de datos
- Manejo de errores y mensajes informativos
- Control de sesión de usuario

### 3. Control de Permisos
- Sistema de roles basado en la tabla `permisos`
- Acceso controlado a diferentes módulos según el rol del usuario
- Mensajes de acceso denegado para usuarios sin permisos

### 4. Interfaces Integradas
El sistema incluye todas las interfaces principales:
- Form_Inicio - Pantalla de inicio
- Form_Clientes - Gestión de clientes
- Form_Pedidos - Gestión de pedidos
- Form_ControlStock - Control de inventario
- Form_Movimientos - Movimientos de inventario
- Form_Reclamos - Gestión de reclamos
- Form_Ubicacion - Gestión de ubicaciones
- Form_Reporte - Reportes del sistema

## Usuarios de Prueba

### 1. Administrador
- **Nombre:** Ujens
- **Contraseña:** admin
- **Rol:** Admin
- **Permisos:** Cliente, Inicio, ControlStock, Movimiento, Pedido, DetallePedido, Reclamo, Reporte, Ubicacion

### 2. Atención al Cliente
- **Nombre:** Ushirley
- **Contraseña:** cliente
- **Rol:** Atencion Cliente
- **Permisos:** Cliente, Inicio, Pedido, DetallePedido, Reclamo

### 3. Almacenero
- **Nombre:** Uliliana
- **Contraseña:** almacenero
- **Rol:** Almacenero
- **Permisos:** Inicio, ControlStock, Movimiento, Ubicacion

### 4. Soporte
- **Nombre:** Ujuanito
- **Contraseña:** soporte
- **Rol:** Soporte
- **Permisos:** Inicio, Reclamo

## Configuración de la Base de Datos

### 1. Ejecutar el script de actualización de contraseñas:
```sql
-- Ejecutar el archivo actualizar_contraseñas.sql
-- Este script actualiza las contraseñas con hash SHA256
```

### 2. Verificar la conexión:
- Asegurarse de que MySQL esté ejecutándose
- Verificar las credenciales en `ConexionBD.Conexion.java`
- La base de datos debe ser `compu_tec`

## Estructura del Proyecto

### Clases Principales:
- `EncriptacionUtil` - Utilidades de encriptación SHA256
- `AuthController` - Controlador de autenticación
- `UsuarioDAOImpl` - Acceso a datos de usuarios
- `Form_InicioSesion` - Interfaz de login
- `Main` - Ventana principal con control de permisos

### Flujo de Autenticación:
1. Usuario ingresa DNI y contraseña en `Form_InicioSesion`
2. `AuthController` valida las credenciales
3. Se verifica la contraseña usando SHA256
4. Si es correcto, se abre `Main` con los permisos del usuario
5. El menú se configura según los permisos del usuario

## Funcionalidades del Menú

### Menú Dinámico:
- Se muestra según los permisos del usuario
- Opciones disponibles según el rol
- Mensajes de acceso denegado para opciones no permitidas

### Cerrar Sesión:
- Opción para cerrar sesión
- Retorna a la pantalla de login
- Limpia la sesión del usuario

## Seguridad

### Encriptación:
- Contraseñas almacenadas como hash SHA256
- No se almacenan contraseñas en texto plano
- Verificación segura de credenciales

### Control de Acceso:
- Validación de permisos por módulo
- Sesión de usuario controlada
- Acceso denegado para usuarios sin permisos

## Notas Importantes

1. **Base de Datos:** Asegúrese de que la base de datos `compu_tec` esté creada y configurada
2. **Conexión:** Verifique las credenciales de conexión en `ConexionBD.Conexion.java`
3. **Contraseñas:** Las contraseñas de prueba están en el archivo `actualizar_contraseñas.sql`
4. **Permisos:** Los permisos se configuran en la tabla `permisos` de la base de datos

## Uso del Sistema

1. Ejecutar la aplicación
2. Ingresar con las credenciales de cualquier usuario de prueba
3. Navegar por los módulos según los permisos del usuario
4. Usar la opción "Cerrar Sesión" para salir del sistema 