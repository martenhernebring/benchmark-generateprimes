package se.hernebring;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.math.BigInteger;

public class BenchMark {

  @State(Scope.Benchmark)
  public static class Parameter {
    public final int num = Integer.MAX_VALUE;
    public final int zero = 0, one = 1, two = 2, three = 3, four = 4, five = 5, six = 6, eight = 8, twentyFour = 24, fourty = 40;
    public final BigInteger ONE = BigInteger.valueOf(1), TWO = BigInteger.valueOf(2);
    public final int[] INTEGERS_SPRP = {7, 61};
    public final String empty = "";
  }

  @Benchmark
  public static boolean PrimeTime(Parameter parameter, Blackhole blackhole) {
    int num = parameter.num;
    if(num < parameter.four) {
      return num >= parameter.two;
    }
    if((num & parameter.one) == parameter.zero) {
      return false;
    }
    int max = (int) Math.sqrt(num);
    boolean[] notPrimes = new boolean[max];
    if(parameter.eight < max) {
      int findMax = (int) Math.sqrt(notPrimes.length);
      for(int odd = parameter.three; odd <= findMax; odd = odd + parameter.two) {
        if(!notPrimes[odd - parameter.one]) {
          for(int i = odd * parameter.three - parameter.one; i < notPrimes.length; i = i + odd << parameter.one) {
            notPrimes[i] = true;
          }
        }
      }
      blackhole.consume(findMax);
    }
    for(int i = parameter.two; i < notPrimes.length; i = i + parameter.two) {
      if(!notPrimes[i]) {
        if(num % (i + parameter.one) == parameter.zero) {
          return false;
        }
      }
    }
    blackhole.consume(num);
    blackhole.consume(max);
    blackhole.consume(notPrimes);
    return true;
  }

  @Benchmark
  public static boolean PrimeTime2(Parameter parameter, Blackhole blackhole) {
    int num = parameter.num;
    if(num < parameter.four) {
      return num >= parameter.two;
    }
    if((num & parameter.one) == parameter.zero || num % parameter.three == parameter.zero) {
      return false;
    }
    int max = (int) Math.sqrt(num);
    boolean[] notPrimes = new boolean[max];
    if(parameter.twentyFour < max) {
      int findMax = (int) Math.sqrt(notPrimes.length);
      for(int odd = parameter.five; odd <= findMax; odd = odd + parameter.two) {
        if(!notPrimes[odd - parameter.one]) {
          for(int i = odd * odd - parameter.one; i < notPrimes.length; i = i + odd << parameter.one) {
            notPrimes[i] = true;
          }
        }
      }
      blackhole.consume(findMax);
    }
    for(int i = parameter.five; i <= notPrimes.length; i = i + parameter.six) {
      if(!notPrimes[i - parameter.one]) {
        if(num % i == parameter.zero) {
          return false;
        }
      }
      if(i + parameter.one < notPrimes.length && !notPrimes[i + parameter.one]) {
        if(num % (i + parameter.two) == parameter.zero) {
          return false;
        }
      }
    }
    blackhole.consume(num);
    blackhole.consume(max);
    blackhole.consume(notPrimes);
    return true;
  }

  @Benchmark
  public static boolean PrimeTime3(Parameter parameter, Blackhole blackhole) {
    int num = parameter.num;
    if(num < parameter.four) {
      return num >= parameter.two;
    }
    int max = (int) Math.sqrt(num);
    boolean[] notPrimes = new boolean[max];
    int findMax = (int) Math.sqrt(notPrimes.length);
    for(int n = parameter.two; n <= findMax; n++) {
      if(!notPrimes[n - parameter.one]) {
        for(int i = (n << parameter.one) - parameter.one; i < notPrimes.length; i = i + n) {
          notPrimes[i] = true;
        }
      }
    }
    blackhole.consume(findMax);
    for(int i = parameter.one; i < notPrimes.length; i++) {
      if(!notPrimes[i]) {
        if(num % (i + parameter.one) == parameter.zero) {
          return false;
        }
      }
    }
    blackhole.consume(num);
    blackhole.consume(max);
    blackhole.consume(notPrimes);
    return true;
  }

  @Benchmark
  public static boolean PrimeTime4(Parameter parameter) {
    BigInteger bi = new BigInteger(parameter.num + parameter.empty);
    return bi.isProbablePrime(parameter.fourty);
  }

  @Benchmark
  public static boolean PrimeTime5(Parameter parameter, Blackhole blackhole) {
    int num = parameter.num;
    if(num < parameter.four) {
      return num >= parameter.two;
    }
    if(num == parameter.INTEGERS_SPRP[parameter.zero] || num == parameter.INTEGERS_SPRP[parameter.one]) {
      return true;
    }
    if((num & parameter.one) == parameter.zero) {
      return false;
    }
    BigInteger bi = new BigInteger(num + parameter.empty);
    blackhole.consume(num);
    return passesMillerRabinStrongProbablePrimeBaseForIntegers(bi, parameter, blackhole);
  }

  private static boolean passesMillerRabinStrongProbablePrimeBaseForIntegers(BigInteger biUnderTest, Parameter parameter, Blackhole blackhole) {
    // Find a and m such that m is odd and this == 1 + 2**a * m
    BigInteger thisMinusOne = biUnderTest.subtract(parameter.ONE);
    BigInteger m = thisMinusOne;
    int a = m.getLowestSetBit();
    m = m.shiftRight(a);

    // Do the tests
    for (int i = parameter.zero; i < parameter.INTEGERS_SPRP.length; i++) {
      int j = parameter.zero;
      BigInteger z = new BigInteger(parameter.INTEGERS_SPRP[i] + parameter.empty).modPow(m, biUnderTest);
      while (!((j == parameter.zero && z.equals(parameter.ONE)) || z.equals(thisMinusOne))) {
        if (j > parameter.zero && z.equals(parameter.ONE) || ++j == a)
          return false;
        z = z.modPow(parameter.TWO, biUnderTest);
      }
      blackhole.consume(z);
    }
    blackhole.consume(biUnderTest);
    blackhole.consume(thisMinusOne);
    blackhole.consume(a);
    blackhole.consume(m);
    return true;
  }

}
