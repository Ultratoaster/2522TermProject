//import javafx.animation.PauseTransition;
//import javafx.util.Duration;
//
//private void handleCharacterInput(final int index) {
//    final String typedChar = textFields[index].getText();
//    final TypingGame.Enemy currentEnemy = enemies.get(currentEnemyIndex);
//
//    if (typedChar.length() == 1) {
//        final char typedLetter = Character.toLowerCase(typedChar.charAt(ZEROTH_INDEX));
//        final char correctLetter = Character.toLowerCase(currentWord.charAt(index));
//
//        if (typedLetter != correctLetter) {
//            setTextFieldsDisabled(true);
//            playerHealth -= currentEnemy.getDamage();
//            playerHealthBar.setProgress(playerHealth / PROGRESS_BAR_MAX_PERCENT);
//        }
//
//        if (isWordCompleted()) {
//            setTextFieldsDisabled(true);
//            currentEnemy.takeDamage(PLAYER_DAMAGE_AMOUNT);
//            double healthPercentage = (double) currentEnemy.getHealth() / currentEnemy.getMaxHealth();
//            enemyHealthBar.setProgress(healthPercentage);
//
//            // Check if the enemy is defeated
//            if (currentEnemy.getHealth() <= 0) {
//                nextEnemy();
//            }
//        }
//
//        PauseTransition pause = new PauseTransition(Duration.seconds(1));
//        pause.setOnFinished(event -> {
//            if (typedLetter != correctLetter || isWordCompleted()) {
//                clearTextFields();
//                textFields[ZEROTH_INDEX].requestFocus();
//                setTextFieldsDisabled(false);
//            }
//        });
//        pause.play();
//
//        if (typedLetter == correctLetter && index < currentWord.length() - 1) {
//            textFields[index + 1].requestFocus();
//        }
//    }
//}