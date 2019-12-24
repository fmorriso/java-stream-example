import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Driver
{

	public static void main(String[] args)
	{
		final int NUM_VALUES = 30, MIN = 50, MAX = 100;
		int[] values = generateRandomIntArray(NUM_VALUES, MIN, MAX);
		System.out.println(Arrays.toString(values));

		// multiple passes over the same int array of values
		int largest = Arrays.stream(values).max().getAsInt();
		int smallest = Arrays.stream(values).min().getAsInt();
		System.out.format("1. smallest value = %d, largest value = %d%n", smallest, largest);
	
		// Must convert int[] array to Integer[] array because streams and tees don't "play nice" with primitives
		//Integer[] valuesBoxed = Arrays.stream(values).boxed().toArray(Integer[]::new);
		
		// generate Integer[] array so we can use Stream and Tee in a single (logical) pass over it.
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
	}

	public static int[] generateRandomIntArray(int size, int min, int max)
	{
		int[] values = new int[size];
		Random r = new Random();
		values = r.ints(size, min, max + 1).toArray();
		return values;
	}
	
	public static Integer[] generateRandomIntegerArray(int n, int min, int max) {
		Integer[] values = new Integer[n];
		Random r = new Random();
		
		// 1. Generate n random whole numbers
		// 2. convert the resulting IntStream to Stream<Integer>
		// 3. convert the Stream<Integer> to Integer[]
		// IntStream -> Stream<Integer> -> Integer[];		
		values = r.ints(n, min, max + 1)
				  .boxed()
				  .toArray(Integer[]::new);
		
		return values;
	}

}
