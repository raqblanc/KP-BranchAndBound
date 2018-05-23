package branchAndBound;
import java.util.PriorityQueue;

public class MochilaRP {
	/**
	 * Solución del problema de la mochila menos eficiente, la única poda es la de factibilidad.
	 * @param P
	 * @param V
	 * @param M
	 * @param n
	 * @return solución obtenida
	 */
	public Solucion mochilaRPSoloFactible(float P[], float V[], float M, int n) {
		Solucion sol = new Solucion(new int[n], 0, 0);
		Nodo X, Y, Z;
		/* Generamos la raíz */
		Y = new Nodo(-1, n);
		Y.setPeso(0);
		Y.setBeneficio(0);

		PriorityQueue <Nodo> cola = new PriorityQueue<Nodo>(1, new Comparador());
		cola.add(Y);
		/* Expandimos */
		while (!cola.isEmpty()) {
			Y = cola.poll(); // Lo saca de la cola
			/* Generamos hijos */
			// X (tomar objeto) 
			X = new Nodo(Y.getK()+1, n);
			X.setSolucion(Y.getSolucion());
			// Z (no tomar objeto)
			Z = new Nodo(Y.getK()+1, n);
			Z.setSolucion(Y.getSolucion());

			/* METER EL OBJETO */
			if (Y.getPeso() + P[X.getK()] <= M) { // Si el objeto cabe en la mochila
				X.setiEsimaSolucion(X.getK(), 1); // Marcaje: Marcamos el objeto k-ésimo
				X.setPeso(Y.getPeso() + P[X.getK()]); // Aumentamos al peso acumulado el peso del objeto k-ésimo
				X.setBeneficio(Y.getBeneficio() + V[X.getK()]); // Aumentamos al beneficio acumulado el valor del objeto k-ésimo
				if (X.getK() == n - 1) { // Si ya no quedan más objetos
					if (sol.getBenefMejor() < X.getBeneficio()) {
						sol.setSolMejor(X.getSolucion());
						sol.setBenefMejor(X.getBeneficio());
					}
				}
				else { // Si sí quedan más objetos encolamos X
					cola.add(X);
				}

			}

			/* NO METER EL OBJETO */
			Z.setiEsimaSolucion(Z.getK(), 0);
			// No sumamos beneficio ni peso, porque no se mete el objeto
			Z.setPeso(Y.getPeso()); 
			Z.setBeneficio(Y.getBeneficio());
			if (Z.getK() == n - 1) { //Si es el último objeto
				if (sol.getBenefMejor() < Z.getBeneficio()) {
					sol.setSolMejor(Z.getSolucion());
					sol.setBenefMejor(Z.getBeneficio());
				}
			}
			else {
				cola.add(Z);
			}

			sol.addNodoExpandido();
		}
		return sol;
	}




