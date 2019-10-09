package com.ysl.rxjava;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;

public class RxJavaTest {

    public static void main(String[] args) {
//        useRxJava();

//        createObserver();
//        operator();

        List<String> stringList = new ArrayList<>();
        stringList.add("ad");
        stringList.add("sdfsdf");
        stringList.add("sd23sdf");
        stringList.add("213");
//        List<String> stringList2 = new ArrayList<>();
//        for (String ss: stringList) {
//            Observable.just(ss)
//                    .filter(new Predicate<String>() {
//                        @Override
//                        public boolean test(String s) throws Exception {
//                            return s.contains("d");
//                        }
//                    }).subscribe(new Consumer<String>() {
//                @Override
//                public void accept(String s) throws Exception {
//                    System.out.println("onNext : filter : " + s);
//                    stringList2.add(s);
//                }
//            });
//        }
//        System.out.println(stringList2);


        Observable.just(stringList)
                .filter(new Predicate<List<String>>() {
                    @Override
                    public boolean test(List<String> strings) throws Exception {
                        return strings.contains("ad8");
                    }
                }).subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> strings) throws Exception {
                System.out.println(strings);
            }
        });

    }

    public static void useRxJava(){
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("hahaha");
                emitter.onNext("sdfaf");
                emitter.onComplete();
            }
        });
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("d:"+d);
            }

            @Override
            public void onNext(String s) {
                System.out.println("s:"+s);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("e:"+e);
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };
        observable.subscribe(observer);
    }
    /**
     * 简单使用方法
     */
    public static void createObserver(){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("hahads");
                emitter.onComplete();
            }
        }).subscribe(/*new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext : " + s );
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("onNext : onComplete" );
            }
        }*/
                new Subject<String>() {
                    @Override
                    public boolean hasObservers() {
                        return false;
                    }

                    @Override
                    public boolean hasThrowable() {
                        return false;
                    }

                    @Override
                    public boolean hasComplete() {
                        return false;
                    }

                    @Override
                    public Throwable getThrowable() {
                        return null;
                    }

                    @Override
                    protected void subscribeActual(Observer<? super String> observer) {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("onNext : " + s );
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onNext : onComplete" );
                    }
                });
    }

    /**
     * 操作符的使用
     */
    public static void operator() {

        //背压模式；是指在异步场景中，被观察者发送事件速度远快于观察者的处理速度的情况下，一种告诉上游的被观察者降低发送速度的策略。
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> emitter) throws Exception {
                emitter.onNext("1");
                emitter.onNext("2");
                emitter.onNext("3");
                emitter.onNext("4");
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("onNext : Flowable" + s);
                    }
                });



        //发射数据并匹配为integer类型；打印完整流程的日志
//        onNext : doOnSubscribe:io.reactivex.internal.operators.observable.ObservableDoOnEach$DoOnEachObserver@515f550a
//        onNext : subscribe:1
//        onNext : throwable:java.lang.ClassCastException: Cannot cast java.lang.String to java.lang.Integer
//        onNext : doOnTerminate
//        onNext : subscribe:java.lang.ClassCastException: Cannot cast java.lang.String to java.lang.Integer
        Observable.just(1,"2",3)
                .cast(Integer.class)
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("onNext : doOnComplete");
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onNext : throwable" + throwable);
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        System.out.println("onNext : doOnTerminate");
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        System.out.println("onNext : doOnSubscribe" + disposable);
                    }
                })
                .subscribe(new Consumer<Serializable>() {
                    @Override
                    public void accept(Serializable serializable) throws Exception {
                        System.out.println("onNext : subscribe" + serializable);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onNext : subscribe" + throwable);
                    }
                });


        //发射数据并匹配为integer类型；匹配失败后，打印错误日志；但不影响前面发射成功的数据
//        onNext : retryWhen : throwableObservable
//        onNext : retryWhen : 1
        Observable.just(1,"2",3)
                .cast(Integer.class)
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Observable<Throwable> throwableObservable) throws Exception {
                        return new ObservableSource<String>() {
                            @Override
                            public void subscribe(Observer<? super String> observer) {
                                System.out.println("onNext : retryWhen : throwableObservable");
                            }
                        };
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : retryWhen : " + integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onNext : retryWhen : " + throwable);
                    }
                });



        //发射数据并匹配为integer类型；匹配失败后，又重试两次；再失败抛出异常
