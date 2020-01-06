import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

public class SudokuSolver2x2 {
	public static ISolver solver = SolverFactory.newDefault();

	public static void main(String[] args) {

		solver.setTimeout(3600);
		solver.newVar(500);
		solver.setExpectedNumberOfClauses(400);

		// Klausel = int[] {5, 6, -7} ==> 5 or 6 or not 7 is right
		// 0 Kann nicht verwendet werden ==> +-0 == 0

		// Zeile
		for (int i = 1; i <= 4; i++) {
			// Spalte
			for (int j = 1; j <= 4; j++) {

				int[] literals = new int[4];

				// Jedes Feld hat nur eine Zahl
				for (int k = 1; k <= 4; k++) {
					literals[k - 1] = 100 * i + 10 * j + k;
				}
				generateOnlyOnce(literals);
			}
		}
		// Zeile
		for (int i = 1; i <= 4; i++) {
			// Jede Zahl nur einmal
			for (int k = 1; k <= 4; k++) {
				int[] literals = new int[4];
				// Spalte
				for (int j = 1; j <= 4; j++) {
					literals[j - 1] = 100 * i + 10 * j + k;
				}
				generateOnlyOnce(literals);
			}
		}
		// Spalte
		for (int j = 1; j <= 4; j++) {
			// Jede Zahl nur einmal
			for (int k = 1; k <= 4; k++) {
				int[] literals = new int[4];
				// Zeile
				for (int i = 1; i <= 4; i++) {
					literals[i - 1] = 100 * i + 10 * j + k;
				}
				generateOnlyOnce(literals);
			}
		}
		// Bloecke in der Horizontalen
		for (int i = 0; i < 2; i++) {
			// Bloecke in der Vertikalen
			for (int j = 0; j < 2; j++) {
				// Zahl
				for (int z = 1; z <= 4; z++) {
					// Durch den Block
					int[] literals = new int[4];
					int cnt = 0;
					for (int k = 2 * i + 1; k <= 2 * i + 2; k++) {
						// Durch anderen Block
						for (int l = 2 * j + 1; l <= 2 * j + 2; l++) {
							literals[cnt] = 100 * k + 10 * l + z;
							cnt++;
						}
					}
					generateOnlyOnce(literals);
				}
			}
		}
		addSudokuClauses();

		IProblem problem = solver;
		try {
			if (problem.isSatisfiable()) {
				int[] model = problem.model();
				printSudoku(model);
			} else {
				System.out.println("Unsatisfiable");
			}
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void printSudoku(int[] model) {
		// TODO Auto-generated method stub
		int[] matrix = filtermodel(model);
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				System.out.print(matrix[i * 4 + j] % 5 + " | ");
			}
			System.out.println("\n---------------");
		}
	}

	private static int[] filtermodel(int[] model) {
		// TODO Auto-generated method stub
		int[] ret = new int[16];
		int cnt = 0;
		int lastfield = 0;
		for (int i = 0; i < model.length; i++) {
			if (cnt <= 16 && model[i] > 0) {
				if (lastfield == model[i] / 5) {
					System.out.println("Doppelbelegung in " + lastfield);
				} else {
					lastfield = model[i] / 5;
					ret[cnt] = model[i];
					cnt++;
				}
			}
			if (cnt > 16 && model[i] > 0) {
				System.out.println("Mehr als eine Zahl in einem Feld");
				break;
			}
		}
		return ret;
	}

	private static void addSudokuClauses() {
		// TODO Auto-generated method stub
		try {
			solver.addClause(new VecInt(new int[] { 121 }));
			solver.addClause(new VecInt(new int[] { 212 }));
			solver.addClause(new VecInt(new int[] { 133 }));
			solver.addClause(new VecInt(new int[] { 343 }));
			solver.addClause(new VecInt(new int[] { 422 }));
			solver.addClause(new VecInt(new int[] { 121 }));
			solver.addClause(new VecInt(new int[] { 431 }));

		} catch (ContradictionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void generateOnlyOnce(int[] literals) {
		// TODO Auto-generated method stub
		if (literals.length != 4) {
			System.out.println("Nicht 4 Literale");
			return;
		}
		for (int i = 0; i < 4; i++) {
			if (literals[i] / 100 == 0 || literals[i] / 10 < 10 || literals[i] < 100) {
				System.out.println("Literale besitzen falsches Format");
				return;
			}

		}

		for (int i = -1; i < 2; i += 2) {
			for (int j = -1; j < 2; j += 2) {
				for (int k = -1; k < 2; k += 2) {
					for (int l = -1; l < 2; l += 2) {

						int[] clause = new int[4];
						// 1 + 1 + 1 - 1 = 2
						// Es darf genau nur eines falsch sein. -> KNF
						if (i + j + k + l == 2) {
							continue;
						}
						clause[0] = i * literals[0];
						clause[1] = j * literals[1];
						clause[2] = k * literals[2];
						clause[3] = l * literals[3];

						try {
							solver.addClause(new VecInt(clause));
						} catch (ContradictionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}

			}
		}
	}
}
