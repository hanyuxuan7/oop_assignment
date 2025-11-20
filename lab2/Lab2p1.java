package lab2;
import java.util.Scanner;
import java.util.Random;

public class Lab2p1 {
    public static void main(String[] args){
        int choice;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("Perform the following methods:");
            System.out.println("1: multiplication test");
            System.out.println("2: quotient using division by subtraction");
            System.out.println("3: remainder using division by subtraction");
            System.out.println("4: count the number of digits");
            System.out.println("5: position of a digit");
            System.out.println("6: extract all odd digits");
            System.out.println("7: quit");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    mulTest();
                    break;
                case 2:
                    System.out.println("4/7 = " + divide(4, 7));
                    System.out.println("7/7 = " + divide(7, 7));
                    System.out.println("25/7 = " + divide(25, 7));
                    break;
                case 3:
                    System.out.println("4 % 7 = " + modulus(4, 7));
                    System.out.println("7 % 7 = " + modulus(7, 7));
                    System.out.println("25 % 7 = " + modulus(25, 7));
                    break;
                case 4:
                    System.out.println("-12 - count = " + countDigits(-12)); // returns -1 as error message
                    System.out.println("123 - count = " + countDigits(123));
                    System.out.println("121456 - count = " + countDigits(121456));
                    break;
                case 5:
                    System.out.println("position = " + position(12345, 3));
                    System.out.println("position = " + position(123, 4));
                    System.out.println("position = " + position(12145, 1));
                    break;
                case 6:
                    System.out.println("oddDigits = " + extractOddDigits(12345));
                    System.out.println("oddDigits = " + extractOddDigits(54123));
                    System.out.println("oddDigits = " + extractOddDigits(246));
                    System.out.println("oddDigits = " + extractOddDigits(-12));
                    break;
                case 7: System.out.println("Program terminating...");}
                } while (choice < 7);
                sc.close();}

    public static void mulTest() {
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();
        int num1, num2, answer, response, score;
        score = 0;
        
        for (int counter = 0; counter < 5; counter++) {
        
            num1 = rand.nextInt(9) + 1; //generates random integers in range 1 to 9
            num2 = rand.nextInt(9) + 1;
            answer = num1 * num2;
            System.out.print("How much is " + num1 + " times " + num2 + "? ");
            response = sc.nextInt();

            if (response == answer) score += 1;
        };

        System.out.println(score + " answers out of 5 are correct.");
    }

    public static int divide(int m, int n) {
        int result = 0;
        for (;m >= n; m -= n) result++;
        return result;
    }

    public static int modulus(int m, int n) {
        int result = 0;
        for (;m >= n; m -= n) result++;
        return m;
    }

    public static int countDigits(int n) {
        if (n > 0) {
            String num = Integer.toString(n);
            return num.length();}
        return -1;
    }

    public static int position(int n, int digit) {
        String num = Integer.toString(n);
        int result = -1;
        result = num.lastIndexOf(Integer.toString(digit));
        if (result >= 0) return (num.length() - result);
        return (result);
    }

    public static long extractOddDigits(long n) {
        if (n < 0) throw new IllegalArgumentException("Error!");
        long result = 0;
        String num = Long.toString(n);
        for (int c = 0; c < num.length(); c++) {
            if ((num.charAt(c) - '0')%2 == 1) result = result*10 + (num.charAt(c) - '0');
        }
        if (result == 0) return -1;
        return result;
    }

} 