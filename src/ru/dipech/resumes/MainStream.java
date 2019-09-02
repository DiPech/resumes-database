package ru.dipech.resumes;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainStream {

    public static void main(String[] args) {

        System.out.println(minValue(new int[]{1, 2, 3, 3, 2, 3}));
        System.out.println(minValue(new int[]{9, 8}));
        System.out.println(minValue(new int[]{4, 4, 2, 9, 7, 7}));

        System.out.println("=========================================================================================");

        testOddOrEven(MainStream::oddOrEven);

        System.out.println("=========================================================================================");

        testOddOrEven(MainStream::oddOrEvenThroughPartitioning);
    }

    private static int minValue(int[] values) {
        return Arrays.stream(values).distinct().sorted().reduce(0, (left, right) -> 10 * left + right);
    }

    private static void testOddOrEven(OddOrEvenProcessor processor) {
        computeOddOrEvenAndPrint(Arrays.asList(1, 1, 2), processor);
        computeOddOrEvenAndPrint(Arrays.asList(3, 3, 1), processor);
        computeOddOrEvenAndPrint(Arrays.asList(1, 3, 2, 5, 8, 8, 9), processor);
        computeOddOrEvenAndPrint(Arrays.asList(3, 2, 1, 2, 3), processor);
    }

    private static void computeOddOrEvenAndPrint(List<Integer> integers, OddOrEvenProcessor processor) {
        System.out.println(processor.oddOrEven(integers));
    }

    private interface OddOrEvenProcessor {
        List<Integer> oddOrEven(List<Integer> integers);
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        int sumMod2 = integers.stream().reduce(0, Integer::sum) % 2;
        return integers.stream()
                .filter(i -> i % 2 != sumMod2)
                .collect(Collectors.toList());
    }

    private static List<Integer> oddOrEvenThroughPartitioning(List<Integer> integers) {
        Map<Boolean, List<Integer>> result = integers.stream().collect(Collectors.partitioningBy(i -> i % 2 == 0));
        return result.get(result.get(true).size() % 2 == 0);
    }

}
