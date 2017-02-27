package com.en.helpers;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Created by atrposki on 14-Feb-17.
 */
public class ExpressionSplitter {
    private Set<String> allOperations;
    private BiFunction<Character,Character,Boolean> wordSplitCriteria;
    private List<String> prioritizedSplitTokenList;


    public ExpressionSplitter(Set<String> operationTokens, BiFunction<Character,Character,Boolean> wordSplitCriteria ) {
        this.allOperations=operationTokens;
        this.wordSplitCriteria =wordSplitCriteria;
        this.prioritizedSplitTokenList = allOperations.stream().filter(x->!"".equals(x)).collect(Collectors.toList());
        Collections.sort(prioritizedSplitTokenList,(l, r)->Integer.compare(r.length(),l.length()));
    }

    public List<String> split(String rawExpression){
        List<String> expressions= Arrays.asList(rawExpression);
        for (String operation : prioritizedSplitTokenList) {
            expressions = expressions.stream()
                         .flatMap(x->split(x,operation).stream())
                         .collect(Collectors.toList());
        }

        expressions = expressions.stream()
                .flatMap(x->split(x, wordSplitCriteria).stream())
                .collect(Collectors.toList());

        return expressions.stream()
                          .filter(x->x!=null)
                          .filter(x->{
                              String trimmed = x.trim();
                              return !trimmed.equals("") && trimmed.length()>0;
                          })
                          .filter(x->x.length()>0)
                          .filter(x->!"".equals(x))
                          .collect(Collectors.toList());
    }

    public List<String> split(String string,String delimiter){

        if(string.length()<delimiter.length() || allOperations.contains(string)){
            return Arrays.asList(string);
        }

        ArrayList<String> result = new ArrayList<>();
        StringBuilder match= new StringBuilder();
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < string.length() ; i++) {
            char c = string.charAt(i);
            buffer.append(c);

            if(match.length()>=delimiter.length()){
                match.deleteCharAt(0);
            }
            match.append(c);

            if(match.toString().equals(delimiter)){
                buffer.setLength(buffer.length()-delimiter.length());
                if(buffer.length()>0){
                    result.add(buffer.toString());
                }
                result.add(delimiter);
                buffer= new StringBuilder();
                match = new StringBuilder();
            }
        }

        result.add(buffer.toString());

        return result;
    }

    public List<String> split(String str,BiFunction<Character,Character,Boolean> splitCriteria){

        if(str.length()<=0){
            return  new ArrayList<>();
        }

        List<String> result= new ArrayList<>();
         StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if(builder.length()>0 &&
                    splitCriteria.apply(builder.charAt(builder.length()-1),c)){
                result.add(builder.toString());
                builder=new StringBuilder();
            }
            builder.append(c);
        }

        if(builder.length()>0){
            result.add(builder.toString());
        }

        return result;
    }
}


























