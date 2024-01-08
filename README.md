Desafio CRUD

Camilo Navarrete Portiño:


Instrucciones:


1.- Clonar repositorio a ambiente local.

2.- Configurar entorno de trabajo para Java + SpringBoot + Maven.

3.- Levantar proyecto


En este punto segun los requerimientos solicitados:


Registro
● Ese endpoint deberá recibir un usuario con los campos "nombre", "correo", "contraseña",
más un listado de objetos "teléfono", respetando el siguiente formato:
{
"name": "Juan Rodriguez",
"email": "juan@rodriguez.org",
"password": "hunter2",
"phones": [
{
"number": "1234567",
"citycode": "1",
"contrycode": "57"
}
]
}


Como para este caso no se especifica si el usuario debe tener autenticacion previo a crear el usuario, se asume que debe existir un usuario maestro que permita dar acceso al uso de aquel endpoint,
de otro modo quedaria libre desde un inicio.


Por dicho motivo se configuro en properties un usuario valido solo para fines de activar la autenticacion:

El usuario es
JSON:


{
"email": "admin@mail.com"
"pass": "admin123",
}

Se debe invocar en la ruta localhost:8080/login como Body en formato raw JSON

<img width="958" alt="image" src="https://github.com/CryNoF/desafio/assets/49507741/0e45b244-4617-4a31-9475-610324d912ee">


Una vez validado, retornara un Bearer token para ser usado en la consulta de creacion del usuario.

Para este punto, no existe aun usuarios en la DB H2, por lo que para crearlos se debe invocar al endpoint

localhost:8080/user/create

El token obtenido del login, debe ser agregado en la pestaña Autorizacion como Bearer Token:

<img width="1234" alt="image" src="https://github.com/CryNoF/desafio/assets/49507741/cc30f2a0-0e6e-49ff-8056-b21121e5371b">

Se le debe enviar en formato JSON conteniendo los siguientes campos:

{
"name": "Camilo Navarrete",
"email": "cnavarrete@gmail.com",
"password": "prueba",
"phones": [
{
"number": "1234567",
"citycode": "1",
"countrycode": "57"
}
]
}

Esto retornara los campos solicitados:

○ name: nombre del usuario
○ id: id del usuario en UUID
○ created: fecha de creación del usuario
○ modified: fecha de la última actualización de usuario
○ last_login: del último ingreso (en caso de nuevo usuario, va a coincidir con la
fecha de creación)
○ token: token de acceso de la API en JWT
○ isactive: Indica si el usuario sigue habilitado dentro del sistema.

<img width="1233" alt="image" src="https://github.com/CryNoF/desafio/assets/49507741/b34c5969-6c3d-4d49-99bd-95f6c49baf58">

<img width="1678" alt="image" src="https://github.com/CryNoF/desafio/assets/49507741/c51419bf-3b67-4cbf-aade-dd9e0dfc867e">

Este usuario ya creado servira para poder generar nuevos logins, es decir, las credenciales de mail y password del usuario en DB,
seran posibles de utilizar para generar sesiones a partir de su propio token y por ende poder utilizar el endpoint de creacion de usuarios
como lo permitio en un inicio el SuperUser. Esto se puede comprobar tomando el token generado al momento de crear el usuario como llave para 
los endopints, continuando en operacion bajo el nuevo token. Si el usuario decide generar un login con las credenciales del usuario creado
se actualizara la fecha de ultimo ingreso.


● Responder el código de status HTTP adecuado

Se devuelven errores del tipo <img width="553" alt="image" src="https://github.com/CryNoF/desafio/assets/49507741/dfaad596-b02a-405d-a720-6138599dc7a1">


● Si caso el correo conste en la base de datos, deberá retornar un error "El correo ya
registrado".

En este caso arroja por consola el mensaje de error "El correo electrónico ya está registrado"

● El correo debe seguir una expresión regular para validar que formato sea el correcto.
(aaaaaaa@dominio.cl)

La expresion regular esta configurada como propiedad la cual es usada como validacion, y es modificable segun se requiera.


● La clave debe seguir una expresión regular para validar que formato sea el correcto. (El
valor de la expresión regular debe ser configurable)

La clave no recurre a la expresion regular, sino mas bien es encriptada mediante el algoritmo Base64 BCryptPasswordEncoder,
el cual se encarga de encodear o decodear el texto plano ingresado por el JSON segun una llave maestra de validacion configurada como propiedad.

El token deberá ser persistido junto con el usuario

Estos datos quedan persistidos en la base de datos, pero como estamos hablando de una base de datos en memoria H2, al bajar la aplicacion igualmente son borrados,
por la misma caracteristica de la DB volatil. De otro modo se tendria que haber recurrido a DB en HDD.

Si se requiere ver los campos en la DB, basta con ingresar a localhost:8080/h2-console e ingresar los siguientes datos:

Usuario: cnavarrete
Password: desafio

<img width="497" alt="image" src="https://github.com/CryNoF/desafio/assets/49507741/3f3875a6-30aa-4c3b-b56f-154aba0c8acb">

Respecto a Swagger, se dejo hecha la implementacion pero hubieron problemas de compatibilidad de las dependencias con la version 3.2 de SpringBoot, dando error de HttpServlet (lo cual ha sido reportado por la comunidad)
por lo que era imposible levantar el proyecto sin omitir dichas dependencias.



Si bien se crearon metodos (No solicitados para este desafio) correspondientes a lo que un CRUD debiese tener, se centro la atencion en el funcionamiento del metodo de creacion
segun las caracteristicas solicitadas y en el proceso de seguridad de Spring Security y JWT.
Los demas metodos extras necesitan ser reestructurados en implementacion puesto que los metodos de JPA por ID, no son compatibles directamente con el formato UUID, sino mas bien Long,
por lo que se requiere una implementacion especial para cada uno.



