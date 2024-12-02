package TypingGame;

import java.net.URL;

/**
 * Controller responsible for updating and retrieving the background image path
 * based on the current game level.
 */
class BackgroundImageController implements LevelObserver
{

    private static final int INITIAL_LEVEL = 1;
    private String backgroundImagePath;

    /**
     * Constructor that sets the default background for level 1.
     */
    BackgroundImageController()
    {
        updateLevel(INITIAL_LEVEL);
    }

    /**
     * Updates the background image based on the provided level.
     * If the image for the current level is not found, it defaults to level 1 background.
     *
     * @param level The current game level used to determine the background image.
     */
    @Override
    public void updateLevel(final int level)
    {
        final String folderPath;
        final String imagePath;
        final URL imageURL;

        folderPath = "images/level_backgrounds";
        imagePath = folderPath + "/level" + level + "_background.png";
        imageURL = getClass().getClassLoader().getResource(imagePath);


        if(imageURL != null)
        {
            backgroundImagePath = imageURL.toString();
        }
        else
        {
            backgroundImagePath = "images/level_backgrounds/level1_background.png";
        }
    }

    /**
     * Retrieves the path to the background image for the current level.
     *
     * @return The file path or URL of the background image.
     */
    String getBackground()
    {
        return backgroundImagePath;
    }
}
