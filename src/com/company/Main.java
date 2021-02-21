package com.company;

import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) throws Exception {
        Work work = new Work();
        work.run();
    }
}

class Work {
    private boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Float calculate(String polishNotation) throws Exception {
        Stack<Float> stack = new Stack<>();

        for (String token : polishNotation.split(" ")) {
            if (token.equals("")) continue;

            if (isFloat(token)) {
                stack.push(Float.parseFloat(token));
                continue;
            }
            Float right = stack.pop();

            if (stack.isEmpty()) {
                if (token.equals("+") || token.equals("-")) stack.push(token.equals("+") ? right + 1 : right - 1);
                else throw new Exception("Bad formula");
                continue;
            }

            Float left = stack.pop();

            switch (token) {
                case "+" -> stack.push(left + right);

                case "-" -> stack.push(left - right);

                case "*" -> stack.push(left * right);

                case "/" -> stack.push(left / right);

            }
        }

        return stack.pop();
    }

    private String parseStringToPolishNotation(String inputString) throws Exception {
        List<Character> operators = List.of('-', '+', '/', '*');
        List<Character> separatorNumber = List.of('.', ',');
        Character openBracket = '(';
        Character closeBracket = ')';
        Stack<Character> stack = new Stack<>();

        StringBuilder outString = new StringBuilder();

        for (Character token : inputString.toCharArray()) {
            if (token.equals(' ')) continue;

            if (token.equals(openBracket)) {
                stack.push(token);
                continue;
            }

            if (Character.isDigit(token) || separatorNumber.contains(token)) {
                outString.append(token);
                continue;
            }

            if (operators.contains(token)) {
                outString.append(" ");
                if (!stack.isEmpty() && operators.indexOf(stack.peek()) >= operators.indexOf(token)) {
                    outString.append(stack.pop());
                    outString.append(" ");
                }
                stack.push(token);
                continue;
            }

            if (token.equals(closeBracket)) {
                Character prevHeadStack;
                do {
                    prevHeadStack = stack.pop();
                    outString.append(" ");
                    outString.append(prevHeadStack);
                    if (stack.isEmpty() && !prevHeadStack.equals(closeBracket)) throw new Exception("Bad formula");
                } while (prevHeadStack.equals(openBracket)) ;
                continue;
            }

            throw new Exception("Unexpected characters in a formula");
        }

        if (!stack.isEmpty()) {
            Character prevHeadStack;
            do {
                prevHeadStack = stack.pop();
                if (prevHeadStack.equals(openBracket)) throw new Exception("Bad formula");
                outString.append(" ");
                outString.append(prevHeadStack);
            } while (!stack.isEmpty());

        }


        return outString.toString().replace(',', '.');
    }

    private void print(Object message) {
        System.out.println(message);
    }

    private void print() {
        System.out.println();
    }

    public void run() throws Exception {
        String exitCommand = "exit";

        while (true) {
            print("Enter expression (or exit to exit): ");
            Scanner in = new Scanner(System.in);
            String inputString = in.nextLine();

            if (inputString.equals(exitCommand)) break;

            try {
                String polishNotation = parseStringToPolishNotation(inputString);
                Float result = calculate(polishNotation);

                print(polishNotation);
                print(result);
            } catch (Exception e) {
                if (e.getMessage().equals("Bad formula")) print(e.getMessage());
                else if (e.getMessage().equals("Unexpected characters in a formula")) print(e.getMessage());
                else print("Something went wrong. Try again");
            }
            print();
        }
    }
}
