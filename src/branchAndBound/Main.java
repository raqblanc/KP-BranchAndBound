package branchAndBound;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		int n, sol[] = null, nodosExpandidos = 0;
		float  P[] = null, V[] = null, M = 0, benefMejor = 0;
		String datostxt = "datos.txt";


		try {
			Scanner in = new Scanner(new FileReader(datostxt));


			while(in.hasNext()) { 
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
				MochilaRP soloFactible = new MochilaRP();
				Solucion s = soloFactible.mochilaRPSoloFactible(P, V, M, n);
				System.out.println("Mochila solo factibilidad:\n - " + s.getNodosExpandidos() + " nodos expandidos\n - Tiempo total: ");
				System.out.println("Solución:");
				int solmejor[] = s.getSolMejor();
				for (int i = 0; i < solmejor.length; i++) {
					System.out.println(solmejor[i]);
				}
				System.out.println("Beneficio mejor: " + s.getBenefMejor());
				/*
				 * Nodos explorados
				 * Tiempo total
				 */
				//MochilaRP ingenua = new MochilaRP();
				//ingenua.mochilaRPingenua(P, V, M, sol, benefMejor, nodosExpandidos, n);
				/*
				 * Nodos explorados
				 * Tiempo total
				 */
				//MochilaRP ajustada = new MochilaRP();
				//ajustada.mochilaRPajustada(P, V, M, sol, benefMejor, nodosExpandidos, n);
				/*
				 * Nodos explorados
				 * Tiempo total
				 */
			}

			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error con el fichero");

		}



		//  Tiempo medio / Nodos expandidos de los 3

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