//        onNext : retry : 1
//        onNext : retry : 1
//        onNext : retry : 1
//        onNext : retry : java.lang.ClassCastException: Cannot cast java.lang.String to java.lang.Integer
        Observable.just(1,"2",3)
                .cast(Integer.class)
                .retry(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : retry : " + integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println("onNext : retry : " + throwable);
                    }
                });


        //发射数据并匹配为integer类型；匹配失败后，发射一个错误码
//        onNext : onErrorReturn : 1
//        onNext : onErrorReturn : 100
        Observable.just(1,"2",3)
                .cast(Integer.class)
                .onErrorReturn(new Function<Throwable, Integer>() {
                    @Override
                    public Integer apply(Throwable throwable) throws Exception {
                        return 100;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : onErrorReturn : " + integer);
                    }
                });



        //发射数据并匹配为integer类型；匹配失败后，发射另一组数据
//        onNext : onErrorResumeNext : 1
//        onNext : onErrorResumeNext : 123
//        onNext : onErrorResumeNext : 22
//        onNext : onErrorResumeNext : 32
        Observable.just(1,"2",3)
                .cast(Integer.class)
                .onErrorResumeNext(Observable.just(123,22,32))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : onErrorResumeNext : " + integer);
                    }
                });



        //把数据分割成了Observable，每个Observable的数据个数为2
        Observable.just(2,3,5,6)
                .window(2)
                .subscribe(new Consumer<Observable<Integer>>() {
                    @Override
                    public void accept(Observable<Integer> integerObservable) throws Exception {
                        integerObservable.subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                System.out.println("onNext : window : " + integer);
                            }
                        });
                    }
                });

        //window与buffer区别：window是把数据分割成了Observable，buffer是把数据分割成List

        //把发射的数据以集合方式接收，并指定集合大小为2；
//        onNext : buffer : [2, 3]
//        onNext : buffer : [5, 6]
        Observable.just(2,3,5,6)
                .buffer(2)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        System.out.println("onNext : buffer : " + integers);
                    }
                });



        //对发射数据进行跟组处理：
//        onNext : groupBy : 偶数:2
//        onNext : groupBy : 奇数:3
//        onNext : groupBy : 奇数:5
//        onNext : groupBy : 偶数:6
        Observable.just(2,3,5,6)
                .groupBy(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return integer % 2 == 0 ? "偶数" : "奇数";
                    }
                })
                .subscribe(new Consumer<GroupedObservable<String, Integer>>() {
                    @Override
                    public void accept(final GroupedObservable<String, Integer> stringIntegerGroupedObservable) throws Exception {
                        stringIntegerGroupedObservable.subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                System.out.println("onNext : groupBy : " + stringIntegerGroupedObservable.getKey()+":"+integer.toString());
                            }
                        });
                    }
                });



        //默认添加了初始值-1；接收到前后两个相加的结果：-1，1，4，9
        Observable.just(2,3,5)
                .scan(-1,new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        return integer + integer2;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : scan : " + integer);
                    }
                });



        //对发射的数据进行前后相加；输出：2，5，10
        Observable.just(2,3,5)
                .scan(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        return integer + integer2;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : scan : " + integer);
                    }
                });



        //对发射数据进行一组处理
//        onNext : concatMap : 2
//        onNext : concatMap : 20
//        onNext : concatMap : 200
//        onNext : concatMap : 2000
        Observable.just(2,3,5)
                .concatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(final Integer integer) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                                emitter.onNext(integer*1+"");
                                emitter.onNext(integer*10+"");
                                emitter.onNext(integer*100+"");
                                emitter.onNext(integer*1000+"");
                                emitter.onComplete();
                            }
                        });
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("onNext : concatMap : " + s);
                    }
                });



        //对发射数据进行多次处理后返回；
