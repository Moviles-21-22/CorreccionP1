# Arquitectura 0hn0

Para realizar la práctica multiplataforma se dispone de los siguientes módulos:

 ![image](https://user-images.githubusercontent.com/47497948/141697394-0b9e7560-50cf-4004-b82f-39043564833f.png)

 - **Engine**: es el módulo que contiene las interfaces y clases comunes para ambas plataformas. No tiene dependencias.
 - **GameLogic**: módulo contenedor de todas las clases necesesarias para implementar la lógica del juego, es decir, contiene cada uno de los estados del juego (clases que implementan la interfaz **App** de **Engine**) así como todos los objetos, funciones y algoritmos necesarios para la resolución del tablero, pistas, renderizado, etc. Tiene una dependencia de **Engine**.
 - **AndroidEngine**: contiene las clases que implementan cada unas de las interfaces del módulo Engine para que funcione en dispositivos Android. Tiene una dependencia con **Engine**
 - **DesktopEngine**: análogamente igual que AndroidEngine, también con la dependencia de **Engine**.

<img src = "https://user-images.githubusercontent.com/47497948/141701064-c2fab299-0f91-4463-ae87-bbfb5be2f8d8.png" width = "300" allignment = "left">

<img src = "https://user-images.githubusercontent.com/47497948/141701264-c99aa58a-5a7e-48a9-a6cb-469ce9100964.png" width = "280">

 - **app / DesktopGame**: sirven únicamente para inicializar el juego en dispositivos Android y en PC respectivamente, de manera que dependen del módulo **GameLogic** y de **Engine**, puesto que hace falta poner en marcha el motor.
 
 <div style="page-break-after: always;"></div>

 # Módulo: Engine

Se han creado las iterfaces pedidas en el enunciado de la práctica, ampliando debidamente con **AbstractGraphics**, **ButtonCallback** y **Vector2**.

![image](https://user-images.githubusercontent.com/47497948/141698235-68754608-9643-4bd3-9869-0dded8306fe3.png)

- **AbstractGraphics**: implementa la interfaz de Graphics, de manera que contiene cálculos comunes para las transformaciones de los objetos en Android y PC.
- **ButtonCallback**: interfaz que contiene un método **doSomething** diseñado para crear callbacks en los botones del juego.
- **Vector2**: contiene dos enteros (x, y) para ser utilizados a forma de vector o **Pair**.

 <div style="page-break-after: always;"></div>

# Módulo: GameLogic
<img src = "https://user-images.githubusercontent.com/47497948/141700937-1f7c9a29-b5a9-41c3-884a-7931d59332f7.png" width = 200>

Para la lógica hemos utilizado un sistema polimórfico a través de la herencia de **GameObject** de manera que en los **estados**, los cuales implementan la interfaz de **App**, poseen una lista de cada uno de los objetos del juego para procesar su input, render y update.

De esta misma manera funcionan los estados, es decir, desde el **Engine** principal, tanto de Android como de PC, se está llamando continuamente al **handleInput**, **render** y **update** del estado actual del juego.
