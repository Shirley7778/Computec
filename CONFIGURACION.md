# Configuración del Sistema de Login - Computec

## Pasos para Configurar el Sistema

### 1. Configuración de la Base de Datos

#### 1.1 Crear la Base de Datos
```sql
-- Ejecutar en MySQL
CREATE DATABASE IF NOT EXISTS compu_tec
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE compu_tec;
```

#### 1.2 Ejecutar el Script de Tablas
```sql
-- Ejecutar el script completo de la base de datos proporcionado
-- Este script crea todas las tablas necesarias
```

#### 1.3 Actualizar Contraseñas
```sql
-- Ejecutar el archivo actualizar_contraseñas.sql
-- Este script actualiza las contraseñas con hash SHA256
```

### 2. Verificar Configuración de Conexión

#### 2.1 Revisar ConexionBD.Conexion.java
```java
// Verificar que estos valores coincidan con tu configuración
public static String cadena = "jdbc:mysql://localhost:3306/compu_tec";
public static String usuario = "root";
public static String clave = "root";
```

#### 2.2 Ajustar según tu configuración:
- **Host:** Cambiar `localhost` si MySQL está en otro servidor
- **Puerto:** Cambiar `3306` si usas otro puerto
- **Usuario:** Cambiar `root` por tu usuario de MySQL
- **Contraseña:** Cambiar `root` por tu contraseña de MySQL

### 3. Compilar y Ejecutar

#### 3.1 Verificar Dependencias
Asegúrate de que todos los archivos JAR estén en el directorio raíz:
- mysql-connector-j-9.1.0.jar
- Todos los archivos JAR de iText

#### 3.2 Compilar el Proyecto
```bash
# En NetBeans: Build Project (F11)
# O desde línea de comandos:
javac -cp ".:*.jar" src/**/*.java
```

#### 3.3 Ejecutar la Aplicación
```bash
# Desde NetBeans: Run Project (F6)
# O desde línea de comandos:
java -cp ".:*.jar:src" Vista.Main
```

### 4. Probar el Sistema

#### 4.1 Usuarios de Prueba Disponibles

**Administrador:**
- Nombre: Ujens
- Contraseña: admin
- Permisos: Cliente, Inicio, ControlStock, Movimiento, Pedido, DetallePedido, Reclamo, Reporte, Ubicacion

**Atención al Cliente:**
- Nombre: Ushirley
- Contraseña: cliente
- Permisos: Cliente, Inicio, Pedido, DetallePedido, Reclamo

**Almacenero:**
- Nombre: Uliliana
- Contraseña: almacenero
- Permisos: Inicio, ControlStock, Movimiento, Ubicacion

**Soporte:**
- Nombre: Ujuanito
- Contraseña: soporte
- Permisos: Inicio, Reclamo

#### 4.2 Flujo de Prueba
1. Ejecutar la aplicación
2. Aparecerá la pantalla de login
3. Ingresar **nombre** y contraseña de cualquier usuario
4. Verificar que se abra la ventana principal
5. Navegar por los menús según los permisos
6. Probar la opción "Cerrar Sesión"

### 5. Solución de Problemas

#### 5.1 Error de Conexión a Base de Datos
```
Error: Error en la conexion local
```
**Solución:**
- Verificar que MySQL esté ejecutándose
- Revisar credenciales en ConexionBD.Conexion.java
- Verificar que la base de datos `compu_tec` exista

#### 5.2 Error de Usuario No Encontrado
```
Error: Usuario no encontrado
```
**Solución:**
- Verificar que los datos de la tabla `usuarios` estén correctos
- Verificar que el **nombre** ingresado exista en la base de datos
- Usar exactamente los nombres: Ujens, Ushirley, Uliliana, Ujuanito

#### 5.3 Error de Contraseña Incorrecta
```
Error: Contraseña incorrecta
```
**Solución:**
- Verificar que esté usando las contraseñas correctas
- Asegurarse de que el script de actualización se ejecutó
- Verificar que no haya espacios extra en la contraseña

#### 5.4 Error de Permisos
```
Error: No tiene permisos para acceder a [módulo]
```
**Solución:**
- Verificar que el usuario tenga el rol correcto
- Revisar la tabla `permisos` en la base de datos
- Verificar que el campo `acceso` contenga los permisos necesarios

### 6. Estructura de Archivos

```
Computec/
├── src/
│   ├── ConexionBD/
│   │   └── Conexion.java
│   ├── Controlador/
│   │   └── AuthController.java
│   ├── DAO/
│   │   ├── UsuarioDAO.java
│   │   └── impl/
│   │       └── UsuarioDAOImpl.java
│   ├── Modelo/
│   │   ├── Usuario.java
│   │   └── Permiso.java
│   ├── Utilidades/
│   │   └── EncriptacionUtil.java
│   └── Vista/
│       ├── Form_InicioSesion.java
│       ├── Main.java
│       ├── Menu.java
│       └── [otros formularios]
├── actualizar_contraseñas.sql
├── README_LOGIN.md
├── CONFIGURACION.md
└── test_login.java
```

### 7. Comandos Útiles

#### 7.1 Verificar Base de Datos
```sql
-- Verificar usuarios
SELECT * FROM usuarios;

-- Verificar permisos
SELECT * FROM permisos;

-- Verificar relación usuarios-permisos
SELECT u.nombre, u.apellido, u.dni, p.rol, p.acceso 
FROM usuarios u 
INNER JOIN permisos p ON u.permiso_id = p.id_permiso;
```

#### 7.2 Generar Hash SHA256 (para nuevas contraseñas)
```java
// Usar la clase EncriptacionUtil
String hash = EncriptacionUtil.encriptarSHA256("nueva_contraseña");
System.out.println(hash);
```

### 8. Notas de Seguridad

1. **Contraseñas:** Nunca almacenar contraseñas en texto plano
2. **Conexión:** Usar credenciales seguras para la base de datos
3. **Permisos:** Revisar regularmente los permisos de usuarios
4. **Logs:** Implementar logging para auditoría de accesos
5. **Backup:** Realizar respaldos regulares de la base de datos

### 9. Mejoras Futuras

1. **Encriptación:** Implementar salt para mayor seguridad
2. **Sesiones:** Implementar timeout de sesiones
3. **Auditoría:** Agregar logs de acceso
4. **Recuperación:** Implementar recuperación de contraseñas
5. **Validación:** Agregar validación de fortaleza de contraseñas 