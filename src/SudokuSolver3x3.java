import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

public class SudokuSolver3x3 {
    public static ISolver solver = SolverFactory.newDefault();

    public static void main(String[] args) {

        solver.setTimeout(3600);
        solver.newVar(1000);
        solver.setExpectedNumberOfClauses(500);

        // Klausel = int[] {5, 6, -7} ==> 5 or 6 or not 7 is right
        // 0 Kann nicht verwendet werden ==> +-0 == 0

        // Zeile
        for (int i = 1; i <= 9; i++) {
            // Spalte
            for (int j = 1; j <= 9; j++) {

                int[] literals = new int[9];

                // Jedes Feld hat nur eine Zahl
                for (int k = 1; k <= 9; k++) {
                    literals[k - 1] = 100 * i + 10 * j + k;
                }
                generateOnlyOnce(literals);
            }
        }
        // Zeile
        for (int i = 1; i <= 9; i++) {
            // Jede Zahl nur einmal
            for (int k = 1; k <= 9; k++) {
                int[] literals = new int[9];
                // Spalte
                for (int j = 1; j <= 9; j++) {
                    literals[j - 1] = 100 * i + 10 * j + k;
                }
                generateOnlyOnce(literals);
            }
        }
        // Spalte
        for (int j = 1; j <= 9; j++) {
            // Jede Zahl nur einmal
            for (int k = 1; k <= 9; k++) {
                int[] literals = new int[9];
                // Zeile
                for (int i = 1; i <= 9; i++) {
                    literals[i - 1] = 100 * i + 10 * j + k;
                }
                generateOnlyOnce(literals);
            }
        }
        // Bloecke in der Horizontalen
        for (int i = 0; i < 3; i++) {
            // Bloecke in der Vertikalen
            for (int j = 0; j < 3; j++) {
                // Zahl
                for (int z = 1; z <= 9; z++) {
                    // Durch den Block
                    int[] literals = new int[9];
                    int cnt = 0;
                    for (int k = 3 * i + 1; k <= 3 * i + 3; k++) {
                        // Durch anderen Block
                        for (int l = 3 * j + 1; l <= 3 * j + 3; l++) {
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
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(matrix[i * 9 + j] % 10 + " | ");
            }
            System.out.println("\n---------------");
        }
    }

    private static int[] filtermodel(int[] model) {
        // TODO Auto-generated method stub
        int[] ret = new int[81];
        int cnt = 0;
        int lastfield = 0;
        for (int i = 0; i < model.length; i++) {
            if (cnt <= 81 && model[i] > 0) {
                if (lastfield == model[i] / 10) {
                    System.out.println("Doppelbelegung in " + lastfield);
                } else {
                    lastfield = model[i] / 10;
                    ret[cnt] = model[i];
                    cnt++;
                }
            }
            if (cnt > 81 && model[i] > 0) {
                System.out.println("Mehr als eine Zahl in einem Feld");
                break;
            }
        }
        return ret;
    }

    private static void addSudokuClauses() {
        try {
            solver.addClause(new VecInt(new int[]{112}));
            solver.addClause(new VecInt(new int[]{135}));
            solver.addClause(new VecInt(new int[]{213}));
            solver.addClause(new VecInt(new int[]{238}));
            solver.addClause(new VecInt(new int[]{246}));
            solver.addClause(new VecInt(new int[]{279}));
            solver.addClause(new VecInt(new int[]{341}));
            solver.addClause(new VecInt(new int[]{374}));
            solver.addClause(new VecInt(new int[]{455}));
            solver.addClause(new VecInt(new int[]{481}));
            solver.addClause(new VecInt(new int[]{559}));
            solver.addClause(new VecInt(new int[]{582}));
            solver.addClause(new VecInt(new int[]{618}));
            solver.addClause(new VecInt(new int[]{627}));
            solver.addClause(new VecInt(new int[]{652}));
            solver.addClause(new VecInt(new int[]{758}));
            solver.addClause(new VecInt(new int[]{769}));
            solver.addClause(new VecInt(new int[]{793}));
            solver.addClause(new VecInt(new int[]{836}));
            solver.addClause(new VecInt(new int[]{863}));
            solver.addClause(new VecInt(new int[]{895}));
            solver.addClause(new VecInt(new int[]{915}));
            solver.addClause(new VecInt(new int[]{934}));
            solver.addClause(new VecInt(new int[]{991}));

        } catch (ContradictionException e) {
            e.printStackTrace();
        }
    }

    private static void generateOnlyOnce(int[] literals) {
        // TODO Auto-generated method stub
        if (literals.length != 9) {
            System.out.println("Nicht 9 Literale");
            return;
        }
        for (int i = 0; i < 9; i++) {
            if (literals[i] / 100 == 0 || literals[i] / 10 < 10 || literals[i] < 100) {
                System.out.println("Literale besitzen falsches Format");
                return;
            }

        }
        for (int i = -1; i < 2; i += 2) {
            for (int j = -1; j < 2; j += 2) {
                for (int k = -1; k < 2; k += 2) {
                    for (int l = -1; l < 2; l += 2) {
                        for (int m = -1; m < 2; m += 2) {
                            for (int n = -1; n < 2; n += 2) {
                                for (int o = -1; o < 2; o += 2) {
                                    for (int p = -1; p < 2; p += 2) {
                                        for (int q = -1; q < 2; q += 2) {
                                            int clause[] = new int[9];
                                            if (i + j + k + l + m + n + o + p + q == 7)
                                                continue;

                                            clause[0] = i * literals[0];
                                            clause[1] = j * literals[1];
                                            clause[2] = k * literals[2];
                                            clause[3] = l * literals[3];
                                            clause[4] = m * literals[4];
                                            clause[5] = n * literals[5];
                                            clause[6] = o * literals[6];
                                            clause[7] = p * literals[7];
                                            clause[8] = q * literals[8];


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
                }
            }

        }
    }
}
