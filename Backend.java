// --== CS400 File Header Information ==--
// Name: <Kenneth Diao>
// Email: <kdiao2@wisc.edu>
// Team: <Red>
// Group: <KF>
// TA: <Keren Chen>
// Lecturer: <Gary Dahl>
// Notes to Grader: I created an extra class HashNode to contain the Keys and Values. 
// 					I also typecast the getKey() and getValue() from returning Object to returning KeyType and ValueType respectively
// 					because I didn't see another way of having them properly return KeyType and ValueType instead of Object
//					I also added a few private helper methods

import java.util.List;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.zip.DataFormatException;
import java.io.Reader;
import java.io.FileReader;
import java.io.StringReader;

public class Backend implements BackendInterface {
	private HashTableMap<String, List<MovieInterface>> movieRatingTable;
	private HashTableMap<String, List<MovieInterface>> movieGenreTable;
	private List<MovieInterface> movieList;
	private List<String> ratingList;
	private List<String> genreList;
	public Backend(FileReader file) throws FileNotFoundException, IOException, DataFormatException {
		MovieDataReader data = new MovieDataReader();
		movieList = data.readDataSet(file);
		movieRatingTable = new HashTableMap<String, List<MovieInterface>>();
		movieGenreTable = new HashTableMap<String, List<MovieInterface>>();
		ratingList = new LinkedList<String>();
		genreList = new LinkedList<String>();
		for(int i = 0; i < getAllGenres().size(); i++) {
			movieGenreTable.put(getAllGenres().get(i), new LinkedList<MovieInterface>());
			for(int j = 0; j < movieList.size(); j++) {
				if(movieList.get(j).getGenres().contains(getAllGenres().get(i)) && !movieGenreTable.get(getAllGenres().get(i)).contains(movieList.get(i))) {
					// if the movie is categorized as being a certain genre and that movie isn't included in the list already, add it to that genre's list
					movieGenreTable.get(getAllGenres().get(i)).add(movieList.get(i));
				}
			}
		}
		for(int i = 10; i >= 0; i--) {
			movieRatingTable.put("" + i, new LinkedList<MovieInterface>());
			for(int j = 0; j < movieList.size(); j++) {
				if(Math.floor(movieList.get(j).getAvgVote()) == i) {
					// if a movie's rating, rounded down, is equal to the rating category i, then add it to the list of movies with rating i
					movieRatingTable.get("" + i).add(movieList.get(j)); 
				}
			}
			Collections.sort(movieRatingTable.get("" + i), Collections.reverseOrder()); // sort each bucket in reverse order, so it goes from greatest to least (e.g. 9.999, 9.998, ..., 9.001, 9)
		}
	}
	
	public Backend(StringReader str) throws FileNotFoundException, IOException, DataFormatException {
		movieList = new LinkedList<MovieInterface>();
		MovieDataReader data = new MovieDataReader();
		movieList = data.readDataSet(str);
		ratingList = new LinkedList<String>();
		genreList = new LinkedList<String>();
		for(int i = 0; i < getAllGenres().size(); i++) {
			movieGenreTable.put(getAllGenres().get(i), new LinkedList<MovieInterface>());
			for(int j = 0; j < movieList.size(); j++) {
				if(movieList.get(j).getGenres().contains(getAllGenres().get(i)) && !movieGenreTable.get(getAllGenres().get(i)).contains(movieList.get(i))) {
					// if the movie is categorized as being a certain genre and that movie isn't included in the list already, add it to that genre's list
					movieGenreTable.get(getAllGenres().get(i)).add(movieList.get(i));
				}
			}
		}
		for(int i = 10; i >= 0; i--) {
			movieRatingTable.put("" + i, new LinkedList<MovieInterface>());
			for(int j = 0; j < movieList.size(); j++) {
				if(Math.floor(movieList.get(j).getAvgVote()) == i) {
					// if a movie's rating, rounded down, is equal to the rating category i, then add it to the list of movies with rating i
					movieRatingTable.get("" + i).add(movieList.get(j)); 
				}
			}
			Collections.sort(movieRatingTable.get("" + i), Collections.reverseOrder()); // sort each bucket in reverse order, so it goes from greatest to least (e.g. 9.999, 9.998, ..., 9.001, 9)
		}
	}
	
	@Override
	public void addGenre(String genre) {
		// TODO Auto-generated method stub
		if(!genreList.contains(genre)) {
			genreList.add(genre);
		}
	}

	@Override
	public void addAvgRating(String rating) {
		// TODO Auto-generated method stub
		if(!ratingList.contains(rating)) {
			ratingList.add(rating);
		}
	}

	@Override
	public void removeGenre(String genre) {
		genreList.remove(genre);
		
	}

	@Override
	public void removeAvgRating(String rating) {
		ratingList.remove(rating);
	}

	@Override
	public List<String> getGenres() {
		return genreList;
	}

	@Override
	public List<String> getAvgRatings() {
		return ratingList;
	}

	@Override
	public int getNumberOfMovies() {
		int count = 0;
		for(int i = 0; i < movieRatingTable.size(); i++) {
			// sum the sizes of each bucket
			count+=movieRatingTable.get("" + i).size();
		}
		return count;
	}
	
	public List<MovieInterface> getMovies() {
		List<MovieInterface> fullMovieList = new LinkedList<MovieInterface>();
		for(int i = ratingList.size() - 1; i >= 0; i--) {
			// iterate through each rating bucket
			for(int j = 0; j < movieRatingTable.get("" + i).size(); j++) {
				// add the movies, which have already been sorted in descending order
				fullMovieList.add(movieRatingTable.get("" + i).get(j));
			}
		}
		for(int i = 0; i < fullMovieList.size(); i++) {
			boolean movieContained = false;
			for(int j = 0; j < fullMovieList.get(i).getGenres().size(); j++) {
				// see if the movie contains one of the genres in genreList
				if(genreList.contains(fullMovieList.get(i).getGenres().get(j))) {
					movieContained = true;
				}
			}
			// if the movie does not contain one of the genres in genreList, remove it
			if(!movieContained) {
				fullMovieList.remove(i);
				i--;
			}
		}
		return fullMovieList;
	}

	@Override
	public List<MovieInterface> getThreeMovies(int startingIndex) {
		List<MovieInterface> threeMovies = new LinkedList<MovieInterface>();
		try {
			for(int i = 0; i < 3; i++) {
				// add the three movies from the current fullMovieList in getMovies() (which has already been sorted in descending order)
				threeMovies.add(getMovies().get(startingIndex + i)); 
			}
		} catch (ArrayIndexOutOfBoundsException e) { 
			// if there are less than three movies after the index specified, or the index is out of bounds, return right away
			return threeMovies;
		}
		return threeMovies;
	}

	@Override
	public List<String> getAllGenres() {
		List<String> allGenres = new LinkedList<String>();
		for(int i = 0; i < movieList.size(); i++) {
			// go through the whole movieList
			for(int j = 0; j < movieList.get(i).getGenres().size(); j++) {
				// go through each movie's list of genres
				if(!allGenres.contains(movieList.get(i).getGenres().get(j))) {
					// if a movie is classified as a genre that is not currently contained in allGenres, add said genre to allGenres
					allGenres.add(movieList.get(i).getGenres().get(j));
				}
			}
		}
		return allGenres;
	}

}
