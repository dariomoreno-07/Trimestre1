import java.util.InputMismatchException;
import java.util.Scanner;

public class ControlDatosTeclado {
    public static void main(String[] args) {
        
        //CREAMOS EL SCACNNER
        Scanner teclado = new Scanner (System.in);

        //CAPTURAMOS LA EXCEPCIOn
        try {
        System.out.println("INTRODUCE TU EDAD:");
        int edad = teclado.nextInt();

        if (edad < 0) {
        System.out.println("Error. Introduce un numero valido.");

        } else if (edad >= 18) {
        System.out.println("Eres mayor de edad.");

        } else {
        System.out.println("Eres menor de edad.");
    }
            
        } catch (InputMismatchException e) {
            System.out.println("Error. Introduce un numero valido.");
            teclado.nextLine();
            
        }

        teclado.close();
    }
}
