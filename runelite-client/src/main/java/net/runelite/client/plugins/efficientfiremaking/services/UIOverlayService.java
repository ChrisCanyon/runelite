package net.runelite.client.plugins.efficientfiremaking.services;

import net.runelite.api.coords.WorldPoint;

import java.util.ArrayList;
import java.util.List;

public class UIOverlayService {

    public void drawRectangleFromCenter(int width, int height, WorldPoint center){
        List<WorldPoint> worldPoints = createRectangleWorldPointList(width, height, center, true);

        //TODO draw things
    }

    //TODO look into create rectangle from 2D point
    public static List<WorldPoint> createRectangleWorldPointList(int width, int height, WorldPoint referencePoint) {
        return createRectangleWorldPointList(width, height, referencePoint, 0, 0);
    }

    public static List<WorldPoint> createRectangleWorldPointList(int width, int height, WorldPoint referencePoint, boolean centered) {
        if(!centered){
            return createRectangleWorldPointList(width, height, referencePoint);
        }
        int xOffset, yOffset;

        xOffset = width/-2;
        yOffset = height/-2;

        return createRectangleWorldPointList(width, height, referencePoint, xOffset, yOffset);
    }

    public static List<WorldPoint> createRectangleWorldPointList(int width, int height, WorldPoint referencePoint, int xOffset, int yOffset){
        List<WorldPoint> worldPoints = new ArrayList<>();
        referencePoint = referencePoint.dx(xOffset).dy(yOffset);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                worldPoints.add(referencePoint.dx(x).dy(y));
            }
        }
        return worldPoints;
    }
}
