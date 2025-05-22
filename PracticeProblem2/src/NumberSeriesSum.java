import java.util.Scanner;

public class NumberSeriesSum {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        System.out.println("Even numbers from 2 to 20 (for loop):");
        int evenSum = 0;
        for (int i = 2; i <= 20; i += 2) {
            System.out.print(i + " ");
            evenSum += i;
        }
        System.out.println("\nSum of even numbers: " + evenSum);

        System.out.println("\nOdd numbers from 1 to 19 (while loop):");
        int j = 1, oddSum = 0;
        while (j <= 19) {
            System.out.print(j + " ");
            oddSum += j;
            j += 2;
        }
        System.out.println("\nSum of odd numbers: " + oddSum);

        scanner.close();
    }
}
