/**
 * @description
 * @author      wwg
 * @date        2024/3/15 15:24
 */
enum class WheelEnum(val code: Int, val direction: String) {
    VERTICAL_DIRECTION(3, "VERTICAL"),
    HORIZONTAL_DIRECTION(4, "HORIZONTAL");

    companion object {
        fun getName(code: Int): String? {
            for (buttonEnum in values()) {
                if (buttonEnum.code == code) {
                    return buttonEnum.direction
                }
            }
            return null
        }
    }
}
