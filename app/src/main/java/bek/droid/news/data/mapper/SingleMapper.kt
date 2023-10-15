package bek.droid.news.data.mapper

fun interface SingleMapper<T, R> {
    operator fun invoke(value: T): R
}