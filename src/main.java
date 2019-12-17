import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

public class main {
    public static ISolver solver;

    public static void main(String[] args) {
        solver = SolverFactory.newDefault();
        solver.newVar(1000);

        for (int zeile = 1; zeile <= 9; zeile++) {
            for (int spalte = 1; spalte <= 9; spalte++) {
                int[] literals = new int[9];
                for (int feld = 1; feld <= 9; feld++) {
                    literals[feld - 1] = 100 * zeile + 10 * spalte + feld;
                }
                generateClouses(literals);
            }

        }

        for (int spalte = 1; spalte <= 9; spalte++) {
            for (int zeile = 1; zeile <= 9; zeile++) {
                int[] literals = new int[9];
                for (int feld = 1; feld <= 9; feld++) {
                    literals[feld - 1] = 100 * spalte + 10 * zeile + feld;
                }
                generateClouses(literals);
            }

        }

        for (int blockh = 0; blockh < 3; blockh++) {
            for (int blockv = 0; blockv < 3; blockv++) {

                for (int feld = 1; feld <= 9; feld++) {
                    int[] literals = new int[9];
                    int counter = 0;
                    for (int k = 3 * blockh + 1; k <= 3 * blockh + 3; k++) {
                        for (int l = 3 * blockv + 1; l <= 3 * blockv + 3; l++) {
                            literals[counter] = 100 * k + 10 * l + feld;
                            counter++;
                        }

                    }
                    generateClouses(literals);
                }
            }
        }

        setup();
        solve();


    }

    private static void solve() {
        IProblem problem = solver;

        try {
            if (problem.isSatisfiable()) {

                print(problem.model());

            } else
                System.out.println("unsat");
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }

    private static void print(int[] model) {
        int[] z = filtermodel(model);
        for (int spalte = 0; spalte < 9; spalte++) {
            for (int zeile = 0; zeile < 9; zeile++) {
                System.out.print(z[spalte * 9 + zeile] % 10 + "|");
            }
            System.out.println();
        }
    }

    private static int[] filtermodel(int[] model) {
        int[] ret = new int[81];
        int counter = 0;
        for (int i = 0; i < model.length; i++) {
            if (model[i] > 0) {
                ret[counter]=model[i];
                counter++;
            }
        }
        return ret;
    }

    private static void setup() {
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

    private static void generateClouses(int[] literals) {
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
