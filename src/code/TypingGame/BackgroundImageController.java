package TypingGame;

public class BackgroundImageController implements LevelObserver
{

    private String backgroundImage;

    public BackgroundImageController() {
        this.backgroundImage = "images/level1_background.png";  // Default background for level 1
    }


    @Override
    public void updateLevel(int level) {
        System.out.println("Level increased: " + level);
        // Update the background image based on the level
        switch (level) {
            case 1:
                backgroundImage = "images/level1_background.png";
                break;
            case 2:
                backgroundImage = "images/level2_background.png";
                break;
            case 3:
                backgroundImage = "images/level3_background.png";
                break;
            default:
                break;
        }

        // Here you would actually update the background image in your game (e.g., GUI)
        System.out.println("Updated Background to: " + backgroundImage);
    }

    public String getBackground(){
        return backgroundImage;
    }
}
