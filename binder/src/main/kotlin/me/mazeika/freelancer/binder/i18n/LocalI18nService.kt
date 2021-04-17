package me.mazeika.freelancer.binder.i18n

import java.math.BigDecimal
import java.text.NumberFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

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

    override fun formatTime(instant: Instant): String =
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
            .withLocale(defaultLocale)
            .withZone(defaultZone)
            .format(instant)
}
