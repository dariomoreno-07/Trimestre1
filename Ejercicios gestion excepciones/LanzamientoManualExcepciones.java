import java.util.Scanner;

public class LanzamientoManualExcepciones {
    

        /*Creamos un metodo estatico en formato Double que almacenara la variable numero y lanzara la excepcion con throws*/
        public static double calcularRaiz(double numero) throws IllegalArgumentException {
            if (numero < 0) {
                throw  new IllegalArgumentException ("Error. No se puede calcular la raiz de un numero negativo");
                
            }

            return Math.sqrt(numero);
        }


        
        public static void main(String[] args) {

        //Creamos el scanner
        Scanner teclado = new Scanner (System.in);
            

        System.out.println("INTRODUCE UN NUMERO: ");
        double numero = teclado.nextDouble();

        
    }
}

  

    
 