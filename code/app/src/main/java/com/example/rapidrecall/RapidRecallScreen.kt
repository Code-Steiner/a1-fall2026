package com.example.rapidrecall

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun RapidRecallScreen (
    modifier: Modifier
) {

    // Remembered variables that survive recomposition are mostly what is displayed on screen
    var sequenceLength by remember { mutableStateOf("") }
    var inGame by remember {mutableStateOf(value = false)}
    var currentDigit: Char by remember {mutableStateOf('\u0000')}
    var playerGuess by remember {mutableStateOf("")}
    var displaySequence by remember {mutableStateOf(false)}
    var gameResult by remember {mutableStateOf("")}
    var answer by remember {mutableStateOf("")}
    var playerResult by remember {mutableStateOf("")}
    var animationTracker by remember { mutableIntStateOf(0) }

    Column (
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Text field that requests player input on digit number
        TextField(
            value = sequenceLength,
            onValueChange = {sequenceLength = it},
            label = {Text("Input number of digits")}
        )

        // Button that starts a new round by making inGame true
        Button(onClick = {

            inGame = !inGame

            // When player starts a new round, reset all display text
            gameResult = ""
            playerGuess = ""
            answer = ""
            playerResult = ""

        }) {Text("Generate Sequence") }

        // Spacers make the UI feel less cluttered
        Spacer(modifier = Modifier.height(100.dp))

        // Mainly for flavor, but helps identify when the numbers are switching

        AnimatedContent(
            targetState = currentDigit,
            label = "animated content",
            transitionSpec = {
                if (targetState > initialState) {
                    slideInVertically() togetherWith slideOutVertically()
                }
                else {
                    fadeIn() togetherWith fadeOut()
                }
            }
        ) {
                currentDigit ->
            Text(
                currentDigit.toString(),
                fontSize = 200.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Displays the result of the player's guess
        Text(
            text = gameResult,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(50.dp))

        // Displays how the player's guess compares to the actual sequence
        Text(
            text = playerResult,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(100.dp))

        if (inGame) {

            // When player starts a new round, create Sequence object to get the sequence
            val gameSequence : Sequence = remember(sequenceLength.toDouble()) {
                Sequence(sequenceLength.toDouble())}
            val intString : String = gameSequence.newSequence + " "
            var currentIndex = 0

            // Displaying the numbers on the screen one after the other
            LaunchedEffect(displaySequence) {
                while (currentDigit != ' ') {
                    currentDigit = intString[currentIndex]
                    currentIndex += 1
                    delay(1500.milliseconds) // A slight delay to make the game doable
                }
            }


            Row(modifier = modifier.fillMaxWidth()) {
                TextField(
                    value = playerGuess,
                    onValueChange = { playerGuess = it },
                    label = { Text("Type Guess") }
                )

                Button(
                    modifier = modifier.weight(2f),
                    onClick = {

                        // Once player makes a guess, starts resetting display text for next round
                        inGame = !inGame
                        displaySequence = !displaySequence
                        currentDigit = '\u0000'

                        // Check if the player made a correct guess
                        if (playerGuess == gameSequence.newSequence) {
                            gameResult = "Nice one!"
                            answer = gameSequence.newSequence
                            playerResult = "Your Answer: $playerGuess\nActual Sequence: $answer"
                        }
                        else {
                            gameResult = "Too bad..."
                            answer = gameSequence.newSequence
                            playerResult = "Your Answer: $playerGuess\nActual Sequence: $answer"
                        }

                    }) {Text("Enter Guess") }
            }

        }

    }

}