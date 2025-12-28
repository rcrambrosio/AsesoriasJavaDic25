package main;

import java.util.Locale;
import java.util.Scanner;

public class Exponential {

    // Estructura simple para devolver (valor, N usado)
    static class Result {
        final double value;
        final int n;

        Result(double value, int n) {
            this.value = value;
            this.n = n;
        }
    }

    /**
     * Parte A: calcula S(N) = sum_{i=0..N} x^i / i!
     * usando y0 = 1, yi = (x/i)*y_{i-1}
     * con un for hasta N fijo.
     */
    public static double expSeriesFixedN(double x, int N) {
        double sum = 1.0;  // S(0) = 1
        double y = 1.0;    // y0 = 1

        for (int i = 1; i <= N; i++) {
        	y = (x / i) * y;  // yi = (x/i)*y_{i-1}
            sum += y;      // S(i) = S(i-1) + yi
        }
        return sum;
    }

    /**
     * Parte B: calcula S(N) con criterio de paro:
     * |S(N) - S(N+1)| <= eps
     * usando do-while e imprime/retorna el N donde se detuvo.
     */
    public static Result expSeriesAuto(double x, double eps, int maxIter) {
        double sum = 1.0;   // S(0)
        double y = 1.0;     // y0
        double sumOld;
        int k = 0;

        // do-while para garantizar al menos una iteración (calcular y1 y S(1))
        do {
            sumOld = sum;    // S(N)
            k++;             // pasamos a N+1
            y = (x / k) * y;   // yk = (x/k)*y(k-1)
            sum += y;        // S(N+1)

            if (k >= maxIter) {
                // Evita bucles infinitos si algo sale mal o eps es demasiado pequeño
                break;
            }
        } while (Math.abs(sum - sumOld) > eps);

        return new Result(sum, k);
    }

    /**
     * Parte C: mejora para x negativo usando:
     * e^{-x} = 1 / e^{x}
     * Calcula e^{|x|} (más estable) y si x<0 invierte.
     */
    public static Result expSeriesStable(double x, double eps, int maxIter) {
        if (x >= 0) {
            return expSeriesAuto(x, eps, maxIter);
        }
        Result pos = expSeriesAuto(Math.abs(x), eps, maxIter);
        return new Result(1.0 / pos.value, pos.n);
    }

    private static void printComparison(String label, double x, double approx, int nUsed) {
        double exact = Math.exp(x);
        double absErr = Math.abs(approx - exact);
        double relErr = absErr / Math.abs(exact);

        System.out.printf(Locale.US,
                "%s | x=%8.2f | approx=% .16e | N=%5d | Math.exp=% .16e | absErr=% .3e | relErr=% .3e%n",
                label, x, approx, nUsed, exact, absErr, relErr);
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        Scanner sc = new Scanner(System.in);

        System.out.print("Ingresa x (double): ");
        double x = sc.nextDouble();

        System.out.print("Ingresa N (entero) para la parte A (por ejemplo 20): ");
        int N = sc.nextInt();

        double eps = 1e-13;
        int maxIter = 1_000_000;

        // Parte A: N fijo con for
        double a = expSeriesFixedN(x, N);
        System.out.printf(Locale.US, "Parte A (for, N fijo): S(%d) = %.16e%n", N, a);

        // Parte B: paro automático con do-while
        Result b = expSeriesAuto(x, eps, maxIter);
        System.out.printf(Locale.US, "Parte B (do-while, eps=1e-13): S(%d) = %.16e%n", b.n, b.value);

        // Parte C: estable para x negativo
        Result c = expSeriesStable(x, eps, maxIter);
        System.out.printf(Locale.US, "Parte C (estable con 1/exp(|x|)): S(%d) = %.16e%n", c.n, c.value);

        // Comparación con Math.exp(x)
        System.out.println("\n--- Comparación contra Math.exp(x) ---");
        printComparison("A (fijo)", x, a, N);
        printComparison("B (auto)", x, b.value, b.n);
        printComparison("C (stab)", x, c.value, c.n);

        // Pruebas sugeridas en el enunciado
        System.out.println("\n--- Pruebas sugeridas: x = ±1, ±10, ±50, ±100 (usando B y C) ---");
        double[] tests = {-100, -50, -10, -1, 1, 10, 50, 100};
        for (double t : tests) {
            Result tb = expSeriesAuto(t, eps, maxIter);
            Result tc = expSeriesStable(t, eps, maxIter);

            printComparison("B (auto)", t, tb.value, tb.n);
            printComparison("C (stab)", t, tc.value, tc.n);
        }

        sc.close();
    }
}
