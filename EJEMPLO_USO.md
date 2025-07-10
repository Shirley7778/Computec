# Ejemplo de Uso del Sistema de Login - Computec

## Cómo Usar el Sistema

### 1. Iniciar la Aplicación
1. Ejecutar la aplicación desde NetBeans o línea de comandos
2. Se abrirá la pantalla de login (`Form_InicioSesion`)

### 2. Pantalla de Login
- **Campo "Nombre":** Ingresar el nombre del usuario (no DNI)
- **Campo "Contraseña":** Ingresar la contraseña correspondiente
- **Botón "Iniciar Sesión":** Para autenticarse
- **Botón "Cerrar":** Para salir de la aplicación

### 3. Ejemplos de Login

#### Ejemplo 1: Administrador
```
Nombre: Ujens
Contraseña: admin
```
**Resultado:** Acceso completo a todos los módulos

#### Ejemplo 2: Atención al Cliente
```
Nombre: Ushirley
Contraseña: cliente
```
**Resultado:** Acceso a Clientes, Pedidos, Reclamos

#### Ejemplo 3: Almacenero
```
Nombre: Uliliana
Contraseña: almacenero
```
**Resultado:** Acceso a Control Stock, Movimientos, Ubicación

#### Ejemplo 4: Soporte
```
Nombre: Ujuanito
Contraseña: soporte
```
**Resultado:** Acceso solo a Reclamos

### 4. Navegación en el Sistema

#### 4.1 Menú Principal
Una vez autenticado, aparecerá el menú principal con las siguientes opciones:
- **Inicio** - Pantalla de bienvenida
- **Clientes** - Gestión de clientes
- **Pedidos** - Gestión de pedidos
- **Control Stock** - Control de inventario
- **Movimientos** - Movimientos de inventario
- **Reclamos** - Gestión de reclamos
- **Ubicación** - Gestión de ubicaciones
- **Reportes** - Reportes del sistema
- **Cerrar Sesión** - Salir del sistema

#### 4.2 Control de Permisos
- Cada usuario verá solo las opciones a las que tiene acceso
- Si intenta acceder a un módulo sin permisos, aparecerá un mensaje de "Acceso Denegado"
- El sistema muestra el nombre y rol del usuario en la parte superior del menú

### 5. Casos de Prueba

#### Caso 1: Login Exitoso
1. Abrir la aplicación
2. Ingresar: Nombre = "Ujens", Contraseña = "admin"
3. Hacer clic en "Iniciar Sesión"
4. **Resultado esperado:** Mensaje "Bienvenido Ujens Luna" y apertura del menú principal

#### Caso 2: Usuario No Encontrado
1. Abrir la aplicación
2. Ingresar: Nombre = "UsuarioInexistente", Contraseña = "cualquiera"
3. Hacer clic en "Iniciar Sesión"
4. **Resultado esperado:** Mensaje "Usuario no encontrado"

#### Caso 3: Contraseña Incorrecta
1. Abrir la aplicación
2. Ingresar: Nombre = "Ujens", Contraseña = "contraseña_incorrecta"
3. Hacer clic en "Iniciar Sesión"
4. **Resultado esperado:** Mensaje "Contraseña incorrecta"

#### Caso 4: Acceso Denegado
1. Hacer login como "Ujuanito" (Soporte)
2. Intentar acceder a "Control Stock"
3. **Resultado esperado:** Mensaje "No tiene permisos para acceder a Control de Stock"

### 6. Verificación de Permisos

#### 6.1 Usuario Admin (Ujens)
- ✅ Puede acceder a todos los módulos
- ✅ Ve todas las opciones del menú
- ✅ Puede gestionar clientes, pedidos, stock, etc.

#### 6.2 Usuario Atención Cliente (Ushirley)
- ✅ Puede acceder a: Clientes, Pedidos, Reclamos
- ❌ No puede acceder a: Control Stock, Movimientos, Ubicación, Reportes
- ⚠️ Mensaje de acceso denegado para módulos restringidos

#### 6.3 Usuario Almacenero (Uliliana)
- ✅ Puede acceder a: Control Stock, Movimientos, Ubicación
- ❌ No puede acceder a: Clientes, Pedidos, Reclamos, Reportes
- ⚠️ Mensaje de acceso denegado para módulos restringidos

#### 6.4 Usuario Soporte (Ujuanito)
- ✅ Puede acceder a: Reclamos
- ❌ No puede acceder a: Clientes, Pedidos, Control Stock, Movimientos, Ubicación, Reportes
- ⚠️ Mensaje de acceso denegado para módulos restringidos

### 7. Cerrar Sesión

#### 7.1 Proceso de Cierre
1. Hacer clic en "Cerrar Sesión" en el menú
2. Aparecerá un diálogo de confirmación
3. Confirmar el cierre de sesión
4. **Resultado:** Retorna a la pantalla de login

#### 7.2 Verificación
- La sesión del usuario se limpia
- Se puede hacer login con otro usuario
- Los permisos se restablecen para el nuevo usuario

### 8. Datos de la Base de Datos

#### 8.1 Tabla Usuarios
```sql
SELECT nombre, apellido, dni, contrasena, permiso_id FROM usuarios;
```

#### 8.2 Tabla Permisos
```sql
SELECT rol, acceso FROM permisos;
```

#### 8.3 Relación Usuarios-Permisos
```sql
SELECT u.nombre, u.apellido, p.rol, p.acceso 
FROM usuarios u 
INNER JOIN permisos p ON u.permiso_id = p.id_permiso;
```

### 9. Troubleshooting

#### 9.1 Error de Conexión
**Síntoma:** "Error en la conexion local"
**Solución:** Verificar que MySQL esté ejecutándose y las credenciales sean correctas

#### 9.2 Usuario No Encontrado
**Síntoma:** "Usuario no encontrado"
**Solución:** Verificar que el nombre esté escrito exactamente como en la base de datos

#### 9.3 Contraseña Incorrecta
**Síntoma:** "Contraseña incorrecta"
**Solución:** Verificar que la contraseña sea la correcta para ese usuario

#### 9.4 Acceso Denegado
**Síntoma:** "No tiene permisos para acceder a [módulo]"
**Solución:** Usar un usuario con los permisos necesarios para ese módulo

### 10. Notas Importantes

1. **Nombres exactos:** Usar exactamente los nombres de la base de datos
2. **Contraseñas:** Las contraseñas están encriptadas con SHA256
3. **Permisos:** Cada rol tiene permisos específicos definidos en la base de datos
4. **Sesión:** Solo un usuario puede estar autenticado a la vez
5. **Seguridad:** Las contraseñas nunca se muestran en texto plano 