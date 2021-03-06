package pintosim;

import java.awt.Image;
import java.io.File;
import java.awt.Point;
import java.io.FileReader;
import java.util.*;

public class GUISim {
    private static final String CONFIG_FILENAME = "config.properties";
    
    //lol a unicode fail whale...
    private static final String failureMessage = "failure initializing\n\n"
                              + "▄██████████████▄▐█▄▄▄▄█▌\n"
                              + "██████▌▄▌▄▐▐▌███▌▀▀██▀▀\n"
                              + "████▄█▌▄▌▄▐▐▌▀███▄▄█▌\n"
                              + "▄▄▄▄▄██████████████▀\n";
    
    
	public static void main(String[] args) {

        try {
            Properties props = new Properties();
            props.load(new FileReader(new File(CONFIG_FILENAME)));
            showGUIInterface(props);
        } catch (Exception e) {
            System.out.println(failureMessage);
            e.printStackTrace();
        }
	}
    
    
    
    
    
	
	public static void showGUIInterface(Properties props) throws Exception {
        final GraphicsPackage graphicsPackage = new PropertiesBasedGraphicsPackage(props);
        final MapFeatures mapFeatures = new ImageMapAnalyzer(graphicsPackage.getMapImage());
        final EnviornmentMap map = new EnviornmentMap(mapFeatures);
        final PintoManager pintoManager = new PintoManager(map);
        final PathFinder pathFinder = new DijkstraPathFinder(map);

        final int tileSize = graphicsPackage.getTileSize();
        final int ANIMATION_DURATION = Integer.parseInt(
            props.getProperty("GameUpdateFrequencyMS")
        );
        final int GAME_UPDATE_FREQUENCY_MS = ANIMATION_DURATION;

        final AnimPanel animPanel = new AnimPanel(
              map.getWidth() * tileSize
            , map.getHeight() * tileSize
            , Integer.parseInt(props.getProperty("CanvasRenderingFrequencyMS"))
        );

        animPanel.addPaintable(new FramesPerSecond());



        // create sprites for the walls and floor
        // we will every single space with either a wall or floor graphic
        // other things get just painted on top of these
        Image floorGraphic = graphicsPackage.getFloorImage();
        Image wallGraphic = graphicsPackage.getWallImage();
        boolean[][] matrix = mapFeatures.getFreeSpaceMatrix();
        for (int i=0; i < matrix.length; i++) {
            for (int j=0; j<matrix[i].length; j++) {
                animPanel.addPaintable(new Sprite(
                    matrix[i][j] ? floorGraphic : wallGraphic
                    , graphicsPackage.translateCoords(new Point(i, j))
                    , 0
                ));
            }
        }


        final Tween tween = new BasicTweenFactory().create(
            props.getProperty("TweeningAlgorithm")
          , props.getProperty("TweeningAlgorithmAction")
        );
        
        // this listener gets called when we add a new item to the enviornmentMap.


        //add pintos
        for (Point pintoLocation : mapFeatures.getPintoLocations()) {
            Pinto pinto = new Pinto(pintoLocation, map, pintoManager, pathFinder);
            map.trackObject(pinto);
            pintoManager.addPinto(pinto);
            Point initialLocation = graphicsPackage.translateCoords(pintoLocation);
            MovementTransitionDecorator sprite = new MovementTransitionDecorator(
                new Sprite(graphicsPackage.getPintoImage()
              , initialLocation
              , 10
            ));
            pinto.addLocationChangeListener(
                new TransitionCreator(ANIMATION_DURATION, tileSize, sprite, tween)
            );
            animPanel.addPaintable(new OrbitalDecorator(sprite, 2, rand(50, 200)));
        }


        // add items.
        // the image map defines how many items exist at the start(and the item locations)
        // we try to use named items which have specific graphics to fill up the slots. 
        // any remaining slots get the default item grpahic, and a sequential name like a1 a2 a3

        Iterator<Map.Entry<String, Image>> namedItems = graphicsPackage.getNamedExtraImages().entrySet().iterator();
        int itemSerial = 1;
        for (Point itemLocation : mapFeatures.getItemLocations()) {
            String name;
            Image img;
            
            if (namedItems.hasNext()) {
                Map.Entry<String, Image> entry = namedItems.next();
                name = entry.getKey().split("\\.")[0];
                img = entry.getValue();
            } else {
                name = "a" + itemSerial++;
                img = graphicsPackage.getItemImage();
            }
            
            Item item = new Item(name, itemLocation.x, itemLocation.y);
            map.trackItem(item);
            Point initialLocation = graphicsPackage.translateCoords(itemLocation);
            MovementTransitionDecorator sprite = new MovementTransitionDecorator(
                new Sprite(img, initialLocation, 5)
            );
            item.addLocationChangeListener(
                new TransitionCreator(ANIMATION_DURATION, tileSize, sprite, tween)
            );
            animPanel.addPaintable(sprite);
        }
        

        //we add the listener after we manually add items otherwise they get added twice
        map.addItemTrackedListener(new ItemTrackedListener() {
            public void itemAdded(Item item) {
                Point initialLocation = graphicsPackage.translateCoords(
                    new Point(item.getX(), item.getY())
                );
                MovementTransitionDecorator sprite = new MovementTransitionDecorator(
                    new Sprite(graphicsPackage.getItemImage()
                  , initialLocation
                  , 5
                ));
                item.addLocationChangeListener(
                    new TransitionCreator(ANIMATION_DURATION, tileSize, sprite, tween)
                );
                animPanel.addPaintable(sprite);
            }
        });
        
        
        

        Point initialLocation = graphicsPackage.translateCoords(
            mapFeatures.getPintoDockingStationLocation()
        );
        animPanel.addPaintable(
            new Sprite(graphicsPackage.getDockingImage()
          , initialLocation
          , 5)
        );
        initialLocation = graphicsPackage.translateCoords(
            mapFeatures.getPersonLocation()
        );
        Paintable p = new Sprite(
            graphicsPackage.getPersonImage()
          , initialLocation
          , 5
        );

        animPanel.addPaintable(new BackAndForthDecorator(p, 2, 100));
     
        
        

        map.trackObject(new Person(mapFeatures.getPersonLocation()));

        final PintoManager pm = pintoManager;
        Timer t = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                pm.work();
            }
        };
        t.scheduleAtFixedRate(task, 0, GAME_UPDATE_FREQUENCY_MS);

        new GUI(pintoManager, map, animPanel);
	}
    
    private static int rand(int Min, int Max) {
        return Min + (int)(Math.random() * ((Max - Min) + 1));
    }
}