package com.example

import cats.effect.IOApp
import cats.effect.IO
import cats.effect.ExitCode
import cats.effect.Resource
import java.io._
import java.util.Scanner

object Files {

  def openScanner(name: String): Resource[IO, Scanner] =
    Resource.make(
      IO.blocking {
        val file = new File(name)
        val inputStream = new FileInputStream(file)
        new Scanner(inputStream)
      }
    )(scanner => IO.blocking(scanner.close()))

  def openDestinationFile(name: String): Resource[IO, PrintWriter] =
    Resource.make(
      IO.blocking {
        new PrintWriter(new File(name))
      }
    )(printer => IO.blocking(printer.close()))

  // Используя блокирующее апи java.io._ написать алгоритм, который бы мог копировать данные из одного файла в другой
  object Ex1 extends IOApp {
    override def run(args: List[String]): IO[ExitCode] = {
      val r = for {
        input <- openScanner("input.txt")
        output <- openDestinationFile("output.txt")
      } yield (input, output)

      r.use { case (scanner, printer) =>
        IO.blocking {
          val line = scanner.nextLine()
          printer.print(line)
        }
      }.as(ExitCode.Success)
    }
  }

  // Модифицировать алгоритм, чтобы при возникновении ошибки при открытии первого файла вместо него пытался открыться запасной
  object Ex2 extends IOApp {
    override def run(args: List[String]): IO[ExitCode] = {
      openScanner("input.txt")
      openScanner("input.txt")
        .handleErrorWith[Scanner, Throwable](_ => openScanner("fallback.txt"))
        .both(openDestinationFile("output.txt"))
        .use { case (scanner, printer) =>
          IO.blocking {
            val line = scanner.nextLine()
            printer.print(line)
          }
        }
        .as(ExitCode.Success)
    }
  }
}
