package com.example.familyeducationhelp.classList;

import cn.leancloud.sms.AVSMS;
import cn.leancloud.sms.AVSMSOption;
import cn.leancloud.types.AVNull;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SendMessageRequest implements Observer<AVNull>{
    private String content;
    private SendMessage mSendMessage;

    public SendMessageRequest(String content){
        this.content = content;
    }
    public void sendRequest(){
        if (content.length() == 11) {
            AVSMSOption option = new AVSMSOption();
            option.setApplicationName("家教帮");
            option.setSignatureName("loginFEH");
            AVSMS.requestSMSCodeInBackground(content, option).subscribe(this);
        }
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(AVNull avNull) {
        mSendMessage.SendSucceed();
    }

    @Override
    public void onError(Throwable e) {
        mSendMessage.SendFalse(e);
    }

    @Override
    public void onComplete() {

    }

    public void setIsSendMessage(SendMessage message){
        this.mSendMessage = message;
    }
    //定义接口
    public interface SendMessage{
        void SendSucceed();
        void SendFalse(Throwable throwable);
    }
}

