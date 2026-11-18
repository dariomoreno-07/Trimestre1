import java.util.Scanner;   // Scanner permite leer lo que el usuario escribe por teclado

/*
 * LA MAZMORRA
 * ----------------------------------------------------------------------------
 * Juego de consola en el que controlas a una "P" dentro de una matriz (tablero).
 *
 * OBJETIVO: conseguir la llave en la tienda, abrir la puerta del nivel 1,
 *           derrotar al jefe del nivel 2 y coger el tesoro.
 *
 * ESTRUCTURA DEL PROGRAMA (programación estructurada: solo métodos estáticos):
 *   1. Constantes y variables  -> los datos que el juego necesita recordar
 *   2. main y menús            -> el punto de entrada y el menú principal
 *   3. Lectura segura          -> leer números sin que el programa se rompa
 *   4. Partida                 -> reiniciar, repetir y jugar una partida
 *   5. Preparación de niveles  -> crear el tablero y colocar los elementos
 *   6. Dibujo del tablero      -> mostrar el mapa por pantalla
 *   7. Movimiento              -> mover a la P y reaccionar a cada casilla
 *   8. Cofres                  -> contenido aleatorio de los cofres
 *   9. Tienda                  -> comprar vida y conseguir la llave
 *  10. Combate contra el jefe  -> pelea por turnos
 */
public class JuegoMazmorra {

    // =====================================================
    //  1. CONSTANTES Y VARIABLES
    // =====================================================

    // ---------- CARACTERES DEL TABLERO ----------
    // Cada elemento del mapa se guarda como un carácter dentro de la matriz.
    // Son constantes (static final) para no repetir letras sueltas por el código
    // y poder cambiarlas en un único sitio.
    static final char PUNTO = '.';
    static final char JUGADOR = 'P';
    static final char TRAMPA = 'X';    // oculta
    static final char ENEMIGO = 'E';   // oculto
    static final char MONEDA = 'o';
    static final char COFRE = 'C';
    static final char COFRE_ESPADA = 'Z';   // cofre con la espada (en el mapa se ve como una C)
    static final char TIENDA = 'S';
    static final char JEFE = 'B';
    static final char PUERTA = 'D';
    static final char TESORO = '*';

    // ---------- PRECIOS Y COMBATE ----------
    // Números que controlan la dificultad. Si el juego es muy fácil o muy difícil,
    // basta con cambiar estos valores sin tocar la lógica.
    static final int PRECIO_VIDA = 2;
    static final int VIDA_JEFE = 3;
    static final double PROB_ACIERTO_JUGADOR = 0.7;  // 70 % de golpear al jefe
    static final double PROB_ACIERTO_JEFE = 0.4;     // 40 % de que el jefe te golpee
    static final int DANIO_BASE = 1;                 // dano al jefe sin espada
    static final int DANIO_ESPADA = 2;               // dano al jefe con espada

    // ---------- CONFIGURACION (se conserva entre partidas) ----------
    // Lo que el jugador elige en el menú de configuración. No se reinicia al
    // empezar una partida: se usa para saber con cuántas vidas arrancar.
    static int vidasConfig = 3;

    // ---------- ESTADO DEL JUEGO ----------
    // Todo lo siguiente es "static" porque los métodos del programa son estáticos
    // y solo pueden usar directamente variables estáticas.

    // El Scanner es static para poder usarlo en los menus, la tienda y el combate
    static Scanner sc = new Scanner(System.in);

    // El tamano ya no es fijo: cada nivel crea su propia matriz
    // (tablero[fila][columna]: la primera posición es la fila y la segunda la columna)
    static char[][] tablero;

    // Dónde está la P ahora mismo
    static int filaJugador;
    static int colJugador;
    static char casillaBajo = PUNTO;   // que hay debajo de la P (por ejemplo la tienda)

    // Datos de la partida en curso (se reinician en reiniciarPartida)
    static int vidas;
    static int monedas;
    static boolean tieneLlave;
    static boolean victoria;
    static boolean jefeDerrotado;
    static boolean tieneEspada;
    static int nivel;

    // =====================================================
    //  2. MAIN Y MENU PRINCIPAL
    // =====================================================

