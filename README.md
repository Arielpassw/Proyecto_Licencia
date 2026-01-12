# Sistema de Entrega de Licencias

Proyecto desarrollado para la asignatura **Programación Orientada a Objetos**, que simula un sistema real de gestión de trámites para la **entrega de licencias de conducir**, utilizando **Java Swing**, **Programación Orientada a Objetos** y **base de datos en la nube**.

---

## Introducción

El sistema permite gestionar de forma ordenada los trámites para la obtención de licencias de conducir, facilitando el registro, control y seguimiento de cada solicitud.  
Incluye un sistema de autenticación con **roles** (Administrador y Analista), control de estados del trámite y generación de licencias en **formato PDF**.

La información se almacena en una **base de datos alojada en la nube**, garantizando disponibilidad y simulando el funcionamiento de un sistema profesional real.

---

## Objetivo del Sistema

Automatizar el proceso de entrega de licencias para:

- Reducir errores manuales  
- Mejorar la organización de la información  
- Agilizar la atención a los solicitantes  
- Aplicar correctamente los principios de **Programación Orientada a Objetos**

---

## Tecnologías Utilizadas

- **Lenguaje:** Java 8  
- **Interfaz gráfica:** Java Swing  
- **Base de datos:**  
  - MySQL (Clever Cloud) 
- **Conexión BD:** JDBC   
- **Generación de PDF:** iText  
- **Hash de contraseñas:** BCrypt  
- **Control de versiones:** Git y GitHub  

---

## Roles del Sistema

### Analista
- Registrar solicitantes
- Verificar requisitos
- Registrar exámenes
- Gestionar trámites
- Generar licencias (solo si el trámite está aprobado)
- Cerrar sesión

### Administrador
Incluye todas las funciones del Analista y además:
- Gestión de usuarios (crear, modificar, activar, desactivar un usuario)
- Reportes y estadísticas
- Exportación de reportes (CSV / Excel / PDF)
- Consulta de totales por estado
- Cerrar sesión

---

## Funcionalidades Principales

### Login
- Validación de credenciales contra la base de datos
- Diferenciación de roles
- Límite de 3 intentos
- Usuarios inactivos no pueden ingresar
<img width="537" height="360" alt="image" src="https://github.com/user-attachments/assets/14e9593d-67b6-4696-b326-c74f409251aa" />

### Registro de Solicitantes
- Cédula
- Nombre
- Tipo de licencia (un menu desplegable)
- Fecha de solicitud automática
- Crea automáticamente un trámite en estado **pendiente**
<img width="788" height="509" alt="image" src="https://github.com/user-attachments/assets/c5c423db-93ae-4e3c-861c-5a83b721e0e6" />

### Verificación de Requisitos
- Certificado médico
- Pago
- Multas
- Observaciones
- Si todo es correcto → estado **en_examenes**
**Primero nos pide seleccionar un tramite**
<img width="339" height="160" alt="image" src="https://github.com/user-attachments/assets/c3dce98b-6cac-44bf-8c51-7aa8fa2a474b" />

Luego nos muestra la siguiente ventana

<img width="792" height="507" alt="image" src="https://github.com/user-attachments/assets/98240bd5-de8c-4df6-9e6a-7ab3d258f3d2" />

### Registro de Exámenes
- Nota teórica
- Nota práctica
- Si promedio ≥ 14 → **aprobado**
- Caso contrario → **reprobado**
<img width="604" height="409" alt="image" src="https://github.com/user-attachments/assets/eb316f7d-698a-41c0-a4cc-0b213a092d0f" />

### Gestión de Trámites
- Tabla con:
  - ID
  - Cédula
  - Nombre
  - Tipo de licencia
  - Fecha
  - Estado
- Filtros por estado
- Acciones según el estado del trámite
<img width="1160" height="786" alt="image" src="https://github.com/user-attachments/assets/4ec57510-6004-4811-b5ff-e12029ba4a61" />
En esta interfaz se puede filtrar datos, ver sus detalles, registrar examen y generar licencia en caso de aprobado.

### Generación de Licencia
- Número de licencia
- Fecha de emisión
- Fecha de vencimiento (5 años)
- Generación de PDF
- Cambio de estado a **licencia_emitida**
<img width="864" height="614" alt="image" src="https://github.com/user-attachments/assets/5790357e-6937-442c-9dd0-29ee62bc9e83" />

Aqui puede agregar una imagen guardar la licencia o exportarla.

### Reportes (Administrador)
- Filtros por fecha, estado, tipo de licencia y cédula
- Totales por estado
- Total de licencias generadas
- Exportación de reportes
<img width="1161" height="804" alt="image" src="https://github.com/user-attachments/assets/7769f96a-7df2-437d-b331-db408aed6db7" />

## Gestion de usuarios (Administrador)
- Crear usuario
- Activar/Desactivar
- Actualizar datos 
- Roles 
<img width="915" height="668" alt="image" src="https://github.com/user-attachments/assets/027b4a8a-7739-482a-9765-de33966fbb97" />

---

## Flujo del Sistema

1. El **Administrador** crea usuarios  
2. El **Analista** registra solicitantes  
3. Se verifican requisitos  
4. Se registran exámenes  
5. Si el trámite es aprobado → se genera la licencia  
6. El **Administrador** consulta reportes  

---

## Reglas del Negocio

- Edad mínima: **18 años**
- Nota mínima aprobatoria: **14**
- Solo se puede generar licencia si el trámite está **aprobado**
- Transacciones críticas manejadas de forma atómica
- Usuarios inactivos no pueden iniciar sesión
- Número de licencia único
- Auditoría básica (created_by, created_at)

---

## Estructura del Proyecto

<img width="481" height="872" alt="image" src="https://github.com/user-attachments/assets/fe0e2493-1d12-4fb8-a19c-aef45da9cf63" />

**DataBase (dao)**
En este paquete se encuentra la conexión del programa con la base de datos en la nube. 

<img width="160" height="69" alt="image" src="https://github.com/user-attachments/assets/1b3cfdf3-964c-498c-ad54-ba075b965840" />

**Licencia (UI-models)**
En este paquete se encuentran los forms y parte de las clases modelos, las cuales estan programados para presentar las interfaces y hacerlas funcionales.
## Clase Btn_Regresar_base
Es una clase creada para hacer que funcione el boton regresar de los formularios, en este se programó la funcionalidad respectiva dependiendo las credenciales 
del login nos redireccionará al dashboard correspondiente (ADMIN-ANALISTA). 

<img width="363" height="688" alt="image" src="https://github.com/user-attachments/assets/3a890138-3d87-49d2-a803-5ed34d38c923" />

## Clase Main
En esta clase encuentra la estructura para hacer visible el panel Login 

<img width="462" height="218" alt="image" src="https://github.com/user-attachments/assets/2c4c0c7e-d9a2-453d-8ab4-316f58bcf2f5" />

---

## Credenciales de Prueba

## Administrador
Usuario: **Emilia**
Contraseña: **admin1234**

## Analista
Usuario: Josue
Contraseña: analista123

