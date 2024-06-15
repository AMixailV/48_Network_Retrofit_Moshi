package ru.mixail_akulov.a47_network_retrofit_moshi.sources

fun <T, R> Collection<T>.fold(
    initial: R,
    combine: (acc: R, nextElement: T) -> R
): R {
    var accumulator: R = initial
    for (element: T in this) {
        accumulator = combine(accumulator, element)
    }
    return accumulator
}

fun main() {
    val items = listOf(1, 2, 3, 4, 5)

    // Лямбда - это блок кода, заключенный в фигурные скобки.
    items.fold(0, {
        // Если у лямбды есть параметры, то они указываются перед знаком '->'
            acc: Int, i: Int ->
        print("acc = $acc, i = $i, ")
        val result = acc + i
        println("result = $result")
        // Последнее выражение в лямбде считается возвращаемым значением:
        result
    })

    // Типы параметров в лямбде необязательны, если они могут быть выведены:
    val joinedToString = items.fold("Elements:", { acc, i -> acc + " " + i + 0})

    // Ссылки на функции также могут использоваться для вызовов функций высшего порядка:
    val product = items.fold(1, Int::times)

    println("joinedToString = $joinedToString")
    println("product = $product")
}