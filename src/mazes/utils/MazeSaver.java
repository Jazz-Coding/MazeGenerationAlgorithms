package mazes.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MazeSaver {
    /**
     * Methods to save the mazes as image files.
     */
    public static BufferedImage getAsImage(int arrayCols, int arrayRows, String[][] maze, String mazeCharacterIndicator) {
        BufferedImage image = new BufferedImage(arrayCols, arrayRows,BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < arrayRows; i++) {
            for (int j = 0; j < arrayCols; j++) {
                String mazeElement = maze[i][j];
                if(i == 1 && j == 0 || i == arrayRows -2 && j == arrayCols -1){ // exits
                    image.setRGB(j,i,(255 << 16 | 255 << 8 | 255));
                } else if(!mazeElement.equals(mazeCharacterIndicator)){
                    image.setRGB(j,i,0);
                } else {
                    image.setRGB(j,i,(255 << 16 | 255 << 8 | 255));
                }
            }
        }
        return image;
    }
    public static void saveLocally(BufferedImage image, String filename){
        String fileFormat = "png";
        try {
            File outputFile = new File(filename+"."+fileFormat);
            outputFile.createNewFile();
            ImageIO.write(image,fileFormat,outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
