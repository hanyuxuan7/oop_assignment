import java.util.Scanner;
public class Strings
{
// --------------------------------------------
// Reads in an array of integers, sorts them,
// then prints them in sorted order.
// --------------------------------------------
public static void main (String[] args)
{
	String[] stringList; //stringList is a list of strings
	int size; 
	Scanner scan = new Scanner(System.in);
	System.out.print ("\nHow many words do you want to sort? ");
	size = scan.nextInt(); // size variable takes integer input
	stringList = new String[size]; //stringList of size size is created
	System.out.println ("\nEnter the wordss...");
	for (int i = 0; i < size; i++)
		stringList[i] = scan.next(); // string is inputted into list based on index + input
		Sorting.insertionSort(stringList); //sorts the list using sorting.java
		System.out.println ("\nYour words in sorted order...");
	for (int i = 0; i < size; i++)
		System.out.print(stringList[i] + " ");
	System.out.println ();
	}
}