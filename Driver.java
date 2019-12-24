import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Driver
{

	public static void main(String[] args)
	{
		final int NUM_VALUES = 25, MIN = 50, MAX = 100;
		int[] values1 = generateRandomIntArray(NUM_VALUES, MIN, MAX);
		System.out.println(Arrays.toString(values1));

		// multiple passes over the same int array of values to find smallest and largest value
		int largest = Arrays.stream(values1).max().getAsInt();
		int smallest = Arrays.stream(values1).min().getAsInt();
		System.out.format("1. smallest value = %d, largest value = %d%n", smallest, largest);
		
		// generate Integer[] array so we can use Stream and Tee in a single (logical) pass over it.
		// to find the smallest and largest value.
		Integer[] values2 = generateRandomIntegerArray(NUM_VALUES, MIN, MAX);
		System.out.println(Arrays.toString(values2));
		// Find minimum and maximum values by making a single pass over an Integer[] array and create a special class to hold the result
		MinMax minmax = Stream.of(values2)
				.collect(
						Collectors.teeing(
								Collectors.minBy(Comparator.naturalOrder()),
								Collectors.maxBy(Comparator.naturalOrder()),
								(Optional<Integer> a, Optional<Integer> b) -> new MinMax(a.orElse(Integer.MIN_VALUE), b.orElse(Integer.MAX_VALUE))
								)
						);

		largest = minmax.getMax().intValue();
		smallest = minmax.getMin().intValue();
		System.out.format("2. smallest value = %d, largest value = %d%n", smallest, largest);
		
		List<Integer> values3 = generateRandomIntegerList(NUM_VALUES, MIN, MAX);
		System.out.println(values3);
		//TODO: figure out how to get smallest and largest from above list using Collectors.teeing
		
		List<Integer> values4 = generateRandomIntegerListExplicitCollector(NUM_VALUES, MIN, MAX);
		System.out.println(values4);
		//TODO: figure out how to get smallest and largest from above list using Collectors.teeing
	}

	public static int[] generateRandomIntArray(int size, int min, int max)
	{
		Random r = new Random();
		// generate random whole numbers as an IntStream and convert to int[]
		// using IntStream.toArray()
		int[] values = r.ints(size, min, max + 1)
				        .toArray();
		return values;
	}
	
	public static Integer[] generateRandomIntegerArray(int n, int min, int max) {
		Random r = new Random();		
		// 1. Generate n random whole numbers
		// 2. convert the resulting IntStream to Stream<Integer>
		// 3. convert the Stream<Integer> to Integer[]
		// IntStream -> Stream<Integer> -> Integer[];		
		Integer []values = r.ints(n, min, max + 1)
				            .boxed()
				            .toArray(Integer[]::new);
		
		return values;
	}
	
	public static List<Integer> generateRandomIntegerList(int n, int min, int max) {	
		Random r = new Random();
		// convert the IntStream coming out of r.ints to a Stream<Integer) via boxed()
		// and finally to a List<Integer> using a built-in collector
		List<Integer> values = r.ints(n, min, max + 1)
				                .boxed()				
				                .collect(Collectors.toList());
		return values;
	}
	
	public static List<Integer> generateRandomIntegerListExplicitCollector(int n, int min, int max){
		Random r = new Random();
		// convert IntStream generated by r.ints to a List<Integer> by
		// collecting with a "supplier" : ArrayList<Integer::new>
		//             and an "accumulator" : ArrayList::add 
		//             and a "combiner" : ArrayList:: addAll
		List<Integer> values = r.ints(n, min, max + 1)
				                .collect(ArrayList<Integer>::new, ArrayList::add, ArrayList::addAll);
		return values;
	}

}
