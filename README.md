# Sistema de Citas Médicas

Aplicación de consola desarrollada en Java para la gestión de doctores, pacientes y agendamiento de citas médicas. El proyecto aplica principios fundamentales de Programación Orientada a Objetos (POO) y persistencia de datos en archivos locales planos.

## Instalación y Configuración
1. Clonar el repositorio: `git clone https://github.com/mdem26x-lang/sistema-citas-medicas.git`
2. Abrir el proyecto en NetBeans IDE utilizando la opción "Open Project" (Requiere JDK 11 o superior).
3. Asegurarse de que las dependencias de Maven se descarguen correctamente.
4. El sistema cuenta con una carpeta llamada `db` en la raíz donde se almacena localmente la información.

## Uso del Programa
* Ejecutar la clase principal `SistemadeCitasMedicas`.
* Utilizar el menú interactivo por consola (Opciones 1 a 7).
* Al seleccionar la opción **7. Guardar y Salir**, el sistema escribe de forma automática los registros en `db/citas_medicas.txt`. Al reiniciar la aplicación, los datos se recargan de forma automática.

## Créditos
* **Desarrollador:** Víctor Miguel López Juárez
* Estudiante de Desarrollo de Software.

## Licencia
Este proyecto se distribuye bajo la Licencia MIT.
