package com.hqucsx.rxjavatest;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hqucsx.rxjavatest.data.DataFactory;
import com.hqucsx.rxjavatest.model.Course;
import com.hqucsx.rxjavatest.model.Student;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.et_message)
    AppCompatEditText mEtMessage;
    @BindView(R.id.btn_message)
    AppCompatButton mBtnMessage;
    @BindView(R.id.tv_message)
    TextView mTvMessage;
    @BindView(R.id.iv_message)
    AppCompatImageView mIvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        rxBindingTest10();
    }

    @OnClick(R.id.btn_message)
    public void onClick() {
//        rxTest5();
        Logger.i("开始使用RxJava");
        rxBindingTest9();
    }

    /**
     * 基本概念
     * 被观察者Observables（事件源）：触发事件
     * 观察者Observer(Subscriber，Observer的抽象实现类)：事件回调
     * subscribe：订阅，Observables和Observer通过subscribe()方法实现订阅关系，从而在Observables发出事件的时候通知Observer
     * RxJava使用的是被动观察，即在被观察者有事件发生时通知观察者对事件进行处理
     */
    private void rxTest1() {
        /**
         * 创建事件源
         * 这里传入了一个OnSubscribe对象作为参数，继承自Action
         * OnSubscribe会被存储在返回的Observable对象中，她的作用相当于一个计划表
         * 当Observable被订阅的时候，OnSubscribe的方法call就会被调用，事件序列就会按照设定依次触发
         *
         * create()方法为最基本的创建事件序列的方法
         */
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("你好");
                subscriber.onNext("，我是RxJava");
                subscriber.onNext("，欢迎使用！");
                subscriber.onError(new Throwable("哈哈哈哈哈"));
                subscriber.onCompleted();
            }
        });
        /**
         * 创建观察者，回调事件源发出的事件
         */
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Logger.i("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                mTvMessage.append(s);
                Logger.i(s);
            }
        };
        /**
         * 订阅，需要在onStop或者onDestroy的时候解除订阅
         */
        observable.subscribe(observer);

    }

    /**
     * create()仅仅是最基本的创建事件序列的方法
     * 下面
     */
    private void rxTest2() {
        /**
         * 创建事件序列
         */
        Observable<String> observable = Observable.just("你好，", "我是RxJava,", "欢迎使用！");
        /**
         *依次执行顺序为
         * subscribe.onNext("你好，");
         * subscribe.onNext("我是RxJava，");
         * subscribe.onNext("欢迎使用");
         * subscribe.onCompleted();
         */


        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Logger.i("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Logger.e(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                mTvMessage.append(s);
                //程序出现异常则调用onError()方法
//                Integer.valueOf("我草");
            }
        };
        observable.subscribe(observer);
    }


    /**
     * 对观察者的家暖
     */
    private void rxTest3() {
        Observable<String> observable = Observable.just("Hello ,", "I am RxJava", "nice to meet you!");
        /**
         * 单个参数的Action方法
         */
        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                mTvMessage.append(s);
//                Integer.valueOf("我草");
            }
        };
        /**
         *
         */
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Logger.e(throwable.getMessage());
            }
        };

        Action0 onCompletedAction = new Action0() {
            @Override
            public void call() {
                Logger.i("onCompleted");
            }
        };

        /**
         * 不完全定义方式订阅回调
         * 自动创建Subscribe，并使用onNextAction,onErrorAction,onCompletedAction分别来定义onNext(),onError(),onCompleted()
         */
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);
    }

    /**
     * Scheduler线程控制
     */
    private void rxTest4() {
        /**
         * 在io线程读取Drawable
         * 在主线程显示Drawable
         * subscribeOn()只执行一次(第一次)
         * observeOn()可执行多次
         */
//        Observable.create(new Observable.OnSubscribe<Drawable>() {
//            @Override
//            public void call(Subscriber<? super Drawable> subscriber) {
//                Drawable drawable = ContextCompat.getDrawable(MainActivity.this, R.mipmap.ic_launcher);
//                subscriber.onNext(drawable);
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Drawable>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(Drawable drawable) {
//                        mIvMessage.setImageDrawable(drawable);
//                    }
//                });
        /**
         * 使用just+不完全定义的方式简写
         */
        Observable.just(ContextCompat.getDrawable(this, R.mipmap.ic_launcher))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Drawable>() {
                    @Override
                    public void call(Drawable drawable) {
                        mIvMessage.setBackgroundDrawable(drawable);
                    }
                });
    }

    /**
     * 变化map,通过map操作将图片id直接作为参数传入
     */
    private void rxTest5() {
        /**
         * 使用map变换()
         */
        Observable.just(R.mipmap.ic_launcher)
                .map(new Func1<Integer, Drawable>() {
                    @Override
                    public Drawable call(Integer integer) {
                        return ContextCompat.getDrawable(MainActivity.this, integer);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Drawable>() {
                    @Override
                    public void call(Drawable drawable) {
                        mIvMessage.setBackgroundDrawable(drawable);
                    }
                });
    }

    /**
     * 实例练习,获取学生的名字
     */
    private void rxTest6() {
        Observable.from(DataFactory.getData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Student>() {
                    @Override
                    public void call(Student student) {
                        mTvMessage.append(student.toString() + "\n");
                    }
                });
    }

    /**
     * 使用map变换获取学生的的姓名
     */
    private void rxTest7() {
        Observable.from(DataFactory.getData())
                .map(new Func1<Student, String>() {
                    @Override
                    public String call(Student student) {
                        return student.name;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mTvMessage.append(s + "\n");
                    }
                });

    }

    /**
     * 使用flatMap变换获取学生的课程的名称
     */
    private void rxTest8() {
        Observable.from(DataFactory.getData())
                .subscribeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(Student student) {
                        mTvMessage.append(student.name + "\n");
                        return Observable.from(student.courses);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Course, String>() {
                    @Override
                    public String call(Course course) {
                        return course.name;
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        mTvMessage.append(s + "\n");
                    }
                });

    }

    /**
     * 引入RxBinding-------------------------------------------------
     */
    /**
     * 引入RxBinding,防抖动,即多次点击
     */
    private void rxBindingTest9(){
        RxView.clicks(mIvMessage)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Toast.makeText(MainActivity.this,"哈哈哈",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 每一秒监听EditText的内容变换,并作出响应
     */
    private void rxBindingTest10(){
        RxTextView.textChangeEvents(mEtMessage)
                .debounce(1000,TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<TextViewTextChangeEvent>() {
                    @Override
                    public void call(TextViewTextChangeEvent textViewTextChangeEvent) {
                        mTvMessage.setText(textViewTextChangeEvent.text().toString());
                    }
                });
    }


}
