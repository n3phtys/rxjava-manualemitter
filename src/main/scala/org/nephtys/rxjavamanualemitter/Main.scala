package org.nephtys.rxjavamanualemitter

import scala.concurrent.Future
import concurrent.ExecutionContext.Implicits.global

/**
  * Created by nephtys on 10/28/16.
  */
object Main extends App {

  val emitter = new SynchronizedManualRxEmitter[Int]

  emitter.Observable.subscribe(i => println(i))

  for (k <- 1 to 10) {
    Future {
      emitter.Observable.subscribe(i => println(i))
    }
}

  for (k <- 1 to 10) {
    Future {
      emitter.emit(k)
    }
  }


  Thread.sleep(100)

}
