import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import recursos.SieteYMedia

@Composable
@Preview
fun app() {
    var juego by remember { mutableStateOf(SieteYMedia()) } // El juego se reinicia solo al inicio de un nuevo juego
    val (estadoJugador, setEstadoJugador) = remember { mutableStateOf("") }
    val (estadoBanca, setEstadoBanca) = remember { mutableStateOf("") }
    val (resultado, setResultado) = remember { mutableStateOf("") }

    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Bienvenido al juego Siete y Media")

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    if (resultado.isNotEmpty()) {
                        // Solo reinicia al inicio de un nuevo juego
                        juego = SieteYMedia()
                        setEstadoJugador("")
                        setEstadoBanca("")
                        setResultado("")
                    }
                    juego.pedirCartaJugador()
                    setEstadoJugador(
                        "Cartas Jugador: ${
                            juego.getCartasJugador().filterNotNull().joinToString { it.toString() }
                        } (Valor: ${juego.valorCartasJugador()})"
                    )
                    when {
                        juego.valorCartasJugador() > 7.5 -> setResultado("¡Perdiste! Te pasaste de 7.5")
                        juego.valorCartasJugador() == 7.5 -> {
                            juego.turnoBanca()
                            setEstadoBanca(
                                "Cartas Banca: ${
                                    juego.getCartasBanca().filterNotNull().joinToString { it.toString() }
                                } (Valor: ${juego.valorCartasBanca()})"
                            )
                            when {
                                juego.valorCartasBanca() > 7.5 -> setResultado("¡Ganaste! La banca se pasó.")
                                juego.valorCartasBanca() >= 7.5 -> setResultado("¡Perdiste! La banca te superó.")
                                else -> setResultado("¡Ganaste!")
                            }
                        }
                    }
                }) {
                    Text("Pedir Carta")
                }

                Button(onClick = {
                    if (resultado.isEmpty()) { // Solo permite finalizar si no hay resultado
                        juego.turnoBanca()
                        setEstadoBanca(
                            "Cartas Banca: ${
                                juego.getCartasBanca().filterNotNull().joinToString { it.toString() }
                            } (Valor: ${juego.valorCartasBanca()})"
                        )
                        when {
                            juego.valorCartasBanca() > 7.5 -> setResultado("¡Ganaste! La banca se pasó.")
                            juego.valorCartasBanca() >= juego.valorCartasJugador() -> setResultado("¡Perdiste! La banca te superó.")
                            else -> setResultado("¡Ganaste!")
                        }
                    }
                }) {
                    Text("Finalizar Turno")
                }
            }

            Text(estadoJugador)
            Text(estadoBanca)
            Text(resultado)
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Siete y Media") {
        app()
    }
}
