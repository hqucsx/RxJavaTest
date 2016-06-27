package com.hqucsx.rxjavatest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.et_message)
    AppCompatEditText mEtMessage;
    @BindView(R.id.btn_message)
    AppCompatButton mBtnMessage;
    @BindView(R.id.tv_message)
    TextView mTvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_message)
    public void onClick() {
        rxTest3();
        Logger.i("开始使用RxJava");
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
        observable.subscribe(onNextAction,onErrorAction,onCompletedAction);
    }
}
