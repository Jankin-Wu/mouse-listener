/**
 * @description
 * @author      wwg
 * @date        2024/3/15 15:24
 */
enum class ButtonEnum(val code: Int, val buttonName: String) {
    NOBUTTON(0, "NOBUTTON"),
    BUTTON1(1, "LEFT"),
    BUTTON2(2, "RIGHT"),
    BUTTON3(3, "MIDDLE"),
    BUTTON4(4, "SIDE_BACK"),
    BUTTON5(5, "SIDE_FORWARD");

    companion object {
        fun getName(code: Int): String? {
            for (buttonEnum in values()) {
                if (buttonEnum.code == code) {
                    return buttonEnum.buttonName
                }
            }
            return null
        }
    }
}
