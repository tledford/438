import java.util.Scanner;

public class Sorting 
{
	public static void main(String[] args) {

		/*
		 * Arrays that store integers
		 */
		int numArray [] = new int[30];//size of initial array

		/*
		 * Integer variables below
		 */
		int min=1;//minimum number of entries allowed
		int num1;//input from user

		/*
		 * Boolean variable below
		 */
		boolean test=true;//used to test if entries are valid
		
		/*
		 * Scanner variable
		 */
		Scanner in = new Scanner(System.in);
		
		/*
		 * String variable
		 */
		String answer = null;//used for user char input

		//outer loop checks if user wants to have another run
		do
		{	/*
		 * inner loop uses boolean operation to 
		 * determine if the user wants to have another go
		 */

			do
			{
				System.out.println("How many input values [max: 30]? ");
				num1=in.nextInt();
				//if statement checks for a valid entry
				if(num1 > 30 || num1 < min)
				{
					System.out.println("INVALID ENTRY!!");
					test = false;
				}
				else
				{
					test = true;
				}
			}while(test == false);
			
			/* when a valid entry is used the program displays 
			 * the amount of numbers user can enter
			 */
			System.out.println("Enter " + num1 + " numbers.");
			inputArrayValues(numArray, num1, in);
			
			//do sorting
			sorting(numArray, num1);

			//display sorted results
			System.out.println("Sorted Results:");
			for (int i=0; i<num1; i++)
			{
				System.out.println(numArray[i]);
			}

			System.out.println();

			System.out.println("Continue (Y/N)?");
			while(!in.hasNext("[YyNn]"))
			{
				System.out.println("INVALID ENTRY!");
				System.out.println();
				System.out.println("Continue (Y/N)?");
				in.next();
			}
			answer = in.next();
			//set all occurrences of numArray to zero
			for (int i=0; i<10; i++)
			{
				numArray[i]=0;
			}
		}while(answer.toLowerCase().equals( "y" ) );
		System.out.println("Have a nice day!!");
	}

	public static void sorting(int[] numArray, int num1)
	{
		int temp = 0;
		boolean sort = false;
		for (int i=0; i<(num1 - 1); i++)
		{
			if (numArray[i]>numArray[i+1])
			{
				temp = numArray[i];
				numArray[i] = numArray[i+1];
				numArray[i+1] = temp;
				sort = true;
			}
			if (sort == true)
			{ 
				i = -1;
				sort = false;
			}
		}
	}

	public static void inputArrayValues(int[] numArray, int num1, Scanner in)
	{
		for(int i=0; i<num1; i++)
		{
			numArray[i]=in.nextInt();
			/* if the user does not enter a
			 * value between 0 and 9 there
			 * is an error message that is displayed
			 */
			if(numArray[i]>9 || numArray[i]<0)
			{
				System.out.println("INVALID ENTRY!!");
				i--;
				/* i is reset to previous 
				 * entry in the array as to not leave
				 * a bucket empty
				 */
			}
		}
	}
}