import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
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
		final int NUM_VALUES = 100, MIN = 50, MAX = 100;
		int smallestWholeNumber, largestWholeNumber;
		double average, smallestDecimalNumber, largestDecimalNumber;

		int[] values1 = generateRandomIntArray(NUM_VALUES, MIN, MAX);
		// System.out.println(Arrays.toString(values1));

		// multiple passes over the same int array of values to find smallest and largest value
		largestWholeNumber = Arrays.stream(values1).max().getAsInt();
		smallestWholeNumber = Arrays.stream(values1).min().getAsInt();
		System.out.format("1. smallest value = %d, largest value = %d%n", smallestWholeNumber, largestWholeNumber);

		// generate Integer[] array so we can use Stream and Tee in a single (logical) pass over it.
		// to find the smallest and largest value.
		// NOTE: The switch from int[] to Integer[] is forced by java due to limits on what Stream, Tee and Collect
		// can work with.
		Integer[] values2 = generateRandomIntegerArray(NUM_VALUES, MIN, MAX);
		// System.out.println(Arrays.toString(values2));
		// Find minimum and maximum values by making a single pass over an Integer[] array and create a special class to hold the result
		MinMax minmax = Stream	.of(values2)
								.collect(
										Collectors.teeing(
												Collectors.minBy(Comparator.naturalOrder()),
												Collectors.maxBy(Comparator.naturalOrder()),
												(Optional<Integer> a, Optional<Integer> b) -> new MinMax(a.orElse(Integer.MIN_VALUE), b.orElse(Integer.MAX_VALUE))));

		largestWholeNumber = minmax.getMax().intValue();
		smallestWholeNumber = minmax.getMin().intValue();
		System.out.format("2. smallest value = %d, largest value = %d%n", smallestWholeNumber, largestWholeNumber);

		List<Integer> values3 = generateRandomIntegerList(NUM_VALUES, MIN, MAX);
		// System.out.println(values3);
		largestWholeNumber = values3.stream()
									.max(Comparator.naturalOrder())
									.get()
									.intValue();
		smallestWholeNumber = values3	.stream()
										.min(Comparator.naturalOrder())
										.get()
										.intValue();
		System.out.format("3. smallest value = %d, largest value = %d%n", smallestWholeNumber, largestWholeNumber);

		// Get min/max in single pass using special built-in statistics class
		IntSummaryStatistics stats3 = values3	.stream()
												.collect(IntSummaryStatistics::new, IntSummaryStatistics::accept, IntSummaryStatistics::combine);
		smallestWholeNumber = stats3.getMin();
		largestWholeNumber = stats3.getMax();
		average = stats3.getAverage();
		System.out.format("4. smallest value = %d, largest value = %d, average = %.1f%n", smallestWholeNumber, largestWholeNumber, average);

		List<Integer> values4 = generateRandomIntegerListExplicitCollector(NUM_VALUES, MIN, MAX);
		// System.out.println(values4);
		largestWholeNumber = values4.stream()
									.max(Comparator.naturalOrder()) //  -> Optional<Integer>
									.get() // -> Integer
									.intValue(); // int
		smallestWholeNumber = values4	.stream()
										.min(Comparator.naturalOrder()) //  -> Optional<Integer>
										.get() // Integer
										.intValue(); // int
		System.out.format("5. smallest value = %d, largest value = %d%n", smallestWholeNumber, largestWholeNumber);

		// use Stream.collect(supplier, accumulator, combiner) to gather statistics on the values
		IntSummaryStatistics stats4 = values4	.stream()
												.collect(IntSummaryStatistics::new, IntSummaryStatistics::accept, IntSummaryStatistics::combine);
		smallestWholeNumber = stats4.getMin();
		largestWholeNumber = stats4.getMax();
		average = stats4.getAverage();
		System.out.format("6. smallest value = %d, largest value = %d, average = %.1f%n", smallestWholeNumber, largestWholeNumber, average);

		double[] values5 = generateRandomDoubleArray(NUM_VALUES, MIN, MAX);
		// System.out.println(Arrays.toString(values5));
		DoubleSummaryStatistics stats5 = Arrays	.stream(values5)
												.collect(DoubleSummaryStatistics::new, DoubleSummaryStatistics::accept, DoubleSummaryStatistics::combine);
		smallestDecimalNumber = stats5.getMin();
		largestDecimalNumber = stats5.getMax();
		average = stats5.getAverage();
		System.out.format("7. smallest value = %.3f, largest value = %.3f, average = %.3f%n", smallestDecimalNumber, largestDecimalNumber, average);

		Double[] values6 = generateRandomBoxedDoubleArray(NUM_VALUES, MIN, MAX);
		// use Stream.collect(supplier, accumulator, combiner) to gather statistics on the values
		DoubleSummaryStatistics stats6 = Arrays	.stream(values6)
												.collect(DoubleSummaryStatistics::new, DoubleSummaryStatistics::accept, DoubleSummaryStatistics::combine);
		smallestDecimalNumber = stats6.getMin();
		largestDecimalNumber = stats6.getMax();
		average = stats6.getAverage();
		System.out.format("8. smallest value = %.3f, largest value = %.3f, average = %.3f%n", smallestDecimalNumber, largestDecimalNumber, average);

	}

	/**
	 * 1. Generate n random whole numbers as an IntStream using built-in .ints() feature of Random 2. Convert IntStream to int[] using IntStream.toArray()
	 * 
	 * @param n
	 *            - the number of values to generate
	 * @param min
	 *            - the minimum value to generate
	 * @param max
	 *            - the maximum value to generate
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
	 * 1. Generate n random whole numbers as an IntStream using built-in feature of Random 2. convert the resulting IntStream to Stream<Integer> using boxed() 3. convert the Stream<Integer> to
	 * Integer[]
	 * 
	 * @param n
	 *            - the number of values to generate
	 * @param min
	 *            - the minimum value to generate
	 * @param max
	 *            - the maximum value to generate
	 * @return - an array of Integers
	 */
	public static Integer[] generateRandomIntegerArray(int n, int min, int max)
	{
		Random r = new Random();

		// NOTE: the ints() upper level bound is exclusive, which is why we have to add 1 when invoking it.
		Integer[] values = r.ints(n, min, max + 1)
							.boxed()
							.toArray(Integer[]::new);

		return values;
	}

	/**
	 * 1. Generate n random whole numbers as an IntStream using built-in .ints() feature of Random 2. convert the IntStream coming out of r.ints() to a Stream<Integer) via boxed() 3. convert
	 * Stream<Integer> to a List<Integer> using a built-in collector
	 * 
	 * @param n
	 *            - the number of values to generate
	 * @param min
	 *            - the minimum value to generate
	 * @param max
	 *            - the maximum value to generate
	 * @return
	 */
	public static List<Integer> generateRandomIntegerList(int n, int min, int max)
	{
		Random r = new Random();

		// NOTE: the ints() upper level bound is exclusive, which is why we have to add 1 when invoking it.
		List<Integer> values = r.ints(n, min, max + 1)
								.boxed()
								.collect(Collectors.toList());

		return values;
	}

	/**
	 * 1. Generate n random whole numbers as an IntStream using built-in .ints() feature of Random 2. convert IntStream generated by r.ints() to a List<Integer> by 2.1 collecting with an explicit
	 * "supplier" : ArrayList<Integer::new> 2.2 and an explicit "accumulator" : ArrayList::add 2.3 and an explicit "combiner" : ArrayList:: addAll
	 * 
	 * @param n
	 *            - the number of values to generate
	 * @param min
	 *            - the minimum value to generate
	 * @param max
	 *            - the maximum value to generate
	 * @return - a list of Integer objects
	 */
	public static List<Integer> generateRandomIntegerListExplicitCollector(int n, int min, int max)
	{
		Random r = new Random();

		// NOTE: the ints() upper level bound is exclusive, which is why we have to add 1 when invoking it.
		List<Integer> values = r.ints(n, min, max + 1)
								.collect(ArrayList<Integer>::new, ArrayList::add, ArrayList::addAll);
		return values;
	}

	/**
	 * 1. Generate n random decimal numbers as a DoubleStream using built-in .doubles() feature of Random
	 * 
	 * 2. Convert DoubleStream to doubles[] using DoubleStream.toArray()
	 * 
	 * @param n
	 *            - the number of values to generate
	 * @param min
	 *            - the minimum value to generate
	 * @param max
	 *            - the maximum value to generate
	 * @return - an array of doubles
	 */
	public static double[] generateRandomDoubleArray(int n, double min, double max)
	{

		Random r = new Random();

		// NOTE: Random.doubles() upper level bound is exclusive
		double[] values = r	.doubles(n, min, max)
							.toArray();

		return values;
	}

	/**
	 * 1. Generate n random decimal numbers as a DoubleStream using built-in .doubles() feature of Random
	 * 
	 * 2. Convert DoubleSream to Stream<Double> using DoubleStream.boxed()
	 * 
	 * 3. Convert DoubleStream to Double[] using Stgream.toArray()
	 * 
	 * 
	 * @param n
	 *            - the number of values to generate
	 * @param min
	 *            - the minimum value to generate
	 * @param max
	 *            - the maximum value to generate
	 * @return - an array of Double
	 */
	public static Double[] generateRandomBoxedDoubleArray(int n, double min, double max)
	{
		Random r = new Random();

		// NOTE: Random.doubles() upper level bound is exclusive
		Double[] values = r	.doubles(n, min, max)
							.boxed()
							.toArray(Double[]::new);

		return values;
	}

}
