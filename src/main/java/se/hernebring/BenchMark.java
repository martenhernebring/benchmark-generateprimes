package se.hernebring;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.math.BigInteger;

public class BenchMark {

  @State(Scope.Benchmark)
  public static class Parameter {
    public int num = Integer.MAX_VALUE;
    public int zero = 0, one = 1, two = 2, three = 3, four = 4, five = 5, six = 6, twentyFour = 24;
  }

  @Benchmark
  public static boolean PrimeTime(Parameter parameter, Blackhole blackhole) {
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
  public static boolean PrimeTime2(Parameter parameter) {
    int num = parameter.num;
    BigInteger bi = new BigInteger(num + "");
    return bi.isProbablePrime(100);
  }

  @Benchmark
  public static boolean PrimeTime3(Parameter parameter) {
    int num = parameter.num;
    BigInteger bi = new BigInteger(num + "");
    return bi.isProbablePrime(90);
  }

  @Benchmark
  public static boolean PrimeTime4(Parameter parameter) {
    int num = parameter.num;
    BigInteger bi = new BigInteger(num + "");
    return bi.isProbablePrime(80);
  }

  @Benchmark
  public static boolean PrimeTime5(Parameter parameter) {
    int num = parameter.num;
    BigInteger bi = new BigInteger(num + "");
    return bi.isProbablePrime(70);
  }

  @Benchmark
  public static boolean PrimeTime6(Parameter parameter) {
    int num = parameter.num;
    BigInteger bi = new BigInteger(num + "");
    return bi.isProbablePrime(60);
  }

  @Benchmark
  public static boolean PrimeTime7(Parameter parameter) {
    int num = parameter.num;
    BigInteger bi = new BigInteger(num + "");
    return bi.isProbablePrime(50);
  }

  @Benchmark
  public static boolean PrimeTime8(Parameter parameter) {
    int num = parameter.num;
    BigInteger bi = new BigInteger(num + "");
    return bi.isProbablePrime(40);
  }

  @Benchmark
  public static boolean PrimeTime9(Parameter parameter) {
    int num = parameter.num;
    BigInteger bi = new BigInteger(num + "");
    return bi.isProbablePrime(30);
  }

  @Benchmark
  public static boolean PrimeTime10(Parameter parameter) {
    int num = parameter.num;
    BigInteger bi = new BigInteger(num + "");
    return bi.isProbablePrime(20);
  }

  @Benchmark
  public static boolean PrimeTime11(Parameter parameter) {
    int num = parameter.num;
    BigInteger bi = new BigInteger(num + "");
    return bi.isProbablePrime(10);
  }

  @Benchmark
  public static boolean PrimeTime12(Parameter parameter) {
    int num = parameter.num;
    BigInteger bi = new BigInteger(num + "");
    return bi.isProbablePrime(1);
  }

}
