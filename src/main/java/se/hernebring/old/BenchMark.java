package se.hernebring.old;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.BitSet;

public class BenchMark {

  //@State(Scope.Benchmark)
  public static class Parameter {
    public int num = Integer.MAX_VALUE;
    public int zero = 0, one = 1, two = 2, three = 3, four = 4, eight = 8;
  }

  //@Benchmark
  public static boolean PrimeTime(Parameter parameter, Blackhole blackhole) {
    int num = parameter.num;
    if(num < parameter.four) {
      return true;
    }
    if(num % parameter.two == parameter.zero) {
      return false;
    }
    int max = (int) Math.sqrt(num);
    boolean[] notPrimes = new boolean[max];
    if(parameter.eight < max) {
      int findMax = (int) Math.sqrt(notPrimes.length);
      for(int odd = parameter.three; odd <= findMax; odd = odd + parameter.two) {
        if(!notPrimes[odd - parameter.one]) {
          for(int i = odd * parameter.three - parameter.one; i < notPrimes.length; i = i + parameter.two * odd) {
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

  //@Benchmark
  public static boolean PrimeTime2(Parameter parameter, Blackhole blackhole) {
    int num = parameter.num;
    if(num < parameter.four) {
      return true;
    }
    if(num % parameter.two == parameter.zero) {
      return false;
    }
    int max = (int) Math.sqrt(num);
    BitSet notPrimes = new BitSet(max);
    if(parameter.eight < max) {
      int findMax = (int) Math.sqrt(max);
      for(int odd = parameter.three; odd <= findMax; odd = odd + parameter.two) {
        if(!notPrimes.get(odd - parameter.one)) {
          for(int i = odd * parameter.three - parameter.one; i < max; i = i + parameter.two * odd) {
            notPrimes.set(i);
          }
        }
      }
      blackhole.consume(findMax);
    }
    for(int i = parameter.two; i < max; i = i + parameter.two) {
      if(!notPrimes.get(i)) {
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

}