//        onNext : flatMapIterable : 20
//        onNext : flatMapIterable : 200
//        onNext : flatMapIterable : 30
//        onNext : flatMapIterable : 300
//        onNext : flatMapIterable : 50
//        onNext : flatMapIterable : 500
        Observable.just(2,3,5)
                .flatMapIterable(new Function<Integer, List<String>>() {
                    @Override
                    public List<String> apply(final Integer integer) throws Exception {
                        return Arrays.asList(integer*10+"",integer*100+"");
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("onNext : flatMapIterable : " + s);
                    }
                });



        //对发射数据进行处理后再发射出去；
//        onNext : flatMap : 2
//        onNext : flatMap : 20
//        onNext : flatMap : 200
//        onNext : flatMap : 2000
        Observable.just(2,3,5)
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(final Integer integer) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                                emitter.onNext(integer*1+"");
                                emitter.onNext(integer*10+"");
                                emitter.onNext(integer*100+"");
                                emitter.onNext(integer*1000+"");
                                emitter.onComplete();
                            }
                        });
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("onNext : flatMap : " + s);
                    }
                });




        //发射数据匹配位integer类型
        Observable.just(2,8, 9, 3,4)
                .cast(Integer.class)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : cast : " + integer);
                    }
                });



        //发射int数据，使用string接收
        Observable.just(2,8, 9, 3,4)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "item:"+integer;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("onNext : map : " + s);
                    }
                });



        //发射数据并用map接收，onNext : toMultimap : {key2=[value2], key3=[value3], key4=[value4], key9=[value9], key8=[value8]}
        Observable.just(2,8, 9, 3,4)
                .toMultimap(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "key" + integer;
                    }
                }, new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "value" + integer;
                    }
                })
                .subscribe(new Consumer<Map<String, Collection<String>>>() {
                    @Override
                    public void accept(Map<String, Collection<String>> stringCollectionMap) throws Exception {
                        System.out.println("onNext : toMultimap : " + stringCollectionMap.toString());
                    }
                });



        //发射数据并以map形式接收onNext : toMap : {key2=value2, key3=value3, key4=value4, key9=value9, key8=value8}
        Observable.just(2,8, 9, 3,4)
                .toMap(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "key" + integer;
                    }
                }, new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "value"+integer;
                    }
                })
                .subscribe(
                        new Consumer<Map<String, String>>() {
                            @Override
                            public void accept(Map<String, String> stringStringMap) throws Exception {
                                System.out.println("onNext : toMap : " + stringStringMap.toString());
                            }
                        }
//                        new Consumer<Map<String, Integer>>() {
//                            @Override
//                            public void accept(Map<String, Integer> stringIntegerMap) throws Exception {
//                                System.out.println("onNext : toMap : " + stringIntegerMap.toString());
//                            }
//                        }
                );



        //把发射的数据变成list;并且进行了从大到小的排序：onNext : toList : [9, 8, 4, 3, 2]
        Observable.just(2,8, 9, 3,4)
                .toSortedList(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer integer, Integer t1) {
                        return t1 - integer;
                    }
                })
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        System.out.println("onNext : toList : " + integers.toString());
                    }
                });




        //发射的数据变成list；onNext : toList : [2, 3, 4]
        Observable.just(2,3,4)
                .toList()
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        System.out.println("onNext : toList : " + integers.toString());
                    }
                });



        //发射每个数据之前都会调用doOnNext；
        // onNext : doOnNext : 准备发射
        // onNext : doOnNext : 2
        // onNext : doOnNext : 准备发射
        // onNext : doOnNext : 3
        // onNext : doOnNext : 准备发射
        // onNext : doOnNext : 4
        Observable.just(2,3,4)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : doOnNext : 准备发射");
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : doOnNext : " + integer);
                    }
                });



        //接收到发射的个数，3个
        Observable.just(2,3,4)
                .count()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        System.out.println("onNext : count : " + aLong);
                    }
                });



        //把发射的数据变成一个数组；接收到的是：[2, 3, 4, 5]
        Observable.just(2,3,4,5)
                .collect(new Callable<List<Integer>>() {
                    @Override
                    public List<Integer> call() throws Exception {
                        return new ArrayList<Integer>();
                    }
                }, new BiConsumer<List<Integer>, Integer>() {
                    @Override
                    public void accept(List<Integer> integers, Integer integer) throws Exception {
                        integers.add(integer);
                    }
                })
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        System.out.println("onNext : collect : " + integers.toString());
                    }
                });



        //发射这几个数据的apply中是求和的运算；结果：14
        Observable.just(2,3,4,5)
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        return integer+integer2;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : reduce : " + integer);
                    }
                });


        //先忽略原有的发射数据，直到发射了一个数据（15）后，开始发射原来的数据：2，3，4，5
        Observable.just(2,3,4,5)
                .skipUntil(Observable.just(15))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : skipUntil : " + integer);
                    }
                });



        //舍弃原有的发射数据，直到满足大于等于10的数据开始发射，结果：14，5
        Observable.just(2,6,14,5)
                .skipWhile(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer < 10; //舍弃原Observable发射的数据，直到发射的数据>=10，才继续发射
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : skipWhile : " + integer);
                    }
                });



        //发射数据知道满足条件；（等于3时就停下了；发射的是2，3，4）
        Observable.just(2,3,4,5)
                .takeUntil(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer == 4;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : takeUntil : " + integer);
                    }
                });



        //只发射满足条件的数据，小于3的有2，3
        Observable.just(2,3,4,5)
                .takeWhile(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer <= 3;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : takeWhile : " + integer);
                    }
                });



        //如果发射的数据为空就发射2，3，4
        Observable.empty()
                .switchIfEmpty(Observable.just(2,3,4))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object integer) throws Exception {
                        System.out.println("onNext : switchIfEmpty : " + integer);
                    }
                });



        //传递两个或多个Observable给Amb时，它只发射其中首先发射数据或通知（onError或onCompleted）的那个Observable的所有数据，
        // 而其他所有的Observable的发射物将被丢弃。
        Observable.ambArray(
                Observable.just(1, 2, 3).delay(1, TimeUnit.SECONDS),//第一个Observable延迟1秒发射数据
                Observable.just(4, 5, 6))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : ambArray : " + integer);
                    }
                });



        //判断发射的数据是否为空
        Observable.just(5,6,4,8798)
                .isEmpty()
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        System.out.println("onNext : isEmpty : " + aBoolean);
                    }
                });



        //判断发射的两个序列是否相等；false
        Observable.sequenceEqual(Observable.just(2,4,5),Observable.just(2,3,4,5))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        System.out.println("onNext : sequenceEqual : " + aBoolean);
                    }
                });


        //判断发射的数据是否都满足某个条件（包含8），满足返回true，否则返回false
        Observable.just(7,8,65,23,42)
                .contains(8).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                System.out.println("onNext : contains : " + aBoolean);
            }
        });



        //判断发射的数据是否都满足某个条件（小于100），满足返回true，否则返回false
        Observable.just(7,8,65,23,42)
                .all(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer < 100;
                    }
                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                System.out.println("onNext : all : " + aBoolean);
            }
        });



        //相邻重复的多个数据只会发射一次
        Observable.just(3,4,5,6,3,3,4,9)
                .distinctUntilChanged()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : distinct : " + integer);
                    }
                });



        //发射去掉重复后的数据
        Observable.just(3,4,5,6,3,3,4,9)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : distinct : " + integer);
                    }
                });


        //跳过结尾的两个数据，再发射数据；30,4
        Observable.just(30,4,50,6)
                .skipLast(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : skipLast : " + integer);
                    }
                });



        //跳过开始的两个数据，再发射数据；50,6
        Observable.just(30,4,50,6)
                .skip(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : skip : " + integer);
                    }
                });



        //发射第一个数据30
        Observable.just(30,4,5,6)
                .first(0)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : first : " + integer);
                    }
                });



        //发射最后一个数据6
        Observable.just(3,4,5,6)
                .last(3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : last : " + integer);
                    }
                });



        //100毫秒后发射最后的两个数据；结果：7，8
        Observable.just(3,4,5,6,7,8)
                .takeLast(2)//发射前三个数据项
                .takeLast(100, TimeUnit.MILLISECONDS)//发射100ms内的数据
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : takeLast : " + integer);
                    }
                });



        //100毫秒后发射前三个数据；结果：3,4,5
        Observable.just(3,4,5,6,7,8)
                .take(3)//发射前三个数据项
                .take(100, TimeUnit.MILLISECONDS)//发射100ms内的数据
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : take : " + integer);
                    }
                });



        //发射String类型的数据；结果："w", "s"
        Observable.just(1,2,"w", "s")
                .ofType(String.class)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("onNext : ofType : " + s);
                    }
                });



        //过滤并发射小于10的数据；结果：7，8
        Observable.just(7,8,65,23,42)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer < 10;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                System.out.println("onNext : filter : " + integer);
            }
        });



        //先发射b,再发射a;结果："B1", "B2", "B3", "A1", "A2", "A3", "A4"
        String[] aStrings = {"A1", "A2", "A3", "A4"};
        String[] bStrings = {"B1", "B2", "B3"};
        Observable<String> aObservable = Observable.fromArray(aStrings);
        Observable<String> bObservable = Observable.fromArray(bStrings);
        Observable.merge(bObservable, aObservable)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("onNext : merge : " + s);
                    }
                });



        //合并observable3，observable4发射；结果：1，2，3，4，5，4，5，6
        Observable<Integer> observable3=Observable.just(1,2,3,4, 5);
        Observable<Integer> observable4=Observable.just(4,5,6);
        Observable.concat(observable3, observable4)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : concat : " + integer);
                    }
                });


        //在发射之前发射observable5；结果：104，105，106，1，2，3，4，5
        Observable<Integer> observable5=Observable.just(104,105,106);
        Observable.just(1,2,3,4,5)
                .startWith(observable5)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : startWith : " + integer);
                    }
                });



        //发射callable
        Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "hahahhah";
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("onNext : fromCallable : " + s);
            }
        });


        //顺延发射
        Observable.defer(new Callable<ObservableSource<String>>() {
            @Override
            public ObservableSource<String> call() throws Exception {
                return Observable.just("hello");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("onNext : defer : " + s);
            }
        });



        //发射数据，从2开始，向后的五个数据（2，3，4，5，6）
        Observable.range(2,5)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        System.out.println("onNext : range : " + integer);
                    }
                });


        //每隔1秒发射一次数据
        Observable.interval(1, TimeUnit.SECONDS, Schedulers.trampoline())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        System.out.println("onNext : interval : " + aLong);
                    }
                });
        //延迟1秒开始发射第一个数据，以后每个数据2秒发射一次
        Observable.interval(1, 2, TimeUnit.SECONDS, Schedulers.trampoline())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        System.out.println("onNext : interval : " + aLong);
                    }
                });
        //延迟1秒开始发射第一个数据，以后每个数据2秒发射一次
        //数据是从20开始的，包括20；发射5个
