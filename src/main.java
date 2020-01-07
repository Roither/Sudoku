import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class main {
    public static ISolver solver;

    /*
        Vordefiniertes Spielfeld
         |9|2|6| |7|4| |5|
        5| |8| | |4|2| | |
         |3| |9| |5| |7|8|
         |1|9| |4|3|5| | |
        7|2| |5|6| | |1|3|
         | |3|2|1| |9|4| |
         |8|1| | |6|7| |4|
        9| |7|4|5| | |8|6|
        3| |5|8|7|2| |9|1|
    */

    private static int[][] BoardtoSolve = new int[9][9];

    static {
        BoardtoSolve[0] = new int[]{0, 9, 2, 6, 0, 7, 4, 0, 5};
        BoardtoSolve[1] = new int[]{5, 0, 8, 0, 0, 4, 2, 0, 0};
        BoardtoSolve[2] = new int[]{0, 3, 0, 9, 0, 5, 0, 7, 8};
        BoardtoSolve[3] = new int[]{0, 1, 9, 0, 4, 3, 5, 0, 0};
        BoardtoSolve[4] = new int[]{7, 2, 0, 5, 6, 0, 0, 1, 3};
        BoardtoSolve[5] = new int[]{0, 0, 3, 2, 1, 0, 9, 4, 0};
        BoardtoSolve[6] = new int[]{0, 8, 1, 0, 0, 6, 7, 0, 4};
        BoardtoSolve[7] = new int[]{9, 0, 7, 4, 5, 0, 0, 8, 2};
        BoardtoSolve[8] = new int[]{3, 0, 5, 8, 7, 2, 0, 9, 1};
    }

    public static void main(String[] args) {

        long timer = System.nanoTime();

        printBoard(BoardtoSolve);

        ISolver solver = SolverFactory.newDefault();

        generateDefaultClauses(solver);

        fillBoard(solver);

        IProblem problem = solver;
        try {
            if (problem.isSatisfiable()) {
                printBoard(filtermodel(problem.model()));
            } else
                System.out.println("Keine Lösung möglich");
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        System.out.println(System.nanoTime()-timer);
        System.exit(0);

    }

    private static void generateDefaultClauses(ISolver solver) {
        List<int[]> clauses = new ArrayList();
        int clauseToinsert[];
        int clauseToinsert2[];

        //Jede Zelle Zahlen von 1-9
        for (int zeile = 1; zeile < 10; zeile++) {
            for (int spalte = 1; spalte < 10; spalte++) {
                clauseToinsert = new int[9];
                for (int zahl = 1; zahl < 10; zahl++) {
                    clauseToinsert[zahl - 1] = zeile * 100 + spalte * 10 + zahl;

                }
                clauses.add(clauseToinsert);

            }
        }

        //Jede Zelle nur eine Zahl
        for (int zeile = 1; zeile < 10; zeile++) {
            for (int spalte = 1; spalte < 10; spalte++) {
                for (int zahl = 1; zahl < 10; zahl++) {
                    for (int nz = zahl + 1; nz < 10; nz++) {
                        clauses.add(new int[]{(zeile * 100 + spalte * 10 + zahl) * -1, (zeile * 100 + spalte * 10 + nz) * -1});
                        clauses.add(new int[]{(spalte * 100 + zahl * 10 + zeile) * -1, (spalte * 100 + nz * 10 + zeile) * -1});
                    }
                }
            }
        }


        //Jede Spalte Zahlen von 1-9
        for (int zahl = 1; zahl < 10; zahl++) {
            for (int spalte = 1; spalte < 10; spalte++) {
                clauseToinsert = new int[9];
                clauseToinsert2 = new int[9];
                for (int zeile = 1; zeile < 10; zeile++) {
                    clauseToinsert[zeile - 1] = (zeile * 100 + spalte * 10 + zahl) * 1;
                    clauseToinsert2[zeile - 1] = (spalte * 100 + zeile * 10 + zahl) * 1;
                }
                clauses.add(clauseToinsert);
                clauses.add(clauseToinsert2);
            }
        }


//Clauses einfügen
        clauses.forEach(clause -> {

            try {
                solver.addClause(new VecInt(clause));
            } catch (ContradictionException e) {
                e.printStackTrace();
            }

        });

        System.out.println("Anzahl an Regeln: " + clauses.size());

    }

    private static void fillBoard(ISolver solver) {

        for (int zeile = 1; zeile <= 9; zeile++) {
            for (int spalte = 1; spalte <= 9; spalte++) {
                if (BoardtoSolve[zeile - 1][spalte - 1] != 0)
                    try {
                        solver.addClause((new VecInt(new int[]{
                                zeile * 100 + spalte * 10 + BoardtoSolve[zeile - 1][spalte - 1]})));
                    } catch (ContradictionException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    private static void printBoard(int[][] board) {
        for (int spalte = 0; spalte < 9; spalte++) {
            if (spalte % 3 == 0)
                System.out.println();
            for (int zeile = 0; zeile < 9; zeile++) {
                if (zeile % 3 == 0)
                    System.out.print(" |");
                System.out.print((board[spalte][zeile] == 0 ? "_" : board[spalte][zeile]) + "|");
            }
            System.out.println();
        }
    }

    private static int[][] filtermodel(int[] model) {

        int[][] solvedBoard = new int[9][9];
        for (int i = 0; i < model.length; i++) {
            if (model[i] > 0) {
                int zeile = (model[i] / 100) - 1;
                int spalte = ((model[i] % 100) / 10) - 1;
                int zahl = model[i] % 10;

                if (solvedBoard[zeile][spalte] != 0)
                    System.out.printf("Mehr als eine Zahl im Feld %d:%d", zeile, spalte);
                else
                    solvedBoard[zeile][spalte] = zahl;
            }
        }
        return solvedBoard;
    }

}
