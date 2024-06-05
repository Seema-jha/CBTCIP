import java.util.Scanner;
import java.util.Random;

public class GuessTheNumber {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        
        final int MAX_ATTEMPTS = 10;
        int totalScore = 0;
        int round = 1;
        char playAgain;
        
        System.out.println("Welcome to the Guess the Number game!");
        
        do {
            int numberToGuess = random.nextInt(100) + 1;
            int numberOfTries = 0;
            int guess = 0;
            boolean win = false;
            int roundScore = 0;
            
            System.out.println("Round " + round + ":");
            System.out.println("I have randomly chosen a number between 1 and 100.");
            System.out.println("Try to guess it within " + MAX_ATTEMPTS + " attempts!");
            
            while (!win && numberOfTries < MAX_ATTEMPTS) {
                System.out.print("Enter your guess: ");
                guess = scanner.nextInt();
                numberOfTries++;
                
                if (guess < 1 || guess > 100) {
                    System.out.println("Your guess is out of range. Please guess a number between 1 and 100.");
                } else if (guess < numberToGuess) {
                    System.out.println("Your guess is too low.");
                } else if (guess > numberToGuess) {
                    System.out.println("Your guess is too high.");
                } else {
                    win = true;
                    roundScore = (MAX_ATTEMPTS - numberOfTries + 1) * 10;
                    totalScore += roundScore;
                    System.out.println("Congratulations! You've guessed the number!");
                    System.out.println("It took you " + numberOfTries + " tries.");
                    System.out.println("You scored " + roundScore + " points this round.");
                }
            }
            
            if (!win) {
                System.out.println("Sorry, you've used all " + MAX_ATTEMPTS + " attempts. The correct number was " + numberToGuess + ".");
            }
            
            System.out.println("Your total score so far is: " + totalScore + " points.");
            
            System.out.print("Do you want to play another round? (y/n): ");
            playAgain = scanner.next().charAt(0);
            round++;
            
        } while (playAgain == 'y' || playAgain == 'Y');
        
        System.out.println("Thank you for playing! Your final score is: " + totalScore + " points.");
        
        scanner.close();
    }
}
