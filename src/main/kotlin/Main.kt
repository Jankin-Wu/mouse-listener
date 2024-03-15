import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.NativeHookException
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import com.github.kwhat.jnativehook.mouse.*

class Main : NativeMouseListener, NativeMouseInputListener, NativeMouseWheelListener, NativeKeyListener {

    private var isListening by mutableStateOf(false)
    private var xCoordinate by mutableStateOf("")
    private var yCoordinate by mutableStateOf("")
    private var buttonName by mutableStateOf("")
    private var buttonCode by mutableStateOf(0)
    private var wheelAmount by mutableStateOf("")
    private var wheelRotation by mutableStateOf("")
    private var wheelDirection by mutableStateOf(0)

    @Composable
    fun App() {
        MaterialTheme {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = xCoordinate,
                        onValueChange = {},
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        readOnly = true,
                        label = { Text("X") }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = yCoordinate,
                        onValueChange = {},
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        readOnly = true,
                        label = { Text("Y") }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    (if (buttonCode == 0) "" else ButtonEnum.getName(buttonCode))?.let {
                        OutlinedTextField(
                            value = it,
                            onValueChange = {},
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            readOnly = true,
                            label = { Text("Button Name") }
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = wheelAmount,
                        onValueChange = {},
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        readOnly = true,
                        label = { Text("Wheel Amount") }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = wheelRotation,
                        onValueChange = {},
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        readOnly = true,
                        label = { Text("Wheel Rotation") }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    (if (wheelDirection == 0) "" else WheelEnum.getName(wheelDirection))?.let {
                        OutlinedTextField(
                            value = it,
                            onValueChange = {},
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            readOnly = true,
                            label = { Text("Wheel Direction") }
                        )
                    }
                }

                Button(
                    onClick = {
                        if (!isListening) {
                            wheelAmount = ""
                        }
                        isListening = !isListening
                        if (!isListening) {
                            xCoordinate = ""
                            yCoordinate = ""
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    enabled = !isListening
                ) {
                    Text(if (isListening) "Listening..." else "Click to listen")
                }
                Text(
                    if (isListening) "Press any key on the keyboard to stop" else "",
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }

    override fun nativeMouseClicked(e: NativeMouseEvent) {
        if (isListening) {
            xCoordinate = "${e.x}"
            yCoordinate = "${e.y}"
//            buttonCode = e.button
//            isListening = false
        }
    }

    override fun nativeMousePressed(e: NativeMouseEvent) {
        if (isListening) {
            buttonCode = e.button
        }
    }
    override fun nativeMouseReleased(e: NativeMouseEvent) = Unit

    override fun nativeMouseMoved(e: NativeMouseEvent) {
        if (isListening) {
            xCoordinate = "${e.x}"
            yCoordinate = "${e.y}"
        }
    }

    override fun nativeMouseDragged(e: NativeMouseEvent) = Unit

    override fun nativeMouseWheelMoved(e: NativeMouseWheelEvent) {
        if (isListening) {
            if (wheelAmount == "") {
                wheelAmount = "0"
            }
            if (e.wheelRotation > 0) {
                wheelAmount = (wheelAmount.toInt() + e.scrollAmount).toString()
                wheelRotation = "DOWN"
            } else {
                wheelAmount = (wheelAmount.toInt() - e.scrollAmount).toString()
                wheelRotation = "UP"
            }
            wheelDirection = e.wheelDirection
//            if (wheelAmount.toInt() > 0) {
//                wheelRotation = "DOWN";
//            } else if (wheelAmount.toInt() < 0) {
//                wheelRotation = "UP";
//            }
        }
    }

    override fun nativeKeyPressed(e: NativeKeyEvent?) {
        if (isListening) {
            isListening = false
        }
    }
}


fun main() = application {
    try {
        GlobalScreen.registerNativeHook()
    } catch (e: NativeHookException) {
        e.printStackTrace()
    }

    val app = Main()
    GlobalScreen.addNativeMouseListener(app)
    GlobalScreen.addNativeMouseMotionListener(app)
    GlobalScreen.addNativeMouseWheelListener(app)
    GlobalScreen.addNativeKeyListener(app)

    Window(
        title = "MouseListener",
        onCloseRequest = ::exitApplication,
        icon = painterResource("img/mouse_1.png"),
        state = WindowState(width = 600.dp, height = 290.dp),
        alwaysOnTop = true
    ) {
        app.App()
    }
}