    // El programa empieza aquí. Solo se encarga del menú principal: muestra las
    // opciones, lee la elegida y llama al método correspondiente.
    public static void main(String[] args) {
        boolean salir = false;

        // El menu se repite hasta que el usuario elige "Salir"
        while (!salir) {
            mostrarMenuPrincipal();
            int opcion = leerEntero(1, 5);   // devuelve siempre un número válido entre 1 y 5

            switch (opcion) {
                case 1: nuevaPartida(); break;
                case 2: menuConfiguracion(); break;
                case 3: mostrarInstrucciones(); break;
                case 4: mostrarCreditos(); break;
                case 5:
                    salir = true;   // hace que el while termine y el programa acabe
                    System.out.println("\nHasta pronto!");
                    break;
            }
        }

        sc.close();   // cerramos el Scanner al terminar el programa
    }

    // Solo escribe las opciones del menú; no lee nada ni decide nada
    static void mostrarMenuPrincipal() {
        System.out.println("\n=== LA MAZMORRA ===");
        System.out.println("1. Nueva Partida");
        System.out.println("2. Configuracion");
        System.out.println("3. Instrucciones");
        System.out.println("4. Creditos");
        System.out.println("5. Salir");
    }

    // Permite elegir las vidas iniciales; el valor se guarda en vidasConfig
    static void menuConfiguracion() {
        System.out.println("\n=== CONFIGURACION ===");
        System.out.println("Vidas iniciales (actual: " + vidasConfig + ")");
        System.out.println("1. 3 vidas");
        System.out.println("2. 4 vidas");
        System.out.println("3. 5 vidas");

        // Se lee 1, 2 o 3 y se le suma 2 para convertirlo en las vidas reales
        vidasConfig = leerEntero(1, 3) + 2;   // opcion 1 -> 3, opcion 2 -> 4, opcion 3 -> 5
        System.out.println("Configuracion guardada: " + vidasConfig + " vidas");
    }

    // Explica al jugador cómo se juega
    static void mostrarInstrucciones() {
        System.out.println("\n=== INSTRUCCIONES ===");
        System.out.println("Eres la P. Explora el nivel 1 y encuentra la tienda (S).");
        System.out.println("En la tienda consigue la llave (???) y abre la puerta (D).");
        System.out.println("Tras la puerta hay una mazmorra pequena con un jefe (B).");
        System.out.println("Derrota al jefe para poder coger el tesoro (*) y ganar.");
        System.out.println();
        System.out.println("Controles: W arriba, S abajo, A izquierda, D derecha, Q salir.");
        System.out.println("Cuidado: hay trampas y enemigos ocultos. Las monedas (o) sirven");
        System.out.println("para restaurar vidas en la tienda.");
        System.out.println("Los cofres (C) pueden dar monedas, una vida... o ser una trampa.");
        System.out.println("Uno de ellos esconde una espada que hace mas dano al jefe.");
    }

    static void mostrarCreditos() {
        System.out.println("\n=== CREDITOS ===");
        System.out.println("Juego: La Mazmorra");
        System.out.println("Autor: [Tu nombre]");
    }

    // =====================================================
    //  3. LECTURA SEGURA DE NUMEROS (menus)
    // =====================================================
    // Problema: si el usuario escribe una letra donde se espera un número, el
    // programa se rompe. Solución: dos métodos que leen, comprueban y repiten
    // hasta que la entrada es correcta.

    // Lanza una excepcion con throw si la opcion no existe en el menu
    // ("throws" en la firma avisa de que este método puede lanzar la excepción;
    //  "throw" dentro del método es lo que la lanza de verdad)
    static void validarOpcion(int opcion, int min, int max) throws IllegalArgumentException {
        if (opcion < min || opcion > max) {
            throw new IllegalArgumentException("Opcion inexistente. Elige entre " + min + " y " + max);
        }
    }

