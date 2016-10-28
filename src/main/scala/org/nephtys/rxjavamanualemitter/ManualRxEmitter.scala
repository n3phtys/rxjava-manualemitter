package org.nephtys.rxjavamanualemitter

import java.util.concurrent.atomic.AtomicBoolean

import rx.lang.scala.{Observable, Subscriber, Subscription}

import scala.collection.parallel.mutable.{ParHashMap, ParHashSet}
import rx.lang.scala

/**
  *  NOT THREADSAFE
  * Created by nephtys on 10/28/16.
  */
class ManualRxEmitter[T] {

  private val hashmap : ParHashSet[Subscriber[T]] = ParHashSet.empty[Subscriber[T]]

  private val completed = new AtomicBoolean(false)
  private val errored = new AtomicBoolean(false)

  /**
    * NOT THREADSAFE!!!!
    * calls onNext() for each subscriber
    * @param t
    */
  def emit(t : T) = {
    if (!completed.get() && !errored.get()) {
      hashmap.foreach(sub => if(!sub.isUnsubscribed) {
        sub.onNext(t)
      })
    }
  }

  def complete() = {
    if (!completed.get() && !errored.get()) {
      hashmap.foreach(sub => if(!sub.isUnsubscribed) {
        sub.onCompleted()
      })
      completed.set(true)
      hashmap.clear()
    }
  }

  def err(e : Throwable) = {
    if (!completed.get() && !errored.get()) {
      hashmap.foreach(sub => if(!sub.isUnsubscribed) {
        sub.onError(e)
      })
      errored.set(true)
      hashmap.clear()
    }
  }



  private def subscribefunc(subscriber : Subscriber[T]) : Unit = {
    hashmap.+=(subscriber)
  }

  val Observable : Observable[T] = scala.Observable.apply[T](subscriber => subscribefunc(subscriber))

}
