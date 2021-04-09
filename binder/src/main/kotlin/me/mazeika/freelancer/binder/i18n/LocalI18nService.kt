package me.mazeika.freelancer.binder.i18n

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class LocalI18nService : I18nService {

    override val availableCurrencies: List<Currency> =
        Currency.getAvailableCurrencies().sortedBy { it.currencyCode }
    override val defaultLocale: Locale = Locale.getDefault()
    override val defaultCurrency: Currency = Currency.getInstance(defaultLocale)

    override fun formatMoney(amount: BigDecimal, currency: Currency): String =
        NumberFormat.getCurrencyInstance().let {
            it.currency = currency
            it.maximumFractionDigits = 32
            it.format(amount)
        }
}