    // Lee un entero entre min y max y repite hasta que sea valido
    static int leerEntero(int min, int max) {
        while (true) {   // bucle "infinito": solo se sale con el return cuando todo es correcto
            try {
                System.out.print("Seleccione una opcion: ");
                // Se lee la línea entera como texto y luego se convierte a número.
                // Si el texto no es un número (por ejemplo "hola"), parseInt lanza NumberFormatException.
                int opcion = Integer.parseInt(sc.nextLine().trim());
                validarOpcion(opcion, min, max);   // si está fuera de rango, lanza IllegalArgumentException
                return opcion;                     // solo se llega aquí si no hubo ningún error
            } catch (NumberFormatException e) {
                // Va primero porque NumberFormatException hereda de IllegalArgumentException
                System.out.println("Error: debes introducir un numero entero");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());   // muestra el texto puesto en el throw
            }
        }
    }

    // =====================================================
    //  4. PARTIDA
    // =====================================================

    // Deja el estado como al empezar, usando las vidas de la configuracion.
    // Es imprescindible porque las variables son static y conservan su valor:
    // sin esto, la segunda partida empezaría con la llave y la espada ya conseguidas.
    static void reiniciarPartida() {
        vidas = vidasConfig;
        monedas = 0;
        tieneLlave = false;
        victoria = false;
        jefeDerrotado = false;
        tieneEspada = false;
        nivel = 1;
    }

    // Repite partidas mientras el jugador quiera jugar otra vez
    // (do-while: la partida se juega siempre al menos una vez y luego se pregunta)
    static void nuevaPartida() {
        boolean otraVez;

        do {
            jugarPartida();
            otraVez = preguntarJugarOtraVez();
        } while (otraVez);   // si responde "No", se vuelve al menú principal
    }

    // Devuelve true si el jugador quiere otra partida y false si no
    static boolean preguntarJugarOtraVez() {
        System.out.println("\nDesea jugar otra vez?");
        System.out.println("1. Si");
        System.out.println("2. No");
        return leerEntero(1, 2) == 1;   // la comparación da true si eligió la opción 1
    }

    // Una partida completa: este es el antiguo contenido del main
    static void jugarPartida() {
        reiniciarPartida();
        prepararNivel1();

        boolean salir = false;

        // Cada vuelta del bucle es un turno. La partida continúa mientras el
        // jugador no abandone, le queden vidas y no haya ganado.
        while (!salir && vidas > 0 && !victoria) {
            mostrarTablero();
            System.out.println("Mover: W S A D | Q (salir)");
            System.out.print("Opcion: ");

            // trim() quita espacios y toLowerCase() convierte a minúsculas: da igual escribir W o w
            String opcion = sc.nextLine().trim().toLowerCase();

            // Cada tecla llama a mover con cuánto cambia la fila y la columna:
            //   arriba  = fila -1      abajo     = fila +1
            //   izquierda = columna -1 derecha   = columna +1
            switch (opcion) {
                case "w": mover(-1, 0); break;
                case "s": mover(1, 0);  break;
                case "a": mover(0, -1); break;
                case "d": mover(0, 1);  break;
                case "q": salir = true; break;
                default:
                    System.out.println("Movimiento no valido");
            }
        }

        // Al salir del bucle hay tres finales posibles: victoria, derrota o abandono
        if (victoria) {
            System.out.println("\nFELICIDADES! Has encontrado el tesoro y has escapado de la mazmorra");
        } else if (vidas <= 0) {
            System.out.println("\nGAME OVER, te has quedado sin vidas");
            mostrarTableroFinal();   // revela dónde estaban las trampas y los enemigos
        } else {
            System.out.println("\nHas abandonado la partida");
        }

        System.out.println("Fin de la partida.");
    }

    // =====================================================
    //  5. PREPARACION DE NIVELES
    // =====================================================
    // Un nivel es una matriz nueva con su tamaño y sus elementos. Cambiar de nivel
    // consiste simplemente en crear otro tablero con otras medidas.

    // Crea la matriz del tamano pedido, la llena de puntos y coloca al jugador
    static void crearTablero(int filas, int columnas, int filaInicio, int colInicio) {
        tablero = new char[filas][columnas];   // crea la matriz con el tamaño pedido

        // Dos for anidados recorren todas las casillas: i son las filas y j las columnas
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                tablero[i][j] = PUNTO;
            }
        }

        // Se guarda la posición de inicio y se dibuja la P en esa casilla
        filaJugador = filaInicio;
        colJugador = colInicio;
        tablero[filaJugador][colJugador] = JUGADOR;
        casillaBajo = PUNTO;
    }

    // Nivel 1: mazmorra grande con tienda y puerta
    static void prepararNivel1() {
        nivel = 1;
        crearTablero(10, 20, 5, 10);   // 10 filas x 20 columnas, jugador en el centro
        colocar(TRAMPA, 15);
        colocar(ENEMIGO, 15);
        colocar(MONEDA, 10);
        colocar(COFRE, 4);
        colocar(COFRE_ESPADA, 1);   // la espada esta en un unico cofre
        colocar(TIENDA, 1);
        colocar(PUERTA, 1);
    }

    // Nivel 2: mazmorra pequena con el jefe y el tesoro en una esquina
    static void prepararNivel2() {
        nivel = 2;
        crearTablero(6, 12, 3, 0);   // 6 filas x 12 columnas, jugador a la izquierda

        // El tesoro va en la esquina inferior derecha (antes de colocar lo demas)
        // Última fila = tablero.length - 1; última columna = tablero[0].length - 1
        tablero[tablero.length - 1][tablero[0].length - 1] = TESORO;

        colocar(TRAMPA, 6);
        colocar(ENEMIGO, 4);
        colocar(COFRE, 2);
        colocar(JEFE, 1);
        System.out.println("\nCruzas la puerta y entras en una mazmorra mas pequena...");
    }

    // Coloca "cantidad" elementos del tipo "simbolo" en casillas vacias al azar
    static void colocar(char simbolo, int cantidad) {
        int colocados = 0;

        // Se repite hasta colocar todos. Si el azar elige una casilla ocupada,
        // no se cuenta y se vuelve a intentar con otra.
        while (colocados < cantidad) {
            // Math.random() da un decimal entre 0 y 0,999...; al multiplicarlo por el
            // tamaño y quitar los decimales con (int) sale una posición válida
            int f = (int) (Math.random() * tablero.length);
            int c = (int) (Math.random() * tablero[0].length);

            if (tablero[f][c] == PUNTO) {   // solo se coloca en casillas vacías
                tablero[f][c] = simbolo;
                colocados++;
            }
        }
    }

    // =====================================================
    //  6. DIBUJO DEL TABLERO
    // =====================================================

    // Muestra el tablero ocultando trampas y enemigos
    static void mostrarTablero() {
        // Línea de estado: se construye uniendo texto y variables con "+".
        // (condición ? valorSiSí : valorSiNo) es un if/else en una sola línea.
        System.out.println("\n --- NIVEL " + nivel
                + " | VIDAS: " + vidas
                + " | MONEDAS: " + monedas
                + " | LLAVE: " + (tieneLlave ? "SI" : "NO")
                + (tieneEspada ? " | ESPADA" : "") + " ---");

        // Recorre la matriz fila a fila. print escribe sin salto de línea para dibujar
        // toda una fila en una sola línea; el println() del final baja a la siguiente.
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {
                char casilla = tablero[i][j];

                if (casilla == TRAMPA || casilla == ENEMIGO) {
                    System.out.print(PUNTO + " ");   // lo peligroso se dibuja como un punto
                } else if (casilla == COFRE_ESPADA) {
                    System.out.print(COFRE + " ");   // parece un cofre normal
                } else {
                    System.out.print(casilla + " ");   // el resto se ve tal cual
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    // Muestra todo el tablero revelado (para el GAME OVER)
    static void mostrarTableroFinal() {
        System.out.println("\n --- TABLERO REVELADO ---");
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[0].length; j++) {
                System.out.print(tablero[i][j] + " ");   // aquí no se oculta nada
            }
            System.out.println();
        }
        System.out.println();
    }

    // =====================================================
    //  7. MOVIMIENTO
    // =====================================================
    // Es el método central del juego: mueve a la P y reacciona a lo que haya en
    // la casilla de destino. Sigue siempre estos pasos:
    //   1. Calcular la casilla de destino
    //   2. Comprobar que no se sale del tablero
    //   3. Mirar qué hay en el destino
    //   4. Si es jefe, puerta o tesoro protegido: NO moverse
    //   5. Si no: moverse y aplicar el efecto de la casilla

    static void mover(int deltaFila, int deltaCol) {
        // 1. La casilla de destino es la posición actual más el desplazamiento
        int nuevaFila = filaJugador + deltaFila;
        int nuevaCol = colJugador + deltaCol;

        // 2. Validar que no se salga del tablero
        // (las filas válidas van de 0 a length-1; fuera de ese rango daría un error)
        if (nuevaFila < 0 || nuevaFila >= tablero.length
                || nuevaCol < 0 || nuevaCol >= tablero[0].length) {
            System.out.println("No puedes salir del tablero");
            return;   // termina el método sin mover nada
        }

        // 3. Miramos que hay en la casilla de destino ANTES de movernos
        // (hay que mirar antes de dibujar la P encima, porque después ya no se vería)
        char destino = tablero[nuevaFila][nuevaCol];

        // 4. Casillas a las que NO nos movemos: jefe, puerta y tesoro (si esta protegido)
        if (destino == JEFE) {
            combatirJefe(nuevaFila, nuevaCol);
            return;
        }

        if (destino == PUERTA) {
            if (tieneLlave) {
                System.out.println("Abres la puerta con la llave secreta");
                prepararNivel2();   // cambia de nivel creando un tablero nuevo
            } else {
                System.out.println("La puerta esta cerrada. Necesitas la llave secreta");
            }
            return;
        }

        if (destino == TESORO && !jefeDerrotado) {
            System.out.println("El tesoro esta protegido. Primero debes derrotar al jefe");
            return;
        }

        // 5. Movimiento normal: dejamos en la casilla vieja lo que habia debajo
        // (casillaBajo vale PUNTO casi siempre; vale TIENDA si estábamos sobre la tienda)
        tablero[filaJugador][colJugador] = casillaBajo;
        filaJugador = nuevaFila;
        colJugador = nuevaCol;
        casillaBajo = PUNTO;   // por defecto, la casilla nueva quedará vacía al irnos

        // Efecto de la casilla nueva
        switch (destino) {
            case TRAMPA:
                vidas -= 1;
                System.out.println("PISASTE UNA TRAMPA, has perdido una vida");
                break;
            case ENEMIGO:
                vidas -= 2;
                System.out.println("Te has enfrentado a un enemigo, has perdido 2 vidas");
                break;
            case MONEDA:
                monedas++;
                System.out.println("Has encontrado una moneda");
                break;
            case COFRE:
                abrirCofre();
                break;
            case COFRE_ESPADA:
                tieneEspada = true;
                System.out.println("\nAbres un cofre... Dentro hay una ESPADA! Haras mas dano al jefe");
                break;
            case TIENDA:
                casillaBajo = TIENDA;   // la tienda no desaparece cuando te vas
                abrirTienda();
                break;
            case TESORO:
                victoria = true;
                break;
        }

        // Por último se dibuja la P en su nueva posición
        tablero[filaJugador][colJugador] = JUGADOR;
    }

    // =====================================================
    //  8. COFRES
    // =====================================================
    // Un cofre es un símbolo más del mapa. Al pisarlo se llama a abrirCofre, y como
    // casillaBajo vale PUNTO, el cofre desaparece una vez abierto.

    // Devuelve un entero al azar entre min y max (ambos incluidos)
    static int numeroAleatorio(int min, int max) {
        // Ejemplo con (2, 5): random*4 da 0..3,99 -> (int) da 0..3 -> sumando 2 sale 2..5
        return min + (int) (Math.random() * (max - min + 1));
    }

    // Abre un cofre: el contenido se decide al azar entre 4 posibilidades
    static void abrirCofre() {
        System.out.println("\nAbres un cofre...");

        // Cada número del 1 al 4 tiene un 25 % de probabilidad
        switch (numeroAleatorio(1, 4)) {
            case 1:   // monedas
                int ganadas = numeroAleatorio(2, 5);
                monedas += ganadas;
                System.out.println("Contiene " + ganadas + " monedas");
                break;
            case 2:   // una vida
                vidas++;
                System.out.println("Contiene una pocion: recuperas 1 vida");
                break;
            case 3:   // trampa oculta
                vidas--;
                System.out.println("Era una trampa! Pierdes 1 vida");
                break;
            default:   // cuarto caso: vacío
                System.out.println("Esta vacio");
        }
    }

    // =====================================================
    //  9. TIENDA
    // =====================================================
    // Menú propio de la tienda. Se repite hasta que el jugador elige salir, así
    // puede comprar varias veces seguidas.

    static void abrirTienda() {
        boolean salirTienda = false;

        while (!salirTienda) {
            System.out.println("\n=== TIENDA === Monedas: " + monedas + " | Vidas: " + vidas);
            System.out.println("1. Restaurar 1 vida (" + PRECIO_VIDA + " monedas)");
            System.out.println("2. ???  (gratis)");
            System.out.println("3. Salir de la tienda");

            int opcion = leerEntero(1, 3);   // misma lectura segura que el menú principal

            switch (opcion) {
                case 1:
                    // Solo se cobra si hay monedas suficientes
                    if (monedas >= PRECIO_VIDA) {
                        monedas -= PRECIO_VIDA;
                        vidas++;
                        System.out.println("Has recuperado 1 vida");
                    } else {
                        System.out.println("No tienes suficientes monedas");
                    }
                    break;
                case 2:
                    // La llave es gratis, pero solo se puede coger una vez
                    if (tieneLlave) {
                        System.out.println("Ya te llevaste el objeto misterioso");
                    } else {
                        tieneLlave = true;
                        System.out.println("Has obtenido una LLAVE! Seguro que abre alguna puerta");
                    }
                    break;
                case 3:
                    salirTienda = true;
                    break;
            }
        }
    }

    // =====================================================
    //  10. COMBATE CONTRA EL JEFE
    // =====================================================
    // Un combate es un bucle que se repite hasta que muere uno de los dos.
    // Cada vuelta es una ronda: primero ataca el jugador y luego el jefe.
    // "f" y "c" son la posición del jefe, para borrarlo si lo derrotamos.

    static void combatirJefe(int f, int c) {
        int vidaJefe = VIDA_JEFE;   // vida local: cada combate empieza con la vida completa
        System.out.println("\nTe enfrentas al JEFE!");

        while (vidaJefe > 0 && vidas > 0) {
            System.out.println("Tus vidas: " + vidas + " | Vida del jefe: " + vidaJefe);
            System.out.print("Pulsa ENTER para atacar...");
            sc.nextLine();   // espera a que se pulse ENTER para que se vea cada ronda

            // Turno del jugador
            // Math.random() < 0.7 es verdadero el 70 % de las veces: así se simula el azar
            if (Math.random() < PROB_ACIERTO_JUGADOR) {
                int danio = tieneEspada ? DANIO_ESPADA : DANIO_BASE;   // con espada, más daño
                vidaJefe -= danio;
                System.out.println("Golpeas al jefe y le quitas " + danio + " vida(s)");
            } else {
                System.out.println("Fallas el golpe");
            }

            // Turno del jefe (solo si sigue vivo)
            // Si ya está muerto no ataca, para que un jefe derrotado no pueda herirte
            if (vidaJefe > 0) {
                if (Math.random() < PROB_ACIERTO_JEFE) {
                    vidas--;
                    System.out.println("El jefe te hiere, pierdes una vida");
                } else {
                    System.out.println("Esquivas el ataque del jefe");
                }
            }
        }

        // Si ganamos, el jefe desaparece y el tesoro queda libre
        // Si el bucle terminó porque nos quedamos sin vidas, aquí no se entra
        // y el juego acaba por el while de jugarPartida
        if (vidaJefe <= 0) {   // <= 0 porque con la espada puede quedar en negativo
            jefeDerrotado = true;
            tablero[f][c] = PUNTO;   // el jefe desaparece del mapa
            System.out.println("\nHas derrotado al jefe! Ya puedes coger el tesoro");
        }
    }
}
