package secondtask

// 1. По-моему лучше использовать case class'ы без состояния. Состояние хранить в каком-то персистентном хранилище/кэше
// Использование  case class'а так же сильно сокращает запись, не нужно определять методы eqv, getBalance, getOverdraft
// 2. Непонятно, зачем в исходном варианте поле называется maxOverdraft, если проверки на превышение овердрафта нет
// 3. Обычно для операций с деньгами используют длинную арифметику

case class BankAccount(balance: BigDecimal, overdraft: BigDecimal) {

  // Вместо изменения состояния аккаунта создаем новый инстанс
  def withdraw(amount: Long): BankAccount =
    if (amount > balance) this.copy(balance = 0, overdraft = this.overdraft - balance - amount)
    else this.copy(balance = this.balance - amount)

  // Аналогично предыдущему методу
  def deposit(amount: Long): BankAccount = this.copy(balance = this.balance + amount)
}

object Main extends App {
  val account1 = BankAccount(0, 100)
  val account2 = BankAccount(0, 100)

  val newAccount1 = account1
    .withdraw(30)
    .withdraw(20)
    .deposit(100)

  println(newAccount1.balance)
  println(newAccount1 == account2)

}

