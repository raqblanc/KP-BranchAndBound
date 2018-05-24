package branchAndBound;
import java.util.Comparator;

/**
 * Clase únicamente necesaria para la implementación de la cola de prioridad de máximos
 * @author raqblanc
 *
 */
public class Comparador implements Comparator<Nodo>{

	public int compare(Nodo n1, Nodo n2) {
		if(n1.getBeneficioOpt() <= n2.getBeneficioOpt()) return 1;
		if(n1.getBeneficioOpt() > n2.getBeneficioOpt()) return -1;
		return 0;
	}

}
