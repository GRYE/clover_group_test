package firsttask.fileutils

import scala.io.Source
import java.io._

import scala.util.Try

object FileUtils {

  def read(resourceName: String): List[String] =
    Source.fromInputStream(getClass.getClassLoader.getResourceAsStream(resourceName)).getLines().toList

  def readAs[T](resourceName: String)(implicit parser: StringParser[T]): List[T] =
    read(resourceName).map(parser.parse).flatMap(_.toOption)

  def write[T](fileName: String, objects: Seq[T]): Unit = {
    val pw = new PrintWriter(new File(fileName))
    try {
      objects.foreach(obj => pw.println(obj))
    } finally pw.close()
  }

}

trait StringParser[T] {
  def parse(s: String): Try[T]
}
