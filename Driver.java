import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
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

		// try experiment with String before trying with int
		Stream<String> stringStream = Stream.of("a", "b", "c");
		String[] stringArray = stringStream.toArray(size -> new String[size]);
		Arrays.stream(stringArray).forEach(System.out::println);

		// another attempt
		//int[] vec1 =  Stream.of(1,2,3,4).filter(t -> t != null).mapToInt(t -> t).toArray();

		Stream<Integer> test1 = Stream.of(1, 2, 3, 4);
		int[] test2 = Stream.of(1, 2, 3, 4).mapToInt(i -> i).toArray();

		// 2. Stream.of -> Stream<int[]>
		var temp = Arrays.stream(values);

		//  int[] arr = listOfIntegers.stream().mapToInt(x->x).toArray();
		
		// Convert int[] array to Integer[] array
		Integer[] valuesBoxed = Arrays.stream(values).boxed().toArray(Integer[]::new);
		
		// Find minimum and maximu values by making a single pass over an Integer[] array
		MinMax minmax = Stream.of(valuesBoxed)
				.collect(
						Collectors.teeing(
								Collectors.minBy(Comparator.naturalOrder()),
								Collectors.maxBy(Comparator.naturalOrder()), 
								(Optional<Integer> a, Optional<Integer> b) -> new MinMax(a.orElse(Integer.MIN_VALUE), b.orElse(Integer.MAX_VALUE))
								)
						);

		System.out.format("2. smallest value = %d, largest value = %d%n", minmax.getMin(), minmax.getMax());



	}

	public static int[] generateRandomIntArray(int size, int min, int max)
	{
		int[] values = new int[size];
		Random r = new Random();
		values = r.ints(size, min, max + 1).toArray();
		return values;
	}

}
