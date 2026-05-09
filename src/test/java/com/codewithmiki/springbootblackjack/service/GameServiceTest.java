package com.codewithmiki.springbootblackjack.service;

import com.codewithmiki.springbootblackjack.model.Card;
import com.codewithmiki.springbootblackjack.model.Game;
import com.codewithmiki.springbootblackjack.model.Player;
import com.codewithmiki.springbootblackjack.model.*;
import com.codewithmiki.springbootblackjack.model.deckStrategies.DeckGenerationStrategy;
import jdk.jfr.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {
    @Mock
    private PlayerService playerService;

    @Mock
    private DeckGenerationStrategy deckGenerationStrategy;

    @InjectMocks
    private GameService gameService;

    private final String playerId = "test-uuid";
    private Player player;
    private final String gameId = "test-game-id";

    @BeforeEach
    void setUp() {
        player = new Player();
        player.setMoney(new BigDecimal("1000.00"));
    }

    @Nested
    @DisplayName("Start new game tests")
    class startNewGameTests {
        @Test
        @DisplayName("Utworzenie nowej gry gdy gracz nie jest w innej grze")
        void StartNewGameWhenPlayerNotInOtherGame()
        {
            //given
            BigDecimal betAmount = new BigDecimal("100.00");
            when(playerService.getPlayer(playerId)).thenReturn(player);
            when(deckGenerationStrategy.generateDeck()).thenReturn(new ArrayList<>(List.of(
                    new Card(Value.TWO, Colour.CLUBS),
                    new Card(Value.TWO, Colour.HEARTS),
                    new Card(Value.TWO, Colour.DIAMONDS),
                    new Card(Value.TWO, Colour.SPADES)
            )));

            Game game = gameService.startNewGame(playerId, betAmount);


            //then
            assertNotNull(game);
            assertEquals(player, game.getPlayer());
            assertEquals(2, game.getPlayer().getHand().size());
            assertEquals(2, game.getDealer().getHand().size());
            assertEquals(GameStatus.IN_PROGRESS, game.getStatus());
            assertEquals(new BigDecimal("100.00"), player.getCurrentBet());
        }

        @Test
        @DisplayName("Start with a dealer blackjack")
        void StartWithABlackJack()
        {
            BigDecimal betAmount = new BigDecimal("100.00");
            when(playerService.getPlayer(playerId)).thenReturn(player);
            when(deckGenerationStrategy.generateDeck()).thenReturn(new ArrayList<>(List.of(
                    new Card(Value.JACK, Colour.CLUBS),
                    new Card(Value.TWO, Colour.HEARTS),
                    new Card(Value.ACE, Colour.DIAMONDS),
                    new Card(Value.TWO, Colour.SPADES)
            )));

            Game game = gameService.startNewGame(playerId, betAmount);
            assertNotNull(game);
            assertEquals(player, game.getPlayer());
            assertEquals(2, game.getPlayer().getHand().size());
            assertEquals(2, game.getDealer().getHand().size());
            assertEquals(GameStatus.DEALER_WON, game.getStatus());
            assertEquals(BigDecimal.ZERO, player.getCurrentBet());
            assertEquals(new BigDecimal("900.00"), player.getMoney());
        }
        @Test
        @DisplayName("Start with a player blackjack")
        void StartWithAPlayerBlackJack()
        {
            BigDecimal betAmount = new BigDecimal("100.00");
            when(playerService.getPlayer(playerId)).thenReturn(player);
            when(deckGenerationStrategy.generateDeck()).thenReturn(new ArrayList<>(List.of(
                    new Card(Value.TWO, Colour.HEARTS),
                    new Card(Value.JACK, Colour.CLUBS),
                    new Card(Value.TWO, Colour.SPADES),
                    new Card(Value.ACE, Colour.DIAMONDS)
            )));

            Game game = gameService.startNewGame(playerId, betAmount);
            assertNotNull(game);
            assertEquals(player, game.getPlayer());
            assertEquals(2, game.getPlayer().getHand().size());
            assertEquals(2, game.getDealer().getHand().size());
            assertEquals(GameStatus.PLAYER_WON, game.getStatus());
            assertEquals(BigDecimal.ZERO, player.getCurrentBet());
            assertEquals(0,new BigDecimal("1150.00").compareTo(player.getMoney()));
        }
        @Test
        @DisplayName("Should throw exception - player already in a game")
        void ShouldThrowExceptionWhenPlayerAlreadyInGame() {
            when(playerService.getPlayer(playerId)).thenReturn(player);
            when(deckGenerationStrategy.generateDeck()).thenReturn(new ArrayList<>(List.of(
                    new Card(Value.TEN, Colour.SPADES), new Card(Value.TEN, Colour.HEARTS),
                    new Card(Value.TEN, Colour.CLUBS), new Card(Value.TEN, Colour.DIAMONDS)
            )));

            gameService.startNewGame(playerId, BigDecimal.TEN);

            var exception = assertThrows(IllegalArgumentException.class, () -> {
                gameService.startNewGame(playerId, BigDecimal.TEN);
            });

            assertEquals("Player with id " + playerId + " is already in an active game.", exception.getMessage());
        }

        @Test
        @DisplayName("Start with a blackjacks for both")
        void StartWithABlackJacksForBoth()
        {
            BigDecimal betAmount = new BigDecimal("100.00");
            when(playerService.getPlayer(playerId)).thenReturn(player);
            when(deckGenerationStrategy.generateDeck()).thenReturn(new ArrayList<>(List.of(
                    new Card(Value.ACE, Colour.HEARTS),
                    new Card(Value.JACK, Colour.CLUBS),
                    new Card(Value.JACK, Colour.SPADES),
                    new Card(Value.ACE, Colour.DIAMONDS)
            )));

            Game game = gameService.startNewGame(playerId, betAmount);
            assertNotNull(game);
            assertEquals(player, game.getPlayer());
            assertEquals(2, game.getPlayer().getHand().size());
            assertEquals(2, game.getDealer().getHand().size());
            assertEquals(GameStatus.TIE, game.getStatus());
            assertEquals(BigDecimal.ZERO, player.getCurrentBet());
            assertEquals(0,new BigDecimal("1000.00").compareTo(player.getMoney()));
        }



    }
        @Nested
        @DisplayName("stand tests")
        class standTests{
        @Test
        @DisplayName("Should throw exception if there is no game with this id")
        void gameIsNull(){
            var exception = assertThrows(IllegalArgumentException.class, () -> gameService.stand(gameId));
            assertEquals("Game with id " + gameId + " not found", exception.getMessage());
        }

        @Test
        @DisplayName("Dealer should draw until at least 17")
        void dealerShouldDrawUntilAtLeast17(){
            when(playerService.getPlayer(playerId)).thenReturn(player);
            when(deckGenerationStrategy.generateDeck()).thenReturn(new ArrayList<>(List.of(
                    new Card(Value.TWO, Colour.HEARTS),
                    new Card(Value.TWO, Colour.HEARTS),
                    new Card(Value.TWO, Colour.HEARTS),
                    new Card(Value.TWO, Colour.HEARTS),
                    new Card(Value.TWO, Colour.HEARTS),
                    new Card(Value.TWO, Colour.HEARTS),
                    new Card(Value.TWO, Colour.HEARTS),
                    new Card(Value.TWO, Colour.HEARTS),
                    new Card(Value.TWO, Colour.HEARTS),
                    new Card(Value.TWO, Colour.HEARTS),
                    new Card(Value.TWO, Colour.HEARTS),
                    new Card(Value.THREE, Colour.HEARTS)
            )));
            var game = gameService.startNewGame(playerId, BigDecimal.TEN);
            game = gameService.stand(game.getId());
            assertNotEquals(GameStatus.IN_PROGRESS, game.getStatus());
            assertEquals(BigDecimal.ZERO, player.getCurrentBet());
            assertTrue(game.getDealer().getScore() >= 17);
            verify(playerService).getPlayer(playerId);
            verify(deckGenerationStrategy).generateDeck();
        }




    }







}