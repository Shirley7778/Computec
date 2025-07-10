-- Script para actualizar las contraseñas de los usuarios con hash SHA256
-- Basado en los datos exactos de la base de datos proporcionada

-- Verificar que los usuarios existan con los datos correctos
SELECT id_usuario, nombre, apellido, dni, contrasena, permiso_id FROM usuarios;

-- Las contraseñas ya están en hash SHA256 en la base de datos:
-- Ujens (Admin): contraseña = admin -> hash: 75e6c848206f01c6b1d7455e319c96471b49ae69bde97316b4ef724147bec385
-- Ushirley (Atencion Cliente): contraseña = cliente -> hash: 785c07307d5160984c6a90c8c0a384cada6ce31cbfc09a76c6b2afe06f79bdbc
-- Uliliana (Almacenero): contraseña = almacenero -> hash: d6c025ab876840151b08da4486c16274933e12de0d1ca1e7966f8a17b8545aef
-- Ujuanito (Soporte): contraseña = soporte -> hash: 8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92

-- Verificar permisos
SELECT p.id_permiso, p.rol, p.acceso FROM permisos p;

-- Verificar relación usuarios-permisos
SELECT u.nombre, u.apellido, u.dni, p.rol, p.acceso 
FROM usuarios u 
INNER JOIN permisos p ON u.permiso_id = p.id_permiso; 