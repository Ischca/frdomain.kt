package arrowio.repository

import arrow.core.*
import arrow.fx.IO
import arrowio.ErrorOr
import arrowio.model.Account
import arrowio.model.Balance
import java.time.LocalDate

interface AccountRepository {
    fun query(no: String): IO<ErrorOr<Option<Account>>>
    fun store(a: Account): IO<ErrorOr<Account>>
    fun query(openedOn: LocalDate): IO<ErrorOr<Sequence<Account>>>
    fun all(): IO<ErrorOr<Sequence<Account>>>

    fun balance(no: String): IO<ErrorOr<Balance>> = query(no)
        .map {
            when (it) {
                is Either.Right -> when (val ao = it.b) {
                    is Some -> ao.t.balance.right()
                    is None -> NonEmptyList.of("No account exists with $no").left()
                }
                is Either.Left -> it
            }
        }
}