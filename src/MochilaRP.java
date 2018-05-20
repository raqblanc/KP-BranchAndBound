import java.util.PriorityQueue;

public class MochilaRP {
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
					if (sol.getBenefMejor() <= X.getBeneficio()) {
						sol.setSolMejor(X.getSolucion());
						sol.setBenefMejor(X.getBeneficio());
					}
				}
				else { // Si sí quedan más objetos encolamos
					cola.add(X);
				}
				sol.addNodoExpandido();
			}

			/* NO METER EL OBJETO */
			Z.setiEsimaSolucion(Z.getK(), 0);
			// No sumamos beneficio ni peso, porque no se mete el objeto
			Z.setPeso(Y.getPeso()); 
			Z.setBeneficio(Y.getBeneficio());
			if (Z.getK() == n - 1) { //Si es el último objeto
				if (sol.getBenefMejor() <= Z.getBeneficio()) {
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
	
}