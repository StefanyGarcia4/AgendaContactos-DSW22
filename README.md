# 📱 Agenda de Contactos - CRUD Local con Room

Aplicación móvil desarrollada en Android con persistencia local mediante la biblioteca **Room**, correspondiente a la evaluación del **Examen Práctico #1** de la materia **Desarrollo de Aplicaciones Móviles** (ITCA FEPADE)[cite: 1].

---

## 👥 Integrantes del Equipo
* **Marcelo Sánchez**
* **Stefany García**

---

## 🚀 Funcionalidades Implementadas

La aplicación cumple con todas las operaciones CRUD y validaciones requeridas[cite: 1]:

* **Crear registros:** Formulario modal para registrar contactos (nombre, teléfono, correo, categoría y notas)[cite: 1].
* **Consultar y listar registros:** Lista dinámica y reactiva mediante `Flow`, con barra de búsqueda en tiempo real y filtrado por categorías[cite: 1].
* **Actualizar registros:** Edición completa de datos precargados de cualquier contacto[cite: 1].
* **Eliminar registros:** Borrado de registros con cuadro de diálogo de confirmación para evitar eliminaciones accidentales[cite: 1].
* **Validación de datos:** Control estricto de campos requeridos (longitud mínima de nombre, validación de dígitos telefónicos y formato de correo)[cite: 1].
* **Diseño limpio y moderno:** Interfaz desarrollada con **Material Design 3** y Jetpack Compose[cite: 1].

---

## 🛠️ Tecnologías y Arquitectura

* **Lenguaje:** Kotlin
* **UI:** Jetpack Compose & Material 3
* **Base de datos local:** Android Jetpack Room (Entity, DAO, Database)[cite: 1]
* **Arquitectura:** MVVM (Model - View - ViewModel) + Repository Pattern
* **Manejo de estados:** StateFlow & Kotlin Coroutines

---

## 💻 Instrucciones de Ejecución

1. Clonar el repositorio:
   ```bash
   git clone [https://github.com/StefanyGarcia4/AgendaContactos-DSW22.git](https://github.com/StefanyGarcia4/AgendaContactos-DSW22.git)
