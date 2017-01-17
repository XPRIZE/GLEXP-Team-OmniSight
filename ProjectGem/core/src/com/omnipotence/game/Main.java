package com.omnipotence.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.omnipotence.game.level.LevelManager;
import com.omnipotence.game.util.RendererManager;
import com.omnipotence.game.util.AssetManager;
import com.omnipotence.game.util.MusicManager;
import com.omnipotence.game.util.PreferencesManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The main game class. This will load all the assets needed by the game, as
 * well as initialize the background music and the LevelManager (which then
 * loads all the levels). It will then switch to the Main Menu screen.
 * 
 * @author Faizaan Datoo
 *
 */
public class Main extends Game {
	/**
	 * 0 for English
	 * 1 for Swahili
	 * */
    private String[] languages = {"English", "Swahili"};
	public static final int langIndex = 0;
	public static final String language = "Swahili";
    public Game games = this;
	public LevelManager levelManager;
	public MusicManager musicManager;
    public ArrayList<com.omnipotence.game.Stage.gameStage> gameStages;
    public HashMap<String, String> answerKeyForTheQuestions;
	public SSIM androidSIMM;

	public Main(SSIM androidSIMM) {
		this.androidSIMM = androidSIMM;
	}

	@Override
	public void create() {
		loadAssets();
        setStages();

		// Load all levels into the game
		levelManager = new LevelManager();
		levelManager.loadLevels();

		RendererManager.getInstance();

		// Load the game preferences
		try {
			PreferencesManager.getInstance().loadPreferences();
			PreferencesManager.getInstance().checkDefaults();
		} catch (IOException e) { // Log to user device and exit the game.
			System.err.println("Error: Could not read the preferences file.");
			e.printStackTrace();
			Gdx.app.exit();
			return;
		}

        callNextScreen();
	}

