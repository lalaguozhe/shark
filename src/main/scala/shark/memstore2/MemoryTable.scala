/*
 * Copyright (C) 2012 The Regents of The University California.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package shark.memstore2

import java.util.{HashMap => JavaHashMap}

import scala.collection.JavaConversions._
import scala.collection.mutable.Map

import org.apache.spark.rdd.RDD


/**
 * A container for table metadata specific to Shark and Spark. Currently, this is a lightweight
 * wrapper around either an RDD or multiple RDDs if the Shark table is Hive-partitioned.
 * Note that a Hive-partition of a table is different from an RDD partition. Each Hive-partition
 * is stored as a subdirectory of the table subdirectory in the warehouse directory
 * (e.g. /user/hive/warehouse). So, every Hive-Partition is loaded into Shark as an RDD.
 *
 * TODO(harvey): It could be useful to make MemoryTable a parent class, and have other table types,
 *               such as HivePartitionedTable or TachyonTable, subclass it. For now, there isn't
 *               too much metadata to track, so it should be okay to have a single MemoryTable.
 */
private[shark]
class MemoryTable(
    val tableName: String,
    val isHivePartitioned: Boolean) {

  // Should only be used if the table is not Hive-partitioned.
  var tableRDD: RDD[_] = _

  // CacheMode for the tableRDD.
  var cacheMode: CacheType.CacheType = _

  // Should only be used if a cached table is Hive-partitioned.
  var keyToHivePartitions: Map[String, RDD[_]] = _

  // Map from Hive-Partition key to the cache mode for its RDD.
  var keyToCacheModes: Map[String, CacheType.CacheType] = _
}
