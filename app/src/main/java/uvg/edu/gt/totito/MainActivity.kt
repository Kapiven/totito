package uvg.edu.gt.totito

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeApp()
        }
    }
}

@Composable
fun TicTacToeApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "start") {
        composable("start") { StartScreen(navController) }
        composable("game/{player1}/{player2}/{boardSize}") { backStackEntry ->
            val player1 = backStackEntry.arguments?.getString("player1") ?: ""
            val player2 = backStackEntry.arguments?.getString("player2") ?: ""
            val boardSize = backStackEntry.arguments?.getString("boardSize")?.toInt() ?: 3
            GameScreen(player1, player2, boardSize)
        }
    }
}

@Composable
fun StartScreen(navController: NavHostController) {
    var player1Name by remember { mutableStateOf("") }
    var player2Name by remember { mutableStateOf("") }
    var boardSize by remember { mutableIntStateOf(3) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = "Board Size Image",
            modifier = Modifier.size(300.dp)
        )
        TextField(
            value = player1Name,
            onValueChange = { player1Name = it },
            label = { Text("Nombre del Jugador 1") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = player2Name,
            onValueChange = { player2Name = it },
            label = { Text("Nombre del Jugador 2") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        BoardSizeSelector(onSizeChange = { boardSize = it })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            navController.navigate("game/$player1Name/$player2Name/$boardSize")
        }) {
            Text("Iniciar Juego")
        }
    }
}

@Composable
fun BoardSizeSelector(onSizeChange: (Int) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Selecciona el tamaño del tablero", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Row {
            Button(onClick = { onSizeChange(3) }) {
                Text("3x3")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onSizeChange(4) }) {
                Text("4x4")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onSizeChange(5) }) {
                Text("5x5")
            }
        }
    }
}

@Composable
fun GameScreen(player1: String, player2: String, boardSize: Int) {
    val board by remember { mutableStateOf(Array(boardSize) { Array(boardSize) { "" } }) }
    var currentPlayer by remember { mutableStateOf(if (Random.nextBoolean()) "X" else "O") }
    val currentPlayerName = if (currentPlayer == "X") player1 else player2

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Jugador actual: $currentPlayerName", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        TicTacToeBoard(board, onCellClick = { row, col ->
            if (board[row][col].isEmpty()) {
                board[row][col] = currentPlayer
                currentPlayer = if (currentPlayer == "X") "O" else "X"
            }
        })
    }
}

@Composable
fun TicTacToeBoard(board: Array<Array<String>>, onCellClick: (Int, Int) -> Unit) {
    Column {
        for (row in board.indices) {
            Row {
                for (col in board[row].indices) {
                    TicTacToeCell(board[row][col]) {
                        onCellClick(row, col)
                    }
                }
            }
        }
    }
}

@Composable
fun TicTacToeCell(symbol: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = symbol, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTicTacToeApp() {
    TicTacToeApp()
}