	private void setStages() {
		switch (Main.langIndex) {
			case 1 :
				gameStages = new ArrayList<com.omnipotence.game.Stage.gameStage>(Arrays.asList(
						//Stages from 1 - 5
						new com.omnipotence.game.Stage.gameStage("Alfabeti", new ArrayList<String>(Arrays.asList("A", "B",
								"C", "D", "E","F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
								"R", "S", "T", "U", "V", "W", "Y", "Z")), 180f, 120f, true),
                        new com.omnipotence.game.Stage.gameStage("Graphemes", new ArrayList<String>(Arrays.asList("A", "E",
                                "I", "O", "U", "M", "N", "B", "D", "J", "G", "P", "T", "K", "V",
                                "Z", "F", "S", "H", "R", "L", "W")), 210f, 125f, true),
						new com.omnipotence.game.Stage.gameStage("Graphemes 2", new ArrayList<String>(Arrays.asList("CH",
								"ND", "MB", "NY", "NG'", "NZ", "SH", "TH", "MV", "NG", "NJ", "DH",
                                "GH")), 210f, 125f, true),
						new com.omnipotence.game.Stage.gameStage("Hesabu 1", new ArrayList<String>(Arrays.asList("1", "2",
								"3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
								"16", "17", "18", "19")), 180f, 125f, true),
                        //Stages from 6 - 10
						new com.omnipotence.game.Stage.gameStage("Hesabu 2", new ArrayList<String>(Arrays.asList("20", "21", "22",
								"23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34",
								"35", "36", "37", "38", "39")), 180f, 125f, true),
						new com.omnipotence.game.Stage.gameStage("Hesabu 3", new ArrayList<String>(Arrays.asList("40", "41", "42",
								"43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54",
								"55", "56", "57", "58", "59")), 180f, 125f, true),
						new com.omnipotence.game.Stage.gameStage("Hesabu 4", new ArrayList<String>(Arrays.asList("60", "61", "62",
								"63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74",
								"75", "76", "77", "78", "79")), 180f, 125f, true),
						new com.omnipotence.game.Stage.gameStage("Hesabu 5", new ArrayList<String>(Arrays.asList("80", "81", "82",
								"83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94",
								"95", "96", "97", "98", "99", "100")), 180f, 125f, true),
						new com.omnipotence.game.Stage.gameStage("Alama yakujumlisha 1", new ArrayList<String>(Arrays.asList("0 + 1",
								"1 + 1", "1 + 2", "1 + 3", "1 + 4", "1 + 5", "1 + 6", "1 + 7", "1 + 8",
								"1 + 9")), 180f, 125f, false),
                        //Stages from 11 - 15
						new com.omnipotence.game.Stage.gameStage("Alama yakujumlisha 2", new ArrayList<String>(Arrays.asList("0 + 10",
								"10 + 10", "10 + 20", "10 + 30", "10 + 40", "10 + 50", "10 + 60",
								"10 + 70", "10 + 80", "10 + 90")), 180f, 125f, false),
						new com.omnipotence.game.Stage.gameStage("Alama yakujumlisha 3", new ArrayList<String>(Arrays.asList("2 + 3",
								"4 + 6", "1 + 14", "7 + 13", "6 + 14", "8 + 22", "9 + 26", "15 + 25",
								"21 + 24", "17 + 33")), 180f, 125f, false),
						new com.omnipotence.game.Stage.gameStage("Alama ya kutoa 1", new ArrayList<String>(Arrays.asList("1 - 0",
								"1 - 1", "2 - 1", "3 - 1", "4 - 1", "5 - 1", "6 - 1", "7 - 1", "8 - 1",
								"9 - 1")), 180f, 125f, false),
						new com.omnipotence.game.Stage.gameStage("Alama ya kutoa 2", new ArrayList<String>(Arrays.asList("10 - 0",
								"10 - 10", "20 - 10", "30 - 10", "40 - 10", "50 - 10", "60 - 10",
								"70 - 10", "80 - 10", "90 - 10", "100 - 10")), 180f, 125f, false),
						new com.omnipotence.game.Stage.gameStage("Alama ya kutoa 3", new ArrayList<String>(Arrays.asList("4 - 2",
								"6 - 3", "8 - 4", "12 - 6", "18 - 9", "22 - 11", "28 - 14", "36 - 18" ,
								"44 - 22", "50 - 25", "60 - 30", "80 - 40")), 180f, 125f, false),
                        //Stages from 16 - 20
						new com.omnipotence.game.Stage.gameStage("Alama ya kuzidisha 1", new ArrayList<String>(Arrays.asList("1 * 0",
								"1 * 1", "1 * 2", "1 * 3", "1 * 4", "1 * 5", "1 * 6", "1 * 7", "1 * 8",
								"1 * 9", "1 * 10", "2 * 0", "2 * 1", "2 * 2", "2 * 3", "2 * 4", "2 * 5",
								"2 * 6", "2 * 7", "2 * 8", "2 * 9", "2 * 10")), 180f, 125f, false),
						new com.omnipotence.game.Stage.gameStage("Alama ya kuzidisha  2", new ArrayList<String>(Arrays.asList("3 * 0",
								"3 * 1", "3 * 2", "3 * 3", "3 * 4", "3 * 5", "3 * 6", "3 * 7", "3 * 8",
								"3 * 9", "3 * 10", "4 * 0", "4 * 1", "4 * 2", "4 * 3", "4 * 4", "4 * 5",
								"4 * 6", "4 * 7", "4 * 8", "4 * 9", "4 * 10")), 180f, 125f, false),
						new com.omnipotence.game.Stage.gameStage("Alama ya kuzidisha  3", new ArrayList<String>(Arrays.asList("5 * 0",
								"5 * 1", "5 * 2", "5 * 3", "5 * 4", "5 * 5", "5 * 6", "5 * 7", "5 * 8",
								"5 * 9", "5 * 10", "6 * 0", "6 * 1", "6 * 2", "6 * 3", "6 * 4", "6 * 5",
								"6 * 6", "6 * 7", "6 * 8", "6 * 9", "6 * 10")), 180f, 125f, false),
						new com.omnipotence.game.Stage.gameStage("Alama ya kuzidisha  4", new ArrayList<String>(Arrays.asList("7 * 0",
								"7 * 1", "7 * 2", "7 * 3", "7 * 4", "7 * 5", "7 * 6", "7 * 7", "7 * 8",
								"7 * 9", "7 * 10", "8 * 0", "8 * 1", "8 * 2", "8 * 3", "8 * 4", "8 * 5",
								"8 * 6", "8 * 7", "8 * 8", "8 * 9", "8 * 10")), 180f, 125f, false),
						new com.omnipotence.game.Stage.gameStage("Alama ya kuzidisha  5",new ArrayList<String>(Arrays.asList("9 * 0",
								"9 * 1", "9 * 2", "9 * 3", "9 * 4", "9 * 5", "9 * 6", "9 * 7", "9 * 8",
								"9 * 9", "9 * 10", "10 * 0", "10 * 1", "10 * 2", "10 * 3", "10 * 4",
								"10 * 5", "10 * 6", "10 * 7", "10 * 8", "10 * 9", "10 * 10")),
								180f, 125f, false),
                        //Stages from 21 - 25
						new com.omnipotence.game.Stage.gameStage("Sentences Structure",
                                new ArrayList<String>(Arrays.asList(
                                        "Wanafunzi Wanafundisha.",
                                        "Ninapika Kuku.",
                                        "Nina Mbwa Mmoja.",
                                        "Tunataka Kuona Wewe.",
                                        "Anakunywa Maji.",
                                        "Anasoma Kitabu.")), 750f, 210f, false),
                        //Stages from 26 - 30
                        new com.omnipotence.game.Stage.gameStage("Sentences Structure 2",
                                new ArrayList<String>(Arrays.asList(
                                        "Ninapiga Ngoma.",
                                        "Wanapenda Kula Nyama Choma.",
                                        "Mzee Anatembea Kwa Duka.",
                                        "Ishirini na moja.",
                                        "Ninapenda Matunda Lakini, Sipendi Mboga.",
                                        "Sitaki Kwenda Kwa Shule.")), 750f, 210f, false),
                        new com.omnipotence.game.Stage.gameStage("Sentences Structure 3",
                                new ArrayList<String>(Arrays.asList(
                                        "Mgeni Hawakuli Kuku Yetu.",
                                        "Wanarudi Nyumbani.",
                                        "Ninakula Chakula Cha Asabuhi.",
                                        "Tunachelewa.",
                                        "Mtoto Anasoma Kingereza.",
                                        "Watoto Wanaamka.")), 750f, 210f, false),
                        new com.omnipotence.game.Stage.gameStage("Sentences Structure 4",
                                new ArrayList<String>(Arrays.asList(
                                        "Wanataka Kusome.",
                                        "Hawakunywi Maziwa.",
										"Kumi na mbili.",
                                        "Wewe Ni Mgeni Wangu.",
                                        "Kumi na moja.",
                                        "Jina Lako Silijui.")), 750f, 210f, false),
                        new com.omnipotence.game.Stage.gameStage("Sentences Structure 5",
                                new ArrayList<String>(Arrays.asList(
                                        "Mgaagaa Na Upwa Hali Wali Mkavu.",
                                        "Kumi na tano.",
                                        "Ninafikiri Kufundisha Ni Nzuri.",
                                        "Sipendi Kuenda Nje.",
                                        "Ulimi Unauma Kuliko Meno.",
                                        "Unaishi Kwa Ghorofa."
								)), 750f, 210f, false),
                        new com.omnipotence.game.Stage.gameStage("Sentences Structure 6",
                                new ArrayList<String>(Arrays.asList(
                                        "Mama Anapika Ugali.",
                                        "Unapenda Kula Chakula Machungwa.",
                                        "Watoto Wanaruka.",
                                        "Paka Mnatembea Njia.",
                                        "Twiga Ni Furahi.",
                                        "Unasoma Vitabu Sana.")), 750f, 210f, false),
                        //Stages from 31 - 31
                        new com.omnipotence.game.Stage.gameStage("Sentences Structure 7",
                                new ArrayList<String>(Arrays.asList(
                                        "Tunacheza Kwa Mbwa.",
                                        "Mnasikiliza.",
                                        "Wazazi Wangu Ni Nzuri Sana.",
                                        "Tunatembea Kwa Rafiki.",
                                        "Mlango Ni Kunfungwa.",
                                        "Haraka Haraka Haina Baraka.")), 750f, 210f, false)));
                answerKeyForTheQuestions = new HashMap<String, String>();
                //answers for question 1 - 5
                answerKeyForTheQuestions.put("The cat catches 2 rats in the morning and 5 rats" +
                        " in the evening. How many rats does it catch in a day?", "7");
                answerKeyForTheQuestions.put("Tom wins 1 gold prize and 3 silver prizes." +
                        " How many prizes does he win altogether?", "4");
                answerKeyForTheQuestions.put("In a soccer game, the goal keeper saves 5 goals" +
                        " in the first set and 9 goals in the second set. How many goals does" +
                        " he save during the game?", "14");
                answerKeyForTheQuestions.put("Tammy solves 9 problems in Math and 4 problems" +
                        " in Science. How many problems does he solve altogether?", "13");
                answerKeyForTheQuestions.put("On her birthday, Tara received 7 gifts from her" +
                        " friends and 8 gifts from her relatives. How many gifts did she " +
                        "receive on her birthday?", "15");
                //answers for question 6 - 10
                answerKeyForTheQuestions.put("The cost of each book is $3. What is the cost" +
                        " of 6 books?", "3");
                answerKeyForTheQuestions.put("Roger works 8 hours in a day. How many hours does" +
                        " he work in 5 days?", "6");
                answerKeyForTheQuestions.put("Jenny has 7 dollars. She gives 4 dollars to Dora. " +
                        "How much money does she have left?", "3");
                answerKeyForTheQuestions.put("Kelly fed 8 sparrows. After a minute, 2 sparrows" +
                        " flew away. How many sparrows are left for her to feed?", "4");
                answerKeyForTheQuestions.put("Kitty eats 5 biscuits out of 8 biscuits. How many " +
                        "biscuits are left to eat?", "5");
                //answers for question 11 - 15
                answerKeyForTheQuestions.put("Jack hit some balloons leaving 2 balloons. If " +
                        "there were 6 balloons, how many balloons did he hit?", "18");
                answerKeyForTheQuestions.put("In a basketball practice, each player was " +
                        "given 9 chances to throw the ball into the basket. Justin hit the " +
                        "target 4 times. Find the number of chances he missed?", "40");
                answerKeyForTheQuestions.put("A bag contains 7 balls. How many balls are" +
                        " there in 8 bags?", "56");
                answerKeyForTheQuestions.put("Kitty drinks 4 cups of milk in a day. How much " +
                        "milk does she drink in a week?", "28");
                answerKeyForTheQuestions.put("Flora has 9 times more tulips than roses. If she" +
                        " has 6 roses, how many tulips does she have?", "54");
				break;
			default:
				gameStages = new ArrayList<com.omnipotence.game.Stage.gameStage>(Arrays.asList(
						//Stages from 1 - 5
						new com.omnipotence.game.Stage.gameStage("Alphabet", new ArrayList<String>(Arrays.asList("A", "B",
								"C", "D", "E","F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
                                "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")), 180f,
                                125f, true),
						new com.omnipotence.game.Stage.gameStage("Graphemes", new ArrayList<String>(Arrays.asList("A",
								"B", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
                                "P", "R", "S", "T", "U", "V", "W", "Y", "Z")), 180f, 125f, true),
						new com.omnipotence.game.Stage.gameStage("Shapes", new ArrayList<String>(Arrays.asList("Circle",
								"Triangle", "Square", "Rectangle", "Pentagon", "Hexagon", "Octagon",
								"Decagon")), 240f, 125f, true),
                        new com.omnipotence.game.Stage.gameStage("Numbers Part 1", new ArrayList<String>(Arrays.asList("0", "1",
                                "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14",
                                "15", "16", "17", "18", "19")), 180f, 125f, true),
                        new com.omnipotence.game.Stage.gameStage("Numbers Part 2", new ArrayList<String>(Arrays.asList("20", "21",
                                "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32",
                                "33", "34", "35", "36", "37", "38", "39")), 180f, 125f, true),
                        //Stages from 6 - 10
                        new com.omnipotence.game.Stage.gameStage("Numbers Part 3", new ArrayList<String>(Arrays.asList("40", "41",
                                "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53",
                                "54", "55", "56", "57", "58", "59")), 180f, 125f, true),
                        new com.omnipotence.game.Stage.gameStage("Numbers Part 4", new ArrayList<String>(Arrays.asList("60", "61",
                                "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73",
                                "74", "75", "76", "77", "78", "79")), 180f, 125f, true),
                        new com.omnipotence.game.Stage.gameStage("Numbers Part 5", new ArrayList<String>(Arrays.asList("80", "81",
                                "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93",
                                "94", "95", "96", "97", "98", "99", "100")), 180f, 125f, true),
						new com.omnipotence.game.Stage.gameStage("Addition Part 1", new ArrayList<String>(Arrays.asList("0 + 1",
								"1 + 1", "1 + 2", "1 + 3", "1 + 4", "1 + 5", "1 + 6", "1 + 7", "1 + 8",
								"1 + 9")), 180f, 125f, false),
						new com.omnipotence.game.Stage.gameStage("Addition Part 2", new ArrayList<String>(Arrays.asList("0 + 10",
								"10 + 10", "10 + 20", "10 + 30", "10 + 40", "10 + 50", "10 + 60",
								"10 + 70", "10 + 80", "10 + 90")), 180f, 125f, false),
                        //Stages from 11 - 15
						new com.omnipotence.game.Stage.gameStage("Addition Part 3", new ArrayList<String>(Arrays.asList("2 + 3",
								"4 + 6", "1 + 14", "7 + 13", "6 + 14", "8 + 22", "9 + 26", "15 + 25",
								"21 + 24", "17 + 33")), 180f, 125f, false),
						new com.omnipotence.game.Stage.gameStage("Subtraction Part 1", new ArrayList<String>(Arrays.asList("1 - 0",
								"1 - 1", "2 - 1", "3 - 1", "4 - 1", "5 - 1", "6 - 1", "7 - 1", "8 - 1",
								"9 - 1")), 180f, 125f, false),
						new com.omnipotence.game.Stage.gameStage("Subtraction Part 2", new ArrayList<String>(Arrays.asList("10 - 0",
								"10 - 10", "20 - 10", "30 - 10", "40 - 10", "50 - 10", "60 - 10",
								"70 - 10", "80 - 10", "90 - 10", "100 - 10")), 180f, 125f, false),
						new com.omnipotence.game.Stage.gameStage("Subtraction Part 3", new ArrayList<String>(Arrays.asList("4 - 2",
								"6 - 3", "8 - 4", "12 - 6", "18 - 9", "22 - 11", "28 - 14", "36 - 18",
								"44 - 22", "50 - 25", "60 - 30", "80 - 40")), 180f, 125f, false),
						new com.omnipotence.game.Stage.gameStage("Multiplication Part 1", new ArrayList<String>(Arrays.asList("1 * 0",
								"1 * 1", "1 * 2", "1 * 3", "1 * 4", "1 * 5", "1 * 6", "1 * 7", "1 * 8",
								"1 * 9", "1 * 10", "2 * 0", "2 * 1", "2 * 2", "2 * 3", "2 * 4", "2 * 5",
								"2 * 6", "2 * 7", "2 * 8", "2 * 9", "2 * 10")), 180f, 125f, false),
                        //Stages from 16 - 20
						new com.omnipotence.game.Stage.gameStage("Multiplication Part 2", new ArrayList<String>(Arrays.asList("3 * 0",
								"3 * 1", "3 * 2", "3 * 3", "3 * 4", "3 * 5", "3 * 6", "3 * 7", "3 * 8",
								"3 * 9", "3 * 10", "4 * 0", "4 * 1", "4 * 2", "4 * 3", "4 * 4", "4 * 5",
								"4 * 6", "4 * 7", "4 * 8", "4 * 9", "4 * 10")), 180f, 125f, false),
						new com.omnipotence.game.Stage.gameStage("Multiplication Part 3", new ArrayList<String>(Arrays.asList("5 * 0",
								"5 * 1", "5 * 2", "5 * 3", "5 * 4", "5 * 5", "5 * 6", "5 * 7", "5 * 8",
								"5 * 9", "5 * 10", "6 * 0", "6 * 1", "6 * 2", "6 * 3", "6 * 4", "6 * 5",
								"6 * 6", "6 * 7", "6 * 8", "6 * 9", "6 * 10")), 180f, 125f, false),
						new com.omnipotence.game.Stage.gameStage("Multiplication Part 4", new ArrayList<String>(Arrays.asList("7 * 0",
								"7 * 1", "7 * 2", "7 * 3", "7 * 4", "7 * 5", "7 * 6", "7 * 7", "7 * 8",
								"7 * 9", "7 * 10", "8 * 0", "8 * 1", "8 * 2", "8 * 3", "8 * 4", "8 * 5",
								"8 * 6", "8 * 7", "8 * 8", "8 * 9", "8 * 10")), 180f, 125f, false),
						new com.omnipotence.game.Stage.gameStage("Multiplication Part 5", new ArrayList<String>(Arrays.asList("9 * 0",
								"9 * 1", "9 * 2", "9 * 3", "9 * 4", "9 * 5", "9 * 6", "9 * 7", "9 * 8",
								"9 * 9", "9 * 10", "10 * 0", "10 * 1", "10 * 2", "10 * 3", "10 * 4",
								"10 * 5", "10 * 6", "10 * 7", "10 * 8", "10 * 9", "10 * 10")),
								180f, 125f, false),
						new com.omnipotence.game.Stage.gameStage("Vocabulary", new ArrayList<String>(Arrays.asList("We", "on",
								"of", "the", "in", "for", "a", "to", "go", "and", "it", "I", "ride",
								"bike", "am", "is", "kid", "my")), 180f, 125f, true),
                        //Stages from 21 - 25
                        new com.omnipotence.game.Stage.gameStage("Sentences Structure 1", new ArrayList<String>(Arrays.asList(
                                "A Bug Has Legs.",
                                "A Cap Is A Hat.",
                                "A Pug Is A Dog.",
                                "Go To The Top Of The Hill.",
                                "He Is In Bed.",
                                "He Runs For Fun.")), 750f, 200f, false),
                        new com.omnipotence.game.Stage.gameStage("Sentences Structure 2", new ArrayList<String>(Arrays.asList(
                                "He Wants To Win.",
                                "Jam And Ham Is In The Bin.",
                                "Jam Is In The Can.",
                                "Onion Is In The Pie.",
                                "Sit On Top Of The Hill.",
                                "The Boy Got Big.",
                                "The Bread Is Hot.")), 750f, 200f, false),
                        new com.omnipotence.game.Stage.gameStage("Sentences Structure 3", new ArrayList<String>(Arrays.asList(
                                "The Bug Is Up The Pub.",
                                "The Cat Is On The Rug.",
                                "The City Is Big.",
                                "The Dog Is In The Cage.",
                                "The Dogs Go To The Vet.",
                                "The Fish Is In The Tub.",
                                "The Kids Want A Nap.")), 750f, 200f, false),
                        new com.omnipotence.game.Stage.gameStage("Sentences Structure 4", new ArrayList<String>(Arrays.asList(
                                "The Kitten Is Mad.",
                                "The Pie Is Hot.",
                                "The Sun is Hot.",
                                "We Haul The Van.",
                                "Zip Up The Bag.")), 750f, 200f, false),
						new com.omnipotence.game.Stage.gameStage("Math Word Problems Part 1", new ArrayList<String>(Arrays.asList(
								"The cat catches 2 rats in the morning and 5 rats in the evening. " +
										"How many rats does it catch in a day?",
								"Tom wins 1 gold prize and 3 silver prizes. How many prizes does he win " +
										"altogether?",
								"In a soccer game, the goal keeper saves 5 goals in the first set " +
                                        "and 9 goals in the second set. How many goals does he save" +
                                        " during the game?")), 750f, 450f, false),
						//Stages from 26 - 30
						new com.omnipotence.game.Stage.gameStage("Math Word Problems Part 2", new ArrayList<String>(Arrays.asList(
								"Tammy solves 9 problems in Math and 4 problems in Science. How many problems" +
										" does he solve altogether?",
								"On her birthday, Tara received 7 gifts from her friends and 8 gifts from " +
										"her relatives. How many gifts did she receive on her birthday?",
								"The cost of each book is $3. What is the cost of 6 books?",
                                "Roger works 8 hours in a day. How many hours does he work in 5 days?")), 750f, 450f, false),
						new com.omnipotence.game.Stage.gameStage("Math Word Problems Part 3", new ArrayList<String>(Arrays.asList(
                                "Kitty eats 5 biscuits out of 8 biscuits. How many biscuits are left to eat?",
                                "Jack hit some balloons leaving 2 balloons. If there were 6 balloons, how " +
                                        "many balloons did he hit?",
								"Jenny has 7 dollars. She gives 4 dollars to Dora. How much money does she " +
										"have left?",
								"Kelly fed 8 sparrows. After a minute, 2 sparrows flew away. How many sparrows" +
										" are left for her to feed?")), 750f, 450f, false),
						new com.omnipotence.game.Stage.gameStage("Math Word Problems Part 4", new ArrayList<String>(Arrays.asList(
								"In a basketball practice, each player was given 9 chances to throw the ball " +
										"into the basket. Justin hit the target 4 times. Find the number of " +
										"chances he missed.", "A bag contains 7 balls. How many balls are there in 8 bags?",
                                "Kitty drinks 4 cups of milk in a day. How much milk does she drink in a week?",
                                "Flora has 9 times more tulips than roses. If she has 6 roses, how many tulips " +
                                        "does she have?")), 750f, 420f, false),
                        new com.omnipotence.game.Stage.gameStage("Tools", new ArrayList<String>(Arrays.asList("Coke", "Fanatic",
                                "Germ", "Go", "Paper", "Unit", "Animal", "Bird")), 180f, 125f, false),
                        new com.omnipotence.game.Stage.gameStage("Rime Family", new ArrayList<String>(Arrays.asList("Ig", "Ip",
                                "Ish", "Ob", "Ock", "Op", "Ub", "Uck", "Ug", "Un","Am", "Ap", "At",
                                "Ad", "Ag", "An", "Ick", "Id", "Ill", "In", "It")), 180f, 125f, false)));
                answerKeyForTheQuestions = new HashMap<String, String>();
                //answers for question 1 - 5
                answerKeyForTheQuestions.put("The cat catches 2 rats in the morning and 5 rats " +
                        "in the evening. How many rats does it catch in a day?", "7");
                answerKeyForTheQuestions.put("Tom wins 1 gold prize and 3 silver prizes. How " +
                        "many prizes does he win altogether?", "4");
                answerKeyForTheQuestions.put("In a soccer game, the goal keeper saves 5 goals " +
                        "in the first set and 9 goals in the second set. How many goals does he " +
                        "save during the game?", "14");
                answerKeyForTheQuestions.put("Tammy solves 9 problems in Math and 4 problems in " +
                        "Science. How many problems does he solve altogether?", "13");
                answerKeyForTheQuestions.put("On her birthday, Tara received 7 gifts from her " +
                        "friends and 8 gifts from her relatives. How many gifts did she receive " +
                        "on her birthday?", "15");
                //answers for question 6 - 10
                answerKeyForTheQuestions.put("The cost of each book is $3. What is the cost of " +
                        "6 books?", "3");
                answerKeyForTheQuestions.put("Roger works 8 hours in a day. How many hours does" +
                        " he work in 5 days?", "6");
                answerKeyForTheQuestions.put("Jenny has 7 dollars. She gives 4 dollars to Dora." +
                        " How much money does she have left?", "3");
                answerKeyForTheQuestions.put("Kelly fed 8 sparrows. After a minute, 2 sparrows " +
                        "flew away. How many sparrows are left for her to feed?", "4");
                answerKeyForTheQuestions.put("Kitty eats 5 biscuits out of 8 biscuits. How many " +
                        "biscuits are left to eat?", "5");
                //answers for question 11 - 15
                answerKeyForTheQuestions.put("Jack hit some balloons leaving 2 balloons." +
                        " If there were 6 balloons, how many balloons did he hit?", "18");
                answerKeyForTheQuestions.put("In a basketball practice, each player was given 9 " +
                        "chances to throw the ball into the basket. Justin hit the target 4 " +
                        "times. Find the number of chances he missed?", "40");
                answerKeyForTheQuestions.put("A bag contains 7 balls. How many balls are there" +
                        " in 8 bags?", "56");
                answerKeyForTheQuestions.put("Kitty drinks 4 cups of milk in a day. How much " +
                        "milk does she drink in a week?", "28");
                answerKeyForTheQuestions.put("Flora has 9 times more tulips than roses. " +
                        "If she has 6 roses, how many tulips " +
                        "does she have?", "54");
				break;
		}

	}

