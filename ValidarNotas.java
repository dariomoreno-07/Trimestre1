import java.util.Scanner;

public class ValidarNotas {

    // throws: declara que puede lanzar IllegalArgumentException
    public static void validarNota(double nota) throws IllegalArgumentException {
        if (nota < 0 || nota > 10) {
            throw new IllegalArgumentException("La nota debe estar comprendida entre 0 y 10.");
        }
        System.out.println("Nota introducida correctamente: " + nota);
    }

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);
        boolean valida = false;

        while (!valida) {
            System.out.print("Introduce una nota: ");
            double nota = teclado.nextDouble();

            try {
                validarNota(nota);
                valida = true;   // solo se ejecuta si validarNota NO lanzó excepción
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println();
            }
        }

        teclado.close();
    }
}