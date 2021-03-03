// --== CS400 File Header Information ==--
// Name: Jeremy Boetticher
// Email: boetticher@wisc.edu
// Team: Red
// Group: KF
// TA: Keren Chen
// Lecturer: Florian
// Notes to Grader: -

import java.util.List;
import java.util.Arrays;

public class Movie implements MovieInterface {
	
	/**
	 * Creates a new movie class.
	 * @param title the title of the movie
	 * @param director the director(s) of the movie
	 * @param description a description of the movie
	 * @param year the year the movie was published
	 * @param avgVote the average vote
	 * @param genre a string of comma separated genres
	 */
	public Movie(String title, String director, String description, String year, String avgVote, String genre) {

		this.title = title;
		this.director = director;
		this.description = description;
		
		this.year = Integer.parseInt(year);
		this.avgVote = Float.parseFloat(avgVote);
		
		// finds the right genres
		genres = Arrays.asList(genre.split(", "));
	}
	
	//#region Fields
	
	private String title, director, description;
	private int year;
	private float avgVote;
	private List<String> genres;
	
	//#endregion
	
	
	
	//#region Getters
	
	/**
	 * Returns the movie's title.
	 */
	@Override
	public String getTitle() {
		return title;
	}
	
	/**
	 * Returns the movie's year.
	 */
	@Override
	public Integer getYear() {
		return year;
	}
	
	/**
	 * Returns the movie's genres.
	 */
	@Override
	public List<String> getGenres() {
		// makes a copy so that it isn't mutated
		return genres.subList(0, genres.size());
	}
	/**
	 * 
	 * Returns the movie's director(s).
	 */
	@Override
	public String getDirector() {
		return director;
	}
	
	/**
	 * Returns the movie's description.
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the movie's average vote.
	 */
	@Override
	public Float getAvgVote() {
		return avgVote;
	}
	
	//#endregion
	
	
	
	/**
	 * Compares self to another movie, based on the average vote.
	 * From super interface Comparable
	 */
	@Override
	public int compareTo(MovieInterface otherMovie) {
		return otherMovie.getAvgVote().compareTo(getAvgVote());
	}
	
	/**
	 * Returns a string representation of the movie.
	 */
	@Override
	public String toString() {
		String str = "title: " + title + "\n";
		str += "director: " + director + "\n";
		str += "description: " + description + "\n";
		str += "year: " + year + "\n";
		str += "avgVote: " + avgVote + "\n";
		str += "genre:";
		for(int i = 0; i < genres.size(); i++) str += " " + genres.get(i);
		return str;
	}	
}
