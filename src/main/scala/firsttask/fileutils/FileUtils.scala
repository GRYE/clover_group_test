package firsttask.fileutils

import scala.io.Source
import java.io._

object FileUtils {

  def read(resourceName: String): List[String] =
    Source.fromInputStream(getClass.getClassLoader.getResourceAsStream(resourceName)).getLines().toList

  def write[T](fileName: String, objects: Seq[T]): Unit = {
    val pw = new PrintWriter(new File(fileName))
    try {
      objects.foreach(obj => pw.println(obj))
    } finally pw.close()
  }


}
