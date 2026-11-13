import java.util.Scanner;

public class DivisionNumeros {
    public static void main(String[] args) {
        
        //CREAR SCANNER
        Scanner teclado = new Scanner (System.in);

        //DECLARAR VARIABLES
        System.out.println("INTRODUCE EL PRIMER NUMERO: ");
        int numero1 = teclado.nextInt();

        System.out.println("INTRODUCE EL SEGUNDO NUMERO: ");
        int numero2 = teclado.nextInt();
        


        //CAPTURAMOS LA EXCEPCION CON TRY-CATCH
        try {
        int division = numero1 / numero2;
        System.out.println("El resultado de la division es: " +  division);

            
        } catch (ArithmeticException e) {
            System.out.println("Error. No se puede dividir entre 0.");
            
        }

        //CERRAMOS EL SCANNER
        teclado.close();

    }
}