    private void callNextScreen() {
        this.games = this;
        // Load all background music
        musicManager = new MusicManager();
        this.setScreen(new MenuScreen(this));
    }

	private void loadAssets() {
		// Load game sound effects
		AssetManager.getInstance().registerSound("wrong-answer",
				"sounds/wrong-answer.wav");
		AssetManager.getInstance().registerSound("correct-answer",
				"sounds/correct-answer.wav");
		AssetManager.getInstance().registerSound("button-click",
				"sounds/button-click.wav");

		// Load game music
		AssetManager.getInstance().registerMusic("music0", "music/music0.mp3");
		AssetManager.getInstance().registerMusic("music1", "music/music1.mp3");
		AssetManager.getInstance().registerMusic("music2", "music/music2.mp3");
        AssetManager.getInstance().registerMusic("random", "sounds/winner.wav");
		
		// Textures for level packs
		AssetManager.getInstance().registerTexture("grassyJourney-logo",
				"textures/Grassy-Journey.png");
		AssetManager.getInstance().registerTexture("snowyPlains-logo",
				"textures/Snowy-Plains.png");

		// Background for all menus
		AssetManager.getInstance().registerTexture("menuBackground",
				"Backgrounds/background-3.png");
        AssetManager.getInstance().registerTexture("levelsBackground",
                "textures/bg_castle.png");
        AssetManager.getInstance().registerTexture("practiceBackground",
                "Backgrounds/background-2.png");
        AssetManager.getInstance().registerTexture("battleBackground",
                "Backgrounds/industrial-background.jpg");
		AssetManager.getInstance().registerTexture("background1",
				"Backgrounds/background-1.png");
		AssetManager.getInstance().registerTexture("background2",
				"Backgrounds/background-2.png");
		AssetManager.getInstance().registerTexture("background3",
				"Backgrounds/background-3.png");
		AssetManager.getInstance().registerTexture("background4",
				"Backgrounds/background-4.png");

		// Main menu stuff
		AssetManager.getInstance().registerTexture("playButton", "textures/play.png");
		AssetManager.getInstance().registerTexture("gameLogo",
				"logo.png");

	}

	@Override
	public void render() {
        Gdx.gl.glClearColor(256f, 256f, 256f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// The superclass' render method renders the current screen, call it
		super.render();
	}

	@Override
	public void dispose() {
		// Dispose of background music
		musicManager.dispose();
		// Dispose of all assets on exit
		AssetManager.getInstance().disposeAll();
	}

}
