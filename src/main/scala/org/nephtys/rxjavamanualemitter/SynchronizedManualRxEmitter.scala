package org.nephtys.rxjavamanualemitter

import rx.lang.scala.Observable

/**
  * Created by nephtys on 10/28/16.
  */
class SynchronizedManualRxEmitter[T] {
  private val innerEmitter = new ManualRxEmitter[T]

  def err(e : Throwable) = this.synchronized {
      innerEmitter.err(e)
  }
  def emit(t : T) = this.synchronized {
    innerEmitter.emit(t)
  }
  def complete() = this.synchronized {
    innerEmitter.complete()
  }

  def Observable : Observable[T] = innerEmitter.Observable
}
