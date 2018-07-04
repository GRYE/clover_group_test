package firsttask

import firsttask.fileutils.FileUtils
import firsttask.model.MetricEntity
import firsttask.processing.MetricsProcessor
import firsttask.model.MetricEntity._

object Main extends App {

  val timeouts: Map[String, Long] = Map(
    "slow_sensor" -> 200,
    "fast_sensor" -> 100
  )

  val metrics = FileUtils.readAs[MetricEntity]("metrics.csv")

  print(MetricsProcessor.process(metrics, timeouts).toList)

}
