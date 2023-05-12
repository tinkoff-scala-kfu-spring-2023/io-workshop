package com.example

import cats.effect.IOApp
import cats.effect.{ExitCode, IO}
import cats.effect.std
// import java.net.UnknownHostException

object IOBasics {
  // Зачем вообще нужен IO?
  object Ex1 extends IOApp {

    def networkCall(address: String): Array[Byte] = ???

    override def run(args: List[String]): IO[ExitCode] = {
        val io = IO.delay(networkCall("231"))
        val io2 = IO.delay(networkCall(("123"))).as(ExitCode.Success)
        // val io3 = IO.pure(123)
        io.flatMap(_ => io2)
    }
  }
  
  def networkCallPure(address: String): IO[Array[Byte]] = ???

  // Обработка ошибок IO вычисленний
  object Ex2 extends IOApp {
    override def run(args: List[String]): IO[ExitCode] = 
        networkCallPure("123").as(ExitCode.Success)
  }

  // Алгоритм, который бы считывал данные из консоли и печатал их в консоль
  object Ex3Console extends IOApp {
    override def run(args: List[String]): IO[ExitCode] = 
        IO.readLine.flatMap(line => std.Console[IO].println(line)).as(ExitCode.Success)
  }
}
