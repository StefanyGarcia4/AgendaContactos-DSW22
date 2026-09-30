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


## 🔑 Crear el `debug.keystore` (obligatorio)

El proyecto firma la versión debug con un archivo `debug.keystore` que **no está incluido en el repositorio**. Antes de ejecutar la app, créalo en la raíz del proyecto.

1. Abre una terminal (CMD o PowerShell) y ve a la carpeta donde clonaste el proyecto:

```bash
   cd RUTA\DONDE\CLONASTE\AgendaContactos-DSW22
```

2. Ejecuta el siguiente comando (Windows, con la ruta por defecto de Android Studio):

```bash
   "C:\Program Files\Android\Android Studio\jbr\bin\keytool.exe" -genkeypair -v -keystore debug.keystore -storepass android -alias androiddebugkey -keypass android -keyalg RSA -keysize 2048 -validity 10000 -dname "CN=Android Debug,O=Android,C=US"
```

   > Si instalaste Android Studio en otra ruta, o si usas macOS/Linux, reemplaza la ruta de `keytool` por la de tu instalación, o usa simplemente `keytool` si tienes un JDK 17 en el PATH.

3. Verifica que el archivo `debug.keystore` quedó en la raíz del proyecto (junto a `settings.gradle.kts`).
4. En Android Studio, haz *File > Sync Project with Gradle Files* y presiona **Run**.

> ⚠️ La ruta del proyecto no debe tener acentos ni caracteres especiales (por ejemplo, evita `Imágenes`), porque el plugin de Android falla en Windows.