	/**
	 * Solución del problema de la mochila con ramificación y poda. Poda de factibilidad y con estimaciones optimista y pesimista.
	 * Las estimaciones utilizadas son ingenuas, es decir, la poda es peor.
	 * Es mejor que el algoritmo que solo poda con la factibilidad, pero peor que el que tiene unas estimaciones más ajustadas.
	 * @param P
	 * @param V
	 * @param M
	 * @param n
	 * @return solución obtenida
	 */
	public Solucion mochilaRPingenua(float[] P, float[] V, float M, int n) {
		Solucion sol = new Solucion(new int[n], 0, 0);
		Nodo X, Y, Z;
		/* Generamos la raíz */
		Y = new Nodo(-1, n);
		Y.setPeso(0);
		Y.setBeneficio(0);
		Y.setBeneficioOpt(cotaOptimistaIngenua(Y.getK(), Y.getBeneficio(), V));
		sol.setBenefMejor(cotaPesimistaIngenua(Y.getBeneficio()));

		PriorityQueue <Nodo> cola = new PriorityQueue<Nodo>(1, new Comparador());
		cola.add(Y);
		/* Expandimos */
		while (!cola.isEmpty() && cola.peek().getBeneficioOpt() >= sol.getBenefMejor()) {
			sol.addNodoExpandido();
			Y = cola.poll(); // Lo saca de la cola
			/* Generamos hijos */
			// X (tomar objeto) 
			X = new Nodo(Y.getK()+1, n);
			X.setSolucion(Y.getSolucion());
			// Z (no tomar objeto) 
			Z = new Nodo(Y.getK()+1, n);
			Z.setSolucion(Y.getSolucion());
			/* METER EL OBJETO */
			if (Y.getPeso() + P[X.getK()] <= M) { // Si el objeto cabe en la mochila
				X.setiEsimaSolucion(X.getK(), 1); // Marcaje: Marcamos el objeto k-ésimo
				X.setPeso(Y.getPeso() + P[X.getK()]); // Aumentamos al peso acumulado el peso del objeto k-ésimo
				X.setBeneficio(Y.getBeneficio() + V[X.getK()]); // Aumentamos al beneficio acumulado el valor del objeto k-ésimo
				X.setBeneficioOpt(Y.getBeneficioOpt());
				if (X.getK() == n - 1) { // Si ya no quedan más objetos
					sol.setSolMejor(X.getSolucion());
					sol.setBenefMejor(X.getBeneficio());
				}
				else { // Si sí quedan más objetos encolamos X
					cola.add(X);
				}

			}

			/* NO METER EL OBJETO */
			Z.setBeneficioOpt(cotaOptimistaIngenua(Z.getK(), Y.getBeneficio(), V));
			float benefPes = cotaPesimistaIngenua(Y.getBeneficio());
			if (Z.getBeneficioOpt() >= sol.getBenefMejor()) {
				Z.setiEsimaSolucion(Z.getK(), 0);
				// No sumamos beneficio ni peso, porque no se mete el objeto
				Z.setPeso(Y.getPeso()); 
				Z.setBeneficio(Y.getBeneficio());
				if (Z.getK() == n - 1) { //Si es el último objeto

					sol.setSolMejor(Z.getSolucion());
					sol.setBenefMejor(Z.getBeneficio());


				}
				else {
					cola.add(Z);
					sol.setBenefMejor(Math.max(sol.getBenefMejor(), benefPes));
				}


			}
		}
		return sol;
	}


	public Solucion mochilaRPajustada(float[] P, float[] V, float M, int n) {

		Solucion sol = new Solucion(new int[n], 0, 0);
		Nodo X, Y, Z;

		/* Generamos la raíz */
		Y = new Nodo(-1, n);
		Y.setPeso(0);
		Y.setBeneficio(0);
		Y.setBeneficioOpt(cotaOptimistaAjustada(P, V, Y.getK(), M, Y.getPeso(), Y.getBeneficio()));
		sol.setBenefMejor(cotaPesimistaAjustada(P, V, Y.getK(), M, Y.getPeso(), Y.getBeneficio()));
		PriorityQueue <Nodo> cola = new PriorityQueue<Nodo>(1, new Comparador());
		cola.add(Y);
		/* Expandimos */
		while (!cola.isEmpty() && cola.peek().getBeneficioOpt() >= sol.getBenefMejor()) {
			sol.addNodoExpandido();
			Y = cola.poll(); // Lo saca de la cola
			/* Generamos hijos */
			// X (tomar objeto) 
			X = new Nodo(Y.getK()+1, n);
			X.setSolucion(Y.getSolucion());
			// Z (no tomar objeto)
			Z = new Nodo(Y.getK()+1, n);
			Z.setSolucion(Y.getSolucion());
			/* METER EL OBJETO */
			if (Y.getPeso() + P[X.getK()] <= M) { // Si el objeto cabe en la mochila
				X.setiEsimaSolucion(X.getK(), 1); // Marcaje: Marcamos el objeto k-ésimo

				X.setPeso(Y.getPeso() + P[X.getK()]); // Aumentamos al peso acumulado el peso del objeto k-ésimo
				X.setBeneficio(Y.getBeneficio() + V[X.getK()]); // Aumentamos al beneficio acumulado el valor del objeto k-ésimo
				X.setBeneficioOpt(Y.getBeneficioOpt());
				if (X.getK() == n - 1) { // Si ya no quedan más objetos
					// X.beneficio = X.beneficioOpt(X) ≥ sol.beneficioMejor
					sol.setSolMejor(X.getSolucion());
					sol.setBenefMejor(X.getBeneficio());

				}
				else { // Si sí quedan más objetos encolamos X
					cola.add(X);
				}

			}

			/* NO METER EL OBJETO */
			Z.setBeneficioOpt(cotaOptimistaAjustada(P, V, Z.getK(), M, Y.getPeso(), Y.getBeneficio()));
			float benefPes = cotaPesimistaAjustada(P, V, Z.getK(), M, Y.getPeso(), Y.getBeneficio());
			if (Z.getBeneficioOpt() >= sol.getBenefMejor()) {
				Z.setiEsimaSolucion(Z.getK(), 0);
				// No sumamos beneficio ni peso, porque no se mete el objeto
				Z.setPeso(Y.getPeso()); 
				Z.setBeneficio(Y.getBeneficio());
				if (Z.getK() == n - 1) { //Si es el último objeto

					sol.setSolMejor(Z.getSolucion());
					sol.setBenefMejor(Z.getBeneficio());


				}
				else {
					cola.add(Z);
					sol.setBenefMejor(Math.max(sol.getBenefMejor(), benefPes));
				}

			}
		}
		return sol;
	}




