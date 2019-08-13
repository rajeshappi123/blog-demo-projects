package com.github.mogikanen9.lynda;

import java.util.ArrayList;

/**
 * Challenge: Multiply two matrices
 */

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/* sequential implementation of matrix multiplication */
class SequentialMatrixMultiplier {

    private int[][] A, B;
    private int numRowsA, numColsA, numRowsB, numColsB;

    public SequentialMatrixMultiplier(int[][] A, int[][] B) {
        this.A = A;
        this.B = B;
        this.numRowsA = A.length;
        this.numColsA = A[0].length;
        this.numRowsB = B.length;
        this.numColsB = B[0].length;
        if (numColsA != numRowsB)
            throw new Error(String.format("Invalid dimensions; Cannot multiply %dx%d*%dx%d\n", numRowsA, numRowsB,
                    numColsA, numColsB));
    }

    /* returns matrix product C = AB */
    public int[][] computeProduct() {
        int[][] C = new int[numRowsA][numColsB];
        for (int i = 0; i < numRowsA; i++) {
            for (int k = 0; k < numColsB; k++) {
                int sum = 0;
                for (int j = 0; j < numColsA; j++) {
                    sum += A[i][j] * B[j][k];
                }
                C[i][k] = sum;
            }
        }
        return C;
    }
}

/* parallel implementation of matrix multiplication */
class ParallelMatrixMultiplier {

    private int[][] A, B;
    private int numRowsA, numColsA, numRowsB, numColsB;

    public ParallelMatrixMultiplier(int[][] A, int[][] B) {
        this.A = A;
        this.B = B;
        this.numRowsA = A.length;
        this.numColsA = A[0].length;
        this.numRowsB = B.length;
        this.numColsB = B[0].length;
        if (numColsA != numRowsB)
            throw new Error(String.format("Invalid dimensions; Cannot multiply %dx%d*%dx%d\n", numRowsA, numRowsB,
                    numColsA, numColsB));
    }

    /* returns matrix product C = AB */
    public int[][] computeProduct() {
        int[][] C = new int[numRowsA][numColsB];

        ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<Future<CalcRowResult>> rowFutures = new ArrayList<>();

        for (int i = 0; i < numRowsA; i++) {          

            rowFutures.add(exec.submit(new RunRow(A, B, i)));
        }

        rowFutures.stream().forEach((future) -> {
            try {
                CalcRowResult rs = future.get();
                C[rs.getIndex()] = rs.getRow();
            } catch (InterruptedException | ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        exec.shutdown();

        return C;
    }

    class CalcRowResult {
        private int[] row;
        private int index;

        public CalcRowResult(int index, int[] row) {
            this.row = row;
            this.index = index;
        }

        public int[] getRow() {
            return this.row;
        }

        public int getIndex() {
            return this.index;
        }
    }

    class RunRow implements Callable<CalcRowResult> {

        private int[][] first;
        private int[][] second;
        private int row;

        public RunRow(int[][] first, int[][] second, int row) {
            this.first = first;
            this.second = second;
            this.row = row;
        }

        @Override
        public CalcRowResult call() {
            return new CalcRowResult(row, calculateMatrixRow(first, second, row));
        }

        public int getRow() {
            return this.row;
        }
    }

    private int[] calculateMatrixRow(int[][] first, int[][] second, int row) {
        int[] rs = new int[second[0].length];
        for (int k = 0; k < rs.length; k++) {
            rs[k] = calculateMatrixValue(first, second, row, k);
        }
        return rs;
    }

    private int calculateMatrixValue(int[][] first, int[][] second, int row, int col) {

        int sum = 0;
        for (int j = 0; j < first[0].length; j++) {
            sum += A[row][j] * B[j][col];
        }
        return sum;
    }
}

public class MatrixMultiplyChallenge {

    /* helper function to generate MxN matrix of random integers */
    public static int[][] generateRandomMatrix(int M, int N) {
        System.out.format("Generating random %d x %d matrix...\n", M, N);
        Random rand = new Random();
        int[][] output = new int[M][N];
        for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                output[i][j] = rand.nextInt(100);
        return output;
    }

    /* evaluate performance of sequential and parallel implementations */
    public static void main(String args[]) {
        final int NUM_EVAL_RUNS = 5;
        final int[][] A = generateRandomMatrix(1000, 500);
        final int[][] B = generateRandomMatrix(500, 1000);

        System.out.println("Evaluating Sequential Implementation...");
        SequentialMatrixMultiplier smm = new SequentialMatrixMultiplier(A, B);
        int[][] sequentialResult = smm.computeProduct();
        double sequentialTime = 0;
        for (int i = 0; i < NUM_EVAL_RUNS; i++) {
            long start = System.currentTimeMillis();
            smm.computeProduct();
            sequentialTime += System.currentTimeMillis() - start;
        }
        sequentialTime /= NUM_EVAL_RUNS;

        System.out.println("Evaluating Parallel Implementation...");
        ParallelMatrixMultiplier pmm = new ParallelMatrixMultiplier(A, B);
        int[][] parallelResult = pmm.computeProduct();
        double parallelTime = 0;
        for (int i = 0; i < NUM_EVAL_RUNS; i++) {
            long start = System.currentTimeMillis();
            pmm.computeProduct();
            parallelTime += System.currentTimeMillis() - start;
        }
        parallelTime /= NUM_EVAL_RUNS;

        // display sequential and parallel results for comparison
        if (!Arrays.deepEquals(sequentialResult, parallelResult))
            throw new Error("ERROR: sequentialResult and parallelResult do not match!");
        System.out.format("Average Sequential Time: %.1f ms\n", sequentialTime);
        System.out.format("Average Parallel Time: %.1f ms\n", parallelTime);
        System.out.format("Speedup: %.2f \n", sequentialTime / parallelTime);
        System.out.format("Efficiency: %.2f%%\n",
                100 * (sequentialTime / parallelTime) / Runtime.getRuntime().availableProcessors());
    }
}