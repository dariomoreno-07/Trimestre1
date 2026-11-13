import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Locale;

public class ReservasHotel {

    //METODO PARA ALMACENAR LOS DATOS
    public static void validarReserva(String nombre, int habitacion, int noches, double precioNoche) 
    throws IllegalArgumentException {


        //PRIMERA EXCEPCION: Evitar que el nombre del cliente este vacio
        if (nombre == null  || nombre.trim().isEmpty()) {

            throw new IllegalArgumentException("El nombre del cliente no puede estar vacio. ");

        }

        //SEGUNDA EXCEPCION: Requerir que el numero de noches debe ser mayor que 0
        if (habitacion <= 0 ) {
            throw new IllegalArgumentException("El numero de habitaciones debe ser mayor a 0");
            
        }

        //TERCERA EXCEPCION: Obligar al usuario a que escoja un numero de noches superior a 0
        if (noches <= 0) {
        throw new IllegalArgumentException("El numero de noches debe ser superior a 0");
            
        }


        //CUARTA EXCEPCION: Obligar al usuario a que el precio por noche sea mayor que 0
        if (precioNoche <= 0) {
            throw new IllegalArgumentException("El precio por noche debe ser superior a 0");
            
        }
        
    }

    



    //METODO ESTATICO PARA CALCULAR EL IMPORTE TOTAL DE LA ESTANCIA
    public static double calcularPrecio(int noches, double precioNoche) {

        return noches * precioNoche;
    }

    public static void main(String[] args) {

        //CREAMOS EL SCANNER
        Scanner sc = new Scanner (System.in);
        
        System.out.println("===== SISTEMA DE RESERVAS DE HOTEL =====\n");
        System.out.print("Nombre del cliente: ");
        String nombre = sc.nextLine();


        //
        

        System.out.print("Numero de habitación: ");
        int habitacion = sc.nextInt();
        System.out.print("Numero de noches: ");
        int noches = sc.nextInt();
        System.out.print("Precio por noche: ");
        double precioNoche = sc.nextDouble();

        System.out.println("");



        sc.close();

        try {

            //Llamada al metodo de validacion dentro del try-catch

            validarReserva(nombre, habitacion, noches, precioNoche);

            //Si no se lanza excepcion, la reserva es correcta
            double precioTotal = calcularPrecio(noches, precioNoche);

            System.out.println("- - RESERVA REALIZADA CORRECTAMENTE - -");
            System.out.println();

            System.out.println("CLIENTE: " + nombre);
            System.out.println("HABITACIÓN: " + habitacion);
            System.out.println("Nº DE NOCHES: " + noches);
            System.out.println("PRECIO POR NOCHE: " + precioNoche + "$");
            System.out.println("PRECIO TOTAL: " + precioTotal + "$");


        } catch (IllegalArgumentException e) {
        //Captura de la excepcion y muestra el mensaje de error
        System.out.println("RESERVA INCORRECTA, POR FAVOR, REVISE LOS DATOS");
          
        }
    }
}