	////////////////////////////////////////////////// COTAS //////////////////////////////////////////////////
	/**
	 * Cota pesimista: considerar que el resto de objetos no se introducen en la mochila.
	 * Aunque no tenga sentido este método, es útil para entender cómo se calcula la cota pesimista.
	 * Al no introducirse más objetos en la mochila el beneficio se queda igual, no se suman los valores
	 * del resto de objetos.
	 * @param beneficio
	 * @return valor de la cota pesimista en la etapa k
	 */
	private float cotaPesimistaIngenua(float beneficio) {
		return beneficio;
	}

	/**
	 * Cota optimista: considerar que el resto de objetos se introducen TODOS en la mochila.
	 * Desde la etapa k, se suman los valores de todos los objetos que quedan al ya beneficio acumulado.
	 * @param V
	 * @param k
	 * @param beneficio
	 * @return valor de la cota optimista en la etapa k
	 */
	private float cotaOptimistaIngenua(int k, float benef, float V[]) {
		float cotaOptimista = benef;
		int j = k +1; 
		int n = V.length - 1; 
		while ((j <= n)){
			cotaOptimista += V[j];
			j++;
		}

		return cotaOptimista;
	}

	/**
	 * Cota optimista: utilizando el algoritmo voraz de la mochila que puede ser fraccionada.
	 * Completa la mochila hasta llegar al objeto que hay que fraccionar, que lo fracciona y lo introduce a la mochila.
	 * @param beneficio
	 * @return valor de la cota optimista ajustada en la etapa k
	 */
	private float cotaPesimistaAjustada(float[] P, float[] V, int k, float M, float peso, float beneficio) {
		float hueco = M - peso;
		float cotaPes = beneficio;
		int n = P.length - 1;
		int j = k + 1;
		while (j <= n && P[j] <= hueco) {
			/* Cogemos el objeto entero */
			hueco -= P[j];
			cotaPes += V[j];
			j++;
		}
		while (j <= n && hueco > 0) { /* Todavía quedan objetos -> buscar los que quepan, si importar el valor*/
			if (P[j] <= hueco) {
				hueco -= hueco - P[j];
				cotaPes += V[j];
			}
			j++;
		}
		return cotaPes;
	}

	/**
	 * Cota pesimista: utilizando algoritmo voraz, pero sin fraccionar, cuando llega a un objeto que no entra en la mochila
	 * prueba con el resto de objetos y si caben, los introduce.
	 * @param v
	 * @param n
	 * @param beneficio
	 * @return valor de la cota pesimista ajustada en la etapa k
	 */
	private float cotaOptimistaAjustada(float[] P, float[] V, int k, float M, float peso, float beneficio) {
		float hueco = M - peso;
		float cotaOpt = beneficio;
		int j = k + 1;
		int n = P.length - 1;
		while (j <= n && P[j] <= hueco) {
			/* Cogemos el objeto entero */
			hueco -= P[j];
			cotaOpt += V[j];
			j++;
		}
		if (j <= n) { /* Todavía quedan objetos -> fraccionarlo */
			cotaOpt += (hueco/P[j])*V[j];
		}
		return cotaOpt;
	}
}