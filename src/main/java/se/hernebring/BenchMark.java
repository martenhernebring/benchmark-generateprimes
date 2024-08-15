package se.hernebring;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BenchMark {

  @State(Scope.Benchmark)
  public static class Parameter {
    public String str = "SampleNumber=3234 provider=Dr. M. Welby patient=John Smith priority=High";
    public String regex = "([^ =]*+)(=)([^=]*?)($|( (?= [^ =]*+=)))";
    public int zero= 0, one = 1, two = 2, three = 3, four = 4;
    public String value = "([^=]*+)=";
    public String token = "([^ ]*+)($| )";
    public String equals = "=";
    public String space = " ";
    public char equalsChar = '=';
    public char spaceChar = ' ';
    public Integer nothing = null;
  }

  @Benchmark
  public static String commandLine1(Parameter parameter, Blackhole blackhole) {
    String str = parameter.str;
    Pattern p = Pattern.compile(parameter.regex);
    Matcher m = p.matcher(str);
    StringBuilder sb = new StringBuilder();
    while(m.find()) {
      sb.append(m.group(parameter.one).length());
      sb.append(m.group(parameter.two));
      sb.append(m.group(parameter.three).length());
      sb.append(m.group(parameter.four));
    }
    blackhole.consume(str);
    blackhole.consume(p);
    blackhole.consume(m);
    return sb.toString();
  }

  @Benchmark
  public static String commandLine2(Parameter parameter, Blackhole blackhole) {
    String str = parameter.str;
    Pattern pValue = Pattern.compile(parameter.value);
    Pattern pToken = Pattern.compile(parameter.token);
    StringBuilder data = new StringBuilder(str);
    data.reverse();
    Matcher vm = pValue.matcher(data.toString());
    Matcher tm = pToken.matcher(data.toString());
    Deque<String> stack = new ArrayDeque<>((str.length() + parameter.one)/ parameter.three);
    int tmEnd = parameter.zero;
    while(vm.find(tmEnd) && tm.find(vm.end())) {
      stack.push(String.valueOf(vm.group(parameter.one).length()));
      stack.push(parameter.equals);
      stack.push(String.valueOf(tm.group(parameter.one).length()));
      stack.push(parameter.space);
      tmEnd = tm.end();
    }
    stack.pop(); //initial space
    StringBuilder result = new StringBuilder();
    while(!stack.isEmpty()) {
      result.append(stack.pop());
    }
    blackhole.consume(str);
    blackhole.consume(pValue);
    blackhole.consume(pToken);
    blackhole.consume(data);
    blackhole.consume(vm);
    blackhole.consume(tm);
    blackhole.consume(stack);
    blackhole.consume(tmEnd);
    return result.toString();
  }

  @Benchmark
  public static String commandLine3(Parameter parameter, Blackhole blackhole) {
    String str = parameter.str;
    int currentLastSpace = str.length();
    int currentEquals = str.lastIndexOf(parameter.equalsChar, currentLastSpace);
    Deque<String> stack = new ArrayDeque<>((str.length() + parameter.one)/ parameter.three);
    while(currentEquals >= parameter.zero) {
      stack.push(String.valueOf(currentLastSpace - currentEquals - parameter.one));
      stack.push(parameter.equals);
      currentLastSpace = str.lastIndexOf(parameter.spaceChar, currentEquals);
      stack.push(String.valueOf(currentEquals - currentLastSpace - parameter.one));
      stack.push(parameter.space);
      currentEquals = str.lastIndexOf(parameter.equalsChar, currentLastSpace);
    }
    if(stack.isEmpty()) {
      return String.valueOf(str.length());
    }
    stack.pop(); //remove first space
    StringBuilder sb = new StringBuilder();
    while(!stack.isEmpty()) {
      sb.append(stack.pop());
    }
    blackhole.consume(str);
    blackhole.consume(currentLastSpace);
    blackhole.consume(currentEquals);
    blackhole.consume(stack);
    return sb.toString();
  }

  public static String commandLine(Blackhole blackhole, Parameter parameter, int currentIndex, int previousIndex) {
    String str = parameter.str;
    if(currentIndex < parameter.zero) {
      return String.valueOf(str.length() - parameter.one - previousIndex);
    }
    int previousSpace = str.lastIndexOf(parameter.spaceChar, currentIndex);
    String valueLength = String.valueOf(previousSpace - previousIndex - parameter.one);
    String tokenLength = String.valueOf(currentIndex - previousSpace - parameter.one);
    String result = valueLength + parameter.space + tokenLength + parameter.equals +
        commandLine(blackhole, parameter, str.indexOf(parameter.equalsChar, currentIndex + parameter.one), currentIndex);
    blackhole.consume(str);
    blackhole.consume(currentIndex);
    blackhole.consume(previousIndex);
    blackhole.consume(previousSpace);
    blackhole.consume(valueLength);
    blackhole.consume(tokenLength);
    return result;
  }

  @Benchmark
  public static String commandLine4(Parameter parameter, Blackhole blackhole) {
    String str = parameter.str;
    int previousIndex = str.indexOf(parameter.equalsChar);
    if(previousIndex < parameter.zero) {
      return String.valueOf(str.length());
    }
    String result = previousIndex + parameter.equals +
        commandLine(blackhole, parameter, str.indexOf(parameter.equalsChar, previousIndex + parameter.one), previousIndex);
    blackhole.consume(str);
    blackhole.consume(previousIndex);
    return result;
  }

  @Benchmark
  public static String commandLine5(Parameter parameter, Blackhole blackhole) {
    String str = parameter.str;
    Deque<Integer> deque = new ArrayDeque<>((str.length() + parameter.one)/ parameter.three);
    int nextEqualsSign = str.indexOf(parameter.equalsChar);
    while(nextEqualsSign >= parameter.zero) {
      deque.add(nextEqualsSign);
      nextEqualsSign = str.indexOf(parameter.equalsChar, nextEqualsSign + parameter.one);
    }
    Integer lastEqualsIndex = deque.peekLast();
    if(lastEqualsIndex == parameter.nothing) {
      return String.valueOf(str.length());
    }
    int previousEquals = deque.pollFirst();
    StringBuilder sb = new StringBuilder(String.valueOf(previousEquals));
    for(int index : deque) {
      int previousSpace = str.lastIndexOf(parameter.spaceChar, index);
      sb.append(parameter.equalsChar).append(previousSpace - previousEquals - parameter.one).append(parameter.spaceChar).append(index - previousSpace - parameter.one);
      previousEquals = index;
    }
    sb.append(parameter.equalsChar);
    sb.append(str.length() - parameter.one - lastEqualsIndex);
    blackhole.consume(str);
    blackhole.consume(deque);
    blackhole.consume(nextEqualsSign);
    blackhole.consume(lastEqualsIndex);
    blackhole.consume(previousEquals);
    return sb.toString();
  }

}
