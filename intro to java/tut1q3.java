import java.util.Scanner;
import java.util.Arrays;

public class tut1q3 {
    public static void bubble(int[] arr) {
        int n = arr.length;
        int i,j;
        for (i=n-2;i>-1;i--)
            for (j=0;j<i+1;j++) {
                if (arr[j]>arr[j+1]) {
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    public static void main(String[] args) {
        int n;
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of elements: ");
        n = sc.nextInt();
        
        int[] arr =  new int[n];
        
        for (int i=0;i<n;i++) {
            System.out.println("Enter integer value for element no." + (i+1) + ": ");
            arr[i] = sc.nextInt();
        }
        
        bubble(arr);
        System.out.println("Sorted array: " + Arrays.toString(arr));
        sc.close();
    }
    
}