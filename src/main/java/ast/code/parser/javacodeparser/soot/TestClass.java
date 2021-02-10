package ast.code.parser.javacodeparser.soot;

public class TestClass {

    public void printMe(int k) {
        if (k % 15 == 0)
            System.out.println("FizzBuzz");
        else if (k % 5 == 0)
            System.out.println("Buzz");
        else if (k % 3 == 0)
            System.out.println("Fizz");
        else
            System.out.println(k);
    }
}
