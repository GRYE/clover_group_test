package firsttask.model

import firsttask.fileutils.StringParser

import scala.collection.immutable.TreeMap
import scala.util.Try

case class MetricEntity(timestamp: Long, idKey: String, value: Int)

object MetricEntity {

  def apply(csvString: String, separator: Char = ','): Try[MetricEntity] = Try {
    val splitted = csvString.filter(_ != '"').split(separator)
    MetricEntity(splitted(0).toLong, splitted(1), splitted(2).toInt)
  }

  implicit val parser: StringParser[MetricEntity] = apply(_)

}

// Исользуем TreeMap, чтобы при печати значения были отсортированы по названию сенсора
case class AggregatedMetric(timestamp: Long, values: TreeMap[String, Int]) {
  override def toString: String = s"$timestamp,${values.values.mkString(",")}"
}

