import java.util.Scanner;

public class CalculatorEx {
    private double result;
    public CalculatorEx() {
        result = 0.0;
    }
    public double resultValue() {
        return result;
    }
    public void doCalculation() {
        Scanner sc = new Scanner(System.in);
        char operation;
        operation = sc.next().charAt(0);
        double newdbl;
        newdbl = sc.nextDouble();
        try {
            switch (operation) {
                case '+':
                    result += newdbl;
                    System.out.println("updated result = " + result);
                    doCalculation();
                    break;
                case '-':
                    result -= newdbl;
                    System.out.println("updated result = " + result);
                    doCalculation();
                    break;
                case '*':
                    result *= newdbl;
                    System.out.println("updated result = " + result);
                    doCalculation();
                    break;
                case '/':
                    result /= newdbl;
                    System.out.println("updated result = " + result);
                    doCalculation();
                    break;
                case 'q':
                    System.out.println("Quitting calculation.");
                    break;
                case 'Q':
                    System.out.println("Quitting calculation.");
                    break;
                default:
                    throw new UnknownOperatorException();
            }
            
        }
        catch(UnknownOperatorException e) {
            handleUnknownOperatorException();
        }

    }
    public void handleUnknownOperatorException() {
        System.out.println("Please reenter: ");
        doCalculation();
    }
    public static void main(String[] args) {
        CalculatorEx calc = new CalculatorEx();
        System.out.println("Enter calculation: ");
        calc.doCalculation();
        System.out.println("Final result = " + calc.resultValue());
    }
}