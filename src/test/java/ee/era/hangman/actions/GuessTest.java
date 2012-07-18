package ee.era.hangman.actions;

import ee.era.hangman.model.Word;
import ee.era.hangman.model.Words;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class GuessTest {
  Game game = new Game();
  Guess guess = new Guess();

  @Before
  public void startGame() {
    Map<String, Object> session = new HashMap<String, Object>();
    game.setSession(session);
    guess.setSession(session);
    game.language = "eng";
    game.words = new Words() {
      @Override
      public Word getRandomWord(String language) {
        if ("rus".equals(language))
          return new Word("методологии разработки", "аджайл");
        if ("est".equals(language))
          return new Word("tarkvara metoodikad", "agiilne");
        return new Word("software development", "agile");
      }
    };
    game.startGame();
  }

  @Test
  public void ifLetterIsGuessedItsShown() {
    assertEquals("_____", guess.getWordInWork());
    guess.letter = 'G';
    guess.guessLetter();
    assertEquals("_g___", guess.getWordInWork());
    assertTrue(guess.isGuessed());
  }

  @Test
  public void ifLetterIsNotGuessedThenFailuresCounterGrows() {
    guess.letter = 'x';
    guess.guessLetter();
    assertEquals("_____", guess.getWordInWork());
    assertFalse(guess.isGuessed());
    assertThat(game.getFailures(), equalTo(1));
  }

  @Test
  public void supportsRussian() {
    game.language = "rus";
    game.startGame();

    assertEquals("______", guess.getWordInWork());
    guess.letter = 'Ж';
    guess.guessLetter();
    assertEquals("__ж___", guess.getWordInWork());
    assertTrue(guess.isGuessed());
  }
}
