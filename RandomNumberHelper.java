/**
 * Random number helper utilities
 * 
 * @author Fred Morrison
 *
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomNumberHelper
{

	/**
	 * 1. Generate n random whole numbers as an IntStream using built-in .ints() feature of Random
	 * 
	 * 2. Convert IntStream to int[] using IntStream.toArray()
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
	 * 1. Generate n random whole numbers as an IntStream using built-in feature of Random
	 * 
	 * 2. convert the resulting IntStream to Stream<Integer> using boxed()
	 * 
	 * 3. convert the Stream<Integer> to Integer[]
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
	 * 1. Generate n random whole numbers as an IntStream using built-in .ints() feature of Random
	 * 
	 * 2. convert the IntStream coming out of r.ints() to a Stream<Integer) via boxed()
	 * 
	 * 3. convert Stream<Integer> to a List<Integer> using a built-in collector
	 * 
	 * @param n
	 *            - the number of values to generate
	 * @param min
	 *            - the minimum value to generate
	 * @param max
	 *            - the maximum value to generate
	 * @return - a list of Integer objects
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
	 * 1. Generate n random whole numbers as an IntStream using built-in .ints() feature of Random
	 * 
	 * 2. convert IntStream generated by r.ints() to a List<Integer> by
	 * 
	 * 2.1 collecting with an explicit "supplier" : ArrayList<Integer::new>
	 * 
	 * 2.2 an explicit "accumulator" : ArrayList::add
	 * 
	 * 2.3 an explicit "combiner" : ArrayList:: addAll
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