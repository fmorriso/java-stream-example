import java.util.Arrays;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Driver
{

	public static void main(String[] args)
	{
		final int NUM_VALUES = 100, MIN = 50, MAX = 100;
		int smallestWholeNumber, largestWholeNumber;
		double average, smallestDecimalNumber, largestDecimalNumber;

		int[] primitiveIntArray = RandomNumberHelper.generateRandomIntArray(NUM_VALUES, MIN, MAX);
		// System.out.println(Arrays.toString(values1));

		// multiple passes over the same int array of values to find smallest and largest value
		largestWholeNumber = Arrays	.stream(primitiveIntArray)
									.max()
									.getAsInt();

		smallestWholeNumber = Arrays.stream(primitiveIntArray)
									.min()
									.getAsInt();

		System.out.format("1. smallest value = %d, largest value = %d%n", smallestWholeNumber, largestWholeNumber);

		// generate Integer[] array so we can use Stream and Tee in a single (logical) pass over it.
		// to find the smallest and largest value.
		// NOTE: The switch from int[] to Integer[] is forced by java due to limits on what Stream, Tee and Collect
		// can work with.
		Integer[] values2 = RandomNumberHelper.generateRandomIntegerArray(NUM_VALUES, MIN, MAX);
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

		List<Integer> values3 = RandomNumberHelper.generateRandomIntegerList(NUM_VALUES, MIN, MAX);
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

		List<Integer> values4 = RandomNumberHelper.generateRandomIntegerListExplicitCollector(NUM_VALUES, MIN, MAX);
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

		double[] values5 = RandomNumberHelper.generateRandomDoubleArray(NUM_VALUES, MIN, MAX);
		// System.out.println(Arrays.toString(values5));
		DoubleSummaryStatistics stats5 = Arrays	.stream(values5)
												.collect(DoubleSummaryStatistics::new, DoubleSummaryStatistics::accept, DoubleSummaryStatistics::combine);
		smallestDecimalNumber = stats5.getMin();
		largestDecimalNumber = stats5.getMax();
		average = stats5.getAverage();
		System.out.format("7. smallest value = %.3f, largest value = %.3f, average = %.3f%n", smallestDecimalNumber, largestDecimalNumber, average);

		Double[] values6 = RandomNumberHelper.generateRandomBoxedDoubleArray(NUM_VALUES, MIN, MAX);
		// use Stream.collect(supplier, accumulator, combiner) to gather statistics on the values
		DoubleSummaryStatistics stats6 = Arrays	.stream(values6)
												.collect(DoubleSummaryStatistics::new, DoubleSummaryStatistics::accept, DoubleSummaryStatistics::combine);
		smallestDecimalNumber = stats6.getMin();
		largestDecimalNumber = stats6.getMax();
		average = stats6.getAverage();
		System.out.format("8. smallest value = %.3f, largest value = %.3f, average = %.3f%n", smallestDecimalNumber, largestDecimalNumber, average);

	}

}
