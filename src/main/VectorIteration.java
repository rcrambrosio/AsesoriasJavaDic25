package main;

import java.util.Locale;
import java.util.Scanner;

/**
 * Vektoriteration
 *
 * <p>Implementa la iteración de vectores del enunciado:
 * u^[n] = A^n * u^[0], con DIM = 3.
 *
 * <p>La clase contiene los métodos solicitados:
 * readVec, readMat, writeVec, matPot y matVecMult,
 * además del programa principal (main).
 */
public class VectorIteration {

    /**
     * Lee desde consola un vector inicial u0 de dimensión DIM.
     *
     * @param sc  Scanner para leer desde consola
     * @param DIM dimensión del vector
     * @return vector leído como arreglo unidimensional de float
     */
    public static float[] readVec(Scanner sc, int DIM) {
        float[] u0 = new float[DIM];
        System.out.println("\nIngresa el vector inicial u0 (" + DIM + " valores):");
        for (int i = 0; i < DIM; i++) {
            System.out.print("u0[" + i + "] = ");
            u0[i] = sc.nextFloat();
        }
        return u0;
    }

    /**
     * Lee desde consola una matriz cuadrada A de dimensión DIM x DIM.
     *
     * @param sc  Scanner para leer desde consola
     * @param DIM dimensión de la matriz
     * @return matriz leída como arreglo bidimensional de float
     */
    public static float[][] readMat(Scanner sc, int DIM) {
        float[][] A = new float[DIM][DIM];
        System.out.println("Ingresa la matriz A (" + DIM + "x" + DIM + ") fila por fila:");
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                System.out.print("A[" + i + "][" + j + "] = ");
                A[i][j] = sc.nextFloat();
            }
        }
        return A;
    }

    /**
     * Imprime un vector en consola.
     *
     * @param v vector a imprimir
     */
    public static void writeVec(float[] v) {
        System.out.print("[ ");
        for (int i = 0; i < v.length; i++) {
            System.out.printf(Locale.US, "%.6f", v[i]);
            if (i < v.length - 1) System.out.print(", ");
        }
        System.out.println(" ]");
    }

    /**
     * Calcula la n-ésima potencia A^n de una matriz A.
     *
     * <p>Siguiendo el enunciado:
     * <ul>
     *   <li>Se usan dos matrices auxiliares: Temp y An.</li>
     *   <li>En An se guarda inicialmente A mediante una copia profunda.</li>
     *   <li>Se usa un bucle for y en cada iteración se multiplica Temp por A y se guarda en An.</li>
     *   <li>Al inicio de cada iteración, An se pone en 0.</li>
     *   <li>Al final de cada iteración, Temp toma el valor de An.</li>
     * </ul>
     *
     * <p>Casos:
     * <ul>
     *   <li>Si n == 0, devuelve la identidad.</li>
     *   <li>Si n == 1, devuelve A (copia profunda).</li>
     * </ul>
     *
     * @param A   matriz base
     * @param DIM dimensión de la matriz
     * @param n   exponente (n >= 0)
     * @return A^n
     */
    public static float[][] matPot(float[][] A, int DIM, int n) {
        if (n == 0) {
            return identity(DIM);
        }

        // An guarda A como copia profunda
        float[][] An = deepCopy(A, DIM);

        // Temp es una matriz auxiliar (copia profunda de An)
        float[][] Temp = deepCopy(An, DIM);

        // Para obtener A^n, multiplicamos por A un total de (n-1) veces.
        for (int iter = 1; iter <= n - 1; iter++) {

            // An se pone en 0 al inicio de cada iteración
            for (int i = 0; i < DIM; i++) {
                for (int j = 0; j < DIM; j++) {
                    An[i][j] = 0.0f;
                }
            }

            // An = Temp * A
            for (int i = 0; i < DIM; i++) {
                for (int j = 0; j < DIM; j++) {
                    float sum = 0.0f;
                    for (int k = 0; k < DIM; k++) {
                        sum += Temp[i][k] * A[k][j];
                    }
                    An[i][j] = sum;
                }
            }

            // Temp toma el valor de An (copia profunda)
            Temp = deepCopy(An, DIM);
        }

        return An;
    }

    /**
     * Multiplica una matriz A (DIM x DIM) por un vector u (DIM):
     * b = A * u
     *
     * @param A   matriz A
     * @param u   vector u
     * @param DIM dimensión
     * @return vector resultado b
     */
    public static float[] matVecMult(float[][] A, float[] u, int DIM) {
        float[] b = new float[DIM];
        for (int i = 0; i < DIM; i++) {
            float sum = 0.0f;
            for (int j = 0; j < DIM; j++) {
                sum += A[i][j] * u[j];
            }
            b[i] = sum;
        }
        return b;
    }

    /**
     * Programa principal según el enunciado:
     * <ul>
     *   <li>DIM = 3</li>
     *   <li>Lee A una sola vez</li>
     *   <li>Repite lectura de u0 y n hasta que n <= 0</li>
     *   <li>Calcula u^[n] = A^n * u0 y lo imprime</li>
     * </ul>
     *
     * @param args no se usa
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        sc.useLocale(Locale.US); // para leer decimales con punto (.)

        int DIM = 3;

        // Arreglos solicitados
        float[][] A;
        float[][] An;
        float[] u0;
        float[] u;

        // n inicia en 1 (como pide el enunciado)
        int n = 1;

        // Leer A una sola vez
        A = readMat(sc, DIM);

        // Bucle while: repetir lectura de u0 y n hasta que n <= 0
        while (n > 0) {
            u0 = readVec(sc, DIM);

            System.out.print("Ingresa n (iteraciones, <= 0 para cancelar): ");
            n = sc.nextInt();
            if (n <= 0) {
                break;
            }

            An = matPot(A, DIM, n);
            u = matVecMult(An, u0, DIM);

            System.out.println("\nResultado u^[n] para n = " + n + ":");
            writeVec(u);
        }

        sc.close();
        System.out.println("Programa finalizado.");
    }

    // ----------------- Métodos auxiliares privados -----------------

    /**
     * Crea una copia profunda (deep copy) de una matriz DIM x DIM.
     */
    private static float[][] deepCopy(float[][] M, int DIM) {
        float[][] copy = new float[DIM][DIM];
        for (int i = 0; i < DIM; i++) {
            System.arraycopy(M[i], 0, copy[i], 0, DIM);
        }
        return copy;
    }

    /**
     * Crea la matriz identidad DIM x DIM.
     */
    private static float[][] identity(int DIM) {
        float[][] I = new float[DIM][DIM];
        for (int i = 0; i < DIM; i++) {
            I[i][i] = 1.0f;
        }
        return I;
    }
}