//        onNext : intervalRange : 20
//        onNext : intervalRange : 21
//        onNext : intervalRange : 22
//        onNext : intervalRange : 23
//        onNext : intervalRange : 24
        Observable.intervalRange(20, 5, 1, 2, TimeUnit.SECONDS, Schedulers.trampoline())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        System.out.println("onNext : intervalRange : " + aLong);
                    }
                });

        //延迟2秒发射
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        System.out.println("onNext : timer : " + aLong);
                    }
                });



        Observable.never();//创建一个什么都不做的Observable,直接调用onCompleted。

        Observable.error(new RuntimeException());//创建一个什么都不做直接通知错误的Observable,直接调用onError。这里可以自定义异常

        Observable.empty();//创建一个什么都不做直接通知完成的Observable



        //发射数组，一个一个元素来接收
        String[] names = new String[]{"a","b","c"};
        Observable.fromArray(names)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("onNext : fromArray : " + s + "\n");
                    }
                });


        //发射数组，收到数组
        Observable.just(names)
                .subscribe(new Consumer<String[]>() {
                    @Override
                    public void accept(String[] strings) throws Exception {
                        System.out.println("onNext : just : " + Arrays.toString(strings) + "\n");
                    }
                });


        //发射普通的数据
        Observable.just("ad", "bgf", "dsf")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println("onNext : just3 : " + s);
                    }
                });
    }
}
