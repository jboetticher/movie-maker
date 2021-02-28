import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.LinkedList;

public class MovieDataReader implements MovieDataReaderInterface {
	
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
			try {
				Movie nxtMovie = CreateMovieFromColumns(importantRowStrings);
				movieList.add(nxtMovie);
			}
			catch(Exception e) {
				throw new DataFormatException(e.getMessage());
			}
			
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
