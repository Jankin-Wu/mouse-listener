import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.NativeHookException
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import com.github.kwhat.jnativehook.mouse.*
import java.awt.Dimension
import java.awt.Toolkit

class Main : NativeMouseListener, NativeMouseInputListener, NativeMouseWheelListener, NativeKeyListener {

    private var isListening by mutableStateOf(false)
    private var xCoordinate by mutableStateOf("")
    private var yCoordinate by mutableStateOf("")
    private var buttonCode by mutableStateOf(0)
    private var wheelAmount by mutableStateOf("")
    private var wheelRotation by mutableStateOf("")
    private var wheelDirection by mutableStateOf(0)

    @Composable
    fun App(windowWidth: Int) {
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current
        MaterialTheme {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                focusManager.clearFocus()
                            }
                        )
                    },
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = xCoordinate,
                        onValueChange = {},
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester),
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
                Spacer(modifier = Modifier.height(30.dp))
                ElevatedButton(
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color(0xFFbca0f8)
                    ),
                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 10.dp, focusedElevation = 5.dp, hoveredElevation = 5.dp),
                    shape = RoundedCornerShape(100),
                    onClick = {
                        if (!isListening) {
                            wheelAmount = ""
                            xCoordinate = ""
                            yCoordinate = ""
                            buttonCode = 0
                            wheelDirection = 0
                            wheelRotation = ""
                        }
                        isListening = !isListening
                        if (!isListening) {
                            xCoordinate = ""
                            yCoordinate = ""
                        }
                        focusManager.clearFocus()
                    },
                    modifier = Modifier
                        .height(60.dp)
                        .width(((windowWidth - 70)/3).dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    focusManager.clearFocus()
                                }
                            )
                        }
                        .align(Alignment.CenterHorizontally),
                    enabled = !isListening
                ) {
                    Text(
                        if (isListening) "Listening..." else "Click to listen",
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }
                Text(
                    if (isListening) "Press any key on the keyboard to stop" else "",
                    fontSize = 14.sp,
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

    override fun nativeKeyPressed(e: NativeKeyEvent) {
        if (isListening) {
            isListening = false
        }
    }
}

fun main() = application {
    var windowWidth by remember { mutableStateOf(0) }
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
        state = WindowState(width = 600.dp, height = 338.dp, position =  WindowPosition((getScreenWidth()/3).dp, (getScreenHeight()/3).dp)),
        resizable = false,
        alwaysOnTop = true
    ) {
        windowWidth = this.window.size.width
        app.App(windowWidth)
    }
}

fun getScreenWidth(): Int {
    val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
    return screenSize.width
}

fun getScreenHeight(): Int {
    val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
    return screenSize.height
}