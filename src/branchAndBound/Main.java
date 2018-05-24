package branchAndBound;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Clase Main
 * @author raqblanc
 *
 */
public class Main {

	public static float time = 0;

	public static void main(String[] args) {
		int n, sol[] = null;
		float  P[] = null, V[] = null, M = 0;
		String datostxt = "prueba.txt";
		int cont = 0;

		try {
			Scanner in = new Scanner(new FileReader(datostxt));


			while(in.hasNext()) { 
				cont++;
				M = (float) in.nextDouble() ;// Tamaño máximo de la mochila
				n = in.nextInt(); // Número de objetos
				sol = new int[n]; // 1 -> el objeto i-ésimo se coge, 0 -> lo contrario
				for (int i = 0; i < n; i++) {
					sol[i] = 0;
				}
				P = new float[n]; // Peso del objeto i-ésimo
				V = new float[n]; // Valor del objeto i-ésimo
				for (int i = 0; i < n; i++) { 
					P[i] = in.nextFloat();
				}
				for (int i = 0; i < n; i++) { 
					V[i] = in.nextFloat();
				}
				//Ordenar P y V por vi/pi
				ordenar(P, V);

				System.out.println("###########################  JUEGO DE DATOS  " + cont + " ###########################\n");


				mostrarJuegoDatos(n, P, V, M);

				System.out.println("----------------- SOLO FACTIBILIDAD -----------------");

				Solucion sFactible = calcularSoloFactible(P, V, M, n);
				mostrarResultados(sFactible, time);

				System.out.println("\n----------------- ESTIMACIÓN INGENUA -----------------");

				Solucion  sIngenua = calcularIngenua(P, V, M, n);
				mostrarResultados(sIngenua, time);

				System.out.println("\n----------------- ESTIMACIÓN AJUSTADA -----------------");
				Solucion sAjustada = calcularAjustada(P, V, M, n);
				mostrarResultados(sAjustada, time);

				System.out.println("\n\n");

			}

			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error con el fichero");

		}



		//  Tiempo medio / Nodos expandidos de los 3

	}

	/**
	 * Calcula la solución que poda con estimaciones ajustadas.
	 * @param P
	 * @param V
	 * @param M
	 * @param n
	 * @return el tiempo en ms que tarda en ejecutarse
	 */
	private static Solucion calcularAjustada(float[] P, float[] V, float M, int n) {
		MochilaRP ajustada = new MochilaRP();
		long timeAjustada = 0;
		long startTimeAjustada = System.currentTimeMillis();
		int x = 0;
		Solucion sAjustada = null;
		while (x < 2) {
			sAjustada = ajustada.mochilaRPajustada(P, V, M, n);
			x++;
		}
		long endTimeAjustada = System.currentTimeMillis();
		timeAjustada += (endTimeAjustada-startTimeAjustada)/2;
		time = timeAjustada;
		return sAjustada;
	}

	/**
	 * Calcula la solución que poda con estimaciones ingenuas.
	 * @param P
	 * @param V
	 * @param M
	 * @param n
	 * @return el tiempo en ms que tarda en ejecutarse
	 */
	private static Solucion calcularIngenua(float[] P, float[] V, float M, int n) {
		MochilaRP ingenua = new MochilaRP();
		long timeIngenua = 0;
		long startTimeIngenua = System.currentTimeMillis();
		int x = 0;
		Solucion sIngenua = null;
		while (x < 2) {
			sIngenua = ingenua.mochilaRPingenua(P, V, M, n);
			x++;
		}
		long endTimeIngenua = System.currentTimeMillis();
		timeIngenua += (endTimeIngenua-startTimeIngenua)/2;
		time = timeIngenua;
		return sIngenua;
	}

	/**
	 * Calcula la solución que poda sólo en la factibilidad, la devuelve por parámetro.
	 * @param P
	 * @param V
	 * @param M
	 * @param n
	 * @return el tiempo en ms que tarda en ejecutarse
	 */
	private static Solucion calcularSoloFactible(float P[], float V[], float M, int n) {
		MochilaRP soloFactible = new MochilaRP();
		long timeSoloFactible = 0;
		long startTimeSoloFactible = System.currentTimeMillis();
		int x = 0;
		Solucion s = null;
		
			s = soloFactible.mochilaRPSoloFactible(P, V, M, n);
			x++;
		
		long endTimeSoloFactible = System.currentTimeMillis();
		timeSoloFactible += (endTimeSoloFactible-startTimeSoloFactible);
		Main.time = timeSoloFactible;
		return s;
	}

	/**
	 * Muestra los resultados obtenidos por consola
	 * @param s
	 * @param time
	 */
	private static void mostrarResultados(Solucion s, float time) {
		System.out.println("- Nodos expandidos: "+ s.getNodosExpandidos());
		System.out.println("- Tiempo total: " + time +  " ms");
		System.out.println("- Tiempo medio por nodo: " + time/s.getNodosExpandidos() +  " ms/nodo");
		System.out.println("- Beneficio mejor: " + s.getBenefMejor());
		System.out.println("- Objetos escogidos: ");
		int solMejor[] = s.getSolMejor();
		for (int i = 0; i < solMejor.length; i++) {
			System.out.print("     " + (i+1) + "º) ");
			if (solMejor[i] == 1) {
				System.out.print("Sí");
			}
			else {
				System.out.print("No");
			}
			if (i % 6 == 0) {
				System.out.println("\n");
			}
		}


	}

	/**
	 * Muestra los juegos de datos de entrada por consola
	 * @param n
	 * @param P
	 * @param V
	 * @param M
	 */
	private static void mostrarJuegoDatos(int n, float P[], float V[], float M) {
		System.out.println("Capacidad mochila: " + M);
		for (int i = 0; i < n; i++) {
			System.out.println((i+ 1) + "º) Peso: " + P[i] + ", Valor: " + V[i] + ", Relación: " + (V[i]/P[i]));

		}
		System.out.println("\n");
	}

	/**
	 * Ordena por el método de la burbuja V y P por V/P de mayor a menor
	 * @param P
	 * @param V
	 */
	private static void ordenar(float[] P, float[] V) {
		float relacion[] = new float[P.length];
		for (int i = 0; i < relacion.length; i++) {
			//Vi / Pi
			relacion[i] = V[i] / P[i];
		}

		boolean intercambio = true;
		int j = 0;
		float tmp, tmpv, tmpp;
		while (intercambio) {
			intercambio = false;
			j++;
			for (int i = 0; i < relacion.length - j; i++) {
				if (relacion[i] <= relacion[i + 1]) {
					tmp = relacion[i];
					tmpv = V[i];
					tmpp = P[i];
					relacion[i] = relacion[i + 1];
					V[i] = V[i + 1];
					P[i] = P[i + 1];
					relacion[i + 1] = tmp;
					V[i + 1] = tmpv;
					P[i + 1] = tmpp;
					intercambio = true;					
				}
			}
		}



	}

}
