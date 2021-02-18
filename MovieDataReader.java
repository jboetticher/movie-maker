import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.LinkedList;

public class MovieDataReader implements MovieDataReaderInterface {

	public static void main(String[] args) {
		// test the crap here
		List<MovieInterface> movieList;
		MovieDataReaderInterface readerToTest = new MovieDataReader();
		
		try {
			movieList = readerToTest.readDataSet(new StringReader(
					"title,original_title,year,genre,duration,country,language,director,writer,production_company,actors,description,avg_vote\n"
					+ "The Source of Shadows,The Source of Shadows,2020,Horror,83,USA,English,\"Ryan Bury, Jennifer Bonior\",\"Jennifer Bonior, Trevor Botkin\",Four Thieves Productions,\"Ashleigh Allard, Tom Bonington, Eliane Gagnon, Marissa Kaye Grinestaff, Jenna Heffernan, Joshua Hummel, Janice Kingsley, Chris Labasbas, Jared Laufree, Dominic Lee, Vic May, Sienna Mazzone, Lizzie Mounter, Grace Mumm, Ashley Otis\",\"A series of stories woven together by one of our most primal fears, the fear of the unknown.\",3.5\n"
					+ "The Insurrection,The Insurrection,2020,Action,90,USA,English,Rene Perez,Rene Perez,,\"Michael Paré, Wilma Elles, Joseph Camilleri, Rebecca Tarabocchia, Jeanine Harrington, Malorie Glavan, Danner Boyd, Michael Cendejas, Woody Clendenen, Keely Dervin, Aaron Harvey, Tony Jackson, Michael Jarrod, Angelina Karo, Bernie Kelly\",The director of the largest media company wants to expose how left-wing powers use film to control populations.,2.9\n"
					+ "Valley Girl,Valley Girl,2020,\"Comedy, Musical, Romance\",102,USA,English,Rachel Lee Goldenberg,\"Amy Talkington, Andrew Lane\",Sneak Preview Productions,\"Jessica Rothe, Josh Whitehouse, Jessie Ennis, Ashleigh Murray, Chloe Bennet, Logan Paul, Mae Whitman, Mario Revolori, Rob Huebel, Judy Greer, Alex Lewis, Alex MacNicoll, Danny Ramirez, Andrew Kai, Allyn Rachel\",\"Set to a new wave '80s soundtrack, a pair of young lovers from different backgrounds defy their parents and friends to stay together. A musical adaptation of the 1983 film.\",5.4\n"
			));
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found Exception oh no");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Exception oh no");
			e.printStackTrace();
		} catch (DataFormatException e) {
			System.out.println("Data Format Exception oh no");
			e.printStackTrace();
		}
	}
	
	/**
	 * A list of the keys to look out for in a csv, in alphabetical order.
	 * Note: CreateMovieFromColumns depends on this, so be sure to change it if you
	 *       change this array.
	 */
	private String[] importantKeys = new String[] {
			"avg_vote", "description", "director", "genre", "title", "year"
	};
	
	/**
	 * Reads data from csv format and places it into a list of MovieInterface objects.
	 * @param inputFileReader
	 * TODO: throw proper exceptions and check for things breaking
	 */
	@Override
	public List<MovieInterface> readDataSet(Reader inputFileReader)
			throws FileNotFoundException, IOException, DataFormatException {
		
		// error 1
		if(inputFileReader == null) {
			throw new FileNotFoundException("No input reader given.");
		}
		
		// initializes a set to catalog positions of the importantKeys, in case column
		// positions aren't constant.
		int[] columnSet = new int[importantKeys.length];
		for(int i = 0; i < columnSet.length; i++) columnSet[i] = -1;
		
		// figures out which columns of the data set correspond with the importantKeys.
		// the first line of the csv are always the column names.
		// it will keep looking for columns until the line is over.
		char nxtChr = (char)inputFileReader.read();
		String nxtStr = "";
		int columnPlace = 0;
		while(nxtChr != '\n') {
			if(nxtChr == ',') {
				SearchForComparison(columnSet, columnPlace, nxtStr);
				columnPlace++;
				nxtStr = "";
			}
			else nxtStr += nxtChr;
			nxtChr = (char)inputFileReader.read();
		}
		SearchForComparison(columnSet, columnPlace, nxtStr);
		
		System.out.print("Accepted Columns: ");
		for(int i = 0; i < columnSet.length; i++) {
			System.out.print(columnSet[i] + " ");
		}
		System.out.println("\n---------------");
		
		// creates a Movie object for every row
		int chrInt = inputFileReader.read();
		List<MovieInterface> movieList = new LinkedList<MovieInterface>();
		while(chrInt > -1) {
			// reset row
			nxtChr = (char)chrInt;
			nxtStr = "";
			int currentCol = 0;
			boolean quotationLock = false;
			String[] importantRowStrings = new String[importantKeys.length];
			
			// deciphers row
			while(nxtChr != '\n') {
				if(nxtChr == ',' && !quotationLock) {
					SetParameter(columnSet, currentCol, nxtStr, importantRowStrings);	
					nxtStr = "";
					currentCol++;
				}
				else if(nxtChr == '"') quotationLock = !quotationLock;
				else nxtStr += nxtChr;
				
				// either continues or acts as if it's the end of the word when it
				// hits the end of the file
				chrInt = inputFileReader.read();
				nxtChr = chrInt == -1 ? '\n' : (char)chrInt;
			}
			SetParameter(columnSet, currentCol, nxtStr, importantRowStrings);
			
			// turn into movie
			Movie nxtMovie = CreateMovieFromColumns(importantRowStrings);
			movieList.add(nxtMovie);
			
			// sets chr int so that it can check to either finish or convert  
			// to nxtChr
			chrInt = inputFileReader.read();
		}
		
		inputFileReader.close();
		
		return movieList;
	}
	
	/**
	 * Adds a specified int to an array at a corresponding location of importantKeys, if
	 * a specified string matches.
	 * @param nxtStr the string to cross reference with importantKeys
	 * @param columnSet the array to add to
	 * @param columnPlace the int to add to the array
	 * 
	 * TODO: make it break if the alphabetical order is surpassed 
	 */
	private void SearchForComparison(int[] columnSet, int columnPlace, String nxtStr) {
		//System.out.println(" " + nxtStr);
		for(int i = 0; i < importantKeys.length; i++) { 			
			if(nxtStr.compareTo(importantKeys[i]) == 0) {				
				columnSet[i] = columnPlace; 
				//System.out.println(" --- column set for " + nxtStr + " --- ");
				break;
			}
			
		}
	}

	/**
	 * Adds a specified string to an array if its position is accepted
	 * @param columnSet column positions that will be accepted
	 * @param currentCol the current position to reference the string to
	 * @param nxtStr the string to add if accepted
	 * @param importantRowStrings the array to add nxtStr to if accepted
	 */
	private void SetParameter(int[] columnSet, int currentCol, String nxtStr, String[] importantRowStrings) {
		for(int i = 0; i < columnSet.length; i++) {
			if(columnSet[i] == currentCol) {
				importantRowStrings[i] = nxtStr;
			}
		}	
	}
	
	/**
	 * Creates a movie from an array of strings based on the order of importantKeys.
	 * @param sortedColumns an array of strings in the order of importantKeys
	 * @return A new movie.
	 */
	private Movie CreateMovieFromColumns(String[] sortedColumns) {
		
		// the code assumes the positions of the keys in the importantKeys, so if that
		// is changed, you will HAVE TO CHANGE THIS CODE!
		return new Movie(
				sortedColumns[4],  // title
				sortedColumns[2],  // director
				sortedColumns[1],  // description
				sortedColumns[5],  // year
				sortedColumns[0],  // avg_vote
				sortedColumns[3]); // genre
	}
}
