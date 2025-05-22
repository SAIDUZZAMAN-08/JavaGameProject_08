import java.util.Scanner;

public class EvenSeriesDoWhile {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Even numbers from 2 to 20 (do-while loop):");
        int k = 2, sum = 0;
        do {
            System.out.print(k + " ");
            sum += k;
            k += 2;
        } while (k <= 20);

        System.out.println("\nSum of even numbers: " + sum);

        scanner.close();
    }
}
