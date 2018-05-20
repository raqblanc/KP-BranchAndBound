package branchAndBound;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		int n, sol[] = null;
		float  P[] = null, V[] = null, M = 0;
		String datostxt = "datos.txt";
		int cont = 0;

		try {
			Scanner in = new Scanner(new FileReader(datostxt));


			while(in.hasNext()) { 
				cont++;
				M = in.nextFloat(); // Tamaño máximo de la mochila
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
				
				
				mostrarJuegoDatos(n, P, V);
				
				System.out.println("----------------- SOLO FACTIBILIDAD -----------------");
				Solucion sFactible = new Solucion(new int[n], 0, 0);
				long timeSoloFactible = calcularSoloFactible(P, V, M, n, sFactible);
				mostrarResultados(sFactible, timeSoloFactible);
				
				System.out.println("----------------- ESTIMACIÓN INGENUA -----------------");
				Solucion sIngenua = new Solucion(new int[n], 0, 0);
				long timesIngenua = calcularIngenua(P, V, M, n, sIngenua);
				mostrarResultados(sIngenua, timesIngenua);
				
				System.out.println("----------------- ESTIMACIÓN AJUSTADA -----------------");
				Solucion sAjustada = new Solucion(new int[n], 0, 0);
				long timesAjustada = calcularAjustada(P, V, M, n, sAjustada);
				mostrarResultados(sAjustada, timesAjustada);
				
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
	private static long calcularAjustada(float[] P, float[] V, float M, int n, Solucion sAjustada) {
		MochilaRP ajustada = new MochilaRP();
		long timeAjustada = 0;
		long startTimeAjustada = System.currentTimeMillis();
		sAjustada = ajustada.mochilaRPajustada(P, V, M, n);
		long endTimeAjustada = System.currentTimeMillis();
		timeAjustada += (endTimeAjustada-startTimeAjustada);
		return timeAjustada;
	}

	/**
	 * Calcula la solución que poda con estimaciones ingenuas.
	 * @param P
	 * @param V
	 * @param M
	 * @param n
	 * @return el tiempo en ms que tarda en ejecutarse
	 */
	private static long calcularIngenua(float[] P, float[] V, float M, int n, Solucion sIngenua) {
		MochilaRP ingenua = new MochilaRP();
		long timeIngenua = 0;
		long startTimeIngenua = System.currentTimeMillis();
		sIngenua = ingenua.mochilaRPingenua(P, V, M, n);
		long endTimeIngenua = System.currentTimeMillis();
		timeIngenua += (endTimeIngenua-startTimeIngenua);
		return timeIngenua;
	}

	/**
	 * Calcula la solución que poda sólo en la factibilidad, la devuelve por parámetro.
	 * @param P
	 * @param V
	 * @param M
	 * @param n
	 * @return el tiempo en ms que tarda en ejecutarse
	 */
	private static long calcularSoloFactible(float P[], float V[], float M, int n, Solucion s) {
		MochilaRP soloFactible = new MochilaRP();
		long timeSoloFactible = 0;
		long startTimeSoloFactible = System.currentTimeMillis();
		s = soloFactible.mochilaRPSoloFactible(P, V, M, n);
		long endTimeSoloFactible = System.currentTimeMillis();
		timeSoloFactible += (endTimeSoloFactible-startTimeSoloFactible);
		return timeSoloFactible;
	}

	private static void mostrarResultados(Solucion s, long timeSoloFactible) {
		System.out.println("- Nodos expandidos: "+ s.getNodosExpandidos());
		System.out.println("- Tiempo total: " + timeSoloFactible +  " ms");
		System.out.println("- Objetos escogidos: ");
		int solMejor[] = s.getSolMejor();
		for (int i = 0; i < solMejor.length; i++) {
			System.out.print("     " + (i+1) + "º) ");
			if (solMejor[i] == 0) {
				System.out.print("Sí");
			}
			else {
				System.out.print("No");
			}
			if (i > 6) {
				System.out.println("\n");
			}
		}
		System.out.println("\n- Beneficio mejor: " + s.getBenefMejor() + "\n");
		
	}

	private static void mostrarJuegoDatos(int n, float P[], float V[]) {
		for (int i = 0; i < n; i++) {
			System.out.println((i+ 1) + "º) Peso: " + P[i] + ", Valor: " + V[i] + ", Relación: " + (V[i]/P[i]));
			
		}
		System.out.println("\n");
	}

	private static void ordenar(float[] P, float[] V) {
		float relacion[] = new float[P.length];
		for (int i = 0; i < relacion.length; i++) {
			//Vi / Pi
			relacion[i] = V[i] / P[i];
		}
		//Ordenar por el método de la burbuja
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
