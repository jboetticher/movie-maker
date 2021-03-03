// --== CS400 File Header Information ==--
// Name: Jeremy Boetticher
// Email: boetticher@wisc.edu
// Team: Red
// Group: KF
// TA: Keren Chen
// Lecturer: Florian
// Notes to Grader: -

import java.util.List;
import java.util.zip.DataFormatException;
import java.io.IOException;
import java.io.Reader;

public interface MovieDataReaderInterface {
	
	public List<MovieInterface> readDataSet(Reader inputFileReader) throws IOException, DataFormatException;

}
