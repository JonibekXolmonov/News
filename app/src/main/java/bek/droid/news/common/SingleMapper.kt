package bek.droid.news.common

fun interface SingleMapper<T, R> {
    operator fun invoke(value: T): R
}