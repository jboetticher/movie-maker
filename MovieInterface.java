// --== CS400 File Header Information ==--
// Name: Jeremy Boetticher
// Email: boetticher@wisc.edu
// Team: Red
// Group: KF
// TA: Keren Chen
// Lecturer: Florian
// Notes to Grader: -

import java.util.List;

public interface MovieInterface extends Comparable<MovieInterface> {
	
	public String getTitle();
	public Integer getYear();
	public List<String> getGenres();
	public String getDirector();
	public String getDescription();
	public Float getAvgVote();
	
	// from super interface Comparable
	public int compareTo(MovieInterface otherMovie);

}
