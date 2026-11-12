# Bitácora · Tarea 2 · BIS04

1. En `AsociadoServicio` agregué `cantidadAsociados()` (int), `totalAportes()` (double) y `buscarPorCedula(String)` (devuelve el `Asociado` o `null`).
2. También agregué `registrar(String, String, String)`, que recibe los textos del formulario, hace `trim()`, convierte el saldo y valida. Así el mensaje "El saldo debe ser un numero." sale del servicio y no de la ventana.
3. Puse el recorrido y la suma del total en el servicio porque ahí ya está el acceso a los datos (`dao.listar()`); la ventana solo pide el resultado y lo muestra.
4. Si el `for` que suma estuviera en el botón, habría que reescribirlo cada vez que otro reporte o ventana necesite el total. En el servicio cualquiera lo reutiliza.
5. La búsqueda también va en el servicio, con `equals()` y `trim()`, por la misma razón: es una regla de consulta, no de presentación.
6. No toqué `AsociadoArchivoDAO`: con `listar()` me alcanzó, y la lectura de archivos se queda en la capa de datos.
7. `VentanaPrincipal` solo depende de `AsociadoServicio` y del modelo; no importa clases de archivos ni crea el DAO. `Main` crea el servicio y se lo pasa a la ventana.
8. Los `if` de la ventana solo deciden qué mostrar (mensaje de éxito o de error, lista vacía, asociado no encontrado); ninguno valida datos.
9. Los cuatro mensajes del servicio se ven con `JOptionPane` y en una etiqueta de estado. El registro exitoso limpia los campos, actualiza la lista y avisa.
10. Eliminé `Prueba` y revisé que no quede ningún `System.out.println`.
11. Lo que no me funcionó / dudas: ( no me queria dar la opcion de commit ).
