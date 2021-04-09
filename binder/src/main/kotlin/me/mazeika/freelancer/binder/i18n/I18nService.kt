package me.mazeika.freelancer.binder.i18n

import java.math.BigDecimal
import java.util.*

interface I18nService {

    val availableCurrencies: List<Currency>
    val defaultCurrency: Currency
    val defaultLocale: Locale

    fun formatMoney(amount: BigDecimal, currency: Currency): String
}
