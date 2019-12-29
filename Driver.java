import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Driver
{

	public static void main(String[] args)
	{
		final int NUM_VALUES = 25, MIN = 50, MAX = 100;
		int smallest, largest;
		double average;
		
		int[] values1 = generateRandomIntArray(NUM_VALUES, MIN, MAX);
		System.out.println(Arrays.toString(values1));

		// multiple passes over the same int array of values to find smallest and largest value
		largest = Arrays.stream(values1).max().getAsInt();
		smallest = Arrays.stream(values1).min().getAsInt();
		System.out.format("1. smallest value = %d, largest value = %d%n", smallest, largest);
		
		// generate Integer[] array so we can use Stream and Tee in a single (logical) pass over it.
		// to find the smallest and largest value.
		// NOTE: The switch from int[] to Integer[] is forced by java due to limits on what Stream, Tee and Collect
		// can work with.
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
		largest = values3.stream()
				         .max(Comparator.naturalOrder())
				         .get()
				         .intValue();
		smallest = values3.stream()
				          .min(Comparator.naturalOrder())
				          .get()
				          .intValue();
		System.out.format("3. smallest value = %d, largest value = %d%n", smallest, largest);
		
		// Get min/max in single pass using special built-in statistics class
		IntSummaryStatistics stats3 = values3.stream()
				                             .collect(IntSummaryStatistics::new, IntSummaryStatistics::accept, IntSummaryStatistics::combine);
		smallest = stats3.getMin();
		largest = stats3.getMax();
		average = stats3.getAverage();
		System.out.format("4. smallest value = %d, largest value = %d, average = %.1f%n", smallest, largest, average);
		
		List<Integer> values4 = generateRandomIntegerListExplicitCollector(NUM_VALUES, MIN, MAX);
		System.out.println(values4);
		largest = values4.stream()
		                 .max(Comparator.naturalOrder()) //  -> Optional<Integer>
		                 .get() // -> Integer
		                 .intValue(); // int
        smallest = values4.stream() 
		                  .min(Comparator.naturalOrder())  //  -> Optional<Integer>
		                  .get() // Integer
		                  .intValue(); // int
        System.out.format("5. smallest value = %d, largest value = %d%n", smallest, largest);
		
        // use .collect(supplier, accumulator, combiner) to gather statistics on the values
        IntSummaryStatistics stats4 = values4.stream()
                                             .collect(IntSummaryStatistics::new, IntSummaryStatistics::accept, IntSummaryStatistics::combine);
        smallest = stats4.getMin();
        largest = stats4.getMax();
        average = stats4.getAverage();
        System.out.format("6. smallest value = %d, largest value = %d, average = %.1f%n", smallest, largest, average);
	}

	/**
	 * 1. Generate n random whole numbers as an IntStream using built-in .ints() feature of Random
	 * 2. Convert IntStream to int[] using IntStream.toArray()
	 * 
	 * @param n - the number of values to generate
	 * @param min - the minimum value to generate
	 * @param max - the maximum value to generate
	 * @return - an array of integers
	 */
	public static int[] generateRandomIntArray(int n, int min, int max)
	{
		Random r = new Random();
		// NOTE: the ints() upper level bound is exclusive, which is why we have to add 1 when invoking it.
		int[] values = r.ints(n, min, max + 1)
				        .toArray();
		return values;
	}
	
	/**
	 * 1. Generate n random whole numbers as an IntStream using built-in feature of Random
	 * 2. convert the resulting IntStream to Stream<Integer> using boxed()
	 * 3. convert the Stream<Integer> to Integer[]
	 * 
	 * @param n - the number of values to generate
	 * @param min - the minimum value to generate
	 * @param max - the maximum value to generate
	 * @return - an array of Integers
	 */
	public static Integer[] generateRandomIntegerArray(int n, int min, int max) {
		Random r = new Random();		
		// NOTE: the ints() upper level bound is exclusive, which is why we have to add 1 when invoking it.
		Integer []values = r.ints(n, min, max + 1)
				            .boxed()
				            .toArray(Integer[]::new);
		
		return values;
	}
	
	/**
	 * 1. Generate n random whole numbers as an IntStream using built-in .ints() feature of Random
	 * 2. convert the IntStream coming out of r.ints() to a Stream<Integer) via boxed()
	 * 3. convert Stream<Integer> to a List<Integer> using a built-in collector
	 * 
	 * @param n - the number of values to generate
	 * @param min - the minimum value to generate
	 * @param max - the maximum value to generate
	 * @return
	 */
	public static List<Integer> generateRandomIntegerList(int n, int min, int max) {	
		Random r = new Random();
		// NOTE: the ints() upper level bound is exclusive, which is why we have to add 1 when invoking it.
		List<Integer> values = r.ints(n, min, max + 1)
				                .boxed()				
				                .collect(Collectors.toList());
		return values;
	}
	
	/**
	 * 1. Generate n random whole numbers as an IntStream using built-in .ints() feature of Random
	 * 2. convert IntStream generated by r.ints() to a List<Integer> by
	 *    2.1 collecting with an explicit "supplier" : ArrayList<Integer::new>
	 *    2.2 and an explicit "accumulator" : ArrayList::add
	 *    2.3 and an explicit "combiner" : ArrayList:: addAll
	 *    
	 * @param n - the number of values to generate
	 * @param min - the minimum value to generate
	 * @param max - the maximum value to generate
	 * @return - a list of Integer objects
	 */
	public static List<Integer> generateRandomIntegerListExplicitCollector(int n, int min, int max){
		Random r = new Random();
		// NOTE: the ints() upper level bound is exclusive, which is why we have to add 1 when invoking it.
		List<Integer> values = r.ints(n, min, max + 1)
				                .collect(ArrayList<Integer>::new, ArrayList::add, ArrayList::addAll);
		return values;
	}

}
