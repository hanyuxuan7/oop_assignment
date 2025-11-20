import java.util.Scanner;
public class Numbers
{
// --------------------------------------------
// Reads in an array of integers, sorts them,
// then prints them in sorted order.
// --------------------------------------------
public static void main (String[] args)
{
	Integer[] intList; //intList is a list of integers
	int size; 
	Scanner scan = new Scanner(System.in);
	System.out.print ("\nHow many integers do you want to sort? ");
	size = scan.nextInt(); // size variable takes integer input
	intList = new Integer[size]; //intList of size size is created
	System.out.println ("\nEnter the numbers...");
	for (int i = 0; i < size; i++)
		intList[i] = scan.nextInt(); // integer is inputted into list based on index + input
		Sorting.insertionSort(intList); //sorts the list using sorting.java
		System.out.println ("\nYour numbers in sorted order...");
	for (int i = 0; i < size; i++)
		System.out.print(intList[i] + " ");
	System.out.println ();
	}
}