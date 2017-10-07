/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import java.util.Arrays;

import acm.graphics.GLabel;
import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {

	public static void main(String[] args) {
		new Yahtzee().start(args);
	}

	/*
	 * This method runs Yahtzee game;
	 */
	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		while (nPlayers > 4) {
			nPlayers = dialog.readInt("Enter number of players (max is 4)");
		}
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
			while (playerNames[i - 1].length() == 0) {
				playerNames[i - 1] = dialog.readLine("Enter name for player "
						+ i);
			}
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
	}

	/*
	 * This method starts game 13 times for each player. After the game prints
	 * lower and upper scores. Also prints winner name;
	 */
	private void playGame() {
		int turns = N_SCORING_CATEGORIES;
		createScoresMatrics();
		for (int turn = 0; turn < turns; turn++) {
			for (int player = 1; player < nPlayers + 1; player++) {
				nPlayerSPlay(player);
			}
		}
		calculateScores();
		findWinner();
	}

	/*
	 * This method lets player roll dice 3 times then choose category; If
	 * category is available sets score else set 0 in chosen category;
	 */
	private void nPlayerSPlay(int player) {
		int[] dice = new int[N_DICE];
		firstRoll(dice, player);
		secondRoll(dice);
		thirdRoll(dice);
		int category = display.waitForPlayerToSelectCategory();
		category = checkSelectedCategory(category, player);
		if (checkCategory(dice, category)) {
			scores[player - 1][category - 1] = setScore(dice, category);
			display.updateScorecard(category, player, setScore(dice, category));
		} else {
			scores[player - 1][category - 1] = 0;
			display.updateScorecard(category, player, 0);
		}
		calculateTotalScore(player, category, dice);
	}

	/*
	 * This method lets player roll dices first time and prints proper messages;
	 */
	private void firstRoll(int[] dice, int player) {
		display.printMessage(playerNames[player - 1]
				+ "'s turn. Click \"Roll Dice\" button to roll the dice.");
		display.waitForPlayerToClickRoll(player);
		for (int i = 0; i < N_DICE; i++) {
			dice[i] = rgen.nextInt(1, 6);
		}
		display.displayDice(dice);
		display.printMessage("Select the dice you wish to re-roll and click \"Roll Again\"");
		display.waitForPlayerToSelectDice();
	}

	/*
	 * This method lets player roll dices second time and prints proper
	 * messages;
	 */
	private void secondRoll(int[] dice) {
		for (int i = 0; i < N_DICE; i++) {
			if (display.isDieSelected(i))
				dice[i] = rgen.nextInt(1, 6);
		}
		display.displayDice(dice);
		display.printMessage("Select the dice you wish to re-roll and click \"Roll Again\"");
		display.waitForPlayerToSelectDice();
	}

	/*
	 * This method lets player roll dices third time and prints proper messages;
	 */
	private void thirdRoll(int[] dice) {
		for (int i = 0; i < N_DICE; i++) {
			if (display.isDieSelected(i))
				dice[i] = rgen.nextInt(1, 6);
		}
		display.displayDice(dice);
		display.printMessage("Select a category for this roll.");
	}

	/*
	 * This method just creates array of players with array of categories to
	 * save scores of all players;
	 */
	private void createScoresMatrics() {
		scores = new int[nPlayers][N_CATEGORIES];
		for (int i = 0; i < scores.length; i++) {
			for (int j = 0; j < scores[i].length - 1; j++) {
				scores[i][j] = -1;
			}
			scores[i][UPPER_BONUS - 1] = 0;
			scores[i][LOWER_SCORE - 1] = 0;
			scores[i][LOWER_SCORE - 1] = 0;

		}
	}

	/*
	 * This method sets score for chosen category;
	 */
	private int setScore(int[] dice, int category) {
		switch (category) {
		case ONES:
		case TWOS:
		case THREES:
		case FOURS:
		case FIVES:
		case SIXES:
			return calculatePointsFromOnesToSixes(dice, category);
		case THREE_OF_A_KIND:
		case FOUR_OF_A_KIND:
			return sumOfElemets(dice);
		case FULL_HOUSE:
			return 25;
		case SMALL_STRAIGHT:
			return 30;
		case LARGE_STRAIGHT:
			return 40;
		case YAHTZEE:
			return 50;
		case CHANCE:
			return sumOfElemets(dice);
		}
		return 0;
	}

	/*
	 * This method sums up all array elements and returns sum;
	 */
	private int sumOfElemets(int[] arr) {
		int sum = 0;
		for (int i = 0; i < arr.length; i++) {
			sum = sum + arr[i];
		}
		return sum;
	}

	/*
	 * This method checks if category is already selected;
	 */
	private int checkSelectedCategory(int category, int player) {
		display.printMessage("Select a category for this roll.");
		while (scores[player - 1][category - 1] != -1) {
			display.printMessage("Select a category for this roll, which is not already selected.");
			category = display.waitForPlayerToSelectCategory();
		}
		return category;
	}

	/*
	 * This method calculates point for categories from Ones to Sixes and
	 * returns sum of chosen number;
	 */
	private int calculatePointsFromOnesToSixes(int[] dice, int category) {
		int score = 0;
		for (int i = 0; i < dice.length; i++) {
			if (dice[i] == category)
				score++;
		}
		return score * category;
	}

	/*
	 * This method calculates Upper Score and Upper Bonus, if available;
	 */
	private void calculateUpperScore(int player) {
		int sum = 0;
		for (int i = 0; i < SIXES; i++) {
			sum = sum + scores[player - 1][i];
		}
		display.updateScorecard(UPPER_SCORE, player, sum);
		scores[player - 1][UPPER_SCORE - 1] = sum;
		if (sum >= 63) {
			display.updateScorecard(UPPER_BONUS, player, 35);
			scores[player - 1][UPPER_BONUS - 1] = 35;
		} else {
			display.updateScorecard(UPPER_BONUS, player, 0);
			scores[player - 1][UPPER_BONUS - 1] = 0;
		}
	}

	/*
	 * This method calculates Lower Score;
	 */
	private void calculateLowerScore(int player) {
		int sum = 0;
		for (int i = THREE_OF_A_KIND - 1; i < CHANCE; i++) {
			sum = sum + scores[player - 1][i];
		}
		display.updateScorecard(LOWER_SCORE, player, sum);
		scores[player - 1][LOWER_SCORE - 1] = sum;
	}

	/*
	 * This method calculates Total Score;
	 */
	private void calculateTotalScore(int player, int category, int[] dice) {
		if (checkCategory(dice, category)) {
			scores[player - 1][TOTAL - 1] = scores[player - 1][TOTAL - 1]
					+ setScore(dice, category);
		}
		display.updateScorecard(TOTAL, player, scores[player - 1][TOTAL - 1]);
	}

	/*
	 * This method checks if category is Yahtzee;
	 */
	private boolean isYahtzee(int[] arr) {
		if (arr[0] == arr[1] && arr[1] == arr[2] && arr[2] == arr[3]
				&& arr[3] == arr[4])
			return true;
		return false;
	}

	/*
	 * This method checks if category is Full House;
	 */
	private boolean isFullHouse(int[] arr) {
		Arrays.sort(arr);
		boolean threeOfAKind1 = arr[0] == arr[1] && arr[1] == arr[2];
		boolean threeOfAKind2 = arr[2] == arr[3] && arr[3] == arr[4];
		boolean twoOfAkind1 = arr[3] == arr[4];
		boolean twoOfAkind2 = arr[0] == arr[1];
		if ((threeOfAKind1 && twoOfAkind1) || (threeOfAKind2 && twoOfAkind2))
			if (!isYahtzee(arr))
				return true;

		return false;
	}

	/*
	 * This method checks if category is Four Of A Kind;
	 */
	private boolean isFourOfAKind(int[] arr) {
		Arrays.sort(arr);
		boolean case1 = arr[0] == arr[1] && arr[1] == arr[2]
				&& arr[2] == arr[3];
		boolean case2 = arr[1] == arr[2] && arr[2] == arr[3]
				&& arr[3] == arr[4];
		if (case1 || case2)
			return true;
		return false;
	}

	/*
	 * This method checks if category is Three Of A Kind;
	 */
	private boolean isThreeOfAKind(int[] arr) {
		Arrays.sort(arr);
		boolean case1 = arr[0] == arr[1] && arr[1] == arr[2];
		boolean case2 = arr[1] == arr[2] && arr[2] == arr[3];
		boolean case3 = arr[2] == arr[3] && arr[3] == arr[4];
		if (case1 || case2 || case3)
			return true;
		return false;
	}

	/*
	 * This method checks if category is Large Straight;
	 */
	private boolean isLargeStraight(int[] arr) {
		boolean res = true;
		Arrays.sort(arr);
		for (int i = 0; i < arr.length - 1; i++) {
			if (arr[i] + 1 != arr[i + 1])
				res = false;
		}
		return res;
	}

	/*
	 * This method checks if category is Small Straight;
	 */
	private boolean isSmallStraight(int[] arr) {
		int temp = 0;
		Arrays.sort(arr);
		boolean middlePart = arr[1] + 1 == arr[2] || arr[2] + 1 == arr[3];
		if (middlePart) {
			for (int i = 0; i < arr.length - 1; i++) {
				if (arr[i] + 1 != arr[i + 1])
					temp++;
			}
			if (temp < 2) {
				return true;
			}
		}
		return false;
	}

	/*
	 * This method checks selected category and returns true if dices satisfy
	 * category;
	 */
	private boolean checkCategory(int[] dice, int category) {
		switch (category) {
		case ONES:
		case TWOS:
		case THREES:
		case FOURS:
		case FIVES:
		case SIXES:
			return true;
		case THREE_OF_A_KIND:
			return isThreeOfAKind(dice);
		case FOUR_OF_A_KIND:
			return isFourOfAKind(dice);
		case FULL_HOUSE:
			return isFullHouse(dice);
		case SMALL_STRAIGHT:
			return isSmallStraight(dice);
		case LARGE_STRAIGHT:
			return isLargeStraight(dice);
		case YAHTZEE:
			return isYahtzee(dice);
		case CHANCE:
			return true;
		}
		return false;
	}

	/*
	 * This method calculates Upper and Lower Scores (also Upper Bonus if
	 * available);
	 */
	private void calculateScores() {
		for (int player = 1; player < nPlayers + 1; player++) {
			calculateLowerScore(player);
			calculateUpperScore(player);
		}
	}

	/*
	 * This method finds largest total score of all players and then prints his
	 * name and score;
	 */
	private void findWinner() {
		int score = 0;
		String winner = "";
		for (int player = 1; player < nPlayers + 1; player++) {
			if (scores[player - 1][TOTAL - 1] > score) {
				score = scores[player - 1][TOTAL - 1];
				winner = playerNames[player - 1];
			}
		}
		display.printMessage("Congratulations, " + winner
				+ ", you're the winner with a total score of " + score);
	}

	/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private int[][] scores;
	private RandomGenerator rgen = new RandomGenerator();

}
