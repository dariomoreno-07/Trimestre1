import java.util.Scanner;

public class RaizCuadrada {

    // throws: declara que puede lanzar IllegalArgumentException
    public static double calcularRaiz(double numero) throws IllegalArgumentException {
        if (numero < 0) {
            // throw: lanza la excepción de verdad
            throw new IllegalArgumentException(
                    "No se puede calcular la raíz cuadrada de un número negativo.");
        }
        return Math.sqrt(numero);
    }

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        System.out.print("Introduce un número: ");
        double numero = teclado.nextDouble();

        try {
            double resultado = calcularRaiz(numero);
            System.out.println("\nLa raíz cuadrada es: " + resultado);
        } catch (IllegalArgumentException e) {
            System.out.println("\nError: " + e.getMessage());
        }

        teclado.close();
    }
}