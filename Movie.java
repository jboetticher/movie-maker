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
	
	public Movie(String title, String director, String description, String year, String avgVote, String genre) {

		this.title = title;
		this.director = director;
		this.description = description;
		
		this.year = Integer.parseInt(year);
		this.avgVote = Float.parseFloat(avgVote);
		
		// finds the right genres
		genres = Arrays.asList(genre.split(", "));
		
		//System.out.println(this.toString());
		//System.out.println("------------");
	}
	
	//#region Fields
	
	private String title, director, description;
	private int year;
	private float avgVote;
	private List<String> genres;
	
	//#endregion
	
	
	
	//#region Getters
	
	/**
	 * 
	 */
	@Override
	public String getTitle() {
		return title;
	}
	
	@Override
	public Integer getYear() {
		return year;
	}
	
	@Override
	public List<String> getGenres() {
		// makes a copy so that it isn't mutated
		return genres.subList(0, genres.size());
	}
	
	@Override
	public String getDirector() {
		return director;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public Float getAvgVote() {
		return avgVote;
	}
	
	//#endregion
	
	
	
	// from super interface Comparable
	@Override
	public int compareTo(MovieInterface otherMovie) {
		return otherMovie.getAvgVote().compareTo(getAvgVote());
	}
	
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
