package com.example

import cats.effect._
// import cats.effect.implicits._
import cats.implicits._
import java.util.concurrent.TimeoutException
import scala.concurrent.duration._

object Fibers {
  // написать IO, который бы принимал на вход список строк и печатал все строки в консоль в параллель
  object Ex1 extends IOApp {
    override def run(args: List[String]): IO[ExitCode] =
      List("kek", "shrek")
        .parTraverse_(str => IO.println(str))
        .as(ExitCode.Success)
  }

  def networkCallPure(address: String): IO[Array[Byte]] = ???

  // написать IO, который бы дожидался результата выполнения двух вычислений. Если одно из них завершилось с ошибкой, то второе должно быть отменено
  object Ex2 extends IOApp {
    override def run(args: List[String]): IO[ExitCode] =
      IO.both(networkCallPure("123"), networkCallPure("321"))
        .as(ExitCode.Success)
  }

  // написать синтаксическое расширение, чтобы навешивать таймаут на IO
  // и возвращать либо результат до таймаута, либо ошибку таймаута, либо ошибку вычисления
  object Ex3 extends IOApp {
    implicit class TimeoutOps[A](io: IO[A]) {
      def timeoutIO(mill: Long): IO[Either[TimeoutException, A]] =
        IO.race(
          IO.sleep(mill.millis).as(new TimeoutException),
          io
        )
    }

    override def run(args: List[String]): IO[ExitCode] =
      networkCallPure("123").timeoutIO(123).as(ExitCode.Success)
      
      // networkCallPure("123").timeout(123.millis).as(ExitCode.Success) таймаут уже в принципе реализован в IO)
  }
}
