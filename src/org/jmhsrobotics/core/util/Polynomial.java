package org.jmhsrobotics.core.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.DoubleUnaryOperator;;

public class Polynomial implements DoubleUnaryOperator, Comparable<Polynomial>
{
	private double[] coeffs;

	private Polynomial(double[] coeffs)
	{
		this.coeffs = coeffs;
	}

	private static int leadingZeros(double[] arr)
	{
		int leadingZeros = 0;

		for (double d : arr)
			if(d != 0)
				break;
			else
				++leadingZeros;
		
		return leadingZeros;
	}

	public static Polynomial withCoeffs(double... coefficients)
	{
		int leadingZeros = leadingZeros(coefficients);
		Polynomial r = new Polynomial(Arrays.copyOfRange(coefficients, leadingZeros, coefficients.length));
		return r;
	}
	
	public static Polynomial withCoeffs(Collection<Double> coefficients)
	{
		return withCoeffs(coefficients.stream().mapToDouble(Double::doubleValue).toArray());
	}

	private static Polynomial ofDegree(int n)
	{
		Polynomial r = new Polynomial(new double[n + 1]);
		return r;
	}

	public double getCoefficient(int exponent)
	{
		if (exponent > degree())
			return 0;
		return coeffs[degree() - exponent];
	}

	public static Polynomial fromZeros(Iterable<Double> zeros)
	{
		Polynomial p = withCoeffs(1);
		for (double z : zeros)
			p = p.times(withCoeffs(1, -z));
		return p;
	}
	
	public static Polynomial fromZeros(double... zeros)
	{
		return fromZeros(Arrays.stream(zeros)::iterator);
	}

	public int degree()
	{
		return coeffs.length - 1;
	}

	public Polynomial times(double d)
	{
		Polynomial r = withCoeffs(coeffs);
		for (int i = 0; i <= degree(); ++i)
			r.coeffs[i] *= d;
		return r;
	}

	public Polynomial times(Polynomial p)
	{
		Polynomial r = ofDegree(degree() + p.degree());
		for (int i = degree(); i >= 0; --i)
			for (int j = p.degree(); j >= 0; --j)
				r.coeffs[i + j] += coeffs[i] * p.coeffs[j];

		return r;
	}

	public Polynomial plus(Polynomial p)
	{
		if (p.degree() > degree())
			return p.plus(this);

		Polynomial r = withCoeffs(coeffs);

		int shift = degree() - p.degree();
		for (int i = shift; i <= degree(); i++)
			r.coeffs[i] += p.coeffs[i - shift];

		int lz = leadingZeros(r.coeffs);
		if(lz > 0)
			r = new Polynomial(Arrays.copyOfRange(r.coeffs, lz, r.coeffs.length));
		
		return r;
	}

	public double eval(double x)
	{
		double y = 0;
		for (int i = 0; i <= degree(); ++i)
			y += getCoefficient(i) * Math.pow(x, i);
		return y;
	}

	public Polynomial exp(int n)
	{
		Polynomial r = withCoeffs(1);
		while (--n >= 0)
			r = r.times(this);
		return r;
	}

	public Polynomial eval(Polynomial p)
	{
		Polynomial y = withCoeffs();
		for (int i = degree(); i >= 0; --i)
			y = p.exp(i).times(getCoefficient(i)).plus(y);
		return y;
	}

	public Polynomial derivative()
	{
		if (degree() <= 0)
			return ofDegree(-1);

		Polynomial r = new Polynomial(Arrays.copyOf(coeffs, degree()));
		for (int i = 0; i < degree(); ++i)
			r.coeffs[i] *= degree() - i;

		return r;
	}

	public Polynomial integral(double c)
	{
		Polynomial r = new Polynomial(Arrays.copyOf(coeffs, degree() + 2));
		for (int i = 0; i < degree(); ++i)
			r.coeffs[i] /= degree() - i + 1;
		r.coeffs[r.degree()] = c;
		return r;
	}

	@Override
	public String toString()
	{
		if (degree() == -1)
			return "0";

		String s = "";

		for (int exp = degree(); exp >= 0; --exp)
		{
			double coeff = getCoefficient(exp);

			if (exp < degree())
			{
				if (coeff == 0)
					continue;
				else if (coeff > 0)
					s += " + ";
				else
				{
					s += " - ";
					coeff = Math.abs(coeff);
				}
			}
			else if (coeff < 0)
			{
				s += "-";
				coeff = Math.abs(coeff);
			}

			if (coeff != 1 || exp == 0)
				if (coeff == (int) coeff)
					s += (int) coeff;
				else
					s += coeff;

			if (exp > 1)
				s += "x^" + exp;
			else if (exp == 1)
				s += "x";
		}

		return s;
	}

	@Override
	public boolean equals(Object o)
	{
		if(o == null)
			return false;
		
		if(!(o instanceof Polynomial))
			return false;
		
		Polynomial p = (Polynomial) o;
		
		if(p.degree() != degree())
			return false;
		
		for(int i = 0; i <= degree(); ++i)
			if(getCoefficient(i) != p.getCoefficient(i))
				return false;
		
		return true;
	}
	
	@Override
	public int hashCode()
	{
		int code = 1340928346;
		for(double c : coeffs)
			code = code ^ Double.hashCode(c);
		return code;
	}
	
	@Override
	public double applyAsDouble(double arg0)
	{
		return eval(arg0);
	}

	/**
	 * Compares the polynomials by highest degree first.
	 */
	@Override
	public int compareTo(Polynomial p)
	{
		int upperDegree = Math.max(degree(), p.degree());
		for(int i = upperDegree; i >= 0; --i)
			if(getCoefficient(i) > p.getCoefficient(i))
				return 1;
			else if(getCoefficient(i) < p.getCoefficient(i))
				return -1;
		
		return 0;
	}
}
