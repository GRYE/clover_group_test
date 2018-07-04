package firsttask.processing

import firsttask.model.{AggregatedMetric, MetricEntity}

import scala.collection.immutable.TreeMap

object MetricsProcessor {

  def process(metrics: Seq[MetricEntity], timeouts: Map[String, Long]): Seq[AggregatedMetric] = {
    val longestTimeout = timeouts.values.max
    val allKeys = timeouts.keySet

    val aggregated = metrics.foldLeft((Seq[AggregatedMetric](), Map[Long, AggregatedMetric]())) {
      case ((aggregated, halfAggregated), currentMetric) =>

        // делаем предположение, что в пришедших данных нет некорректных id датчиков
        val timeout = timeouts(currentMetric.idKey)

        // убираем все метрики, которые пришли слишком давно
        val halfProcessedWithoutOutdated = halfAggregated.filter { case (_, value) =>
          value.timestamp >= currentMetric.timestamp - longestTimeout
        }

        halfProcessedWithoutOutdated.find { case (_, metric) =>
            metric.timestamp >= currentMetric.timestamp - timeout
        } match {
          case Some((_, metric)) if metric.values.keySet + currentMetric.idKey == allKeys =>
            // если есть значения со всех сенсоров, то метрика готова
            val newAggregated = metric.copy(values = metric.values.updated(currentMetric.idKey, currentMetric.value))
            (aggregated :+ newAggregated,
              halfProcessedWithoutOutdated.filter( m => m._2 != metric))
          case Some((timestamp, metric)) =>
            // не все значения есть
            (aggregated, halfProcessedWithoutOutdated
              .updated(timestamp, metric.copy(values = metric.values.updated(currentMetric.idKey, currentMetric.value))))
          case None =>
            (aggregated, halfProcessedWithoutOutdated + (currentMetric.timestamp ->
              AggregatedMetric(
                currentMetric.timestamp,
                TreeMap((currentMetric.idKey, currentMetric.value)))
              )
            )
        }
    }

    aggregated match {
      case (aggregatedMetrics, _) => aggregatedMetrics
    }
  }

}
