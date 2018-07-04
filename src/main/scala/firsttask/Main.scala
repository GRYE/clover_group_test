package firsttask

import firsttask.fileutils.FileUtils
import firsttask.model.MetricEntity
import firsttask.processing.MetricsProcessor

object Main extends App {

  val timeouts: Map[String, Long] = Map(
    "slow_sensor" -> 200,
    "fast_sensor" -> 100
  )

  val metrics = FileUtils.read("metrics.csv")
    .map(MetricEntity.apply(_))
    .flatMap(_.toOption)

  print(MetricsProcessor.process(metrics, timeouts).toList)

}
