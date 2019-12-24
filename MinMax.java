// custom class to hold a minimum and maximum whole number.
// The original purpose was to use as the output from a complex java Stream / Teeing example
public class MinMax
{
	private final Integer min;
	private final Integer max;
	
	public MinMax() {
		this(Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	
	public MinMax(Integer min, Integer max)
	{
		this.min = min;
		this.max = max;
	}
	
	public Integer getMin()
	{
		return min;
	}

	public Integer getMax()
	{
		return max;
	}

}
