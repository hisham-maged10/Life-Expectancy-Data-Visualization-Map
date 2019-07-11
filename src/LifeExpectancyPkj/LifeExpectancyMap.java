package LifeExpectancyPkj;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import java.util.Map;
import java.util.List;
import processing.core.PApplet;

public class LifeExpectancyMap extends PApplet {
  private UnfoldingMap lifeExpectancyMap;
  private UnfoldingMap usedMap;
  private UnfoldingMap normalMap;
  private Map<String,Float> lifeExpectancyByCountry;
  private List<Feature> countries;
  private List<Marker> countryMarkers;


  public void setup()
  {
    size(1280,720,OPENGL);
    initMap();

  }

  public void draw()
  {
    this.usedMap.draw();
  }

  private void initMap()
  {
    // creates a Map of width 1280 and 720 height
    this.lifeExpectancyMap = new UnfoldingMap(this,0,0,1280,720,new Microsoft.HybridProvider());
    // adds interactivity to Map
    MapUtils.createDefaultEventDispatcher(this,lifeExpectancyMap);
    // gets map of countryId and it's life expectancy Value ranging from 40 to 90
    this.lifeExpectancyByCountry = new LifeExpectancyDataHandler("./data/LifeExpectancyWorldBank.csv").getLifeExpectancyData();
    // reads locations of countries in the whole map
    this.countries = GeoJSONReader.loadData(this,"./data/countries.geo.json");
    // make markers for given List<Feature> which is locations of countries (each location in map)
    this.countryMarkers = MapUtils.createSimpleMarkers(countries);
    // change colors of countries based on life expectancy, bright red for low life expectancy (40)
    // bright blue for high life expectancy (90)
    // ranging in between is between 40 and 90 from red to blue increasing, from blue to red decreasing
    shadeCountries(this.countryMarkers,this.lifeExpectancyByCountry);
    //adding markers tto the map
    this.lifeExpectancyMap.addMarkers(countryMarkers);
    // zooms to a certain level on map
    this.lifeExpectancyMap.zoomToLevel(3);
    //creates a normal map for keypressed function
    this.normalMap = new UnfoldingMap(this,0,0,1280,720,new Microsoft.HybridProvider());
    // adds interactivity
    MapUtils.createDefaultEventDispatcher(this,this.normalMap);
    //adjust zoom level
    this.normalMap.zoomToLevel(3);

    // makes normal map the first map that loads
    this.usedMap = this.lifeExpectancyMap;
  }

  // the method that shades the countries based on life expectancy map value for each country Id and List<Marker>
  private void shadeCountries(List<Marker> countryMarkers,Map<String,Float> lifeExpectancyByCountry)
  {
    for(Marker m : countryMarkers)
    {
      String countryID = m.getId();
      if(lifeExpectancyByCountry.containsKey(countryID))
      {
        float lifeExpectancyValue = lifeExpectancyByCountry.get(countryID);
        // map method takes a float, and starting and ending range (known from data in life expectancy) and maps the
        // values to their comparables using ratios in the 2nd two parameters indicating start and end range,
        // 255 is end range as it will be used as an rgb value for the marker
        int colorLevel = (int) map(lifeExpectancyValue,40,90,10,255);
        // we want the blue level meaning high level expectancy, and red level means low level expectancy
        // so if color level is mapped to close to 0 life expectancy then it will be 255 - color level giving 255 red
        // and 0 blues and if life expectancy is high , blue increases and red decreases, and green is constant at 100
        m.setColor(color(255-colorLevel,100,colorLevel));
      }else{
        m.setColor(color(150));
      }
    }
  }

  // keyPressed method to change maps on click
  public void keyPressed()
  {
    if(key == '1')
      this.usedMap = this.normalMap;
    else if (key == '2')
      this.usedMap = this.lifeExpectancyMap;
  }


}
