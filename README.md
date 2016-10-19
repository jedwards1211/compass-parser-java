# compass-parser-java

This is a parser for the [Compass cave survey data format](http://www.fountainware.com/compass/Documents/FileFormats/SurveyDataFormat.htm).


## Example

```java
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.andork.compass.CompassParseError;
import org.andork.compass.CompassParser;
import org.andork.compass.CompassShot;
import org.andork.compass.CompassTrip;

public class Example {
	public static void main(String[] args) throws IOException {
		CompassParser parser = new CompassParser();
		List<CompassTrip> trips = parser.parseCompassSurveyData(Paths.get(""));

		if (parser.getErrors().isEmpty()) {
			System.out.println("No errors!");
		}
		for (CompassParseError error : parser.getErrors()) {
			System.out.println(error);
		}

		for (CompassTrip trip : trips) {
			System.out.println(trip.getHeader().getCaveName());
			System.out.println(trip.getHeader().getSurveyName());
			System.out.println(trip.getHeader().getDate());

			for (CompassShot shot : trip.getShots()) {
				System.out.println("  " + shot.getFromStationName() + " -> " + shot.getToStationName() + ":");
				System.out.println("    to:      " + shot.getToStationName());
				System.out.println("    length:  " + shot.getLength());
				System.out.println("    fsAzm:   " + shot.getFrontsightAzimuth());
				System.out.println("    fsInc:   " + shot.getFrontsightInclination());
				System.out.println("    bsAzm:   " + shot.getBacksightAzimuth());
				System.out.println("    bsInc:   " + shot.getBacksightInclination());
				System.out.println("    left:    " + shot.getLeft());
				System.out.println("    right:   " + shot.getRight());
				System.out.println("    up:      " + shot.getUp());
				System.out.println("    down:    " + shot.getDown());
				System.out.println("    exclude: " + shot.isExcludedFromLength());
			}
		}
	}
}
```
