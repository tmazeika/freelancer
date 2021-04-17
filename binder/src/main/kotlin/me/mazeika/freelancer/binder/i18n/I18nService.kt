package me.mazeika.freelancer.binder.i18n

import java.math.BigDecimal
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.util.*

interface I18nService {

    val availableCurrencies: List<Currency>
    val defaultCurrency: Currency
    val defaultLocale: Locale
    val defaultZone: ZoneId

    fun formatMoney(amount: BigDecimal, currency: Currency): String

    fun formatLongTime(instant: Instant): String

    fun formatDuration(duration: Duration): String

    fun formatDuration(from: Instant, to: Instant): String
}
