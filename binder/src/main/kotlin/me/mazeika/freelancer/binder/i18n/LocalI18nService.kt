package me.mazeika.freelancer.binder.i18n

import java.math.BigDecimal
import java.text.NumberFormat
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.abs

class LocalI18nService : I18nService {

    override val availableCurrencies: List<Currency> =
        Currency.getAvailableCurrencies().sortedBy { it.currencyCode }
    override val defaultLocale: Locale = Locale.getDefault()
    override val defaultCurrency: Currency = Currency.getInstance(defaultLocale)
    override val defaultZone: ZoneId = ZoneId.systemDefault()

    override fun formatMoney(amount: BigDecimal, currency: Currency): String =
        NumberFormat.getCurrencyInstance(defaultLocale).let {
            it.currency = currency
            it.maximumFractionDigits = 32
            it.format(amount)
        }

    override fun formatLongTime(instant: Instant): String =
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
            .withLocale(defaultLocale)
            .withZone(defaultZone)
            .format(instant)

    override fun formatDuration(duration: Duration): String {
        val seconds = abs(duration.toSeconds())
        return String.format(
            locale = defaultLocale,
            format = "%s%d:%02d:%02d",
            if (duration.isNegative) "-" else "",
            seconds / 3600,
            (seconds % 3600) / 60,
            seconds % 60
        )
    }

    override fun formatDuration(from: Instant, to: Instant): String =
        formatDuration(Duration.ofSeconds(from.until(to, ChronoUnit.SECONDS)))
}
